package com.mci.ticketpilot.data.repository;

import com.mci.ticketpilot.data.entity.Project;
import com.mci.ticketpilot.data.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByProject(Project project);
    //List<Ticket> findByStatus(String ticketStatus);
    //List<Ticket> findByPriority(String ticketPriority);
}