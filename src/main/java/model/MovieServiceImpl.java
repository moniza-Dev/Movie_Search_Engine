package model;

import tools.jackson.databind.ObjectMapper;
import utils.MovieService;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

public class MovieServiceImpl implements MovieService {
    static String Access_Token = System.getenv("TMDB_ACCESS_TOKEN");

    @Override
    public MovieResponse getMoviesByQuery(String input,int currentPage){

        ObjectMapper mapper = new ObjectMapper();
        String encodedQuery = URLEncoder.encode(input, StandardCharsets.UTF_8);
        String url = String.format("https://api.themoviedb.org/3/search/movie?query=%s&page=%d", encodedQuery, currentPage);
        try {
            HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + Access_Token)
                    .GET()
                    .build();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            MovieResponse movieResponse = mapper.readValue(response.body(),MovieResponse.class);

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "TMDB search failed: " + response.statusCode()
                                + "\n" + response.body()
                );
            }
            movieResponse.getResults().forEach(movie -> {
                String videURL = getTrailerByQuery(movie.getId())
                        .map(video -> "https://www.youtube.com/watch?v=" + video.key())
                        .orElse("N/A");
                movie.setTrailerURL(videURL);
            });
            return movieResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<VideoOfMovies> getTrailerByQuery(int movieID){
        ObjectMapper mapper = new ObjectMapper();
        String url = String.format("https://api.themoviedb.org/3/movie/%d/videos",movieID);
        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + Access_Token)
                    .GET()
                    .build();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "TMDB search failed: " + response.statusCode()
                                + "\n" + response.body()
                );
            }
            VideoResponse videoResponse = mapper.readValue(response.body(),VideoResponse.class);
            return videoResponse.results()
                    .stream()
                    .filter(video -> video.type().equalsIgnoreCase("Trailer"))
                    .filter(site -> site.site().equalsIgnoreCase("Youtube"))
                    .findFirst();
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    @Override
    public MovieDetailsResponse getMovieDetailById(int movieID){
        ObjectMapper mapper = new ObjectMapper();
        String url = String.format("https://api.themoviedb.org/3/movie/%d",movieID);
        try {
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + Access_Token)
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "TMDB search failed: " + response.statusCode()
                                + "\n" + response.body()
                );
            }
            MovieDetailsResponse movieDetailResponse = mapper.readValue(response.body(),MovieDetailsResponse.class);
            return movieDetailResponse;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}