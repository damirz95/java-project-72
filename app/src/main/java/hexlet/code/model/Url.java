package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class Url {
    private Long id;
    private String name;
    private LocalDate createdAt;

    public Url(String name) {
        this.name = name;
    }
}
