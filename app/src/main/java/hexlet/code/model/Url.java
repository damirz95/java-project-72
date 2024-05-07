package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class Url {
    private Long id;
    private String name;
    private Date createdAt;
}
