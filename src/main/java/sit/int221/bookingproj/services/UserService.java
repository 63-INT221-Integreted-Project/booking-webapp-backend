package sit.int221.bookingproj.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import sit.int221.bookingproj.dtos.*;
import sit.int221.bookingproj.entities.User;
import sit.int221.bookingproj.enums.RoleEnum;
import sit.int221.bookingproj.exception.NotFoundException;
import sit.int221.bookingproj.exception.UniqueEmailException;
import sit.int221.bookingproj.exception.UniqueEventCategoryNameException;
import sit.int221.bookingproj.exception.UniqueNameException;
import sit.int221.bookingproj.repositories.UserRepository;

import javax.validation.Valid;
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

    public List<UserGetDto> getAllUser(){
        return userRepository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream().map(this::convertEntityToDto).collect(Collectors.toList());
    }

    public UserGetDto getById(Integer id) throws NotFoundException {
        Optional<User> user = Optional.of(new User());
        user = Optional.ofNullable(userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found")));
        return convertEntityToDto(user.get());
    }

    public User createUser(@Valid UserActionDto newUser) throws UniqueEmailException, UniqueNameException {
        if(checkUniqueName(newUser)){
            if(checkUniqueEmail(newUser)) {
                newUser.setName(newUser.getName().trim());
                newUser.setEmail(newUser.getEmail().trim());
                newUser.setRole(newUser.getRole().trim());
                if (newUser.getRole().toUpperCase().equals(RoleEnum.STUDENT.toString()) || newUser.getRole().toUpperCase().equals(RoleEnum.LECTURER.toString()) || newUser.getRole().toUpperCase().equals(RoleEnum.ADMIN.toString())) {
                    newUser.setRole(newUser.getRole().toLowerCase());
                } else {
                    newUser.setRole(RoleEnum.STUDENT.name().toLowerCase());
                }
                return userRepository.saveAndFlush(castUserDto(newUser));
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

    public Optional<User> deleteUser(Integer id) throws NotFoundException {
        Optional<User> user = userRepository.findById(id);
        if(user != null){
            userRepository.deleteById(id);
            return user;
        }
        else{
            throw new NotFoundException("Can not find for id " + id);
        }
    }

    public boolean checkUniqueName(UserActionDto user) throws UniqueNameException {
        boolean check;
        User userFind = userRepository.findAllByName(user.getName());
        if(userFind == null || userFind.getUserId().equals(user.getUserId())){
//            System.out.println(user.getUserId() + " " + userFind.getUserId());
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
        User userFind = userRepository.findAllByEmail(user.getEmail());
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

    public User castUserDto(UserActionDto userAction){
        User user = new User();
        user.setName(userAction.getName());
        user.setRole(userAction.getRole());
        user.setEmail(userAction.getEmail());
        user.setCreatedOn(null);
        user.setUpdatedOn(null);
        return user;
    }
}