package br.com.selectgearmotors.transaction.application.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "SignupRequest", requiredProperties = {"username", "password"})
@Tag(name = "SignupRequest", description = "Model")
public class SignupRequest {
    private String username;
    private String password;
}
