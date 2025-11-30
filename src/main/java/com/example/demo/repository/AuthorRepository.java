package com.example.demo.repository;

import com.example.demo.Author;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class AuthorRepository {

    private static final Map<Long, Author> authors = new ConcurrentHashMap<>();
    private static final AtomicLong idGenerator = new AtomicLong(1);

    public List<Author> findAll() {
        return new ArrayList<>(authors.values());
    }

    public Author findById(Long id) {
        return authors.get(id);
    }

    public Author save(Author author) {
        if (author.getId() == null) {
            author.setId(idGenerator.getAndIncrement());
        }
        authors.put(author.getId(), author);
        return author;
    }

    public Author update(Long id, Author updated) {
        Author existing = authors.get(id);
        if (existing == null) {
            return null;
        }
        updated.setId(id);
        authors.put(id, updated);
        return updated;
    }

    public boolean delete(Long id) {
        return authors.remove(id) != null;
    }
}