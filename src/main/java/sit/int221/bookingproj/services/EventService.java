package sit.int221.bookingproj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.int221.bookingproj.dtos.EventCategoryDto;
import sit.int221.bookingproj.dtos.EventCreateUpdateDto;
import sit.int221.bookingproj.dtos.EventDto;
import sit.int221.bookingproj.dtos.EventGetDto;
import sit.int221.bookingproj.entities.Event;
import sit.int221.bookingproj.entities.EventCategory;
import sit.int221.bookingproj.repositories.EventCategoryRepository;
import sit.int221.bookingproj.repositories.EventRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    public List<EventGetDto> getAllEvent(){
        return eventRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
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
