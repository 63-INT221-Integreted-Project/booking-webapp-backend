package sit.int221.bookingproj.dtos;

import lombok.Data;

@Data
public class UserGetOwnerDto {

    public Integer userId;
    private String name;
    private String email;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer id) {
        this.userId = id;
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
}
