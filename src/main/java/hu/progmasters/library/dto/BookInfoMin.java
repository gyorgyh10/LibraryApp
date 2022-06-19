package hu.progmasters.library.dto;

import hu.progmasters.library.domain.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookInfoMin {
    private Integer id;
    private String title;
}
