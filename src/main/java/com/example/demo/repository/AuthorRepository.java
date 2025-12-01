package com.example.demo.repository;

import com.example.demo.Author;
import com.example.demo.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.util.List;

public class AuthorRepository {

    public List<Author> findAll() {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.createQuery("SELECT a FROM Author a", Author.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Author findById(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            return em.find(Author.class, id);
        } finally {
            em.close();
        }
    }

    public Author save(Author author) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            if (author.getId() == null || author.getId() == 0) {
                em.persist(author);
            } else {
                author = em.merge(author);
            }
            em.getTransaction().commit();
            return author;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Author update(Long id, Author updated) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            Author existing = em.find(Author.class, id);
            if (existing == null) {
                em.getTransaction().rollback();
                return null;
            }
            updated.setId(id);
            Author saved = em.merge(updated);
            em.getTransaction().commit();
            return saved;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public boolean delete(Long id) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            Author author = em.find(Author.class, id);
            if (author != null) {
                em.remove(author);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }
}