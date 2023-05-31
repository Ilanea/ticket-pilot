package com.mci.ticketpilot.data.service;

import com.mci.ticketpilot.data.entity.Project;
import com.mci.ticketpilot.data.entity.Ticket;
import com.mci.ticketpilot.data.entity.Users;
import com.mci.ticketpilot.data.repository.UserRepository;
import com.mci.ticketpilot.data.repository.ProjectRepository;
import com.mci.ticketpilot.data.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PilotService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TicketRepository ticketRepository;

    public PilotService(UserRepository userRepository,
                        ProjectRepository projectRepository,
                        TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.ticketRepository = ticketRepository;
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
        userRepository.save(user);
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
        projectRepository.delete(project);
    }

    public void saveProject(Project project) {
        if (project == null) {
            System.err.println("Project is null.");
            return;
        }
        projectRepository.save(project);
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
    public long countTickets() { return ticketRepository.count(); }

    public void deleteTicket(Ticket ticket) {
        ticketRepository.delete(ticket);
    }

    public void saveTicket(Ticket ticket) {
        if (ticket == null) {
            System.err.println("Ticket is null.");
            return;
        }
        ticketRepository.save(ticket);
    }

    public Project findProjectToTicket(Ticket ticket) {
        return ticketRepository.findProjectToTicket(ticket);
    }
}