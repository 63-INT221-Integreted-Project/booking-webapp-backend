package sit.int221.bookingproj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sit.int221.bookingproj.entities.Event;
import sit.int221.bookingproj.repositories.EventRepository;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/events")
@CrossOrigin(origins = "http://localhost:3000")
public class EventController {
    @Autowired
    public EventRepository eventRepository;

    @GetMapping("/")
    public List getAllEvent(){
        return eventRepository.findAll();
    }
    @GetMapping("/{id}")
    public Optional<Event> getEventById(@PathVariable String id){
        return eventRepository.findById(id);
    }
    @PostMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Event createEvent(@RequestBody Event newEvent){
        return eventRepository.saveAndFlush(newEvent);
    }
    
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Optional<Event> updateEvent(@PathVariable(name = "id") String id, @RequestBody Event updateEvent){
        Optional<Event> eventOptional = eventRepository.findById(id);
        if(!eventOptional.isPresent()){
            return eventOptional;
        }
        return Optional.of(eventRepository.saveAndFlush(updateEvent));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delete(@PathVariable(name = "id") String id){
        eventRepository.deleteById(id);
    }

}