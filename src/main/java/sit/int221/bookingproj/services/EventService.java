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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartFile;
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

import java.io.IOException;
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
    private FileService fileService;

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

    public void deleteEvent(Integer id) throws EventCategoryIdNullException, NotFoundException, NonSelfGetDataException, LecuterPermissionException, IOException {
        Optional<Event> event = eventRepository.findById(id);
        if(getUserByToken().get().getRole().equals("admin")){
            if(event != null){
                if(event.get().getFile() != null) {
                    deleteEventDeleteFile(event.get());
                }
                eventRepository.deleteById(id);
            }
        }
        else if(getUserByToken().get().getRole().equals("student")){
            if(event != null && getUserByToken().get().getEmail().equals(event.get().getBookingEmail())){
                if(event.get().getFile() != null){
                    deleteEventDeleteFile(event.get());
                }
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

    public void deleteEventDeleteFile(Event event) throws IOException {
        if(!event.getFile().getFileId().equals(null))
        deleteFile(event.getFile().getFileId());
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
            if(getUserByToken().isEmpty()){
                event = eventRepository.saveAndFlush(convertDtoToEvent(eventCreateDto));
                emailService.sendMail(event.getBookingEmail(), "Your Booking's Details at OASIP", emailService.createBody(eventCreateDto));
                emailService.sendPreConfiguredMail(emailService.createBody(eventCreateDto));
            }
            // for azure token that don't have role
            else if(getUserByToken().get().getRole().equals("") || getUserByToken().get().getRole().equals(null)){
                Event eventCreate = convertDtoToEvent(eventCreateDto);
                event = eventRepository.saveAndFlush(eventCreate);
                emailService.sendMail(event.getBookingEmail(), "Your Booking's Details at OASIP", emailService.createBody(eventCreateDto));
                emailService.sendPreConfiguredMail(emailService.createBody(eventCreateDto));
            }
            else if(getUserByToken().get().getRole().equals("admin")){
                Event eventCreate = convertDtoToEvent(eventCreateDto);
                event = eventRepository.saveAndFlush(eventCreate);
                emailService.sendMail(event.getBookingEmail(), "Your Booking's Details at OASIP", emailService.createBody(eventCreateDto));
                emailService.sendPreConfiguredMail(emailService.createBody(eventCreateDto));
            }
            else if(getUserByToken().get().getRole().equals("student")){
                if(getUserByToken().get().getEmail().equals(eventCreateDto.getBookingEmail())){
                    Event eventCreate = convertDtoToEvent(eventCreateDto);
                    event = eventRepository.saveAndFlush(eventCreate);
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
                if(checkDuplicateEventTimeForUpdate(events.get(),eventUpdateDto.getEventStartTime())){
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
        Event event = new Event();
        EventCategory eventCategory = new EventCategory();
        eventCategory = eventCategoryRepository.findById(eventCreateDto.getEventCategoryId()).orElse(null);
        if(eventCreateDto.getFileId() != null){
            Optional<File> file = fileRepository.findById(eventCreateDto.getFileId());
            event.setFile(file.get());
        }
        event.setEventId(eventCreateDto.getEventId());
        event.setBookingName(eventCreateDto.getBookingName());
        event.setBookingEmail(eventCreateDto.getBookingEmail());
        event.setEventStartTime(eventCreateDto.getEventStartTime());
        event.setEventDuration(eventCreateDto.getEventDuration());
        event.setEventNotes(eventCreateDto.getEventNotes());
        event.setEventCategory(eventCategory);
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

    public boolean checkDuplicateEventTimeForUpdate(Event event, Instant startTime) throws OverlapTimeException {
        boolean check;
        Event eventTimeIn = new Event();
        eventTimeIn = eventRepository.findAllByEventStartTimeBetweenAndEventCategory_EventCategoryId(startTime, startTime.plusSeconds(event.getEventDuration() * 60),event.getEventCategory().getEventCategoryId(), Sort.by(Sort.Direction.DESC, "eventStartTime"));
        if(eventTimeIn == null || event.getEventId() == eventTimeIn.getEventId()){
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
        List<Event> eventByTime = eventRepository.findAllByEventStartTimeBetween(instant,instant2, Sort.by(Sort.Direction.DESC, "eventStartTime"));
        List<Event> eventGet = new ArrayList<>();
        List<EventGetDto> eventGetDtos = new ArrayList<>();
        if(getUserByToken().get().getRole().toLowerCase().equals("admin")){
            eventGet = eventRepository.findAll();
            eventGetDtos = ListUtils.intersection(eventByTime, eventGet).stream().map(this::convertEntityToDto).collect(Collectors.toList());
        }
        else if(getUserByToken().get().getRole().toLowerCase().equals("student")){
            eventGet = eventRepository.findAllByBookingEmail(getUserByToken().get().getEmail());
            eventGetDtos = ListUtils.intersection(eventByTime, eventGet).stream().map(this::convertEntityToDto).collect(Collectors.toList());
        }
        else if(getUserByToken().get().getRole().toLowerCase().equals("lecturer")){
            Optional<User> user = userRepository.findById(getUserByToken().get().getUserId());
            eventGet = eventRepository.findAllByEventCategory_Owner(user.get());
            eventGetDtos = ListUtils.intersection(eventByTime, eventGet).stream().map(this::convertEntityToDto).collect(Collectors.toList());
        }
        return eventGetDtos;
//        return castTypeToDto(eventRepository.findAllByEventStartTimeBetween(instant,instant2, Sort.by(Sort.Direction.DESC, "eventStartTime")));
    }

    public Optional<EventGetDto> createWithFile(EventCreateDto eventCreateDto, MultipartFile multipartFile) throws NotMatchEmailCreteEventException, LecuterPermissionException, OverlapTimeException, EventTimeNullException, IOException, FileSizeTooLargeException {

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

            if(checkSizeOfFile(multipartFile) == true){
                eventCreateDto.setFileId(uploadFileAndCreateEvent(multipartFile));
            }
            else{
                eventCreateDto.setFileId(null);
            }

//            if(getUserByToken().get().getRole().equals(){
//                event = eventRepository.saveAndFlush(convertDtoToEvent(eventCreateDto));
//                emailService.sendMail(event.getBookingEmail(), "Your Booking's Details at OASIP", emailService.createBody(eventCreateDto));
//                emailService.sendPreConfiguredMail(emailService.createBody(eventCreateDto));
//            }
            // for azure token that don't have role
            try{
                if(getUserByToken().get().getRole().equals("") || getUserByToken().get().getRole().equals(null)){
                    event = eventRepository.saveAndFlush(convertDtoToEvent(eventCreateDto));
                    emailService.sendMail(event.getBookingEmail(), "Your Booking's Details at OASIP", emailService.createBody(eventCreateDto));
                    emailService.sendPreConfiguredMail(emailService.createBody(eventCreateDto));
                }

                else if(getUserByToken().get().getRole().equals("admin")){
                    event = eventRepository.saveAndFlush(convertDtoToEvent(eventCreateDto));
                    emailService.sendMail(event.getBookingEmail(), "Your Booking's Details at OASIP", emailService.createBody(eventCreateDto));
                    emailService.sendPreConfiguredMail(emailService.createBody(eventCreateDto));
                }
                else if(getUserByToken().get().getRole().equals("student")){
                    if(getUserByToken().get().getEmail().equals(eventCreateDto.getBookingEmail())){
                        Event eventCreate = new Event();
                        eventCreate = convertDtoToEvent(eventCreateDto);
                        event = eventRepository.saveAndFlush(eventCreate);
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
            }
            catch (Exception e){
                event = eventRepository.saveAndFlush(convertDtoToEvent(eventCreateDto));
                emailService.sendMail(event.getBookingEmail(), "Your Booking's Details at OASIP", emailService.createBody(eventCreateDto));
                emailService.sendPreConfiguredMail(emailService.createBody(eventCreateDto));
            }
            return Optional.ofNullable(convertEntityToDto(event));
        }
        else{
            throw new OverlapTimeException("Start Time can not overlap");
        }
    }

    public boolean checkSizeOfFile(MultipartFile multipartFile) throws FileSizeTooLargeException{
        if(multipartFile == null){
            return false;
        }
        else if (multipartFile.getSize() > 10485760){
            throw new FileSizeTooLargeException("File Size Too Large");
        }
        return true;
    }

    public Integer uploadFileAndCreateEvent(MultipartFile multipartFile) throws IOException {
        Event event = new Event();
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        long size = multipartFile.getSize();
        String fileCode = fileService.uploadFile(fileName, multipartFile);
        File file = new File();
        file.setFileName(fileName);
        file.setFileSize(Long.toString(size));
        file.setFilePath("/downloadFile/" + fileCode);
        File fileUpload = fileRepository.saveAndFlush(file);
        return fileUpload.getFileId();
    }


    public void updateWithFile(Integer id, EventUpdateDto eventUpdateDto, MultipartFile multipartFile) throws IOException, OverlapTimeException {
        Optional<Event> events = Optional.of(new Event());
        events = eventRepository.findById(id);
        if(events.get() != null){
            if(eventUpdateDto.getEventStartTime() == null) {
                events.get().setEventStartTime(events.get().getEventStartTime());
                eventUpdateDto.setEventStartTime(events.get().getEventStartTime());
            }
            if(checkDuplicateEventTimeForUpdate(events.get(),eventUpdateDto.getEventStartTime())){
                    events.get().setEventNotes(eventUpdateDto.getEventNotes());
                    if(eventUpdateDto.getEventStartTime() != null){
                        events.get().setEventStartTime(eventUpdateDto.getEventStartTime());
                    }
                    if(getUserByToken().get().getRole().equals("admin")){
                        try {
                            Event eventUpdate = uploadAndDelete(events.get(), multipartFile);
                            System.out.println(eventUpdate.toString());
                            eventRepository.saveAndFlush(eventUpdate);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (FileSizeTooLargeException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    else if(getUserByToken().get().getRole().equals("student")){
                        if(events.get().getBookingEmail().equals(getUserByToken().get().getEmail())){
                            try {
                                Event eventUpdate = uploadAndDelete(events.get(), multipartFile);
                                eventRepository.saveAndFlush(eventUpdate);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (FileSizeTooLargeException e) {
                                throw new RuntimeException(e);
                            }
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
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not find eventId" + id);
        }
    }

    public Event uploadAndDelete(Event event, MultipartFile multipartFile) throws IOException, FileSizeTooLargeException {
        if(multipartFile == null){
            return event;
        }
        else if(checkSizeOfFile(multipartFile)) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            long size = multipartFile.getSize();
            String fileCode = fileService.uploadFile(fileName, multipartFile);
            File file = new File();
            file.setFileName(fileName);
            file.setFileSize(Long.toString(size));
            file.setFilePath("/downloadFile/" + fileCode);
            File fileUpload = fileRepository.saveAndFlush(file);
            if(event.getFile() != null){
                deleteFile(event.getFile().getFileId());
            }
            event.setFile(fileUpload);
            return event;
        }
        return event;
    }


    public void deleteFile(Integer fileId) throws IOException {
        Optional<File> file =fileRepository.findById(fileId);
        fileService.deleteFile(file.get());
    }

}