package com.mci.ticketpilot.data.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SendMailServiceTest {

    @Mock
    private SendGrid sendGrid;

    private SendMailService sendMailService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sendGrid = Mockito.mock(SendGrid.class);
        sendMailService = new SendMailService(sendGrid);
    }


    @Test
    public void testSend_WithValidParameters_SendsEmail() throws IOException {
        // Arrange
        String email = "manager@test.com";
        String firstName = "John";
        String lastName = "Doe";
        String ticketName = "Sample Ticket";
        String ticketDescription = "Sample description";

        Response mockResponse = new Response(202, null, null);
        when(sendGrid.api(any(Request.class))).thenReturn(mockResponse);

        // Act
        sendMailService.send(email, firstName, lastName, ticketName, ticketDescription);

        // Assert
        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        verify(sendGrid).api(requestCaptor.capture());

        Request capturedRequest = requestCaptor.getValue();
        assertEquals(Method.POST, capturedRequest.getMethod());
        assertEquals("mail/send", capturedRequest.getEndpoint());
        // Additional assertions on the request body, if needed

        // Additional assertions on the response, if needed
    }
}

