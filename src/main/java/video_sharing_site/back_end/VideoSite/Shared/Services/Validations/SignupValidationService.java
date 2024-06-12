package video_sharing_site.back_end.VideoSite.Shared.Services.Validations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import video_sharing_site.back_end.VideoSite.Entity.UsersEntity;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserEmailException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserEmailValidateException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserPasswordValidateException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserUsernameException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserUsernameValidateException;
import video_sharing_site.back_end.VideoSite.Repository.UsersRepository;

@Service
public class SignupValidationService {

    @Autowired
    private UsersRepository usersRepository;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[.@#$%^&+=!]).{8,}$";

    public void validateEmail(String email) throws UserEmailException, UserException {
        if (email == null || email.isEmpty())
            throw new UserEmailValidateException();

        if (!isValidEmail(email))
            throw new UserEmailValidateException();

        UsersEntity user = usersRepository.findByEmail(email);
        if (user != null)
            throw new UserEmailException();

    }

    public void validateUsername(String username) throws UserUsernameException, UserException {
        if (username == null || username.isEmpty()) {
            throw new UserUsernameValidateException();
        }
        UsersEntity user = usersRepository.findByUserName(username);
        if (user != null) {
            throw new UserUsernameException();
        }
    }

    public void validatePassword(String password) throws UserException {
        if (password == null || password.isEmpty()) {
            throw new UserPasswordValidateException();
        }
        if (!isValidPassword(password)) {
            throw new UserPasswordValidateException();
        }
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
