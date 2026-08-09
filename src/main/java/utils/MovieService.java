package utils;

import model.MovieDetailsResponse;
import model.MovieResponse;
import model.VideoOfMovies;

import java.util.Optional;

public interface MovieService {
    MovieResponse getMoviesByQuery(String input, int currentPage);
    Optional<VideoOfMovies> getTrailerByQuery(int movieID);
    MovieDetailsResponse getMovieDetailById(int movieID);
}
