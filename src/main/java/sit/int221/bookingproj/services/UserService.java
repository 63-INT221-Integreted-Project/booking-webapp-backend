package sit.int221.bookingproj.services;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.bookingproj.dtos.*;
import sit.int221.bookingproj.entities.EventCategory;
import sit.int221.bookingproj.entities.User;
import sit.int221.bookingproj.enums.RoleEnum;
import sit.int221.bookingproj.exception.*;
import sit.int221.bookingproj.repositories.EventCategoryRepository;
import sit.int221.bookingproj.repositories.UserRepository;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class UserService {

    @ExceptionHandler(NotFoundException.class)
    public void handleNotFoundEventException(){}

    @ExceptionHandler(UniqueEmailException.class)
    public void handleUniqueEmailException(){}

    @ExceptionHandler(UniqueNameException.class)
    public void handleUniqueNameException(){}

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public EventCategoryRepository eventCategoryRepository;

    public List<UserGetDto> getAllUser(){
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    public UserGetDto getById(Integer id) throws NotFoundException, NonSelfGetDataException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = Integer.parseInt((String) authentication.getPrincipal());
        System.out.println("get userID " + userId);
        Optional<User> userFind = userRepository.findById(userId);
        Optional<User> userFindRole = Optional.of(new User());
        Optional<User> user = Optional.of(new User());
        if(userFind.isEmpty()){
            user = Optional.ofNullable(userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found")));
        }
        else{
            userFindRole = userRepository.findById(userId);
            if(userFindRole.get().getRole().toLowerCase() == "admin"){
                user = Optional.ofNullable(userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found")));
            }
            else{
                if(id == userFindRole.get().getUserId()){
                    user = Optional.ofNullable(userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found")));
                }
                else{
                    throw new NonSelfGetDataException("Can not get data of other");
                }
            }
        }
        return convertEntityToDto(user.get());
    }

    public User createUser(@Valid UserActionDto newUser) throws UniqueEmailException, UniqueNameException {
        Argon2 argon2 = Argon2Factory.create(
                Argon2Factory.Argon2Types.ARGON2id,
                16,
                16);
        if(checkUniqueName(newUser)){
            if(checkUniqueEmail(newUser)) {
                newUser.setName(newUser.getName().trim());
                newUser.setEmail(newUser.getEmail().trim());
                newUser.setRole(newUser.getRole().trim());
//                newUser.setPassword(newUser.getPassword().trim());
                if (newUser.getRole().toUpperCase().equals(RoleEnum.STUDENT.toString()) || newUser.getRole().toUpperCase().equals(RoleEnum.LECTURER.toString()) || newUser.getRole().toUpperCase().equals(RoleEnum.ADMIN.toString())) {
                    newUser.setRole(newUser.getRole().toLowerCase());
                } else {
                    newUser.setRole(RoleEnum.STUDENT.name().toLowerCase());
                }
                String hash = argon2.hash(22, 65536, 1, newUser.getPassword());
                newUser.setPassword(hash);
                return userRepository.saveAndFlush(castUserDtoCreate(newUser));
            }
            else{
                throw new UniqueEmailException("email must be unique");
            }
        }
        else{
            throw new UniqueNameException("user name must be unique");
        }
    }

    public Optional<User> update(Integer id, @Valid UserActionDto userActionDto) throws UniqueEmailException, UniqueNameException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = Integer.parseInt((String) authentication.getPrincipal());
        Optional<User> user = Optional.of(new User());
        user = userRepository.findById(id);
        User userReturn = new User();
        userActionDto.setUserId(id);
        if(user.isPresent()){
            if(checkUniqueName(userActionDto)){
                if(checkUniqueEmail(userActionDto)){
                    if (userActionDto.getRole().toUpperCase().equals(RoleEnum.STUDENT.toString()) || userActionDto.getRole().toUpperCase().equals(RoleEnum.LECTURER.toString()) || userActionDto.getRole().toUpperCase().equals(RoleEnum.ADMIN.toString())) {
                        userActionDto.setRole(userActionDto.getRole().toLowerCase());
                    } else {
                        userActionDto.setRole(RoleEnum.STUDENT.name().toLowerCase());
                    }
                    user.ifPresent(userData -> {
                        if(userActionDto.getName() != null) userData.setName(userActionDto.getName().trim());
                        if(userActionDto.getRole() != null) userData.setRole(userActionDto.getRole().trim());
                        if(userActionDto.getEmail() != null) userData.setEmail(userActionDto.getEmail().trim());
                        userRepository.saveAndFlush(userData);
                    });
                }
                else{
                    throw new  UniqueEmailException("Email must be unique");
                }
            }
            else{
                throw new  UniqueNameException("Name must be unique");
            }

            return user;
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not find user" + id);
        }
    }

    public Optional<User> deleteUser(Integer id) throws NotFoundException, OneEventCategoryOwnerException {
        Optional<User> user = userRepository.findById(id);
        // ทำการลบผู้ใช้
        if(user.get().getRole().equals("lecturer")){
            List<EventCategory> eventCategories = eventCategoryRepository.findAllByOwner(user.get());
            // เช็คว่ามีคนมากกว่า 1 หรือเปล่า
            if(eventCategories.size() > 1){
                for(int i = 0; i < eventCategories.size(); i++){
                    List ownerList = eventCategories.get(i).getOwner();
                    if(ownerList.size() == 1){
                        throw new OneEventCategoryOwnerException("Event Category Owner Must Be More Than 1");
                    }
                    else {
                        for (int j = 0; j < ownerList.size(); j++) {
                            if (ownerList.get(j).equals(user.get())) {
                                ownerList.remove(j);
                                eventCategories.get(i).setOwner(ownerList);
                            }
                        }
                    }
                    eventCategories.get(i).setOwner(ownerList);
                    eventCategoryRepository.saveAndFlush(eventCategories.get(i));
                }
                // มากกว่า 1 ลบได้
                userRepository.delete(user.get());
            } else {
                List ownerList = eventCategories.get(0).getOwner();
                if(ownerList.size() == 1){
                    throw new OneEventCategoryOwnerException("Event Category Owner Must Be More Than 1");
                }
                else {
                    for (int j = 0; j < ownerList.size(); j++) {
                        if (ownerList.get(j).equals(user.get())) {
                            ownerList.remove(j);
                            eventCategories.get(0).setOwner(ownerList);
                        }
                    }
                }
                eventCategories.get(0).setOwner(ownerList);
                eventCategoryRepository.saveAndFlush(eventCategories.get(0));
            }
        }
        else if(user != null){
            userRepository.delete(user.get());
            return user;
        }
        else{
            throw new NotFoundException("Can not find for id " + id);
        }
        return null;
    }

    public boolean checkUniqueName(UserActionDto user) throws UniqueNameException {
        boolean check;
        User userFind = userRepository.findAllByName(user.getName().trim());
        if(userFind == null || userFind.getUserId().equals(user.getUserId())){
            check = true;
        }
        else{
            check = false;
            throw new UniqueNameException("Name must be Unique");
        }
        return check;
    }

    public boolean checkUniqueEmail(UserActionDto user) throws UniqueEmailException {
        boolean check;
        User userFind = userRepository.findAllByEmail(user.getEmail().trim());
        if(userFind == null || userFind.getUserId().equals(user.getUserId())){
            check = true;
        }
        else{
            check = false;
            throw new UniqueEmailException("Email must be Unique");
        }
        return check;
    }

    public UserGetDto convertEntityToDto(User userInput){
        UserGetDto userGetDto = new UserGetDto(userInput.getUserId(), userInput.getName(), userInput.getEmail(), userInput.getRole(), userInput.getCreatedOn(), userInput.getCreatedOn());
        return userGetDto;
    }

    public User castUserDtoCreate(UserActionDto userAction){
        User user = new User();
        user.setName(userAction.getName());
        user.setRole(userAction.getRole());
        user.setEmail(userAction.getEmail());
        user.setPassword(userAction.getPassword());
        user.setCreatedOn(null);
        user.setUpdatedOn(null);
        return user;
    }

    public UserLoginDto castUserToUserLogin(User user){
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail(user.getEmail());
        userLoginDto.setPassword(user.getPassword());
        return userLoginDto;
    }
    public User castUserDto(UserActionDto userAction){
        User user = new User();
        user.setName(userAction.getName());
        user.setRole(userAction.getRole());
        user.setEmail(userAction.getEmail());
        user.setCreatedOn(null);
        user.setUpdatedOn(null);
        return user;
    }

    public List<UserGetDto> findUserByRole(String role){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = Integer.parseInt((String) authentication.getPrincipal());
        Optional<User> userFind = userRepository.findById(userId);
        if(userFind.get().getRole().equals("admin")){
            return userRepository.findAllByRole(role).stream().map(this::convertEntityToDto).collect(Collectors.toList());
        }
        return null;

    }
}