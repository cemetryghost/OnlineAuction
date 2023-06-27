package com.example.onlineauction.dao;

import com.example.onlineauction.model.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryDAOTest {
    @Mock
    private Connection connectionMock;

    @Mock
    private PreparedStatement statementMock;

    @Mock
    private ResultSet resultSetMock;

    private CategoryDAO categoryDAO;

    @BeforeEach
    public void setup() throws SQLException {
        MockitoAnnotations.openMocks(this);
        categoryDAO = new CategoryDAO(connectionMock);
    }

    @Test
    public void testCreate() throws SQLException {
        Category category = new Category(1, "Test Category");
        String query = "INSERT INTO category (name_category) VALUES (?)";

        when(connectionMock.prepareStatement(query)).thenReturn(statementMock);
        categoryDAO.create(category);

        verify(connectionMock).prepareStatement(query);
        verify(statementMock).setString(1, category.getName());
        verify(statementMock).executeUpdate();
    }

    @Test
    public void testUpdate() throws SQLException {
        Category category = new Category(1, "Updated Category");
        String query = "UPDATE category SET name_category = ? WHERE idcategory = ?";

        when(connectionMock.prepareStatement(query)).thenReturn(statementMock);

        categoryDAO.update(category);

        verify(connectionMock).prepareStatement(query);
        verify(statementMock).setString(1, category.getName());
        verify(statementMock).setInt(2, category.getId());
        verify(statementMock).executeUpdate();
    }

    @Test
    public void testDelete() throws SQLException {
        int categoryId = 1;
        String query = "DELETE FROM category WHERE idcategory = ?";

        when(connectionMock.prepareStatement(query)).thenReturn(statementMock);

        categoryDAO.delete(categoryId);

        verify(connectionMock).prepareStatement(query);
        verify(statementMock).setInt(1, categoryId);
        verify(statementMock).executeUpdate();
    }

    @Test
    public void testIsCategoryUsed_WhenCategoryIsUsed() throws SQLException {
        int categoryId = 1;
        String query = "SELECT COUNT(*) FROM lots WHERE category_id = ?";

        when(connectionMock.prepareStatement(query)).thenReturn(statementMock);
        when(statementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt(1)).thenReturn(1);

        boolean result = categoryDAO.isCategoryUsed(categoryId);

        verify(connectionMock).prepareStatement(query);
        verify(statementMock).setInt(1, categoryId);
        verify(statementMock).executeQuery();
        assertTrue(result);
    }

    @Test
    public void testIsCategoryUsed_WhenCategoryIsNotUsed() throws SQLException {

        int categoryId = 1;
        String query = "SELECT COUNT(*) FROM lots WHERE category_id = ?";

        when(connectionMock.prepareStatement(query)).thenReturn(statementMock);
        when(statementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt(1)).thenReturn(0);

        boolean result = categoryDAO.isCategoryUsed(categoryId);

        verify(connectionMock).prepareStatement(query);
        verify(statementMock).setInt(1, categoryId);
        verify(statementMock).executeQuery();
        assertFalse(result);
    }
}
