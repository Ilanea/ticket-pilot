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
public class TicketService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TicketRepository ticketRepository;


    public TicketService(UserRepository userRepository,
                         ProjectRepository projectRepository,
                         TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<Users> findAllContacts(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return userRepository.findAll();
        } else {
            return userRepository.search(stringFilter);
        }
    }

    public long countUsers() {
        return userRepository.count();
    }
    public long countTickets() { return ticketRepository.count(); }
    public long countProjects() { return projectRepository.count(); }

    public void deleteUser(Users user) {
        userRepository.delete(user);
    }

    public void saveUser(Users user) {
        if (user == null) {
            System.err.println("Contact is null. Are you sure you have connected your form to the application?");
            return;
        }
        userRepository.save(user);
    }


    public List<Project> findAllProjects(){ return projectRepository.findAll(); }
    public List<Ticket> findAllTickets(){
        return ticketRepository.findAll();
    }
    public List<Users> findAllUsers() { return userRepository.findAll(); }


}