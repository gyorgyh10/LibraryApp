package hu.progmasters.library.dto;

import hu.progmasters.library.domain.Borrowing;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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
