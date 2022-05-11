package sit.int221.bookingproj.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sit.int221.bookingproj.dtos.EventCreateDto;
import sit.int221.bookingproj.dtos.EventGetDto;
import sit.int221.bookingproj.dtos.EventUpdateDto;
import sit.int221.bookingproj.entities.Event;
import sit.int221.bookingproj.repositories.EventCategoryRepository;
import sit.int221.bookingproj.repositories.EventRepository;
import sit.int221.bookingproj.services.EventService;
import org.apache.commons.collections4.ListUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController()
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:3000")
public class EventController {
    Logger logger = LoggerFactory.getLogger(EventController.class);
    @Autowired
    public EventRepository eventRepository;

    @Autowired
    public EventService eventService;

    @Autowired
    public EventCategoryRepository eventCategoryRepository;

    @GetMapping("/")
    public List<EventGetDto> getAllEvent(){
        return eventService.getAllEvent();
    }
    @GetMapping("/{id}")
    public EventGetDto getEventById(@PathVariable Integer id){
        return eventService.getById(id);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public void createEvent(@RequestBody EventCreateDto newEvent){
        if(eventService.checkDuplicateEventTime(newEvent) == true){
            eventService.create(newEvent);
        }
    }

    @GetMapping("/find/sort/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<EventGetDto> findAndSort(){
        return eventService.castTypeToDto(eventRepository.findAll(Sort.by(Sort.Direction.DESC, "eventStartTime")));
    }

//    @PutMapping("/{id}")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public Event updateEvent(@PathVariable(name = "id") Integer id, @RequestBody EventCreateUpdateDto updateEvent){
//        return eventService.update(id, updateEvent);
//    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateEvent(@PathVariable Integer id, @RequestBody EventUpdateDto eventUpdateDto){
        Optional<Event> events = Optional.of(new Event());
        events = eventRepository.findById(id);
        events.ifPresent(event -> {
            event.setEventNotes(eventUpdateDto.getEventNotes());
            event.setEventStartTime(eventUpdateDto.getEventStartTime());
            eventRepository.saveAndFlush(event);
        });

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@PathVariable(name = "id") Integer id){
        eventRepository.deleteById(id);
    }

    @GetMapping("/check-between")
    public List getByMonth(@RequestParam(name = "date1") String date1, @RequestParam(name = "date2") String date2){
        String str1 = date1;
        String str2 = date2;
        Instant instant = Instant.parse(str1);
        Instant instant2 = Instant.parse(str2);
//        logger.info(str1);
//        logger.info(str2);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime dateTime1 = LocalDateTime.parse(str1, formatter);
//        LocalDateTime dateTime2 = LocalDateTime.parse(str2, formatter);
        return eventService.castTypeToDto(eventRepository.findAllByEventStartTimeBetween(instant,instant2, Sort.by(Sort.Direction.DESC, "eventStartTime")));
    }

    @GetMapping(value = "/search" , params = {"dateStart", "dateEnd", "category", "word"})
    public List<EventGetDto> getSearch(@RequestParam(name = "dateStart") String dateStart, @RequestParam(name = "dateEnd") String dateEnd, @RequestParam(name = "category") String category, @RequestParam(name = "word") String word) {
        String str1 = dateStart;
        String str2 = dateEnd;
        Instant instant = Instant.parse(str1);
        Instant instant2 = Instant.parse(str2);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime dateTime1 = LocalDateTime.parse(str1, formatter);
//        LocalDateTime dateTime2 = LocalDateTime.parse(str2, formatter);
        List<Event> eventWord = eventRepository.findAllByBookingEmailContainingOrBookingNameContaining(word, word, Sort.by(Sort.Direction.DESC, "eventStartTime"));
        List<Event> eventFilter = eventRepository.findAllByEventStartTimeBetweenAndEventCategory_EventCategoryName(instant, instant2, category, Sort.by(Sort.Direction.DESC, "eventStartTime"));
        List<Event> eventUnion = ListUtils.intersection(eventFilter, eventWord);
        return eventService.castTypeToDto(eventUnion);
    }
    @GetMapping(value = "/search" , params = {"dateStart", "dateEnd", "word"})
    public List<EventGetDto> getSearchDate(@RequestParam(name = "dateStart") String dateStart, @RequestParam(name = "dateEnd") String dateEnd, @RequestParam(name = "word") String word) {
        String str1 = dateStart;
        String str2 = dateEnd;
        Instant instant = Instant.parse(str1);
        Instant instant2 = Instant.parse(str2);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime dateTime1 = LocalDateTime.parse(str1, formatter);
//        LocalDateTime dateTime2 = LocalDateTime.parse(str2, formatter);
        List<Event> eventWord = eventRepository.findAllByBookingEmailContainingOrBookingNameContaining(word, word, Sort.by(Sort.Direction.DESC, "eventStartTime"));
        List<Event> eventFilter = eventRepository.findAllByEventStartTimeBetween(instant, instant2 , Sort.by(Sort.Direction.DESC, "eventStartTime"));
        List<Event> eventUnion = ListUtils.intersection(eventFilter, eventWord);
        return eventService.castTypeToDto(eventUnion);
    }
    @GetMapping(value = "/search" , params = {"category", "word"})
    public List<EventGetDto> getSearchCategory(@RequestParam(name = "category") String category, @RequestParam(name = "word") String word) {
        List<Event> eventWord = eventRepository.findAllByBookingEmailContainingOrBookingNameContaining(word, word , Sort.by(Sort.Direction.DESC, "eventStartTime"));
        List<Event> eventFilter = eventRepository.findAllByEventCategory_EventCategoryName(category, Sort.by(Sort.Direction.DESC, "eventStartTime"));
        List<Event> eventUnion = ListUtils.intersection(eventFilter, eventWord);
        return eventService.castTypeToDto(eventUnion);
    }

    @GetMapping(value = "/search" , params = {"dateStart", "dateEnd", "category"})
    public List<EventGetDto> getSearch(@RequestParam(name = "dateStart") String dateStart, @RequestParam(name = "dateEnd") String dateEnd, @RequestParam(name = "category") String category) throws DateTimeParseException {
        String str1 = dateStart;
        String str2 = dateEnd;
        Instant instant = Instant.parse(str1);
        Instant instant2 = Instant.parse(str2);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime dateTime1 = LocalDateTime.parse(str1, formatter);
//        LocalDateTime dateTime2 = LocalDateTime.parse(str2, formatter);
        return eventService.castTypeToDto(eventRepository.findAllByEventStartTimeBetweenAndEventCategory_EventCategoryName(instant, instant2, category , Sort.by(Sort.Direction.DESC, "eventStartTime")));
    }
    @GetMapping(value = "/search" , params = {"dateStart", "dateEnd"})
    public List<EventGetDto> getSearchDate(@RequestParam(name = "dateStart") String dateStart, @RequestParam(name = "dateEnd") String dateEnd) {
        String str1 = dateStart;
        String str2 = dateEnd;
        Instant instant = Instant.parse(str1);
        Instant instant2 = Instant.parse(str2);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime dateTime1 = LocalDateTime.parse(str1, formatter);
//        LocalDateTime dateTime2 = LocalDateTime.parse(str2, formatter);
        return eventService.castTypeToDto(eventRepository.findAllByEventStartTimeBetween(instant, instant2 , Sort.by(Sort.Direction.DESC, "eventStartTime")));
    }
    @GetMapping(value = "/search" , params = {"category"})
    public List<EventGetDto> getSearchCategory(@RequestParam(name = "category") String category) {
        return eventService.castTypeToDto(eventRepository.findAllByEventCategory_EventCategoryName(category, Sort.by(Sort.Direction.DESC, "eventStartTime")));
    }
}