package sit.int221.bookingproj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.bookingproj.dtos.EventCategoryDto;
import sit.int221.bookingproj.entities.Event;
import sit.int221.bookingproj.entities.EventCategory;
import sit.int221.bookingproj.repositories.EventCategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventCategoryService {
    @Autowired
    public EventCategoryRepository eventCategoryRepository;

    public List<EventCategoryDto> getAllEventCategoryDto(){
        return eventCategoryRepository.findAll(Sort.by(Sort.Direction.DESC, "eventCategoryId")).stream().map(this::castEventCategoryDto).collect(Collectors.toList());
    }

    public Optional<EventCategory> getEventCategoryById(Integer id){
        return Optional.ofNullable(eventCategoryRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "can not find eventCategoryId" + id)));
    }

    public void createEventCategory(EventCategory newEventCategory){
        if(validateForm(newEventCategory)){
            eventCategoryRepository.saveAndFlush(newEventCategory);
        }
    }

    public void updateEventCategory(Integer id, EventCategory updateEventCategory){
        Optional<EventCategory> optionalEventCategory = eventCategoryRepository.findById(id);
        if(optionalEventCategory.isPresent()){
            if(validateForm(updateEventCategory)){
                eventCategoryRepository.saveAndFlush(updateEventCategory);
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "can not find event category id" + id);
        }
    }

    public boolean validateForm(EventCategory newEventCategory){
        boolean check = false;
        EventCategory name = new EventCategory();
        name = eventCategoryRepository.findAllByEventCategoryName(newEventCategory.getEventCategoryName());
        if(name.getEventCategoryName() == newEventCategory.getEventCategoryName()){
            if (newEventCategory.getEventCategoryName().length() < 100 && newEventCategory.getEventCategoryDescription().length() < 500) {
                if (newEventCategory.getEventDuration() > 0 && newEventCategory.getEventDuration() < 480) {
                    check = true;
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "duration is out of range");
                }
            }
            else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "length exceeded the size");
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "event category name is not unique");
        }

        return check;
    }
    public EventCategoryDto castEventCategoryDto(EventCategory eventCategory){
        EventCategoryDto eventCategoryDto = new EventCategoryDto();
        eventCategoryDto.setEventCategoryId(eventCategory.getEventCategoryId());
        eventCategoryDto.setEventCategoryName(eventCategory.getEventCategoryName());
        eventCategoryDto.setEventCategoryDescription(eventCategory.getEventCategoryDescription());
        eventCategoryDto.setEventDuration(eventCategory.getEventDuration());
        return  eventCategoryDto;
    }
}
