package sit.int221.bookingproj.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sit.int221.bookingproj.dtos.EventCreateUpdateDto;
import sit.int221.bookingproj.dtos.EventGetDto;
import sit.int221.bookingproj.entities.Event;
import sit.int221.bookingproj.entities.EventCategory;
import sit.int221.bookingproj.repositories.EventCategoryRepository;
import sit.int221.bookingproj.repositories.EventRepository;
import sit.int221.bookingproj.services.EventService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
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
    public void createEvent(@RequestBody EventCreateUpdateDto newEvent){
        eventService.create(newEvent);
    }
    
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Event updateEvent(@PathVariable(name = "id") Integer id, @RequestBody EventCreateUpdateDto updateEvent){
        return eventService.update(id, updateEvent);
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
        return eventService.castTypeToDto(eventRepository.findAllByEventStartTimeBetween(dateTime1,dateTime2, Sort.by(Sort.Direction.DESC, "eventStartTime")));
    }



//    @GetMapping("/search/eventCategoryName")
//    public List getByCategory(@RequestParam String eventCategoryName){
//        Optional<EventCategory> eventCategory1 = Optional.ofNullable(eventCategoryRepository.findAllByEventCategoryName(eventCategoryName));
//        return eventService.castTypeToDto(eventRepository.findAllByEventCategory(eventCategory1));
//    }

    @GetMapping("/search")
    public List<EventGetDto> getSearchByFilter(@RequestParam String name, @RequestParam String word, @RequestParam String dateStart, @RequestParam String dateEnd){
        List<Event> eventFiltered = new ArrayList<Event>();
        boolean eventAdd = false;
        if(!dateStart.equals(0) && dateEnd.equals(0)){
            eventFiltered = eventService.findByDateTime(dateStart, dateEnd);
            eventAdd = true;
        }
        if(name == null){
            Optional<EventCategory> eventCategory1 = Optional.ofNullable(eventCategoryRepository.findAllByEventCategoryName(name));
            List<Event> resultSet = eventRepository.findAllByEventCategory(eventCategory1);
            for(int i = 0; i < resultSet.size(); i++){
                if(!(eventFiltered.contains(resultSet.get(i)))) {
                    eventFiltered.add(resultSet.get(i));
                }
            }
            eventAdd = true;
        }
        if(word == null){
            List<Event> resultSet = eventRepository.findAllByBookingEmailContainingOrBookingNameContainingOrEventNotesContaining(word, word, word);
            for(int i = 0; i < resultSet.size(); i++){
                if(!(eventFiltered.contains(resultSet.get(i)))){
                    eventFiltered.add(resultSet.get(i));
                }
            }
            eventAdd = true;
        }
        if(eventAdd == true){
            eventFiltered.sort(Comparator.comparing(Event::getEventStartTime).reversed());
            return eventService.castTypeToDto(eventFiltered);
        }
        else{
            return eventService.castTypeToDto(eventRepository.findAll(Sort.by(Sort.Direction.DESC, "eventStartTime")));
        }
    }

//    @GetMapping(value = "/filter/eventsearch", params = "findingWord")
//    public List<Event> getEventBySearchBooking(@RequestParam String findingWord){
//        return eventRepository.findAllByBookingEmailContainingOrBookingNameContainingOrEventNotesContaining(findingWord, findingWord, findingWord);
//    }
//
//    @GetMapping(value = "/filter/eventsearch", params = "findingNumber")
//    public List<Event> getEventBySearchBookingByNumber(@RequestParam Integer findingNumber){
//        return eventRepository.findAllByEventDuration(findingNumber);
//    }

}