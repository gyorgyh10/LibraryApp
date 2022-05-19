package hu.progmasters.library.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exemplar {
    private Integer id;
    private Integer inventoryNumber;
    private Condition condition;
    private Boolean borrowable;
    private Book ofBook;
}
