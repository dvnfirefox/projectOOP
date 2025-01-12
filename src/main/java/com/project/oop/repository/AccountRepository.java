package com.project.oop.repository;

import com.project.oop.model.Account;
import com.project.oop.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findAllByClientCode(String clientCode);
}
