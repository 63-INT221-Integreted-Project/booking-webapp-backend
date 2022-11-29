package sit.int221.bookingproj.services;

import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sit.int221.bookingproj.dtos.UserAzureDto;

@Service
public class AzureService {
    @Autowired
    public TokenService tokenService;

    public JSONObject generateToken(UserAzureDto userAzureDto){
        JSONObject json = new JSONObject();
        json.put("access_token", tokenService.tokenizeToken(userAzureDto));
        json.put("refresh_token", tokenService.tokenizeRefreshTokenAzure(userAzureDto));
        return json;
    }
}
