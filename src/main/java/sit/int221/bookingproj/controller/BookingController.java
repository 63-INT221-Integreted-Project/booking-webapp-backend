package sit.int221.bookingproj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sit.int221.bookingproj.entities.Booking;
import sit.int221.bookingproj.repositories.BookingRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    public BookingRepository bookingRepository;

    @GetMapping("/")
    public List getBooking(){
        return bookingRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Booking> findById(@PathVariable String id){
        return bookingRepository.findById(id);
    }

    @PostMapping("/")
    public Booking createBooking(@RequestBody Booking booking){
        return bookingRepository.saveAndFlush(booking);
    }

    @PutMapping("/{id}")
    public Booking updateBooking(@PathVariable String id, @RequestBody Booking updateBooking){
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        if(!optionalBooking.isPresent()){
            return updateBooking;
        }
        return bookingRepository.saveAndFlush(updateBooking);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteBooking(@PathVariable String id){
        bookingRepository.deleteById(id);
    }




}
