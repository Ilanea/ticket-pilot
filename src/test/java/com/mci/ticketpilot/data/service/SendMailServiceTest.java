package com.mci.ticketpilot.data.service;

import com.sendgrid.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SendMailServiceTest {

    @Mock
    private SendGrid sg;

    private SendMailService sendMailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sendMailService = new SendMailService();
    }

    @Test
    void testSendMail() throws IOException {
        // Mock the SendGrid response
        Response mockResponse = new Response(202, null, null);
        when(sg.api(any(Request.class))).thenReturn(mockResponse);

        // Test data
        String email = "recipient@example.com";
        String firstName = "John";
        String lastName = "Doe";
        String ticketName = "Event Ticket";
        String ticketDescription = "Ticket description";

        // Perform the send operation
        sendMailService.send(email, firstName, lastName, ticketName, ticketDescription);

        // Verify that SendGrid's API was called with the correct parameters
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        verify(sg).api(requestCaptor.capture());
        Request sentRequest = requestCaptor.getValue();

        assertNotNull(sentRequest);
        assertEquals(Method.POST, sentRequest.getMethod());
        assertEquals("mail/send", sentRequest.getEndpoint());
        assertNotNull(sentRequest.getBody());

        // Verify that the email was sent successfully
        assertEquals(202, mockResponse.getStatusCode());
    }
}