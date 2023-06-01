package com.mci.ticketpilot.data.repository;

import com.mci.ticketpilot.data.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    @Query("SELECT u FROM Users u WHERE u.email = :email")
    Optional<Users> findByUsername(@Param("email") String email);
    @Query("select c from Users c " +
            "where lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))")
    List<Users> search(@Param("searchTerm") String searchTerm);

}