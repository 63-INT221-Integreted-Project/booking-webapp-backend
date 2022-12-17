package sit.int221.bookingproj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sit.int221.bookingproj.dtos.UserActionDto;
import sit.int221.bookingproj.dtos.UserGetDto;
import sit.int221.bookingproj.entities.User;
import sit.int221.bookingproj.exception.*;
import sit.int221.bookingproj.repositories.UserRepository;
import sit.int221.bookingproj.services.UserService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<UserGetDto> getAllUser(){
        return userService.getAllUser();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserGetDto getUserById(@PathVariable(name = "id") Integer id) throws NotFoundException, NonSelfGetDataException {
        return userService.getById(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public User createUser(@Valid @RequestBody UserActionDto userActionDto) throws UniqueEmailException, UniqueNameException {
        return userService.createUser(userActionDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> update(@RequestBody UserActionDto userActionDto, @PathVariable(name = "id") Integer id) throws UniqueEmailException, UniqueNameException{
        return userService.update(id, userActionDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<User> delete(@PathVariable Integer id) throws NotFoundException, OneEventCategoryOwnerException {
        return userService.deleteUser(id);
    }

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<UserGetDto> getUserByRole(@RequestParam(name = "role") String role) {
        return userService.findUserByRole(role);
    }


}
