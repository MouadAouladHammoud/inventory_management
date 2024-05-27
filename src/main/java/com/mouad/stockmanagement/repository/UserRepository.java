package com.mouad.stockmanagement.repository;

import com.mouad.stockmanagement.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
    // JPQL query
    @Query(value = "select u from User u where u.email = :email")
    Optional<User> findUserByEmail(@Param("email") String email);

    Optional<User> findByEmail(String email);
}