package tudelft.ewi.cse2000.ruisdael.monitoring.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDTO {

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
