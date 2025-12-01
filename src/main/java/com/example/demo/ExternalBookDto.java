package com.example.demo;

public class ExternalBookDto {
    private String title;
    private String author;
    private Integer firstPublishYear;
    private String isbn;

    public ExternalBookDto() {
    }

    public ExternalBookDto(String title, String author, Integer firstPublishYear, String isbn) {
        this.title = title;
        this.author = author;
        this.firstPublishYear = firstPublishYear;
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getFirstPublishYear() {
        return firstPublishYear;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setFirstPublishYear(Integer firstPublishYear) {
        this.firstPublishYear = firstPublishYear;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
