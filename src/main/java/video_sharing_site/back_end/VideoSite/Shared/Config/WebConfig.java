package video_sharing_site.back_end.VideoSite.Shared.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import video_sharing_site.back_end.VideoSite.Interceptor.*;

@Configuration
public class WebConfig implements WebMvcConfigurer{
    
    @Value("/${video-site.server.api.key}")
    private String apiKey;

    @Autowired
    private SignupInterceptor signupInterceptor;

    @Autowired
    private SigninInterceptor signinInterceptor;

    @Override
    @SuppressWarnings("null")
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(signupInterceptor).addPathPatterns(apiKey + "/auth/signup");

        registry.addInterceptor(signinInterceptor).addPathPatterns(apiKey + "/auth/signin");
    }
}
