package sit.int221.bookingproj.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sit.int221.bookingproj.dtos.EventCreateDto;
import sit.int221.bookingproj.dtos.EventGetDto;
import sit.int221.bookingproj.dtos.EventUpdateDto;
import sit.int221.bookingproj.exception.*;
import sit.int221.bookingproj.repositories.EventCategoryRepository;
import sit.int221.bookingproj.repositories.EventRepository;
import sit.int221.bookingproj.services.EventService;

import javax.validation.Valid;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

@Validated
@RestController()
@RequestMapping("/api/events")
public class EventController {
    Logger logger = LoggerFactory.getLogger(EventController.class);
    @Autowired
    public EventRepository eventRepository;
    @Autowired
    public EventService eventService;

    @Autowired
    public EventCategoryRepository eventCategoryRepository;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<EventGetDto> getAllEvent(){
        return eventService.getAllEvent();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventGetDto getEventById(@PathVariable Integer id) throws NotFoundException, NonSelfGetDataException, LecuterPermissionException {
        return eventService.getById(id);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<EventGetDto> createEvent(@Valid @RequestPart(value = "event",required = false ) EventCreateDto newEvent) throws OverlapTimeException, EventCategoryIdNullException, EventTimeNullException, NotMatchEmailCreteEventException, LecuterPermissionException, NotFoundException {
        return eventService.create(newEvent);
    }

    @PostMapping(value = "/", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_MIXED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public void createEvent(@Valid @RequestPart("event") EventCreateDto newEvent, @RequestPart(value = "file",required = false ) MultipartFile multipartFile) throws OverlapTimeException, EventCategoryIdNullException, EventTimeNullException, NotMatchEmailCreteEventException, LecuterPermissionException, NotFoundException, IOException, FileSizeTooLargeException {
        eventService.createWithFile(newEvent, multipartFile);
    }

    @GetMapping("/find/sort/")
    @ResponseStatus(HttpStatus.OK)
    public List<EventGetDto> findAndSort(){
        return eventService.castTypeToDto(eventRepository.findAll(Sort.by(Sort.Direction.DESC, "eventStartTime")));
    }

    @PatchMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateEvent(@PathVariable Integer id,@Valid @RequestPart(value = "event",required = false ) EventUpdateDto eventUpdateDto) throws OverlapTimeException {
        eventService.update(id,eventUpdateDto);
    }

    @PatchMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_MIXED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public void updateEventWithFile(@PathVariable Integer id,@Valid @RequestPart(value = "event",required = false ) EventUpdateDto eventUpdateDto, @RequestPart(value = "file") MultipartFile multipartFile) throws OverlapTimeException, IOException {
        eventService.uploadWithFile(id, eventUpdateDto, multipartFile);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable(name = "id") Integer id) throws EventCategoryIdNullException, NotFoundException, NonSelfGetDataException, LecuterPermissionException, IOException {
        eventService.deleteEvent(id);
    }

    @GetMapping("/check-between")
    @ResponseStatus(HttpStatus.OK)
    public List getByMonth(@RequestParam(name = "date1") String date1, @RequestParam(name = "date2") String date2){
        String str1 = date1;
        String str2 = date2;
        Instant instant = Instant.parse(str1);
        Instant instant2 = Instant.parse(str2);
        return eventService.checkBetween(instant,instant2);
    }

    @GetMapping(value = "/search" , params = {"dateStart", "dateEnd", "category", "word"})
    public List<EventGetDto> getSearch(@RequestParam(name = "dateStart") String dateStart, @RequestParam(name = "dateEnd") String dateEnd, @RequestParam(name = "category") String category, @RequestParam(name = "word") String word){
        return eventService.getSearch(dateStart,dateEnd,category,word);
    }

}