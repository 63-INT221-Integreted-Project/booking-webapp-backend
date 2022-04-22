package sit.int221.bookingproj.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sit.int221.bookingproj.entities.EventCategory;
import sit.int221.bookingproj.repositories.EventCategoryRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/eventcategory")
public class EventCategoryController {
    @Autowired
    public EventCategoryRepository eventCategoryRepository;

    @GetMapping("/")
    public List getAllEventCategrory(){
        return eventCategoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<EventCategory> getEventCategoryById(@PathVariable String id){
        return eventCategoryRepository.findById(id);
    }

    @PostMapping("/")
    public EventCategory createEventCategory(@RequestBody EventCategory newEventCategory){
        return eventCategoryRepository.saveAndFlush(newEventCategory);
    }

    @PutMapping("/{id}")
    public EventCategory update(@PathVariable(name = "id") String id, @RequestBody EventCategory updateEventCategory){
        Optional<EventCategory> optionalEventCategory = eventCategoryRepository.findById(id);
        if(!optionalEventCategory.isPresent()){
            return updateEventCategory;
        }
        return eventCategoryRepository.saveAndFlush(updateEventCategory);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@PathVariable String id){
        eventCategoryRepository.deleteById(id);
    }

}