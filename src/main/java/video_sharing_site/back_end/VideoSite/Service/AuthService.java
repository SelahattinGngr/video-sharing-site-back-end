package video_sharing_site.back_end.VideoSite.Service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import video_sharing_site.back_end.VideoSite.Dto.UserDTO;
import video_sharing_site.back_end.VideoSite.Entity.UsersEntity;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserInvalidException;
import video_sharing_site.back_end.VideoSite.Repository.UsersRepository;
import video_sharing_site.back_end.VideoSite.Shared.Services.PasswordService;
import video_sharing_site.back_end.VideoSite.Shared.Services.jwt.TokenService;

@Service
public class AuthService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private TokenService tokenService;

    public UsersEntity signup(UserDTO user) {
        UsersEntity newUser = new UsersEntity();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setUserName(user.getUserName());
        newUser.setPassword(passwordService.encodePassword(user.getPassword()));
        newUser.setBirthday(user.getBirthday());
        return usersRepository.save(newUser);
    }

    public UsersEntity signin(UserDTO user) {
        UsersEntity userEntity;
        if (user.getEmail() != null)
            userEntity = usersRepository.findByEmail(user.getEmail());
        else
            userEntity = usersRepository.findByUserName(user.getUserName());
        if (!passwordService.matches(user.getPassword(), userEntity.getPassword()))
            throw new UserInvalidException();
        if (userEntity.getRefreshToken() != null)
            redisTemplate.delete(userEntity.getRefreshToken());
        UserDTO redisDto = entityToDto(userEntity);
        redisTemplate.opsForValue().set(redisDto.getRefreshToken(), redisDto);
        userEntity.setRefreshToken(redisDto.getRefreshToken());
        usersRepository.save(userEntity);
        return userEntity;
    }

    private UserDTO entityToDto(UsersEntity userEntity) {
        Long id = userEntity.getId();
        String username = userEntity.getUserName();
        String email = userEntity.getEmail();
        String firstName = userEntity.getFirstName();
        String lastName = userEntity.getLastName();
        boolean role = userEntity.isRole();
        String password = userEntity.getPassword();
        LocalDate birthday = userEntity.getBirthday();
        String refreshToken = tokenService.createRefreshToken(email, username);
        return new UserDTO(id, role, firstName, lastName, email, username, password, birthday, refreshToken);
    }
}
