package com.example.demo.repository;

import com.example.demo.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class UserRepository {

    private static final Map<Long, User> users = new ConcurrentHashMap<>();
    private static final AtomicLong idGenerator = new AtomicLong(1);

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User findById(Long id) {
        return users.get(id);
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
        }
        users.put(user.getId(), user);
        return user;
    }

    public User update(Long id, User updated) {
        User existing = users.get(id);
        if (existing == null) {
            return null;
        }
        updated.setId(id);
        users.put(id, updated);
        return updated;
    }

    public boolean delete(Long id) {
        return users.remove(id) != null;
    }
}