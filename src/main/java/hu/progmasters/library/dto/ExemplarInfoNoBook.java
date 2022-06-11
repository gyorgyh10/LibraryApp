package hu.progmasters.library.dto;

import hu.progmasters.library.domain.Condition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExemplarInfoNoBook {
    private Integer id;
    private Integer inventoryNumber;
    private Condition condition;
    private Boolean borrowable;
    private List<BorrowingInfoNoExemplar> borrowings;
}
