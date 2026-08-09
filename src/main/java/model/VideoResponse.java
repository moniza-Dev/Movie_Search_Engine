package model;

import java.util.List;

public record VideoResponse(
        Integer id,
        List<VideoOfMovies>results
) {
}
