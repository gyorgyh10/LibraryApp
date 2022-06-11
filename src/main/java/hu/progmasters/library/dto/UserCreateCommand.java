package hu.progmasters.library.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateCommand {

    @NotBlank(message = "Most not be blank")
    @Schema(description = "The name of the user", example="Tom Jhonson")
    private String name;

    @NotBlank(message = "Most not be blank")
    @Schema(description = "The address of the user", example="Principal street, no. 6")
    private String address;

    @NotBlank(message = "Most not be blank")
    @Email
    @Schema(description = "The email address of the user", example="tom.jhonson@gmail.com")
    private String email;

    @NotBlank(message = "Most not be blank")
    @Schema(description = "The phone number of the user", example="6381218763")
    private String phoneNumber;
}
