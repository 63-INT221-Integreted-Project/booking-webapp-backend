package sit.int221.bookingproj.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sit.int221.bookingproj.controller.EventController;
import sit.int221.bookingproj.dtos.EventCategoryDto;
import sit.int221.bookingproj.dtos.EventCreateUpdateDto;
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
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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

    public void create(EventCreateUpdateDto eventCreateUpdateDto){
        eventRepository.saveAndFlush(convertDtoToEvent(eventCreateUpdateDto));
    }

    public Event update(Integer id, EventCreateUpdateDto eventCreateUpdateDto){
        Optional<Event> event = eventRepository.findById(id);
        if(!event.isPresent()){
            return convertDtoToEvent(eventCreateUpdateDto);
        }
        return eventRepository.saveAndFlush(convertDtoToEvent(eventCreateUpdateDto));
    }



    public Event convertDtoToEvent(EventCreateUpdateDto eventCreateUpdateDto){
        EventCategory eventCategory = new EventCategory();
        eventCategory = eventCategoryRepository.findById(eventCreateUpdateDto.getEventCategoryId()).orElse(null);
        Event event = new Event();
        event.setEventId(eventCreateUpdateDto.getEventId());
        event.setBookingName(eventCreateUpdateDto.getBookingName());
        event.setBookingEmail(eventCreateUpdateDto.getBookingEmail());
        event.setEventStartTime(eventCreateUpdateDto.getEventStartTime());
        event.setEventDuration(eventCreateUpdateDto.getEventDuration());
        event.setEventNotes(eventCreateUpdateDto.getEventNotes());
        event.setEventCategory(eventCategory);
        return event;
    }

    public EventGetDto convertEntityToDto(Event event){
        EventGetDto eventDto = new EventGetDto();
        EventCategoryDto eventCategoryDto = new EventCategoryDto(event.getEventCategory().getEventCategoryId(), event.getEventCategory().getEventCategoryName() );
        eventDto.setEventId(event.getEventId());
        eventDto.setBookingName(event.getBookingName());
        eventDto.setBookingEmail(event.getBookingEmail());
        eventDto.setEventStartTime(event.getEventStartTime());
        eventDto.setEventDuration(event.getEventDuration());
        eventDto.setEventNotes(event.getEventNotes());
        eventDto.setEventCategory(eventCategoryDto);
        return eventDto;
    }
}
