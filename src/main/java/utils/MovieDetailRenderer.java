package utils;

import model.Genre;
import model.MovieDetailsResponse;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.Table;

public class MovieDetailRenderer {
    public void DetailRender(MovieDetailsResponse response) {

        Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        StringBuilder genresText = new StringBuilder();
        for (Genre gen : response.genres()){
            if (!genresText.isEmpty()){
                genresText.append(", ");
            }
            genresText.append(gen.getName());
        }

        table.addCell("Movie Information",2);
        table.addCell("Title");
        table.addCell(response.title());
        table.addCell("Release");
        table.addCell(response.release_date());
        table.addCell("Rating");
        table.addCell(response.vote_average().toString());
        table.addCell("Runtime");
        table.addCell(response.runtime() + " Minutes");
        table.addCell("Budget");
        table.addCell(response.budget().toString());
        table.addCell("Genres");
        table.addCell(genresText.toString());
        table.addCell("Origin");
        table.addCell(response.origin_country().toString());
        System.out.println(table.render());
    }
}
