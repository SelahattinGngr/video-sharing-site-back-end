package video_sharing_site.back_end.VideoSite.Interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import video_sharing_site.back_end.VideoSite.Dto.UserDto;
import video_sharing_site.back_end.VideoSite.Exception.BaseUserExceptions;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserEmailException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserEmailValidateException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserPasswordValidateException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserUsernameException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserUsernameValidateException;
import video_sharing_site.back_end.VideoSite.Shared.Services.SignupValidationService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@Component
public class SignupInterceptor implements HandlerInterceptor {

    @Autowired
    private SignupValidationService validationService;

    @Override
    @SuppressWarnings("null")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(request.getInputStream());

            String email = jsonNode.get("email").asText();
            String username = jsonNode.get("userName").asText();
            String password = jsonNode.get("password").asText();
            String firstName = jsonNode.get("firstName").asText();
            String lastName = jsonNode.get("lastName").asText();
            LocalDate birthday = LocalDate.parse(jsonNode.get("birthday").asText());

            validationService.validateEmail(email);
            validationService.validateUsername(username);
            validationService.validatePassword(password);

            UserDto user = new UserDto(firstName, lastName, email, username, password, birthday);
            request.setAttribute("userDto", user);
            return true;
        } catch (UserEmailException e) {
            handleException(new BaseUserExceptions().emailException(), response);
            return false;
        } catch (UserUsernameException e) {
            handleException(new BaseUserExceptions().usernameException(), response);
            return false;
        } catch (UserException e) {
            handleException(new BaseUserExceptions().invalidException(), response);
            return false;
        } catch (UserEmailValidateException e) {
            handleException(new BaseUserExceptions().emailValidateException(), response);
            return false;
        } catch (UserUsernameValidateException e) {
            handleException(new BaseUserExceptions().usernameValidateException(), response);
            return false;
        } catch (UserPasswordValidateException e) {
            handleException(new BaseUserExceptions().passwordValidateException(), response);
            return false;
        }
    }

    private void handleException(ResponseEntity<Map<String, Object>> errorResponse, HttpServletResponse response)
            throws IOException {
        response.setStatus(errorResponse.getStatusCode().value());
        response.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(errorResponse.getBody());
        response.getWriter().write(json);
    }
}
