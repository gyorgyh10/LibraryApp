package hu.progmasters.library.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BorrowingInfoNoExemplar {
    private Integer id;
    private UserInfo user;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Boolean active;
}
