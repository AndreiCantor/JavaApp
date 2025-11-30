package com.example.demo.resource;

import com.example.demo.Book;
import com.example.demo.repository.BookRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    private BookRepository repository = new BookRepository();

    @GET
    public Response getBooks() {
        return Response.ok(repository.findAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") int id) {
        Book book = repository.findById(id);
        if (book == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(book).build();
    }

    @POST
    public Response createBook(Book book) {
        Book created = repository.save(book);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") int id, Book book) {
        Book updated = repository.update(id, book);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") int id) {
        Book deleted = repository.delete(id);
        if (deleted == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(deleted).build();
    }
}
