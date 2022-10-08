package sit.int221.bookingproj.services;

import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.bookingproj.controller.EventController;
import sit.int221.bookingproj.dtos.*;
import sit.int221.bookingproj.entities.Event;
import sit.int221.bookingproj.entities.EventCategory;
import sit.int221.bookingproj.entities.File;
import sit.int221.bookingproj.entities.User;
import sit.int221.bookingproj.exception.*;
import sit.int221.bookingproj.repositories.EventCategoryRepository;
import sit.int221.bookingproj.repositories.EventRepository;
import sit.int221.bookingproj.repositories.FileRepository;
import sit.int221.bookingproj.repositories.UserRepository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService{
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    Logger logger = LoggerFactory.getLogger(EventController.class);
    @ExceptionHandler(IllegalStateException.class)
    public void handleIllegalStateException() {}

    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgumentException() {}

    @ExceptionHandler(NotFoundException.class)
    public void handleNotFoundEventException(){}

    @ExceptionHandler(NotMatchEmailCreteEventException.class)
    public void handleNotMatchEmailCreteEventException(){}


    public Optional<User> getUserByToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = Integer.parseInt((String) authentication.getPrincipal());
        Optional<User> userWithRole = Optional.of(new User());
        userWithRole = userRepository.findById(userId);
        return userWithRole;
    }

    public List<EventGetDto> getAllEvent(){
        List<EventGetDto> eventGetDtos = new ArrayList<>();
        if(getUserByToken().get().getRole().toLowerCase().equals("admin")){
            eventGetDtos = eventRepository.findAll().stream().map(this::convertEntityToDto).collect(Collectors.toList());
        }
        else if(getUserByToken().get().getRole().toLowerCase().equals("student")){
            eventGetDtos = eventRepository.findAllByBookingEmail(getUserByToken().get().getEmail()).stream().map(this::convertEntityToDto).collect(Collectors.toList());
        }
        else if(getUserByToken().get().getRole().toLowerCase().equals("lecturer")){
            Optional<User> user = userRepository.findById(getUserByToken().get().getUserId());
            eventGetDtos = eventRepository.findAllByEventCategory_Owner(user.get()).stream().map(this::convertEntityToDto).collect(Collectors.toList());
        }
        return eventGetDtos;
    }

    public List<EventGetDto> castTypeToDto(List<Event> event){
        return event.stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    public EventGetDto getById(Integer id) throws NotFoundException, NonSelfGetDataException, LecuterPermissionException {
        List<EventGetDto> eventGetDtos = new ArrayList<>();
        Optional<Event> event = Optional.of(new Event());
        if(getUserByToken().get().getRole().equals("admin")){
            event = Optional.ofNullable(eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event not found")));
        }
        else if(getUserByToken().get().getRole().equals("student")){
            Optional<Event> eventFilter = Optional.of(new Event());
            eventFilter = Optional.ofNullable(eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event not found")));
            if(eventFilter.get().getBookingEmail().equals(getUserByToken().get().getEmail())){
                event = eventFilter;
            }
            else{
                throw new NonSelfGetDataException("Can not Get Event That You're not Owner");
            }
        }
        else if(getUserByToken().get().getRole().equals("lecturer")){
            Optional<User> user = userRepository.findById(getUserByToken().get().getUserId());
            eventGetDtos = eventRepository.findAllByEventCategory_Owner(user.get()).stream().map(this::convertEntityToDto).collect(Collectors.toList());
            for(int i = 0; i < eventGetDtos.size(); i++){
                if(id.equals(eventGetDtos.get(i).getEventId())){
                    return convertEntityToDto(event.get());
                }
            }
            throw new LecuterPermissionException("You don't have permission to get data that you're not lecturer");
        }

        event = Optional.ofNullable(eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event not found")));

        return convertEntityToDto(event.get());
    }

    public void deleteEvent(Integer id) throws EventCategoryIdNullException, NotFoundException, NonSelfGetDataException, LecuterPermissionException {
        Optional<Event> event = eventRepository.findById(id);
        if(getUserByToken().get().getRole().equals("admin")){
            if(event != null){
                eventRepository.deleteById(id);
            }
        }
        else if(getUserByToken().get().getRole().equals("student")){
            if(event != null && getUserByToken().get().getEmail().equals(event.get().getBookingEmail())){
                eventRepository.deleteById(id);
            }
            else{
                throw new NonSelfGetDataException("Can not Delete Event That You're not Owner");
            }
        }
        else if(getUserByToken().get().getRole().equals("lecturer")){
            throw new LecuterPermissionException("Lecturer can not do this");
        }
        else{
            throw new NotFoundException("Can not find for id " + id);
        }
    }

    public List<EventGetDto> getSearch(String dateStart, String dateEnd, String category, String word){
        List result = new ArrayList();
        List categoryFind = new ArrayList();
        List wordFind = new ArrayList();
        List dateFind = new ArrayList();
        List resultDateAndCategory = new ArrayList();
        String str1 = dateStart;
        String str2 = dateEnd;
        boolean findCategory = false;

        if(dateStart != "" && dateEnd != ""){
            Instant instant = Instant.parse(str1);
            Instant instant2 = Instant.parse(str2);
            dateFind = eventRepository.findAllByEventStartTimeBetween(instant, instant2 , Sort.by(Sort.Direction.DESC, "eventStartTime"));
        }
        else if(dateStart == "" && dateEnd == ""){
            dateFind = eventRepository.findAll(Sort.by(Sort.Direction.DESC, "eventStartTime"));
        }
        else if(dateStart == "" || dateEnd == ""){
            if(dateStart == ""){
                Instant instant = Instant.now();
                Instant instant2 = Instant.parse(str2);
                dateFind = eventRepository.findAllByEventStartTimeBetween(instant, instant2 , Sort.by(Sort.Direction.DESC, "eventStartTime"));
            }
            else{
                Instant instant = Instant.parse(str1);
                Instant instant2 = Instant.now();
                dateFind = eventRepository.findAllByEventStartTimeBetween(instant, instant2 , Sort.by(Sort.Direction.DESC, "eventStartTime"));
            }
        }
        if(category != ""){
            EventCategory eventCategory = eventCategoryRepository.findAllByEventCategoryName(category);
            categoryFind = eventRepository.findAllByEventCategory(Optional.ofNullable(eventCategory),  Sort.by(Sort.Direction.DESC, "eventStartTime"));
            if(categoryFind.isEmpty()){
                return castTypeToDto(result);
            }
        }
        if(word != ""){
            wordFind = eventRepository.findAllByBookingEmailContainingOrBookingNameContaining(word, word ,Sort.by(Sort.Direction.DESC, "eventStartTime") );
        }

        if(dateFind.isEmpty() || categoryFind.isEmpty()){
            resultDateAndCategory = ListUtils.union(dateFind, categoryFind);
        }
        else{
            resultDateAndCategory = ListUtils.intersection(dateFind, categoryFind);
        }
        if(wordFind.isEmpty()){
            result = ListUtils.union(resultDateAndCategory, wordFind);
        }
        else{
            result = ListUtils.intersection(resultDateAndCategory, wordFind);
        }
        return castTypeToDto(result);
    }


    public Optional<EventGetDto> create(EventCreateDto eventCreateDto) throws OverlapTimeException, EventCategoryIdNullException, EventTimeNullException, NotMatchEmailCreteEventException, LecuterPermissionException {
        if(eventCreateDto.getEventDuration() == null){
            Optional<EventCategory> eventCategory = eventCategoryRepository.findById(eventCreateDto.getEventCategoryId());
            if(checkEventStartTimeNull(eventCreateDto)){
                eventCreateDto.setEventDuration(eventCategory.get().getEventDuration());
            }
        }

        if (checkDuplicateEventTime(eventCreateDto)) {
            eventCreateDto.setBookingName(eventCreateDto.getBookingName().trim());
            eventCreateDto.setBookingEmail(eventCreateDto.getBookingEmail().trim());
            Event event = new Event();

            if(getUserByToken().get().getRole().equals("admin")){
                event = eventRepository.saveAndFlush(convertDtoToEvent(eventCreateDto));
                emailService.sendMail(event.getBookingEmail(), "Your Booking's Details at OASIP", emailService.createBody(eventCreateDto));
                emailService.sendPreConfiguredMail(emailService.createBody(eventCreateDto));
            }
            else if(getUserByToken().get().getRole().equals("student")){
                if(getUserByToken().get().getEmail().equals(eventCreateDto.getBookingEmail())){
                    event = eventRepository.saveAndFlush(convertDtoToEvent(eventCreateDto));
                    emailService.sendMail(event.getBookingEmail(), "Your Booking's Details at OASIP", emailService.createBody(eventCreateDto));
                    emailService.sendPreConfiguredMail(emailService.createBody(eventCreateDto));
                }
                else{
                    throw new NotMatchEmailCreteEventException("Booking Email Must Be The Same as  Student's Email");
                }
            }
            else if(getUserByToken().get().getRole().equals("lecturer")){
                throw new LecuterPermissionException("Lecturer can not do this");
            }
            return Optional.ofNullable(convertEntityToDto(event));
        }
        else{
            throw new OverlapTimeException("Start Time can not overlap");
        }
    }
    public void update(Integer id, EventUpdateDto eventUpdateDto) throws OverlapTimeException {
        Optional<Event> events = Optional.of(new Event());
        events = eventRepository.findById(id);
        if(events.isPresent()){
            if(eventUpdateDto.getEventStartTime() == null) {
                events.get().setEventStartTime(events.get().getEventStartTime());
                eventUpdateDto.setEventStartTime(events.get().getEventStartTime());
            }
                if(checkDuplicateEventTimeForUpdate(events,eventUpdateDto.getEventStartTime())){
                    events.ifPresent(event -> {
                        event.setEventNotes(eventUpdateDto.getEventNotes());
                    if(eventUpdateDto.getEventStartTime() != null){
                        event.setEventStartTime(eventUpdateDto.getEventStartTime());
                    }
                    if(getUserByToken().get().getRole().equals("admin")){
                        eventRepository.saveAndFlush(event);
                    }
                    else if(getUserByToken().get().getRole().equals("student")){
                        if(event.getBookingEmail().equals(getUserByToken().get().getEmail())){
                            eventRepository.saveAndFlush(event);
                        }
                        else{
                            try {
                                throw new NonSelfGetDataException("Can not Update Event That You're not Owner");
                            } catch (NonSelfGetDataException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    else if(getUserByToken().get().getRole().equals("lecturer")){
                        try {
                            throw new LecuterPermissionException("Lecturer can not do this");
                        } catch (LecuterPermissionException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not find eventId" + id);
        }
    }

    public Event convertDtoToEvent(EventCreateDto eventCreateDto){
        EventCategory eventCategory = new EventCategory();
        eventCategory = eventCategoryRepository.findById(eventCreateDto.getEventCategoryId()).orElse(null);
        Optional<File> file = fileRepository.findById(eventCreateDto.getFileId());
        Event event = new Event();
        event.setEventId(eventCreateDto.getEventId());
        event.setBookingName(eventCreateDto.getBookingName());
        event.setBookingEmail(eventCreateDto.getBookingEmail());
        event.setEventStartTime(eventCreateDto.getEventStartTime());
        event.setEventDuration(eventCreateDto.getEventDuration());
        event.setEventNotes(eventCreateDto.getEventNotes());
        event.setEventCategory(eventCategory);
        event.setFile(file.get());
        return event;
    }

    public EventGetDto convertEntityToDto(Event event){
        EventGetDto eventDto = new EventGetDto();
        EventCategoryInEventDto eventCategoryInEventDto = new EventCategoryInEventDto(event.getEventCategory().getEventCategoryId(), event.getEventCategory().getEventCategoryName(),event.getEventCategory().getEventDuration() );
        eventDto.setEventId(event.getEventId());
        eventDto.setBookingName(event.getBookingName());
        eventDto.setBookingEmail(event.getBookingEmail());
        eventDto.setEventStartTime(event.getEventStartTime());
        eventDto.setEventDuration(event.getEventDuration());
        eventDto.setEventNotes(event.getEventNotes());
        eventDto.setEventCategory(eventCategoryInEventDto);
        eventDto.setFile(event.getFile());
        return eventDto;
    }

    public boolean checkDuplicateEventTimeForUpdate(Optional<Event> event, Instant startTime) throws OverlapTimeException {
        boolean check;
        Event eventTimeIn = new Event();
        eventTimeIn = eventRepository.findAllByEventStartTimeBetweenAndEventCategory_EventCategoryId(startTime, startTime.plusSeconds(event.get().getEventDuration() * 60),event.get().getEventCategory().getEventCategoryId(), Sort.by(Sort.Direction.DESC, "eventStartTime"));
        if(eventTimeIn == null || event.get().getEventId() == eventTimeIn.getEventId()){
            check = true;
        }
        else{
            check = false;
            throw new OverlapTimeException("Start Time can not overlap");
        }
        return check;
    }
    public boolean checkEventStartTimeNull(EventCreateDto eventCreateDto) throws EventTimeNullException {
        boolean check = false;
        if(eventCreateDto.getEventStartTime() != null){
            check = true;
        }
        else{
            throw new EventTimeNullException("Event Start Time Can not be null");
        }
        return check;
    }

   public boolean checkDuplicateEventTime(EventCreateDto eventCreateDto){
        boolean check;
        Optional<EventCategory> eventCategory = Optional.ofNullable(eventCategoryRepository.findById(eventCreateDto.getEventCategoryId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "can not find event id" + eventCreateDto.getEventCategoryId())));
        String eventCategoryName = "";
        if(eventCategory.isPresent()){
            eventCategoryName = eventCategory.get().getEventCategoryName();
        }
        List<Event> events = eventRepository.findAllByEventStartTimeBetweenAndEventCategory_EventCategoryName(eventCreateDto.getEventStartTime() , eventCreateDto.getEventStartTime().plusSeconds(eventCreateDto.getEventDuration().longValue() * 60L) ,eventCategoryName ,Sort.by(Sort.Direction.DESC, "eventStartTime"));
        if(events.isEmpty()){
            check = true;
        }
        else{
            check = false;
        }
        return check;
    }

    public List<EventGetDto> checkBetween(Instant instant, Instant instant2){
        return castTypeToDto(eventRepository.findAllByEventStartTimeBetween(instant,instant2, Sort.by(Sort.Direction.DESC, "eventStartTime")));
    }
}