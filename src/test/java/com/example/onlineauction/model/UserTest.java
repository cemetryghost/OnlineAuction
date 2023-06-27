package com.example.onlineauction.model;

import com.example.onlineauction.constants.Role;
import com.example.onlineauction.constants.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User user;

    @BeforeEach
    public void setup() {
        // Инициализация объекта User перед каждым тестом
        user = new User("John", "Doe", "johndoe", "password", LocalDate.of(1990, 5, 15), Role.BUYER, Status.ACTIVE);
    }

    @Test
    public void testGetters() {
        assertEquals("John", user.getName());
        assertEquals("Doe", user.getSurname());
        assertEquals("johndoe", user.getLogin());
        assertEquals("password", user.getPassword());
        assertEquals(LocalDate.of(1990, 5, 15), user.getBirth_date());
        assertEquals(Role.BUYER, user.getRole());
        assertEquals(Status.ACTIVE, user.getStatus());
    }

    @Test
    public void testSetters() {
        user.setName("Jane");
        user.setSurname("Smith");
        user.setLogin("janesmith");
        user.setPassword("newpassword");
        user.setBirth_date(LocalDate.of(1995, 8, 20));
        user.setRole(Role.ADMIN);
        user.setStatus(Status.BLOCK);

        assertEquals("Jane", user.getName());
        assertEquals("Smith", user.getSurname());
        assertEquals("janesmith", user.getLogin());
        assertEquals("newpassword", user.getPassword());
        assertEquals(LocalDate.of(1995, 8, 20), user.getBirth_date());
        assertEquals(Role.ADMIN, user.getRole());
        assertEquals(Status.BLOCK, user.getStatus());
    }

    @Test
    public void testIsBlocked() {
        assertFalse(user.isBlocked());

        user.setStatus(Status.BLOCK);
        assertTrue(user.isBlocked());
    }
}