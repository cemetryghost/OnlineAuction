package com.example.onlineauction.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LotTest {
    private Lot lot;

    @BeforeEach
    public void setUp() {
        lot = new Lot(1, "Example Lot", "Category", 100.0, 150.0, "Active");
    }

    @Test
    public void testGetId() {
        int expectedId = 1;
        int actualId = lot.getId();
        Assertions.assertEquals(expectedId, actualId);
    }

    @Test
    public void testGetName() {
        String expectedName = "Example Lot";
        String actualName = lot.getName();
        Assertions.assertEquals(expectedName, actualName);
    }

    @Test
    public void testGetCategory() {
        String expectedCategory = "Category";
        String actualCategory = lot.getCategory();
        Assertions.assertEquals(expectedCategory, actualCategory);
    }

    @Test
    public void testGetStartPrice() {
        double expectedStartPrice = 100.0;
        double actualStartPrice = lot.getStartPrice();
        Assertions.assertEquals(expectedStartPrice, actualStartPrice, 0.001);
    }

    @Test
    public void testGetCurrentPrice() {
        double expectedCurrentPrice = 150.0;
        double actualCurrentPrice = lot.getCurrentPrice();
        Assertions.assertEquals(expectedCurrentPrice, actualCurrentPrice, 0.001);
    }

    @Test
    public void testGetStatusString() {
        String expectedStatusString = "Active";
        String actualStatusString = lot.getStatusString();
        Assertions.assertEquals(expectedStatusString, actualStatusString);
    }

    @Test
    public void testSetId() {
        int newId = 2;
        lot.setId(newId);
        int updatedId = lot.getId();
        Assertions.assertEquals(newId, updatedId);
    }

    @Test
    public void testSetName() {
        String newName = "New Lot";
        lot.setName(newName);
        String updatedName = lot.getName();
        Assertions.assertEquals(newName, updatedName);
    }

    @Test
    public void testSetCategory() {
        String newCategory = "New Category";
        lot.setCategory(newCategory);
        String updatedCategory = lot.getCategory();
        Assertions.assertEquals(newCategory, updatedCategory);
    }

    @Test
    public void testSetStartPrice() {
        double newStartPrice = 200.0;
        lot.setStartPrice(newStartPrice);
        double updatedStartPrice = lot.getStartPrice();
        Assertions.assertEquals(newStartPrice, updatedStartPrice, 0.001);
    }

    @Test
    public void testSetCurrentPrice() {
        double newCurrentPrice = 250.0;
        lot.setCurrentPrice(newCurrentPrice);
        double updatedCurrentPrice = lot.getCurrentPrice();
        Assertions.assertEquals(newCurrentPrice, updatedCurrentPrice, 0.001);
    }

    @Test
    public void testSetStatusString() {
        String newStatusString = "Inactive";
        lot.setStatusString(newStatusString);
        String updatedStatusString = lot.getStatusString();
        Assertions.assertEquals(newStatusString, updatedStatusString);
    }

}