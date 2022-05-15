package sit.int221.bookingproj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.bookingproj.dtos.EventCategoryDto;
import sit.int221.bookingproj.entities.EventCategory;
import sit.int221.bookingproj.repositories.EventCategoryRepository;

import java.util.List;
import java.util.Objects;
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

    public EventCategory createEventCategory(EventCategory newEventCategory){
        if(validateForm(newEventCategory, "create")) {
            return eventCategoryRepository.saveAndFlush(newEventCategory);
        }
        return newEventCategory;
    }

    public EventCategory updateEventCategory(Integer id, EventCategory updateEventCategory){
        Optional<EventCategory> optionalEventCategory = eventCategoryRepository.findById(id);
        if(optionalEventCategory.isPresent()){
            if(validateForm(updateEventCategory, "update")){
                return eventCategoryRepository.saveAndFlush(updateEventCategory);
            }
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "can not find event category id" + id);
        }
        return updateEventCategory;
    }

    public boolean validateForm(EventCategory newEventCategory, String action) {
        EventCategory name = new EventCategory();
        name = eventCategoryRepository.findAllByEventCategoryName(newEventCategory.getEventCategoryName());
        if (Objects.equals(action, "create")) {
            checkDuplicate(newEventCategory);
            if (name != null)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "event category name is not unique");

            return true;
        }
        checkDuplicate(newEventCategory);
        if(name == null) return true;
        if(Objects.equals(name.getEventCategoryName(), newEventCategory.getEventCategoryName()) && !Objects.equals(name.getEventCategoryId(), newEventCategory.getEventCategoryId()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "event category name is not unique");
        return true;
    }

    private void checkDuplicate(EventCategory newEventCategory) {
        if(newEventCategory.getEventCategoryName().length() > 100)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "categoryName length exceeded the size");
        if(newEventCategory.getEventCategoryDescription().length() > 500)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "categoryDescription length exceeded the size");
        if (!(newEventCategory.getEventDuration() > 0 && newEventCategory.getEventDuration() < 480))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "duration is out of range");
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
