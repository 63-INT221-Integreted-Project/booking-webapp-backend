package sit.int221.bookingproj.services;

import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.bookingproj.controller.EventController;
import sit.int221.bookingproj.dtos.*;
import sit.int221.bookingproj.entities.Event;
import sit.int221.bookingproj.entities.EventCategory;
import sit.int221.bookingproj.repositories.EventCategoryRepository;
import sit.int221.bookingproj.repositories.EventRepository;

import javax.validation.Valid;
import java.sql.SQLOutput;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    Logger logger = LoggerFactory.getLogger(EventController.class);

    public List<EventGetDto> getAllEvent(){
        return eventRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    public List findByDateTime(String date1, String date2){
        String str1 = date1;
        String str2 = date2;
        Instant instant = Instant.parse(str1);
        Instant instant2 = Instant.parse(str2);
        return eventRepository.findAllByEventStartTimeBetween(instant,instant2,Sort.by(Sort.Direction.DESC, "eventStartTime"));
    }

    public List<EventGetDto> castTypeToDto(List<Event> event){
        return event.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    public EventGetDto getById(Integer id){
        Optional<Event> event = Optional.of(new Event());
        event = Optional.ofNullable(eventRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ไม่พบสิ่งที่ต้องการ")));
        return convertEntityToDto(event.get());
    }

    public boolean validateForm(EventCreateDto eventCreateDto){
        boolean check = false;
        if(eventCreateDto.getBookingName().length() <= 100){
            if(eventCreateDto.getBookingEmail().length() <= 50){
                if(eventCreateDto.getEventNotes().length() <= 500){
                    check = true;
                }
            }
        }
        return check;
    }
    public List<EventGetDto> getSearch(String dateStart, String dateEnd, String category, String word){
        List result = new ArrayList();
        List categoryFind = new ArrayList();
        List wordFind = new ArrayList();
        List dateFind = new ArrayList();
        List resultDateAndCategory = new ArrayList();
        String str1 = dateStart;
        String str2 = dateEnd;

        if(dateStart != "" && dateEnd != ""){
            Instant instant = Instant.parse(str1);
            Instant instant2 = Instant.parse(str2);
            dateFind = eventRepository.findAllByEventStartTimeBetween(instant, instant2 , Sort.by(Sort.Direction.DESC, "eventStartTime"));
        }
        else if(dateStart == "" || dateEnd == ""){
            if(dateStart == ""){
                Instant instant = Instant.now();
                Instant instant2 = Instant.parse(str2);
                dateFind = eventRepository.findAllByEventStartTimeBetween(instant, instant2 , Sort.by(Sort.Direction.DESC, "eventStartTime"));
            }
            else{
                Instant instant = Instant.parse(str1);
                Instant instant2 = Instant.now();
                dateFind = eventRepository.findAllByEventStartTimeBetween(instant, instant2 , Sort.by(Sort.Direction.DESC, "eventStartTime"));
            }
        }

        if(category != ""){
            EventCategory eventCategory = eventCategoryRepository.findAllByEventCategoryName(category);
            categoryFind = eventRepository.findAllByEventCategory(Optional.ofNullable(eventCategory));
        }
        if(word != ""){
            wordFind = eventRepository.findAllByBookingEmailContainingOrBookingNameContaining(word, word ,Sort.by(Sort.Direction.DESC, "eventStartTime") );
        }

        if(dateFind.isEmpty() || categoryFind.isEmpty()){
            resultDateAndCategory = ListUtils.union(dateFind, categoryFind);
        }
        else{
            resultDateAndCategory = ListUtils.intersection(dateFind, categoryFind);
        }
        if(wordFind.isEmpty()){
            result = ListUtils.union(resultDateAndCategory, wordFind);
        }
        else{
            result = ListUtils.intersection(resultDateAndCategory, wordFind);
        }
        return castTypeToDto(result);
    }

    public void create(@Valid EventCreateDto eventCreateDto) {
            if (checkDuplicateEventTime(eventCreateDto) == true) {
                eventRepository.saveAndFlush(convertDtoToEvent(eventCreateDto));
            }
    }

    public Event update(Integer id, EventCreateDto eventCreateDto){
        Optional<Event> event = eventRepository.findById(id);
        if(!event.isPresent()){
            return convertDtoToEvent(eventCreateDto);
        }
        return eventRepository.saveAndFlush(convertDtoToEvent(eventCreateDto));

    }

    public Event convertDtoToEvent(EventCreateDto eventCreateDto){
        EventCategory eventCategory = new EventCategory();
        eventCategory = eventCategoryRepository.findById(eventCreateDto.getEventCategoryId()).orElse(null);
        Event event = new Event();
        event.setEventId(eventCreateDto.getEventId());
        event.setBookingName(eventCreateDto.getBookingName());
        event.setBookingEmail(eventCreateDto.getBookingEmail());
        event.setEventStartTime(eventCreateDto.getEventStartTime());
        event.setEventDuration(eventCreateDto.getEventDuration());
        event.setEventNotes(eventCreateDto.getEventNotes());
        event.setEventCategory(eventCategory);
        return event;
    }

    public EventGetDto convertEntityToDto(Event event){
        EventGetDto eventDto = new EventGetDto();
        EventCategoryInEventDto eventCategoryInEventDto = new EventCategoryInEventDto(event.getEventCategory().getEventCategoryId(), event.getEventCategory().getEventCategoryName(),event.getEventCategory().getEventDuration() );
        eventDto.setEventId(event.getEventId());
        eventDto.setBookingName(event.getBookingName());
        eventDto.setBookingEmail(event.getBookingEmail());
        eventDto.setEventStartTime(event.getEventStartTime());
        eventDto.setEventDuration(event.getEventDuration());
        eventDto.setEventNotes(event.getEventNotes());
        eventDto.setEventCategory(eventCategoryInEventDto);
        return eventDto;
    }

   public boolean checkDuplicateEventTime(EventCreateDto eventCreateDto){
        boolean check;
        Optional<EventCategory> eventCategory = Optional.ofNullable(eventCategoryRepository.findById(eventCreateDto.getEventCategoryId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ไม่พบสิ่งที่ต้องการ")));
        String eventCategoryName = "";
        if(eventCategory.isPresent()){
            eventCategoryName = eventCategory.get().getEventCategoryName();
        }
        List<Event> events = eventRepository.findAllByEventStartTimeBetweenAndEventCategory_EventCategoryName(eventCreateDto.getEventStartTime() , eventCreateDto.getEventStartTime().plusSeconds(eventCreateDto.getEventDuration().longValue() * 60) ,eventCategoryName ,Sort.by(Sort.Direction.DESC, "eventStartTime"));
        if(events.isEmpty()){
            check = true;
        }
        else{
            check = false;
        }
        return check;
    }
}