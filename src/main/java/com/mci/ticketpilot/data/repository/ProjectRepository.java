package com.mci.ticketpilot.data.repository;

import com.mci.ticketpilot.data.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("select c from Project c " +
            "where lower(c.title) like lower(concat('%', :searchTerm, '%')) ")
    List<Project> search(@Param("searchTerm") String searchTerm);
}