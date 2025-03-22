package com.swe.libraryprogram.orm;

import com.swe.libraryprogram.domainmodel.Element;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;


public class BorrowsDAO {


    public BorrowsDAO() {
    }


    public Boolean addBorrow(Integer element_id, String user_id) throws SQLException {

        String query = "INSERT INTO borrows (elementid, userid) VALUES (?, ?)";

        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);

        stmt.setInt(1, element_id);
        stmt.setString(2, user_id);

        int rowsAffected = stmt.executeUpdate();
        stmt.close();
        if (rowsAffected > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean removeBorrow(Integer element_id, String user_id) throws SQLException {

        String query = "DELETE FROM borrows WHERE elementid = ? AND userid = ?";

        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);

        stmt.setInt(1, element_id);
        stmt.setString(2, user_id);

        int rowsAffected = stmt.executeUpdate();
        stmt.close();
        if (rowsAffected > 0) {
            return true;
        } else {
            return false;
        }

    }

    public List<Element> getBorrowedElementsForUser(String user_id) throws SQLException {

        List<Element> elements = new ArrayList<>();

        String query = "SELECT elementid FROM borrows WHERE userid = ?";

        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, user_id);

        ResultSet rs = stmt.executeQuery();

        ElementDAO elementDAO = new ElementDAO();

        while (rs.next()) {

            int elementId = rs.getInt("elementid");

            Element element = elementDAO.getElement(elementId);

            if (element != null) {
                elements.add(element);

            }
        }
        stmt.close();
        return elements;
    }


}
