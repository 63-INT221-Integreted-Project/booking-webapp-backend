package sit.int221.bookingproj.controller;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sit.int221.bookingproj.dtos.UserAzureDto;
import sit.int221.bookingproj.services.AzureService;

@RestController
public class AzureController {

    @Autowired
    public AzureService azureService;

    @PostMapping("/api/auth/login-azure")
    public JSONObject getUser(@RequestBody UserAzureDto userAzureDto){
        return azureService.generateToken(userAzureDto);
    }
}