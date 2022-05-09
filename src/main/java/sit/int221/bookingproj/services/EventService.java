package sit.int221.bookingproj.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sit.int221.bookingproj.controller.EventController;
import sit.int221.bookingproj.dtos.EventCategoryDto;
import sit.int221.bookingproj.dtos.EventCategoryInEventDto;
import sit.int221.bookingproj.dtos.EventCreateDto;
import sit.int221.bookingproj.dtos.EventGetDto;
import sit.int221.bookingproj.entities.Event;
import sit.int221.bookingproj.entities.EventCategory;
import sit.int221.bookingproj.repositories.EventCategoryRepository;
import sit.int221.bookingproj.repositories.EventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime1 = LocalDateTime.parse(str1, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(str2, formatter);
        return eventRepository.findAllByEventStartTimeBetween(dateTime1,dateTime2,Sort.by(Sort.Direction.DESC, "eventStartTime"));
    }

    public List<EventGetDto> castTypeToDto(List<Event> event){
        return event.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    public EventGetDto getById(Integer id){
        Optional<Event> event = Optional.of(new Event());
        event = eventRepository.findById(id);
        return convertEntityToDto(event.get());
    }

    public void create(EventCreateDto eventCreateDto){
        eventRepository.saveAndFlush(convertDtoToEvent(eventCreateDto));
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
        EventCategoryInEventDto eventCategoryInEventDto = new EventCategoryInEventDto(event.getEventCategory().getEventCategoryId(), event.getEventCategory().getEventCategoryName() );
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
        Optional<EventCategory> eventCategory = eventCategoryRepository.findById(eventCreateDto.getEventCategoryId());
        String eventCategoryName = "";
        if(eventCategory.isPresent()){
            eventCategoryName = eventCategory.get().getEventCategoryName();
        }
        List<Event> events = eventRepository.findAllByEventStartTimeBetweenAndEventCategory_EventCategoryName(eventCreateDto.getEventStartTime() , eventCreateDto.getEventStartTime().plusMinutes(eventCreateDto.getEventDuration().longValue()) ,eventCategoryName ,Sort.by(Sort.Direction.DESC, "eventStartTime"));
        if(events.isEmpty()){
            check = true;
        }
        else{
            check = false;
        }
        return check;
    }
}