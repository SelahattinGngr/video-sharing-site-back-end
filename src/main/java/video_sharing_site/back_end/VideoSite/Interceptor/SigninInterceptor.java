package video_sharing_site.back_end.VideoSite.Interceptor;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import video_sharing_site.back_end.VideoSite.Dto.UserDTO;
import video_sharing_site.back_end.VideoSite.Exception.BaseUserExceptions;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserEmailValidateException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserInvalidException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserNotFoundException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserPasswordValidateException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserUsernameValidateException;
import video_sharing_site.back_end.VideoSite.Shared.Config.LogConfig;
import video_sharing_site.back_end.VideoSite.Shared.Services.Validations.SigninValidationService;

@Component
public class SigninInterceptor implements HandlerInterceptor {

    @Autowired
    private SigninValidationService validationService;

    @Autowired
    private LogConfig logConfig;

    @Override
    @SuppressWarnings("null")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(request.getInputStream());
        ResponseEntity<Map<String, Object>> errorResponse;
        try {
            UserDTO user = new UserDTO();
            String email = jsonNode.get("email").asText();
            String username = jsonNode.get("userName").asText();
            String password = jsonNode.get("password").asText();

            if ((email.isEmpty() && username.isEmpty()) || password.isEmpty()) {
                throw new UserInvalidException();
            }
            if (!email.isEmpty()) {
                validationService.validateEmail(email);
                user.setEmail(email);
            } else {
                validationService.validateUsername(username);
                user.setUserName(username);
            }
            validationService.validatePassword(password);
            user.setPassword(password);
            request.setAttribute("userDto", user);
            return true;
        } catch (UserInvalidException e) {
            errorResponse = new BaseUserExceptions().invalidException();
        } catch (UserNotFoundException e) {
            errorResponse = new BaseUserExceptions().notFoundException();
        } catch (UserEmailValidateException e) {
            errorResponse = new BaseUserExceptions().emailValidateException();
        } catch (UserUsernameValidateException e) {
            errorResponse = new BaseUserExceptions().usernameValidateException();
        } catch (UserPasswordValidateException e) {
            errorResponse = new BaseUserExceptions().passwordValidateException();
        }
        logConfig.log(request.getMethod(), getClass().getName(), errorResponse.getBody().get("Error").toString(), null);
        return false;
    }
}
