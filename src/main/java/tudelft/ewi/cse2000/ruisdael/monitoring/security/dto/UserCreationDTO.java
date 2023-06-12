package tudelft.ewi.cse2000.ruisdael.monitoring.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationDTO {

    private String username;
    private String password;
    private boolean admin;
}
