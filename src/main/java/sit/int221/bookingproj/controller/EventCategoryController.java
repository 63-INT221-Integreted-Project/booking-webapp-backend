package sit.int221.bookingproj.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sit.int221.bookingproj.dtos.EventCategoryCreateUpdateDto;
import sit.int221.bookingproj.dtos.EventCategoryDto;
import sit.int221.bookingproj.entities.EventCategory;
import sit.int221.bookingproj.exception.NotFoundException;
import sit.int221.bookingproj.exception.UniqueEventCategoryNameException;
import sit.int221.bookingproj.repositories.EventCategoryRepository;
import sit.int221.bookingproj.services.EventCategoryService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/event-categories")
public class EventCategoryController {
    @Autowired
    public EventCategoryRepository eventCategoryRepository;

    @Autowired
    public EventCategoryService eventCategoryService;

    @ExceptionHandler(IllegalStateException.class)
    public void handleIllegalStateException() {}

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List getAllEventCategrory(){
        return eventCategoryService.getAllEventCategoryDto();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<EventCategoryDto> getEventCategoryById(@PathVariable Integer id) throws NotFoundException {
        return eventCategoryService.getEventCategoryById(id);
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public EventCategory createEventCategory(@Valid @RequestBody EventCategoryCreateUpdateDto newEventCategory) throws UniqueEventCategoryNameException {
        return eventCategoryService.createEventCategory(newEventCategory);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventCategory updateEventCategory(@PathVariable(name = "id") Integer id, @Valid @RequestBody EventCategoryCreateUpdateDto updateEventCategory) throws UniqueEventCategoryNameException {
        return eventCategoryService.updateEventCategory(id,updateEventCategory);
    }

//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public void delete(@PathVariable Integer id){
//        eventCategoryRepository.deleteById(id);
//    }

}