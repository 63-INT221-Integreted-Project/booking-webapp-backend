package sit.int221.bookingproj.services;

import io.swagger.models.auth.In;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.bookingproj.controller.EventController;
import sit.int221.bookingproj.dtos.*;
import sit.int221.bookingproj.entities.Event;
import sit.int221.bookingproj.entities.EventCategory;
import sit.int221.bookingproj.repositories.EventCategoryRepository;
import sit.int221.bookingproj.repositories.EventRepository;

import javax.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    public void deleteEvent(Integer id){
        eventRepository.deleteById(id);
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
        else if(dateStart == "" && dateEnd == ""){
            dateFind = eventRepository.findAll(Sort.by(Sort.Direction.DESC, "eventStartTime"));
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

    public boolean validateFormCreate(EventCreateDto eventCreateDto){
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(eventCreateDto.getBookingEmail());
        boolean check = false;
        if(eventCreateDto.getBookingName().length() < 100 || eventCreateDto.getBookingEmail().length() < 50 || eventCreateDto.getEventNotes().length() < 500){
            if(eventCreateDto.getEventDuration() > 0 && eventCreateDto.getEventDuration() < 480){
                if(matcher.find()){
                    check = true;
                }
                else{
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email is invalid");
                }
            }
            else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "duration is invalid");
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "length exceeded the size");
        }
        return check;
    }

    public void create(EventCreateDto eventCreateDto) {
            if (checkDuplicateEventTime(eventCreateDto)) {
                if(validateFormCreate(eventCreateDto)){
                    if(checkFuture(eventCreateDto.getEventStartTime())){
                        eventRepository.saveAndFlush(convertDtoToEvent(eventCreateDto));
                    }
                }
            }else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Overlap Datetime");
            }
    }

    public void update(Integer id, EventUpdateDto eventUpdateDto){
        Optional<Event> events = Optional.of(new Event());
        events = eventRepository.findById(id);
        if(events.isPresent()){
            if(eventUpdateDto.getEventNotes().length() < 500){
                if(checkDuplicateEventTimeForUpdate(events,eventUpdateDto.getEventStartTime())){
                    if(checkFuture(eventUpdateDto.getEventStartTime())){
                        events.ifPresent(event -> {
                            event.setEventNotes(eventUpdateDto.getEventNotes());
                            event.setEventStartTime(eventUpdateDto.getEventStartTime());
                            eventRepository.saveAndFlush(event);
                        });
                    }
                    else{
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stattime can not be past");
                    }
                }
                else{
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stattime can not overlap");
                }
            }
            else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Length exceeded the size");
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not update eventId" + id);
        }
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

    public boolean checkDuplicateEventTimeForUpdate(Optional<Event> event, Instant startTime){
        boolean check;
        List eventTimeIn = new ArrayList();
        eventTimeIn = eventRepository.findAllByEventStartTimeBetweenAndEventCategory_EventCategoryName(startTime, startTime.plusSeconds(event.get().getEventDuration() * 60),event.get().getEventCategory().getEventCategoryName(), Sort.by(Sort.Direction.DESC, "eventStartTime"));
        if(eventTimeIn.isEmpty()){
            check = true;
        }
        else{
            check = false;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Overlap Datetime");
        }
        return check;
    }

   public boolean checkDuplicateEventTime(EventCreateDto eventCreateDto){
        boolean check;
        Optional<EventCategory> eventCategory = Optional.ofNullable(eventCategoryRepository.findById(eventCreateDto.getEventCategoryId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "can not find event id" + eventCreateDto.getEventCategoryId())));
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

    public boolean checkFuture(Instant instantCheck){
        boolean check = false;
        Instant instant = Instant.now();
        if(instant.isBefore(instantCheck)){
            check = true;
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Starttime can not be past");
        }
        return check;
    }
}