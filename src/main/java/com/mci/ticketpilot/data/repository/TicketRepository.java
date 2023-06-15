package com.mci.ticketpilot.data.repository;

import com.mci.ticketpilot.data.entity.*;
import com.vaadin.flow.component.datepicker.DatePicker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("select c from Ticket c " +
            "where lower(c.ticketName) like lower(concat('%', :searchTerm, '%')) ")
    List<Ticket> search(@Param("searchTerm") String searchTerm);

    @Query("SELECT c FROM Ticket c " +
            "WHERE c.assignee = :assignee " +
            "AND LOWER(c.ticketName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Ticket> searchForUser(@Param("searchTerm") String searchTerm, @Param("assignee") Users assignee);

    @Query("SELECT c.ticketProject FROM Ticket c WHERE c = :ticket")
    Project findProjectToTicket(Ticket ticket);

    @Query("SELECT c FROM Ticket c WHERE c.assignee = :assignee")
    List<Ticket> findByAssignee(Users assignee);


    @Query("SELECT c FROM Ticket c WHERE c.assignee = :assignee AND c.ticketStatus IN :status AND c.ticketPriority IN :priority")
    List<Ticket> findByAssigneeStatusPriority(Users assignee, Set<TicketStatus> status, Set<TicketPriority> priority);

    @Query("SELECT t FROM Ticket t WHERE t.assignee = :assignee AND t.ticketCreationDate = :date")
    List<Ticket> findByAssigneeAndCreationDate(@Param("assignee") Users assignee, @Param("date") LocalDate date);

    @Query("SELECT t FROM Ticket t WHERE t.ticketCreationDate BETWEEN :fromDate AND :toDate")
    List<Ticket> findByAssigneeAndCreationDateBetween(@Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate);

}