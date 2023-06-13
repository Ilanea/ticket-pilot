package com.mci.ticketpilot.data.service;

import com.mci.ticketpilot.data.entity.*;
import com.mci.ticketpilot.data.repository.*;
import com.mci.ticketpilot.security.SecurityService;
import com.mci.ticketpilot.security.SecurityUtils;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class PilotService {
    private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;
    private final DocumentRepository documentRepository;

    public PilotService(UserRepository userRepository,
                        ProjectRepository projectRepository,
                        TicketRepository ticketRepository,
                        CommentRepository commentRepository,
                        DocumentRepository documentRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.ticketRepository = ticketRepository;
        this.commentRepository = commentRepository;
        this.documentRepository = documentRepository;

    }

    ////////////////////////////////////////////////////////////////////
    // Users
    ////////////////////////////////////////////////////////////////////
    public List<Users> findAllUsers(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return userRepository.findAll();
        } else {
            return userRepository.search(stringFilter);
        }
    }

    public List<Users> findAllUsers() { return userRepository.findAll(); }
    public long countUsers() {
        return userRepository.count();
    }
    public void deleteUser(Users user) {
        userRepository.delete(user);
    }


    public void saveUser(Users user) {
        if (user == null) {
            System.err.println("Contact is null.");
            return;
        }
        logger.info("Saving user to DB: " + user);
        userRepository.saveAndFlush(user);
    }

    ////////////////////////////////////////////////////////////////////
    // Projects
    ////////////////////////////////////////////////////////////////////
    public List<Project> findAllProjects(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return projectRepository.findAll();
        } else {
            return projectRepository.search(stringFilter);
        }
    }

    public List<Project> findAllProjects(){ return projectRepository.findAll(); }
    public long countProjects() { return projectRepository.count(); }
    public void deleteProject(Project project) {
        if(project.getTickets() == null || project.getTickets().isEmpty()) {
            projectRepository.delete(project);
        } else {
            System.err.println("Project has tickets. Cannot delete.");
        }
    }

    public void saveProject(Project project) {
        if (project == null) {
            System.err.println("Project is null.");
            return;
        }
        logger.info("Saving project to DB: " + project);
        projectRepository.saveAndFlush(project);
    }

    public List<Project> getUserProjects(){
        Users currentUser = SecurityUtils.getLoggedInUser();
        //logger.info("Current user: " + currentUser);
        if (currentUser != null) {
            return projectRepository.findByUser(currentUser);
        }
        return Collections.emptyList();
    }

    ////////////////////////////////////////////////////////////////////
    // Tickets
    ////////////////////////////////////////////////////////////////////
    public List<Ticket> findAllTickets(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return ticketRepository.findAll();
        } else {
            return ticketRepository.search(stringFilter);
        }
    }

    public List<Ticket> findAllTickets(){
        return ticketRepository.findAll();
    }

    public List<Ticket> findUserTickets(){
        logger.info("Finding tickets for user: " + SecurityUtils.getLoggedInUser());
        return ticketRepository.findByAssignee(SecurityUtils.getLoggedInUser());
    }

    public List<Ticket> getTicketsperDate(LocalDate fromDate, LocalDate toDate){
        return ticketRepository.findByAssigneeAndCreationDateBetween(fromDate, toDate);
    }

    public long countTickets() { return ticketRepository.count(); }

    public void deleteTicket(Ticket ticket) {
        ticketRepository.delete(ticket);
    }

    public void saveTicket(Ticket ticket) {
        if (ticket == null) {
            System.err.println("Ticket is null.");
            return;
        }
        logger.info("Saving ticket to DB: " + ticket);
        ticketRepository.saveAndFlush(ticket);
    }

    public void saveComment(Ticket ticket, Comment comment) {
        if (ticket == null || comment == null) {
            System.err.println("Comment is null.");
            return;
        }
        ticket.addComment(comment);
        logger.info("Saving Comment to Comment in DB: " + ticket);
        commentRepository.saveAndFlush(comment);
    }

    public Project findProjectToTicket(Ticket ticket) {
        return ticketRepository.findProjectToTicket(ticket);
    }

    public List<Ticket> getUserTickets(){
        Users currentUser = SecurityUtils.getLoggedInUser();
        //logger.info("Current user: " + currentUser);
        if (currentUser != null) {
            return ticketRepository.findByAssignee(currentUser);
        }
        return Collections.emptyList();
    }

    public boolean isCurrentUserAssignee(Ticket ticket){
        Users currentUser = SecurityUtils.getLoggedInUser();
        return currentUser != null && currentUser.equals(ticket.getAssignee());
    }

    public boolean isCurrentUserManager(Project project){
        Users currentUser = SecurityUtils.getLoggedInUser();
        return currentUser != null && currentUser.equals(project.getManager());
    }

    public InputStream exportToExcel(String filterText) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Tickets");

        List<Ticket> tickets = findAllTickets(filterText);

        int rownum = 0;
        XSSFRow headerRow = sheet.createRow(rownum++);
        headerRow.createCell(0).setCellValue("Ticket Name");
        headerRow.createCell(1).setCellValue("Ticket Priority");
        headerRow.createCell(2).setCellValue("Ticket Status");
        headerRow.createCell(3).setCellValue("Project Name");
        headerRow.createCell(4).setCellValue("Person in Charge");

        // Set the width for the columns
        int numColumns = headerRow.getPhysicalNumberOfCells();
        for (int i = 0; i < numColumns; i++) {
            sheet.setColumnWidth(i, 5000);
        }

        for (Ticket ticket : tickets) {
            XSSFRow row = sheet.createRow(rownum++);
            row.createCell(0).setCellValue(ticket.getTicketName());
            row.createCell(1).setCellValue(ticket.getTicketPriority().ordinal());
            row.createCell(2).setCellValue(ticket.getTicketStatus().ordinal());
            row.createCell(3).setCellValue(ticket.getProject().getProjectName());
            row.createCell(4).setCellValue(ticket.getAssignee().getFirstName() + " " + ticket.getAssignee().getLastName());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            workbook.write(out);
            workbook.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    public void handleUploadedFile(InputStream inputStream, String fileName, Ticket ticket) {
        try {

            Document document = new Document();
            document.setFileName(fileName);
            document.setAuthor(SecurityUtils.getLoggedInUser());
            document.setDocumentCreationDate(LocalDate.now());
            document.setFileTypeFromFileName();
            document.setTicket(ticket);
            byte[] documentData = inputStream.readAllBytes();
            document.setDocumentData(documentData);

            documentRepository.saveAndFlush(document);

            logger.info("File uploaded successfully. File name: " + fileName);
        } catch (IOException e) {
            logger.error("Could not store the file. Error: " + e.getMessage());
        }
    }

    public List<Document> getUploadedFilesForTicket(Ticket currentTicket) {
        return documentRepository.findByTicket(currentTicket);
    }

    public void deleteDocument(Document file) {
        documentRepository.delete(file);
    }
}