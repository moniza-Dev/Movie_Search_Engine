package model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Movie {

        private Integer id;
        private String title;
        private String release_date;
        private double vote_average;
        private String TrailerURL;
    }

