package com.example.demo.repository;

import com.example.demo.Loan;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class LoanRepository {

    private static final Map<Long, Loan> loans = new ConcurrentHashMap<>();
    private static final AtomicLong idGenerator = new AtomicLong(1);

    public List<Loan> findAll() {
        return new ArrayList<>(loans.values());
    }

    public Loan findById(Long id) {
        return loans.get(id);
    }

    public List<Loan> findByUserId(Long userId) {
        List<Loan> result = new ArrayList<>();
        for (Loan loan : loans.values()) {
            if (loan.getUserId().equals(userId)) {
                result.add(loan);
            }
        }
        return result;
    }

    public Loan save(Loan loan) {
        if (loan.getId() == null) {
            loan.setId(idGenerator.getAndIncrement());
        }
        loans.put(loan.getId(), loan);
        return loan;
    }

    public boolean delete(Long id) {
        return loans.remove(id) != null;
    }
}