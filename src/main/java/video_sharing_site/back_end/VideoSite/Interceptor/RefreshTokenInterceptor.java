package video_sharing_site.back_end.VideoSite.Interceptor;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import video_sharing_site.back_end.VideoSite.Exception.BaseTokenExceptions;
import video_sharing_site.back_end.VideoSite.Shared.Config.LogConfig;
import video_sharing_site.back_end.VideoSite.Shared.Services.jwt.TokenService;

@Component
public class RefreshTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private LogConfig logConfig;

    @Autowired
    private TokenService tokenService;

    @Override
    @SuppressWarnings("null")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String refreshTokenValue = request.getHeader("Authorization");
        try {
            if (refreshTokenValue != null && refreshTokenValue.startsWith("RefreshToken ")) {
                String refreshToken = refreshTokenValue.split(" ")[1];
                if (tokenService.verifyRefreshToken(refreshToken) == null)
                    throw new JwtException("Invalid refresh token");
            } else {
                throw new JwtException("Invalid refresh token");
            }
            return true;
        } catch (JwtException e) {
            handleException(new BaseTokenExceptions().invalidException(), response);
            logConfig.log(request.getMethod(), getClass().getName(), "Invalid refresh token", null);;
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
