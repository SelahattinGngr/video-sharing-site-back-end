package video_sharing_site.back_end.VideoSite.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import video_sharing_site.back_end.VideoSite.Dto.UserDto;
import video_sharing_site.back_end.VideoSite.Entity.UsersEntity;
import video_sharing_site.back_end.VideoSite.Repository.UsersRepository;

@Service
public class AuthService {

    @Autowired
    UsersRepository usersRepository;

    public UsersEntity signup(UserDto user) {
        UsersEntity newUser = new UsersEntity();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setUserName(user.getUserName());
        newUser.setPassword(user.getPassword());
        newUser.setBirthday(user.getBirthday());
        return usersRepository.save(newUser);
    }
}
