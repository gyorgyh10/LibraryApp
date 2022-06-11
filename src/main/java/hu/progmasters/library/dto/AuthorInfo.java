package hu.progmasters.library.dto;

import hu.progmasters.library.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorInfo {
    private Integer id;
    private String name;
}
