package com.project.oop.repository;

import com.project.oop.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Iterable<Transaction> findAllByAccount(String accountCode);
}
