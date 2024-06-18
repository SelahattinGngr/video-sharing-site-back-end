package video_sharing_site.back_end.VideoSite.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import video_sharing_site.back_end.VideoSite.Entity.FollowersEntity;
import video_sharing_site.back_end.VideoSite.Entity.UsersEntity;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserForbiddenException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserNotFoundException;
import video_sharing_site.back_end.VideoSite.Repository.FollowersRepository;
import video_sharing_site.back_end.VideoSite.Repository.UsersRepository;
import video_sharing_site.back_end.VideoSite.Shared.Services.jwt.TokenService;

@Service
public class FollowersService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private FollowersRepository followersRepository;

    public Map<String, Object> getFollowers(String token, String userName) {
        UsersEntity user = usersRepository.findByUserName(userName);
        if (user == null)
            throw new UserNotFoundException();
        List<FollowersEntity> followings = followersRepository.findByFollowedUserId(user);
        Map<String, Object> response = new HashMap<>();
        ArrayList<String> followers = new ArrayList<>();
        for (FollowersEntity following : followings) {
            followers.add(following.getFollowingUserId().getUserName());
        }
        response.put("followers", followers);
        return response;
    }

    public Map<String, Object> followUser(String token, String userName) {
        UsersEntity user = usersRepository.findByUserName(userName);
        if (user == null)
            throw new UserNotFoundException();
        String email = tokenService.getUserFromAccessToken(token);
        UsersEntity currentUser = usersRepository.findByEmail(email);
        if (currentUser.getId().longValue() == user.getId().longValue()) {
            throw new UserForbiddenException();
        }
        if (followersRepository.findByFollowedUserIdAndFollowingUserId(user, currentUser) != null) {
            return Map.of("message", "User followed successfully");
        }
        FollowersEntity followersEntity = new FollowersEntity();
        followersEntity.setFollowedUserId(user);
        followersEntity.setFollowingUserId(currentUser);
        followersRepository.save(followersEntity);
        return Map.of("message", "User followed successfully");
    }

    public Map<String, Object> unfollowUser(String token, String userName) {
        UsersEntity user = usersRepository.findByUserName(userName);
        if (user == null)
            throw new UserNotFoundException();
        String email = tokenService.getUserFromAccessToken(token);
        UsersEntity currentUser = usersRepository.findByEmail(email);
        if (currentUser.getId().longValue() == user.getId().longValue()) {
            throw new UserForbiddenException();
        }
        FollowersEntity followersEntity = followersRepository.findByFollowedUserIdAndFollowingUserId(user, currentUser);
        if (followersEntity == null) {
            return Map.of("message", "User unfollowed successfully");
        }
        followersRepository.delete(followersEntity);
        return Map.of("message", "User unfollowed successfully");
    }
}
