package com.example.demo.resource;

import com.example.demo.Loan;
import com.example.demo.User;
import com.example.demo.repository.LoanRepository;
import com.example.demo.repository.UserRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private LoanRepository loanRepo = new LoanRepository();
    private UserRepository userRepo = new UserRepository(); // Add this repository

    @POST
    public Response createUser(User user) {
        User saved = userRepo.save(user);
        return Response.created(URI.create("/api/users/" + saved.getId()))
                .entity(saved)
                .build();
    }

    @GET
    @Path("/{id}/loans")
    public Response getLoans(@PathParam("id") long userId) {
        return Response.ok(loanRepo.findByUserId(userId)).build();
    }

    @POST
    @Path("/{id}/loans")
    public Response createLoan(@PathParam("id") Long userId, Loan loanRequest) {
        loanRequest.setUserId(userId);
        Loan created = loanRepo.save(loanRequest);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
}