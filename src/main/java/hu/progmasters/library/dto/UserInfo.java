package hu.progmasters.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    private Integer id;
    private String name;
    private String address;
    private String email;
    private String phoneNumber;
}
