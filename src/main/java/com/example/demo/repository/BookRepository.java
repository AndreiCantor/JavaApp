package com.example.demo.repository;

import com.example.demo.Book;
import com.example.demo.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class BookRepository {

    public List<Book> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT b FROM Book b", Book.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Book findById(int id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(Book.class, id);
        } finally {
            em.close();
        }
    }

    public Book save(Book book) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            // If id is 0, it's new; otherwise we assume it's an update or new entry
            // Ideally for new entries we use persist, for updates merge.
            // Since id is generated, we can just use merge for safety or simple persist.
            if (book.getId() == 0) {
                em.persist(book);
            } else {
                book = em.merge(book);
            }
            em.getTransaction().commit();
            return book;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Book update(int id, Book book) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            book.setId(id); // Ensure ID is set
            Book updated = em.merge(book);
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Book delete(int id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            Book book = em.find(Book.class, id);
            if (book != null) {
                em.remove(book);
                em.getTransaction().commit();
                return book;
            }
            em.getTransaction().rollback();
            return null;
        } finally {
            em.close();
        }
    }

    public List<Book> findByAuthorId(Long authorId) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            // Note: Book has 'int' authorId, we are casting param to match if needed or relying on DB type
            TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.authorId = :aid", Book.class);
            query.setParameter("aid", authorId.intValue()); // Cast Long to int to match Book field
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}