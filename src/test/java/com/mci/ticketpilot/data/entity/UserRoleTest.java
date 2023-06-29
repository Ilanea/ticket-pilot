package com.mci.ticketpilot.data.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserRoleTest {

    @Test
    public void testEnumValues() {
        // Assert
        Assertions.assertEquals(3, UserRole.values().length);
        Assertions.assertEquals(UserRole.ADMIN, UserRole.valueOf("ADMIN"));
        Assertions.assertEquals(UserRole.MANAGER, UserRole.valueOf("MANAGER"));
        Assertions.assertEquals(UserRole.USER, UserRole.valueOf("USER"));
    }

    @Test
    public void testRoleValues() {
        // Arrange
        UserRole adminRole = UserRole.ADMIN;
        UserRole managerRole = UserRole.MANAGER;
        UserRole userRole = UserRole.USER;

        // Assert
        Assertions.assertEquals(0, adminRole.ordinal());
        Assertions.assertEquals(1, managerRole.ordinal());
        Assertions.assertEquals(2, userRole.ordinal());
    }
}


