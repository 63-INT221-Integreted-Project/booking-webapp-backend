package sit.int221.bookingproj.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.Instant;
import java.time.LocalDateTime;

@Data
public class UserGetDto {
    private Integer userId;
    private String name;
    private String email;
    private String role;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime updatedOn;

    public UserGetDto(Integer userId, String name, String email, String role, LocalDateTime createdOn, LocalDateTime updatedOn) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public  LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
}