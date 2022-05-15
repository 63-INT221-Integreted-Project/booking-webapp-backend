package sit.int221.bookingproj.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.bookingproj.dtos.EventCreateDto;
import sit.int221.bookingproj.dtos.EventGetDto;
import sit.int221.bookingproj.dtos.EventUpdateDto;
import sit.int221.bookingproj.entities.Event;
import sit.int221.bookingproj.repositories.EventCategoryRepository;
import sit.int221.bookingproj.repositories.EventRepository;
import sit.int221.bookingproj.services.EventService;
import org.apache.commons.collections4.ListUtils;

import javax.validation.Valid;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController()
@RequestMapping("/api/events")
@CrossOrigin()
public class EventController {
    Logger logger = LoggerFactory.getLogger(EventController.class);
    @Autowired
    public EventRepository eventRepository;

    @Autowired
    public EventService eventService;

    @Autowired
    public EventCategoryRepository eventCategoryRepository;

    @ExceptionHandler(IllegalStateException.class)
    public void handleIllegalStateException() {}

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<EventGetDto> getAllEvent(){
        return eventService.getAllEvent();
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventGetDto getEventById(@PathVariable Integer id){
        return eventService.getById(id);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public void createEvent(@RequestBody EventCreateDto newEvent)  {
        eventService.create(newEvent);
    }

    @GetMapping("/find/sort/")
    @ResponseStatus(HttpStatus.OK)
    public List<EventGetDto> findAndSort(){
        return eventService.castTypeToDto(eventRepository.findAll(Sort.by(Sort.Direction.DESC, "eventStartTime")));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateEvent(@PathVariable Integer id, @RequestBody EventUpdateDto eventUpdateDto){
        eventService.update(id,eventUpdateDto);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable(name = "id") Integer id){
        eventService.deleteEvent(id);
    }

    @GetMapping("/check-between")
    @ResponseStatus(HttpStatus.OK)
    public List getByMonth(@RequestParam(name = "date1") String date1, @RequestParam(name = "date2") String date2){
        String str1 = date1;
        String str2 = date2;
        Instant instant = Instant.parse(str1);
        Instant instant2 = Instant.parse(str2);
        return eventService.castTypeToDto(eventRepository.findAllByEventStartTimeBetween(instant,instant2, Sort.by(Sort.Direction.DESC, "eventStartTime")));
    }

    @GetMapping(value = "/search" , params = {"dateStart", "dateEnd", "category", "word"})
    public List<EventGetDto> getSearchTest(@RequestParam(name = "dateStart") String dateStart, @RequestParam(name = "dateEnd") String dateEnd, @RequestParam(name = "category") String category, @RequestParam(name = "word") String word){
        return eventService.getSearch(dateStart,dateEnd,category,word);
    }
//    @GetMapping(value = "/search" , params = {"dateStart", "dateEnd", "category", "word"})
//    @ResponseStatus(HttpStatus.OK)
//    public List<EventGetDto> getSearch(@RequestParam(name = "dateStart") String dateStart, @RequestParam(name = "dateEnd") String dateEnd, @RequestParam(name = "category") String category, @RequestParam(name = "word") String word) {
//        String str1 = dateStart;
//        String str2 = dateEnd;
//        Instant instant = Instant.parse(str1);
//        Instant instant2 = Instant.parse(str2);
//        List<Event> eventWord = eventRepository.findAllByBookingEmailContainingOrBookingNameContaining(word, word, Sort.by(Sort.Direction.DESC, "eventStartTime"));
//        List<Event> eventFilter = eventRepository.findAllByEventStartTimeBetweenAndEventCategory_EventCategoryName(instant, instant2, category, Sort.by(Sort.Direction.DESC, "eventStartTime"));
//        List<Event> eventUnion = ListUtils.intersection(eventFilter, eventWord);
//        if(eventUnion.isEmpty()){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ไม่พบสิ่งที่ต้องการ");
//        }
//        return eventService.castTypeToDto(eventUnion);
//    }
//    @GetMapping(value = "/search" , params = {"dateStart", "dateEnd", "word"})
//    @ResponseStatus(HttpStatus.OK)
//    public List<EventGetDto> getSearchDate(@RequestParam(name = "dateStart") String dateStart, @RequestParam(name = "dateEnd") String dateEnd, @RequestParam(name = "word") String word) {
//        String str1 = dateStart;
//        String str2 = dateEnd;
//        Instant instant = Instant.parse(str1);
//        Instant instant2 = Instant.parse(str2);
//        List<Event> eventWord = eventRepository.findAllByBookingEmailContainingOrBookingNameContaining(word, word, Sort.by(Sort.Direction.DESC, "eventStartTime"));
//        List<Event> eventFilter = eventRepository.findAllByEventStartTimeBetween(instant, instant2 , Sort.by(Sort.Direction.DESC, "eventStartTime"));
//        List<Event> eventUnion = ListUtils.intersection(eventFilter, eventWord);
//        if(eventUnion.isEmpty()){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ไม่พบสิ่งที่ต้องการ");
//        }
//        return eventService.castTypeToDto(eventUnion);
//    }
//    @GetMapping(value = "/search" , params = {"category", "word"})
//    @ResponseStatus(HttpStatus.OK)
//    public List<EventGetDto> getSearchCategory(@RequestParam(name = "category") String category, @RequestParam(name = "word") String word) {
//        List<Event> eventWord = eventRepository.findAllByBookingEmailContainingOrBookingNameContaining(word, word , Sort.by(Sort.Direction.DESC, "eventStartTime"));
//        List<Event> eventFilter = eventRepository.findAllByEventCategory_EventCategoryName(category, Sort.by(Sort.Direction.DESC, "eventStartTime"));
//        List<Event> eventUnion = ListUtils.intersection(eventFilter, eventWord);
//        if(eventUnion.isEmpty()){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ไม่พบสิ่งที่ต้องการ");
//        }
//        return eventService.castTypeToDto(eventUnion);
//    }
//
//    @GetMapping(value = "/search" , params = {"dateStart", "dateEnd", "category"})
//    @ResponseStatus(HttpStatus.OK)
//    public List<EventGetDto> getSearch(@RequestParam(name = "dateStart") String dateStart, @RequestParam(name = "dateEnd") String dateEnd, @RequestParam(name = "category") String category) throws DateTimeParseException {
//        String str1 = dateStart;
//        String str2 = dateEnd;
//        Instant instant = Instant.parse(str1);
//        Instant instant2 = Instant.parse(str2);
//        return eventService.castTypeToDto(eventRepository.findAllByEventStartTimeBetweenAndEventCategory_EventCategoryName(instant, instant2, category , Sort.by(Sort.Direction.DESC, "eventStartTime")));
//    }
//    @GetMapping(value = "/search" , params = {"dateStart", "dateEnd"})
//    @ResponseStatus(HttpStatus.OK)
//    public List<EventGetDto> getSearchDate(@RequestParam(name = "dateStart") String dateStart, @RequestParam(name = "dateEnd") String dateEnd) {
//        String str1 = dateStart;
//        String str2 = dateEnd;
//        Instant instant = Instant.parse(str1);
//        Instant instant2 = Instant.parse(str2);
//        return eventService.castTypeToDto(eventRepository.findAllByEventStartTimeBetween(instant, instant2 , Sort.by(Sort.Direction.DESC, "eventStartTime")));
//    }
//    @GetMapping(value = "/search" , params = {"category"})
//    @ResponseStatus(HttpStatus.OK)
//    public List<EventGetDto> getSearchCategory(@RequestParam(name = "category") String category) {
//        return eventService.castTypeToDto(eventRepository.findAllByEventCategory_EventCategoryName(category, Sort.by(Sort.Direction.DESC, "eventStartTime")));
//    }
}