package com.example.demo.resource;

import com.example.demo.ExternalBookDto;
import com.example.demo.services.OpenLibraryService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/openlibrary")
@Produces(MediaType.APPLICATION_JSON)
public class OpenLibraryResource {

    private final OpenLibraryService openLibraryService = new OpenLibraryService();

    @GET
    @Path("/search")
    public Response searchBooks(@QueryParam("title") String title) {
        if (title == null || title.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Query parameter 'title' is required")
                    .build();
        }

        List<ExternalBookDto> books = openLibraryService.searchByTitle(title);
        return Response.ok(books).build();
    }
}
