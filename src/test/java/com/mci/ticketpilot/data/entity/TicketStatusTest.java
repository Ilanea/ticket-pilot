package com.mci.ticketpilot.data.entity;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TicketStatusTest {

    @Test
    public void testEnumValues() {
        // Assert
        Assertions.assertEquals(6, TicketStatus.values().length);
        Assertions.assertEquals(TicketStatus.OPEN, TicketStatus.valueOf("OPEN"));
        Assertions.assertEquals(TicketStatus.IN_PROGRESS, TicketStatus.valueOf("IN_PROGRESS"));
        Assertions.assertEquals(TicketStatus.RESOLVED, TicketStatus.valueOf("RESOLVED"));
        Assertions.assertEquals(TicketStatus.CLOSED, TicketStatus.valueOf("CLOSED"));
        Assertions.assertEquals(TicketStatus.REOPENED, TicketStatus.valueOf("REOPENED"));
        Assertions.assertEquals(TicketStatus.ON_HOLD, TicketStatus.valueOf("ON_HOLD"));
    }
}

