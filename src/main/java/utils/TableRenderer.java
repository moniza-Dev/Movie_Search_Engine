package utils;

import model.Movie;
import model.MovieResponse;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;

public class TableRenderer {
    public void Display(MovieResponse response) {
        String[] columns = {"ID","Title","Release Date","Rating","Trailer"};
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        for (String column : columns){
            table.addCell(column);
        }


        for(Movie movie : response.getResults() ){
            table.addCell(movie.getId().toString());
            table.addCell(movie.getTitle());
            table.addCell(movie.getRelease_date());
            table.addCell(String.valueOf(movie.getVote_average()));
            table.addCell(movie.getTrailerURL());
        }
        System.out.println(table.render());
    }
}