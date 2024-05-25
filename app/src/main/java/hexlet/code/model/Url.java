package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Url {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public Url(String name) {
        this.name = name;
    }
    public Url(Long id, String name) {
        this.name = name;
    }
}
