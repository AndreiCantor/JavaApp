package com.example.demo.services;

import com.example.demo.ExternalBookDto;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.annotation.JsonbProperty;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class OpenLibraryService {

    private static final String BASE_URL = "https://openlibrary.org";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Jsonb jsonb = JsonbBuilder.create();

    public static class OpenLibrarySearchResponse {
        @JsonbProperty("docs")
        public List<OpenLibraryDoc> docs;
    }

    public static class OpenLibraryDoc {
        public String title;

        @JsonbProperty("author_name")
        public List<String> authorName;

        @JsonbProperty("first_publish_year")
        public Integer firstPublishYear;

        public List<String> isbn;

        public List<String> ia;
    }


    public List<ExternalBookDto> searchByTitle(String title) {
        try {
            String encoded = URLEncoder.encode(title, StandardCharsets.UTF_8);
            String url = BASE_URL + "/search.json?title=" + encoded;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return List.of();
            }

            String body = response.body();

            OpenLibrarySearchResponse ol =
                    jsonb.fromJson(body, OpenLibrarySearchResponse.class);

            List<ExternalBookDto> result = new ArrayList<>();

            if (ol.docs != null) {
                for (OpenLibraryDoc d : ol.docs) {
                    String author = (d.authorName != null && !d.authorName.isEmpty())
                            ? d.authorName.get(0)
                            : null;

                    String isbn = null;

                    if (d.isbn != null && !d.isbn.isEmpty()) {
                        isbn = d.isbn.get(0);
                    }

                    if (isbn == null && d.ia != null) {
                        for (String iaId : d.ia) {
                            if (iaId != null && iaId.startsWith("isbn_")) {
                                isbn = iaId.substring("isbn_".length());
                                break;
                            }
                        }
                    }

                    result.add(new ExternalBookDto(
                            d.title,
                            author,
                            d.firstPublishYear,
                            isbn
                    ));

                }
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException("Eroare la apelarea OpenLibrary", e);
        }
    }
}
