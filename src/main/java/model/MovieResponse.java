package model;

import lombok.*;

import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class MovieResponse {

    private Integer page;
    private List<Movie> results;
    private Integer total_pages;
    private Integer total_results;
}
