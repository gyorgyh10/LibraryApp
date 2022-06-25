package hu.progmasters.library.dto;

import hu.progmasters.library.domain.Condition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExemplarInfo {
    private Integer id;
    private Integer inventoryNumber;
    private Condition condition;
    private Boolean borrowable;
    private BookInfoMin ofBook;
}
