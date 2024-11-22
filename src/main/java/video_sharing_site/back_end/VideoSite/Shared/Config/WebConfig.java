package video_sharing_site.back_end.VideoSite.Shared.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import video_sharing_site.back_end.VideoSite.Interceptor.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("/${video-site.server.api.key}")
    private String apiKey;

    @Autowired
    private SignupInterceptor signupInterceptor;

    @Autowired
    private SigninInterceptor signinInterceptor;

    @Autowired
    private RefreshTokenInterceptor refreshTokenInterceptor;

    @Autowired
    private AccessTokenInterceptor accessTokenInterceptor;

    @Override
    @SuppressWarnings("null")
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(signupInterceptor).addPathPatterns(apiKey + "/auth/signup");

        registry.addInterceptor(signinInterceptor).addPathPatterns(apiKey + "/auth/signin");

        registry.addInterceptor(refreshTokenInterceptor).addPathPatterns(apiKey + "/auth/signout", apiKey + "/auth/refresh-token");

        registry.addInterceptor(accessTokenInterceptor).addPathPatterns(apiKey + "/**")
            .excludePathPatterns(apiKey + "/auth/**", apiKey + "/videos/**");
    }
}
