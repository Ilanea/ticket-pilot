package com.mci.ticketpilot.data.repository;

import com.mci.ticketpilot.data.entity.Document;
import com.mci.ticketpilot.data.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("SELECT c FROM Document c WHERE c.ticket = :currentTicket")
    List<Document> findByTicket(Ticket currentTicket);

    @Query("SELECT c FROM Document c WHERE c.id = :id")
    Document getDocumentById(Long id);

}
