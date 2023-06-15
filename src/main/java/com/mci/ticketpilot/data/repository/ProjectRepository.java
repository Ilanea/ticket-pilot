package com.mci.ticketpilot.data.repository;

import com.mci.ticketpilot.data.entity.Project;
import com.mci.ticketpilot.data.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("select c from Project c " +
            "where lower(c.projectName) like lower(concat('%', :searchTerm, '%')) ")
    List<Project> search(@Param("searchTerm") String searchTerm);

    @Query("SELECT c FROM Project c " +
            "WHERE c.projectManager = :projectManager " +
            "AND LOWER(c.projectName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Project> searchForUser(@Param("searchTerm") String searchTerm, @Param("projectManager") Users projectManager);

    @Query("SELECT c FROM Project c WHERE c.projectManager = :projectManager")
    List<Project> findByUser(Users projectManager);
}