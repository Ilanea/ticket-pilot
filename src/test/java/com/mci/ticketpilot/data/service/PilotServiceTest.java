package com.mci.ticketpilot.data.service;

import com.mci.ticketpilot.data.entity.Users;
import com.mci.ticketpilot.data.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class PilotServiceTest {
    private PilotService pilotService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private DocumentRepository documentRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        pilotService = new PilotService(userRepository, projectRepository, ticketRepository, commentRepository, documentRepository);
    }

    @Test
    public void testFindAllUsers_WithNullFilter_ReturnsAllUsers() {
        // Arrange
        List<Users> users = new ArrayList<>();
        users.add(new Users());
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<Users> result = pilotService.findAllUsers(null);

        // Assert
        verify(userRepository).findAll();
        // Add additional assertions as needed
    }

    // Add more test methods for other methods in PilotService class

    // Example test method for saveUser
    @Test
    public void testSaveUser_WithValidUser_CallsSaveAndFlush() {
        // Arrange
        Users user = new Users();

        // Act
        pilotService.saveUser(user);

        // Assert
        verify(userRepository).saveAndFlush(user);
    }
}
