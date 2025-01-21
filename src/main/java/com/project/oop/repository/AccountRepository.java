package com.project.oop.repository;

import com.project.oop.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Iterable<Account> findAllByClientCode(String clientCode);
    Optional<Account> findByClientCodeAndType(String clientCode, String type);
    Optional<Account> findById(Long id);
    Iterable<Account> findAllByType(String type);
}
