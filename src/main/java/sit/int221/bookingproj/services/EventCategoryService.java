package sit.int221.bookingproj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sit.int221.bookingproj.dtos.EventCategoryDto;
import sit.int221.bookingproj.entities.EventCategory;
import sit.int221.bookingproj.repositories.EventCategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventCategoryService {
    @Autowired
    public EventCategoryRepository eventCategoryRepository;

    public List<EventCategoryDto> getAllEventCategoryDto(){
        return eventCategoryRepository.findAll(Sort.by(Sort.Direction.DESC, "eventCategoryId")).stream().map(this::castEventCategoryDto).collect(Collectors.toList());
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
