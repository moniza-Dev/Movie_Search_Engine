package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Genre {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;
}

