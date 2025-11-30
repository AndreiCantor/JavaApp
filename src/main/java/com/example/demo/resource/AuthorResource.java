package com.example.demo.resource;

import com.example.demo.Author;
import com.example.demo.repository.AuthorRepository;
import com.example.demo.repository.BookRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {

    private AuthorRepository authorRepo = new AuthorRepository();
    private BookRepository bookRepo = new BookRepository();

    // GET /authors
    @GET
    public Response getAllAuthors() {
        return Response.ok(authorRepo.findAll()).build();
    }

    // GET /authors/{id}
    @GET
    @Path("/{id}")
    public Response getAuthorById(@PathParam("id") Long id) {
        Author author = authorRepo.findById(id);
        if (author == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(author).build();
    }

    // POST /authors
    @POST
    public Response createAuthor(Author author) {
        Author saved = authorRepo.save(author);
        return Response.created(URI.create("/api/authors/" + saved.getId()))
                .entity(saved)
                .build();
    }

    // PUT /authors/{id}
    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") Long id, Author updatedAuthor) {
        Author updated = authorRepo.update(id, updatedAuthor);
        if (updated == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updated).build();
    }

    // DELETE /authors/{id}
    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") Long id) {
        boolean removed = authorRepo.delete(id);
        if (!removed) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    // GET /authors/{id}/books
    @GET
    @Path("/{id}/books")
    public Response getBooksByAuthor(@PathParam("id") Long id) {
        return Response.ok(bookRepo.findByAuthorId(id)).build();
    }
}
