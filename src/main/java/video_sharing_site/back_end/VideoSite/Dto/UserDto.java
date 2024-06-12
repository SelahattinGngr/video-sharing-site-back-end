package video_sharing_site.back_end.VideoSite.Dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    private boolean role;

    private String firstName;

    private String lastName;

    private String email;

    private String userName;

    private String password;

    private LocalDate birthday;

    private String refreshToken;

}
