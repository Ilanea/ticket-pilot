package com.mci.ticketpilot.security;

import com.mci.ticketpilot.data.entity.UserRole;
import com.mci.ticketpilot.data.entity.UserStatus;
import com.mci.ticketpilot.data.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class PilotUserPrincipalTest {

    @Test
    void testGetAuthorities_ReturnsUserRoleWithRolePrefix() {
        Users user = new Users();
        user.setUserRole(UserRole.USER);

        PilotUserPrincipal userPrincipal = new PilotUserPrincipal(user);

        Collection<? extends GrantedAuthority> authorities = userPrincipal.getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void testGetPassword_ReturnsUserPassword() {
        String password = "password123";

        Users user = new Users();
        user.setPassword(password);

        PilotUserPrincipal userPrincipal = new PilotUserPrincipal(user);

        String retrievedPassword = userPrincipal.getPassword();

        assertEquals(password, retrievedPassword);
    }

    @Test
    void testGetUsername_ReturnsUserEmail() {
        String email = "user@test.com";

        Users user = new Users();
        user.setEmail(email);

        PilotUserPrincipal userPrincipal = new PilotUserPrincipal(user);

        String retrievedEmail = userPrincipal.getUsername();

        assertEquals(email, retrievedEmail);
    }

    @Test
    void testIsAccountNonExpired_AlwaysReturnsTrue() {
        Users user = new Users();

        PilotUserPrincipal userPrincipal = new PilotUserPrincipal(user);

        assertTrue(userPrincipal.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked_AlwaysReturnsTrue() {
        Users user = new Users();

        PilotUserPrincipal userPrincipal = new PilotUserPrincipal(user);

        assertTrue(userPrincipal.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired_AlwaysReturnsTrue() {
        Users user = new Users();

        PilotUserPrincipal userPrincipal = new PilotUserPrincipal(user);

        assertTrue(userPrincipal.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled_UserStatusActive_ReturnsTrue() {
        Users user = new Users();
        user.setUserStatus(UserStatus.ACTIVE);

        PilotUserPrincipal userPrincipal = new PilotUserPrincipal(user);

        assertTrue(userPrincipal.isEnabled());
    }

    @Test
    void testIsEnabled_UserStatusInactive_ReturnsFalse() {
        Users user = new Users();
        user.setUserStatus(UserStatus.INACTIVE);

        PilotUserPrincipal userPrincipal = new PilotUserPrincipal(user);

        assertFalse(userPrincipal.isEnabled());
    }

    @Test
    void testGetUser_ReturnsUserObject() {
        Users user = new Users();

        PilotUserPrincipal userPrincipal = new PilotUserPrincipal(user);

        assertEquals(user, userPrincipal.getUser());
    }
}
