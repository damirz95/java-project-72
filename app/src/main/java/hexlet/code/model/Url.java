package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Url {
    private Long id;
    private String name;
    private LocalDate createdAt;

    public Url(String name) {
        this.name = name;
    }
    public Url(Long id, String name) {
        this.name = name;
    }
}
