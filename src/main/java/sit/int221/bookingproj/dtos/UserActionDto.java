package sit.int221.bookingproj.dtos;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserActionDto {
    private Integer userId;

    @NotBlank(message = "can not blank")
    @Size(max = 100, message = "length exceeded the size")
    private String name;

    @NotBlank(message = "can not blank")
    @Email(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$", message = "Email is invalid")
    @Size(max = 50, message = "length exceeded the size")
    private String email;

    @Size(max = 50, message = "length exceeded the size")
    private String role;

    @Size(max = 14, min = 8, message = "length exceeded the size")
    private String password;
}