package video_sharing_site.back_end.VideoSite.Interceptor;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import video_sharing_site.back_end.VideoSite.Exception.BaseTokenExceptions;
import video_sharing_site.back_end.VideoSite.Exception.TokenExceptions.TokenInvalidException;
import video_sharing_site.back_end.VideoSite.Shared.Services.jwt.TokenService;

@Component
public class AccessTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenService tokenService;

    @Override
    @SuppressWarnings("null")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String accessTokenValue = request.getHeader("Authorization");
        try {
            if (accessTokenValue == null || !accessTokenValue.startsWith("Bearer "))
                throw new TokenInvalidException();
            String accessToken = accessTokenValue.split(" ")[1];
            if (tokenService.verifyAccessToken(accessToken) == null)
                throw new TokenInvalidException();
        } catch (TokenInvalidException e) {
            handleException(new BaseTokenExceptions().invalidException(), response);
            return false;
        } catch (Exception e) {
            handleException(new BaseTokenExceptions().invalidException(), response);
            return false;
        }
        return true;
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
