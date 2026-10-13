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

    public static void updatePageNumber(int pageNumber, int total_pages) {
        if (total_pages < 1) {
            total_pages = 1;
        }
        if (pageNumber < 1) {
            pageNumber = 1;
        }
        if (pageNumber > total_pages) {
            pageNumber = total_pages;
        }
        currentPage = pageNumber;
    }

    private static int readInt(Scanner input, String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(input.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return -1;
        }
    }

    public static void main(String[] args) {
        String token = System.getenv("TMDB_ACCESS_TOKEN");
        if (token == null || token.isBlank()) {
            System.out.println("Missing environment variable TMDB_ACCESS_TOKEN. See README for setup.");
            return;
        }

        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        Scanner input = new Scanner(System.in);
        MovieService service = new MovieServiceImpl();
        TableRenderer renderer = new TableRenderer();
        MovieDetailRenderer detailRenderer = new MovieDetailRenderer();

        System.out.println("""
                
                
                `7MMM.     ,MMF' .g8""8q.`7MMF'   `7MF'`7MMF'`7MM""\"YMM       .M""\"bgd `7MM""\"YMM        db      `7MM""\"Mq.   .g8""\"bgd `7MMF'  `7MMF'
                  MMMb    dPMM .dP'    `YM.`MA     ,V    MM    MM    `7      ,MI    "Y   MM    `7       ;MM:       MM   `MM..dP'     `M   MM      MM \s
                  M YM   ,M MM dM'      `MM VM:   ,V     MM    MM   d        `MMb.       MM   d        ,V^MM.      MM   ,M9 dM'       `   MM      MM \s
                  M  Mb  M' MM MM        MM  MM.  M'     MM    MMmmMM          `YMMNq.   MMmmMM       ,M  `MM      MMmmdM9  MM            MMmmmmmmMM \s
                  M  YM.P'  MM MM.      ,MP  `MM A'      MM    MM   Y  ,     .     `MM   MM   Y  ,    AbmmmqMA     MM  YM.  MM.           MM      MM \s
                  M  `YM'   MM `Mb.    ,dP'   :MM;       MM    MM     ,M     Mb     dM   MM     ,M   A'     VML    MM   `Mb.`Mb.     ,'   MM      MM \s
                .JML. `'  .JMML. `"bmmd"'      VF      .JMML..JMMmmmmMMM     P"Ybmmd"  .JMMmmmmMMM .AMA.   .AMMA..JMML. .JMM. `"bmmmd'  .JMML.  .JMML.
                
                
                    """);
        System.out.println("Type a movie title to search, or E to exit.");

        while (true) {
            System.out.print("Enter movie title : ");
            String choice = input.nextLine().trim();
            if (choice.equalsIgnoreCase("e")) {
                break;
            }
            if (choice.isEmpty()) {
                System.out.println("Please enter a movie title.");
                continue;
            }
            currentPage = 1;
            searchLoop:
            while (true) {
                MovieResponse response;
                try {
                    response = service.getMoviesByQuery(choice, currentPage);
                } catch (RuntimeException e) {
                    System.out.println("Something went wrong while contacting TMDB: " + e.getMessage());
                    break;
                }
                renderer.Display(response);
                System.out.printf("Page %d out of %s | Total Results : %d \n", currentPage, response.getTotal_pages(), response.getTotal_results());
                System.out.println("=== Page navigation ===");
                System.out.println("""
                        [N] Next Page        [B] Back to search
                        [P] Previous Page    [md] Movie detail
                        [G] Go to page       [E] Exit
                        """);
                System.out.print("Enter an option: ");
                String op = input.nextLine().trim();
                switch (op) {
                    case "N", "n" -> updatePageNumber(currentPage + 1, response.getTotal_pages());
                    case "P", "p" -> updatePageNumber(currentPage - 1, response.getTotal_pages());
                    case "G", "g" -> {
                        int requestedPage = readInt(input, "Enter page number to go: ");
                        if (requestedPage > 0) {
                            updatePageNumber(requestedPage, response.getTotal_pages());
                        }
                    }
                    case "md", "MD", "m", "d" -> {
                        int movieID = readInt(input, "Enter the movie ID : ");
                        if (movieID > 0) {
                            try {
                                detailRenderer.DetailRender(service.getMovieDetailById(movieID));
                            } catch (RuntimeException e) {
                                System.out.println("Could not load movie details: " + e.getMessage());
                            }
                        }
                    }
                    case "B", "b" -> {
                        break searchLoop;
                    }
                    case "E", "e" -> System.exit(0);
                    default -> System.out.println("Unknown option. Please choose from the menu.");
                }
            }
        }
    }
}
