package org.example;

import model.MovieResponse;
import model.MovieServiceImpl;
import utils.MovieDetailRenderer;
import utils.MovieService;
import utils.TableRenderer;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {
    static int currentPage = 1;

    public static void updatePageNumber(int pageNumber,int total_pages) {
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageNumber > total_pages) {
            pageNumber = total_pages;
        }
        currentPage = pageNumber;
    }

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        Scanner input = new Scanner(System.in);
        MovieService service = new MovieServiceImpl();
        TableRenderer renderer = new TableRenderer();
        MovieDetailRenderer detailRenderer = new MovieDetailRenderer();
        outerLoop :

        System.out.println("""
                
                
                `7MMM.     ,MMF' .g8""8q.`7MMF'   `7MF'`7MMF'`7MM""\"YMM       .M""\"bgd `7MM""\"YMM        db      `7MM""\"Mq.   .g8""\"bgd `7MMF'  `7MMF'
                  MMMb    dPMM .dP'    `YM.`MA     ,V    MM    MM    `7      ,MI    "Y   MM    `7       ;MM:       MM   `MM..dP'     `M   MM      MM \s
                  M YM   ,M MM dM'      `MM VM:   ,V     MM    MM   d        `MMb.       MM   d        ,V^MM.      MM   ,M9 dM'       `   MM      MM \s
                  M  Mb  M' MM MM        MM  MM.  M'     MM    MMmmMM          `YMMNq.   MMmmMM       ,M  `MM      MMmmdM9  MM            MMmmmmmmMM \s
                  M  YM.P'  MM MM.      ,MP  `MM A'      MM    MM   Y  ,     .     `MM   MM   Y  ,    AbmmmqMA     MM  YM.  MM.           MM      MM \s
                  M  `YM'   MM `Mb.    ,dP'   :MM;       MM    MM     ,M     Mb     dM   MM     ,M   A'     VML    MM   `Mb.`Mb.     ,'   MM      MM \s
                .JML. `'  .JMML. `"bmmd"'      VF      .JMML..JMMmmmmMMM     P"Ybmmd"  .JMMmmmmMMM .AMA.   .AMMA..JMML. .JMM. `"bmmmd'  .JMML.  .JMML.
                
                
                    """);
        while(true) {
            System.out.print("Enter movie title : ");
            String choice = input.nextLine().trim();
            if (choice.equalsIgnoreCase("e")) {
                break;
            }
            currentPage = 1;
            while (true) {
                MovieResponse response = service.getMoviesByQuery(choice, currentPage);
                renderer.Display(response);
                System.out.printf("Page %d out of %s | Total Results : %d \n", currentPage, response.getTotal_pages(),response.getTotal_results());
                System.out.println("=== Page navigation ===");
                System.out.println("""
                        [N] Next Page        [B] Back
                        [P] Previous Page    [md] Movie detail
                        [G] Go to page
                        """);
                System.out.print("Enter an option: ");
                String op = input.nextLine();
                switch (op) {
                    case "N", "n" -> {
                        updatePageNumber(currentPage + 1, response.getTotal_pages());
                        break;
                    }
                    case "P", "p" -> updatePageNumber(currentPage - 1, response.getTotal_pages());
                    case "G", "g" -> {
                        System.out.print("Enter page number to go: ");
                        int requestedPage = Integer.parseInt(input.nextLine().trim());

                        updatePageNumber(
                                requestedPage, response.getTotal_pages()
                        );
                        break;
                    }
                    case "md", "MD","m","d" -> {
                        System.out.print("Enter the movie ID : ");
                        int movieID = Integer.parseInt(input.nextLine().trim());
                        detailRenderer.DetailRender(service.getMovieDetailById(movieID));

                    }
                    case "E", "e" -> System.exit(0);
                }
            }
        }
    }
}
