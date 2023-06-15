package com.mci.ticketpilot.data.service;
// using SendGrid's Java Library
// https://github.com/sendgrid/sendgrid-java
import com.mci.ticketpilot.security.SecurityService;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.stereotype.Service;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SendMailService {
    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);
    private String sendgridApiKey;
    private String senderEmail;
    public SendMailService() {
        sendgridApiKey = "test";
        senderEmail =  "TicketPilot1337@gmail.com";
    }

    public void send(String Email, String firstname, String lastname, String ticketname, String ticketdescription) throws IOException {
        Email from = new Email(senderEmail);
        String subject = "A new Ticket for you " + firstname + " " + lastname + "!";
        Email to = new Email(Email);

        if(ticketdescription == null || ticketdescription.isEmpty()) {
            ticketdescription = "No description provided.";
        }

        Content content = new Content("text/plain", ticketdescription);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
            if(response.getStatusCode() == 202) {
               logger.info("Email sent to " + Email + " successfully.");
            }
        } catch (IOException ex) {
            throw ex;
        }
    }
}
