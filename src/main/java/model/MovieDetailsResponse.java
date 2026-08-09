package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record MovieDetailsResponse(
        String title,
        @JsonProperty("release_date") String release_date,
        @JsonProperty("vote_average") Double vote_average,
        Integer runtime,
        Integer budget,
        List<Genre> genres,
        @JsonProperty("origin_country") List<String> origin_country
) {
}
