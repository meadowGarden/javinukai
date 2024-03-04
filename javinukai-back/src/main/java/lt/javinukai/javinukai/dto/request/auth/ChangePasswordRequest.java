package lt.javinukai.javinukai.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class ChangePasswordRequest {
    @NotBlank
    private String resetToken;
    @NotBlank
    private String newPassword;
    @NotBlank
    private String oldPassword;
}
