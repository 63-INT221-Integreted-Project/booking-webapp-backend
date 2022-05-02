package sit.int221.bookingproj.controller;

import io.swagger.annotations.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sit.int221.bookingproj.entities.Event;
import sit.int221.bookingproj.entities.EventCategory;
import sit.int221.bookingproj.repositories.EventCategoryRepository;
import sit.int221.bookingproj.repositories.EventRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:3000")
public class EventController {
    Logger logger = LoggerFactory.getLogger(EventController.class);
    @Autowired
    public EventRepository eventRepository;

    @Autowired
    public EventCategoryRepository eventCategoryRepository;

    @GetMapping("/")
    public List getAllEvent(){
        return eventRepository.findAll();
    }
    @GetMapping("/{id}")
    public Optional<Event> getEventById(@PathVariable Integer id){
        return eventRepository.findById(id);
    }
    @PostMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Event createEvent(@RequestBody Event newEvent){
        return eventRepository.saveAndFlush(newEvent);
    }
    
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Optional<Event> updateEvent(@PathVariable(name = "id") Integer id, @RequestBody Event updateEvent){
        Optional<Event> eventOptional = eventRepository.findById(id);
        if(!eventOptional.isPresent()){
            return eventOptional;
        }
        return Optional.of(eventRepository.saveAndFlush(updateEvent));
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
        logger.info(str1);
        logger.info(str2);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime1 = LocalDateTime.parse(str1, formatter);
        LocalDateTime dateTime2 = LocalDateTime.parse(str2, formatter);
        return eventRepository.findAllByEventStartTimeBetween(dateTime1,dateTime2);
    }

    @GetMapping("/filter/eventCategoryName")
    public List getByCategory(@RequestParam String eventCategoryName){
        Optional<EventCategory> eventCategory1 = Optional.ofNullable(eventCategoryRepository.findAllByEventCategoryName(eventCategoryName));
        return eventRepository.findAllByEventCategory(eventCategory1);
    }

    @GetMapping("/filter/eventsearch")
    public List<Event> getEventBySearchBooking(@RequestParam String findingWord){
        return eventRepository.findAllByBookingEmailContainingOrBookingNameContainingOrEventNotesContaining(findingWord, findingWord, findingWord);
    }

}