package video_sharing_site.back_end.VideoSite.Shared.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import video_sharing_site.back_end.VideoSite.Entity.LogsEntity;
import video_sharing_site.back_end.VideoSite.Entity.UsersEntity;
import video_sharing_site.back_end.VideoSite.Repository.LogsRepository;
import video_sharing_site.back_end.VideoSite.Repository.UsersRepository;
import video_sharing_site.back_end.VideoSite.Shared.Services.IpService;
import video_sharing_site.back_end.VideoSite.Shared.Services.jwt.TokenService;

@Component
public class LogConfig {
    
    @Autowired
    private LogsRepository logsRepository;

    @Autowired
    private IpService ipService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsersRepository usersRepository;

    public void token(String Authorization, String action, String module, String message) {
        String token = Authorization.split(" ")[1];
        String email = tokenService.getUserFromAccessToken(token);
        UsersEntity user = usersRepository.findByEmail(email);
        log(action, module, message, user);
    }

    public void log(String action, String module, String message, UsersEntity user) {
        // 28 = "video_sharing_site.back_end." length
        module = module.substring(28);
        LogsEntity logsEntity = new LogsEntity();
        logsEntity.setUserId(user);
        logsEntity.setMessage("[" + ipService.getClientIpAddress() + "] | " + action.toUpperCase() + " | " + module + " | " + message);
        logsRepository.save(logsEntity);
    }

}
