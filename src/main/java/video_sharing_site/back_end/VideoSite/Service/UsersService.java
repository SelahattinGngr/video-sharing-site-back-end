package video_sharing_site.back_end.VideoSite.Service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import video_sharing_site.back_end.VideoSite.Entity.UsersEntity;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserForbiddenException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserNotFoundException;
import video_sharing_site.back_end.VideoSite.Repository.UsersRepository;
import video_sharing_site.back_end.VideoSite.Shared.Services.jwt.TokenService;

@Service
public class UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TokenService tokenService;

    public Map<String, Object> getUsers(String token) {
        String email = tokenService.getUserFromAccessToken(token);
        UsersEntity usersEntity = usersRepository.findByEmail(email);
        if (!usersEntity.isRole())
            throw new UserForbiddenException();
        Map<String, Object> response = new HashMap<>();
        response.put("users", usersRepository.findAll());
        return response;
    }

    public Map<String, Object> getUser(String userName) {
        UsersEntity user = usersRepository.findByUserName(userName);
        if (user == null)
            throw new UserNotFoundException();

        Map<String, Object> userData = new HashMap<>();
        userData.put("userName", user.getUserName());
        userData.put("firstName", user.getFirstName());
        userData.put("lastName", user.getLastName());
        userData.put("role", user.isRole());
        userData.put("followersCount", user.getFollowersCount());

        Map<String, Object> response = new HashMap<>();
        response.put("user", userData);
        return response;
    }
}
