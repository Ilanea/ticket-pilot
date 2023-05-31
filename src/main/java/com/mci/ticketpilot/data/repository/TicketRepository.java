package com.mci.ticketpilot.data.repository;

import com.mci.ticketpilot.data.entity.Project;
import com.mci.ticketpilot.data.entity.Ticket;
import com.mci.ticketpilot.data.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByProject(Project project);

    @Query("select c from Ticket c " +
            "where lower(c.ticketName) like lower(concat('%', :searchTerm, '%')) ")
    List<Ticket> search(@Param("searchTerm") String searchTerm);
}