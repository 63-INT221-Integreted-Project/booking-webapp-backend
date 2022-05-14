package sit.int221.bookingproj.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.bookingproj.entities.EventCategory;
import sit.int221.bookingproj.repositories.EventCategoryRepository;
import sit.int221.bookingproj.services.EventCategoryService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/event-categories")
@CrossOrigin()
public class EventCategoryController {
    @Autowired
    public EventCategoryRepository eventCategoryRepository;

    @Autowired
    public EventCategoryService eventCategoryService;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List getAllEventCategrory(){
        return eventCategoryService.getAllEventCategoryDto();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<EventCategory> getEventCategoryById(@PathVariable Integer id){
        return Optional.ofNullable(eventCategoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ไม่่พบสิ่งที่ต้องการ")));
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public EventCategory createEventCategory(@RequestBody EventCategory newEventCategory){
        return eventCategoryRepository.saveAndFlush(newEventCategory);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventCategory update(@PathVariable(name = "id") Integer id, @RequestBody EventCategory updateEventCategory){
        Optional<EventCategory> optionalEventCategory = eventCategoryRepository.findById(id);
        if(!optionalEventCategory.isPresent()){
            return updateEventCategory;
        }
        return eventCategoryRepository.saveAndFlush(updateEventCategory);
    }
//
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.OK)
//    public void delete(@PathVariable Integer id){
//        eventCategoryRepository.deleteById(id);
//    }

}