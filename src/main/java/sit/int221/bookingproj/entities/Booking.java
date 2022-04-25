package sit.int221.bookingproj.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @JsonProperty("bookingId")
    @Column(name = "bookingId", nullable = false, length = 20)
    private String bookingId;

    @Column(name = "bookingName", length = 100)
    private String bookingName;

    @Column(name = "bookingEmail", length = 45)
    private String bookingEmail;


    public String getBookingEmail() {
        return bookingEmail;
    }

    public void setBookingEmail(String bookingEmail) {
        this.bookingEmail = bookingEmail;
    }

    public String getBookingName() {
        return bookingName;
    }

    public void setBookingName(String bookingName) {
        this.bookingName = bookingName;
    }

    public String getId() {
        return bookingId;
    }

    public void setId(String id) {
        this.bookingId = id;
    }
}