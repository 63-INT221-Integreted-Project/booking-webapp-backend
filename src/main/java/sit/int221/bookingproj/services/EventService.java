package sit.int221.bookingproj.services;

import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.bookingproj.controller.EventController;
import sit.int221.bookingproj.dtos.*;
import sit.int221.bookingproj.entities.Event;
import sit.int221.bookingproj.entities.EventCategory;
import sit.int221.bookingproj.exception.EventCategoryIdNullException;
import sit.int221.bookingproj.exception.EventTimeNullException;
import sit.int221.bookingproj.exception.NotFoundEventException;
import sit.int221.bookingproj.exception.OverlapTimeException;
import sit.int221.bookingproj.repositories.EventCategoryRepository;
import sit.int221.bookingproj.repositories.EventRepository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService{
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    Logger logger = LoggerFactory.getLogger(EventController.class);
    @ExceptionHandler(IllegalStateException.class)
    public void handleIllegalStateException() {}
    @ExceptionHandler(OverlapTimeException.class)
    public void handleOverlapTime() {}

    @ExceptionHandler(EventCategoryIdNullException.class)
    public void handleEventCategoryIdNullException() {}

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgumentException() {}

    @ExceptionHandler(EventTimeNullException.class)
    public void handleEventTimeNullException() {}

    @ExceptionHandler(NotFoundEventException.class)
    public void handleNotFoundEventException(){}


    public List<EventGetDto> getAllEvent(){
        return eventRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }
    public List<EventGetDto> castTypeToDto(List<Event> event){
        return event.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    public EventGetDto getById(Integer id) throws NotFoundEventException {
        Optional<Event> event = Optional.of(new Event());
        event = Optional.ofNullable(eventRepository.findById(id).orElseThrow(() -> new NotFoundEventException("Event not found")));
        return convertEntityToDto(event.get());
    }

    public void deleteEvent(Integer id) throws EventCategoryIdNullException, NotFoundEventException {
        Optional<Event> event = eventRepository.findById(id);
        if(event != null){
            eventRepository.deleteById(id);
        }
        else{
            throw new NotFoundEventException("Can not find for id " + id);
        }
    }

    public List<EventGetDto> getSearch(String dateStart, String dateEnd, String category, String word){
        List result = new ArrayList();
        List categoryFind = new ArrayList();
        List wordFind = new ArrayList();
        List dateFind = new ArrayList();
        List resultDateAndCategory = new ArrayList();
        String str1 = dateStart;
        String str2 = dateEnd;
        boolean findCategory = false;

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
            findCategory = true;
            EventCategory eventCategory = eventCategoryRepository.findAllByEventCategoryName(category);
            categoryFind = eventRepository.findAllByEventCategory(Optional.ofNullable(eventCategory),  Sort.by(Sort.Direction.DESC, "eventStartTime"));
        }
        if(word != ""){
            wordFind = eventRepository.findAllByBookingEmailContainingOrBookingNameContaining(word, word ,Sort.by(Sort.Direction.DESC, "eventStartTime") );
        }

        if(categoryFind.isEmpty() && findCategory == true){
            return castTypeToDto(result);
        }
        if(dateFind.isEmpty() || (categoryFind.isEmpty() && findCategory == false)){
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


    public Optional<EventGetDto> create(EventCreateDto eventCreateDto) throws OverlapTimeException, EventCategoryIdNullException, EventTimeNullException {
        if(eventCreateDto.getEventDuration() == null){
            Optional<EventCategory> eventCategory = eventCategoryRepository.findById(eventCreateDto.getEventCategoryId());
            if(checkEventStartTimeNull(eventCreateDto)){
                eventCreateDto.setEventDuration(eventCategory.get().getEventDuration());
            }
        }
        if (checkDuplicateEventTime(eventCreateDto)) {
            eventCreateDto.setBookingName(eventCreateDto.getBookingName().trim());
            eventCreateDto.setBookingEmail(eventCreateDto.getBookingEmail().trim());
            Event event = eventRepository.saveAndFlush(convertDtoToEvent(eventCreateDto));
            return Optional.ofNullable(convertEntityToDto(event));
        }
        else{
            throw new OverlapTimeException("Start Time can not overlap");
        }
    }
    public void update(Integer id, EventUpdateDto eventUpdateDto) throws OverlapTimeException {
        Optional<Event> events = Optional.of(new Event());
        events = eventRepository.findById(id);
        if(events.isPresent()){
            if(eventUpdateDto.getEventStartTime() == null) {
                events.get().setEventStartTime(events.get().getEventStartTime());
                eventUpdateDto.setEventStartTime(events.get().getEventStartTime());
            }
                if(checkDuplicateEventTimeForUpdate(events,eventUpdateDto.getEventStartTime())){
                    events.ifPresent(event -> {
                        event.setEventNotes(eventUpdateDto.getEventNotes());
                    if(eventUpdateDto.getEventStartTime() != null){
                        event.setEventStartTime(eventUpdateDto.getEventStartTime());
                    }
                    eventRepository.saveAndFlush(event);
                });
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not find eventId" + id);
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

    public boolean checkDuplicateEventTimeForUpdate(Optional<Event> event, Instant startTime) throws OverlapTimeException {
        boolean check;
        Event eventTimeIn = new Event();
        eventTimeIn = eventRepository.findAllByEventStartTimeBetweenAndEventCategory_EventCategoryId(startTime, startTime.plusSeconds(event.get().getEventDuration() * 60),event.get().getEventCategory().getEventCategoryId(), Sort.by(Sort.Direction.DESC, "eventStartTime"));
        if(eventTimeIn == null || event.get().getEventId() == eventTimeIn.getEventId()){
            check = true;
        }
        else{
            check = false;
            throw new OverlapTimeException("Start Time can not overlap");
        }
        return check;
    }
    public boolean checkEventStartTimeNull(EventCreateDto eventCreateDto) throws EventTimeNullException {
        boolean check = false;
        if(eventCreateDto.getEventStartTime() != null){
            check = true;
        }
        else{
            throw new EventTimeNullException("Event Start Time Can not be null");
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
        List<Event> events = eventRepository.findAllByEventStartTimeBetweenAndEventCategory_EventCategoryName(eventCreateDto.getEventStartTime() , eventCreateDto.getEventStartTime().plusSeconds(eventCreateDto.getEventDuration().longValue() * 60L) ,eventCategoryName ,Sort.by(Sort.Direction.DESC, "eventStartTime"));
        if(events.isEmpty()){
            check = true;
        }
        else{
            check = false;
        }
        return check;
    }

    public List<EventGetDto> checkBetween(Instant instant, Instant instant2){
        return castTypeToDto(eventRepository.findAllByEventStartTimeBetween(instant,instant2, Sort.by(Sort.Direction.DESC, "eventStartTime")));
    }
}