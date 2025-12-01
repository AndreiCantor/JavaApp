package com.example.demo;

import com.example.demo.resource.AuthorResource;
import com.example.demo.resource.BookResource;
import com.example.demo.resource.UserResource;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;

@ApplicationPath("/api")
@OpenAPIDefinition(
        info = @Info(
                title = "Library Web Service",
                version = "1.0",
                description = "REST API pentru gestionarea cărților, autorilor, utilizatorilor și împrumuturilor."
        )
        ,
        servers = {
                @Server(
                        url = "/demo_war_exploded",
                        description = "Tomcat local"
                )
        }
)
public class HelloApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(BookResource.class);
        resources.add(AuthorResource.class);
        resources.add(UserResource.class);
        resources.add(OpenApiResource.class);

        return resources;
    }
}
