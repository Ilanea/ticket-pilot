package com.mci.ticketpilot.data.entity;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TicketPriorityTest {

    @Test
    public void testEnumValues() {
        // Assert
        Assertions.assertEquals(5, TicketPriority.values().length);
        Assertions.assertEquals(TicketPriority.HIGH, TicketPriority.valueOf("HIGH"));
        Assertions.assertEquals(TicketPriority.MEDIUM, TicketPriority.valueOf("MEDIUM"));
        Assertions.assertEquals(TicketPriority.LOW, TicketPriority.valueOf("LOW"));
        Assertions.assertEquals(TicketPriority.NEXT_SPRINT, TicketPriority.valueOf("NEXT_SPRINT"));
        Assertions.assertEquals(TicketPriority.DEFAULT, TicketPriority.valueOf("DEFAULT"));
    }
}

