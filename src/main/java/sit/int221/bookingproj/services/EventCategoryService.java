package sit.int221.bookingproj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.bookingproj.dtos.EventCategoryCreateUpdateDto;
import sit.int221.bookingproj.dtos.EventCategoryDto;
import sit.int221.bookingproj.dtos.UserGetDto;
import sit.int221.bookingproj.dtos.UserGetOwnerDto;
import sit.int221.bookingproj.entities.EventCategory;
import sit.int221.bookingproj.entities.User;
import sit.int221.bookingproj.exception.NotFoundException;
import sit.int221.bookingproj.exception.UniqueEventCategoryNameException;
import sit.int221.bookingproj.repositories.EventCategoryRepository;
import sit.int221.bookingproj.repositories.UserRepository;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventCategoryService {
    @Autowired
    public EventCategoryRepository eventCategoryRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public UserService userService;

    @ExceptionHandler(UniqueEventCategoryNameException.class)
    public void handleUniqueEventCategoryNameException() {
    }

    @ExceptionHandler(NotFoundException.class)
    public void handleNotFoundEventException() {
    }

    public List<EventCategoryDto> getAllEventCategoryDto() {
        return eventCategoryRepository.findAll(
                        Sort.by(Sort.Direction.DESC, "eventCategoryId")).stream()
                .map(this::castEventCategoryDto).collect(Collectors.toList());
    }

        public Optional<EventCategoryDto> getEventCategoryById (Integer id) throws NotFoundException {
            return Optional.ofNullable(castEventCategoryDtoOptional(Optional.ofNullable(eventCategoryRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Can not find for id " + id)))));
        }

        public EventCategory createEventCategory (EventCategoryCreateUpdateDto newEventCategory) throws
        UniqueEventCategoryNameException {
            if (checkUniqueNameCreate(newEventCategory)) {
                newEventCategory.setEventCategoryName(newEventCategory.getEventCategoryName().trim());
                newEventCategory.setEventCategoryDescription(newEventCategory.getEventCategoryDescription().trim());
                return eventCategoryRepository.saveAndFlush(castCreateDtoToEventCategory(newEventCategory));
            } else {
                throw new UniqueEventCategoryNameException("event category name must be unique");
            }
        }

        public EventCategory castCreateDtoToEventCategory(EventCategoryCreateUpdateDto eventCategoryCreateUpdateDto){
            EventCategory eventCategory = new EventCategory();
            eventCategory.setEventCategoryId(eventCategoryCreateUpdateDto.getEventCategoryId());
            eventCategory.setEventCategoryName(eventCategoryCreateUpdateDto.getEventCategoryName());
            eventCategory.setEventCategoryDescription(eventCategoryCreateUpdateDto.getEventCategoryDescription());
            eventCategory.setEventDuration(eventCategoryCreateUpdateDto.getEventDuration());
            eventCategory.setOwner(addOwnerToEventCategory(eventCategoryCreateUpdateDto));
            return eventCategory;
        }

        public List<User> addOwnerToEventCategory(EventCategoryCreateUpdateDto eventCategoryCreateUpdateDto){
        List<Optional<User>> ownerSet = new ArrayList<>();
        List<User> ownerSetUser = new ArrayList();
        for(int i = 0; i < eventCategoryCreateUpdateDto.getUserId().size(); i++) {
            ownerSet.add(userRepository.findById((Integer) eventCategoryCreateUpdateDto.getUserId().get(i)));
        }
        ownerSetUser = ownerSet.stream()
                    .flatMap(Optional::stream)
                    .collect(Collectors.toList());
        return ownerSetUser;
        }

        public EventCategory updateEventCategory (Integer id, @Valid EventCategoryCreateUpdateDto updateEventCategory) throws
        UniqueEventCategoryNameException {
            Optional<EventCategory> optionalEventCategory = eventCategoryRepository.findById(id);
            if (optionalEventCategory.isPresent()) {
                if (checkUniqueNameCreate(updateEventCategory)) {
                    addOwnerToEventCategory(updateEventCategory);
                    return eventCategoryRepository.saveAndFlush(castCreateDtoToEventCategory(updateEventCategory));
                }
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "can not find event category id" + id);
            }
            return castCreateDtoToEventCategory(updateEventCategory);
        }

        public boolean checkUniqueName (EventCategory eventCategory) throws UniqueEventCategoryNameException {
            boolean check;
            EventCategory eventCategory1 = eventCategoryRepository.findAllByEventCategoryName(eventCategory.getEventCategoryName());
            if (eventCategory1 == null || eventCategory.getEventCategoryId() == eventCategory1.getEventCategoryId()) {
                check = true;
            } else {
                check = false;
                throw new UniqueEventCategoryNameException("Event Category Name must be Unique");
            }
            return check;
        }


    public boolean checkUniqueNameCreate (EventCategoryCreateUpdateDto eventCategory) throws UniqueEventCategoryNameException {
        boolean check;
        EventCategory eventCategory1 = eventCategoryRepository.findAllByEventCategoryName(eventCategory.getEventCategoryName());
        if (eventCategory1 == null || eventCategory.getEventCategoryId() == eventCategory1.getEventCategoryId()) {
            check = true;
        } else {
            check = false;
            throw new UniqueEventCategoryNameException("Event Category Name must be Unique");
        }
        return check;
    }

    public UserGetDto castUserToUserGet(User user){
        UserGetDto userGetDto = new UserGetDto();
        userGetDto.setUserId(user.getUserId());
        userGetDto.setName(user.getName());
        userGetDto.setCreatedOn(user.getCreatedOn());
        userGetDto.setEmail(user.getEmail());
        userGetDto.setUpdatedOn(user.getUpdatedOn());
        userGetDto.setRole(user.getRole());
        return userGetDto;
    }

    public UserGetOwnerDto castUserToUserGetOwner(User user){
        UserGetOwnerDto userGetOwnerDto = new UserGetOwnerDto();
        userGetOwnerDto.setUserId(user.getUserId());
        userGetOwnerDto.setName(user.getName());
        userGetOwnerDto.setEmail(user.getEmail());
        return userGetOwnerDto;
    }

    public EventCategoryDto castEventCategoryDto (EventCategory eventCategory){
            EventCategoryDto eventCategoryDto = new EventCategoryDto();
            eventCategoryDto.setEventCategoryId(eventCategory.getEventCategoryId());
            eventCategoryDto.setEventCategoryName(eventCategory.getEventCategoryName());
            eventCategoryDto.setEventCategoryDescription(eventCategory.getEventCategoryDescription());
            eventCategoryDto.setEventDuration(eventCategory.getEventDuration());
            eventCategoryDto.setOwner(eventCategory.getOwner().stream()
                    .map(this::castUserToUserGetOwner).collect(Collectors.toList()));
            return eventCategoryDto;
    }

    public EventCategoryDto castEventCategoryDtoOptional (Optional<EventCategory> eventCategory){
        EventCategoryDto eventCategoryDto = new EventCategoryDto();
        eventCategoryDto.setEventCategoryId(eventCategory.get().getEventCategoryId());
        eventCategoryDto.setEventCategoryName(eventCategory.get().getEventCategoryName());
        eventCategoryDto.setEventCategoryDescription(eventCategory.get().getEventCategoryDescription());
        eventCategoryDto.setEventDuration(eventCategory.get().getEventDuration());
        eventCategoryDto.setOwner(eventCategory.get().getOwner().stream()
                .map(this::castUserToUserGetOwner).collect(Collectors.toList()));
        return eventCategoryDto;
    }

    }