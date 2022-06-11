package hu.progmasters.library.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BorrowingInfoNoUser {
    private Integer id;
    private ExemplarInfoNoBorrowings exemplar;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Boolean active;
}
