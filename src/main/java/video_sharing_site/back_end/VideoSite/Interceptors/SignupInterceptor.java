package video_sharing_site.back_end.VideoSite.Interceptors;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import video_sharing_site.back_end.VideoSite.Entity.UsersEntity;
import video_sharing_site.back_end.VideoSite.Exception.BaseUserExceptions;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserEmailException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserUsernameException;
import video_sharing_site.back_end.VideoSite.Repository.UsersRepository;

@Component
public class SignupInterceptor implements HandlerInterceptor {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    @SuppressWarnings("null")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(request.getInputStream());

            String email = jsonNode.get("email").asText();
            String username = jsonNode.get("userName").asText();
            {
                if (email == null)
                    throw new UserException();
                UsersEntity user = usersRepository.findByEmail(email);
                if (user != null)
                    throw new UserEmailException();
            }
            {
                if (username == null)
                    throw new UserException();
                UsersEntity user = usersRepository.findByUsername(username);
                if (user != null)
                    throw new UserUsernameException();
            }
            return true;
        } catch (UserEmailException e) {
            handleException(new BaseUserExceptions().emailException(), response);
            return false;
        } catch (UserUsernameException e) {
            handleException(new BaseUserExceptions().usernameException(), response);
            return false;
        } catch (UserException e) {
            handleException(new BaseUserExceptions().exception("signing up, username or e-mail is empty."), response);
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
