package com.mci.ticketpilot.data.service;
// using SendGrid's Java Library
// https://github.com/sendgrid/sendgrid-java
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;

public class SendMail {

    public SendMail() {
    }

    public void send(String Email, String firstname, String lastname, String ticketname, String ticketdescription) throws IOException {
        Email from = new Email("TicketPilot1337@gmail.com");
        String subject = "A new Ticket for you " + firstname + " " + lastname + "!";
        Email to = new Email(Email);
        Content content = new Content("text/plain", ticketdescription);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid("ENTERKEY HERE");
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
            System.out.println("message sent successfully....");
        } catch (IOException ex) {
            throw ex;
        }
    }
}
