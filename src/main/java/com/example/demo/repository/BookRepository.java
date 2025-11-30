package com.example.demo.repository;

import com.example.demo.Book;

import java.util.*;

public class BookRepository {
    private static final Map<Integer, Book> books = new HashMap<>();
    private static int idCounter = 1;

    public List<Book> findAll() {
        return new ArrayList<>(books.values());
    }

    public Book findById(int id) {
        return books.get(id);
    }

    public Book save(Book book) {
        book.setId(idCounter++);
        books.put(book.getId(), book);
        return book;
    }

    public Book update(int id, Book book) {
        book.setId(id);
        books.put(id, book);
        return book;
    }

    public Book delete(int id) {
        return books.remove(id);
    }

    public List<Book> findByAuthorId(Long authorId) {
        List<Book> result = new ArrayList<>();
        for (Book b : books.values()) {
            if (b.getAuthorId() == authorId) result.add(b);
        }
        return result;
    }
}
