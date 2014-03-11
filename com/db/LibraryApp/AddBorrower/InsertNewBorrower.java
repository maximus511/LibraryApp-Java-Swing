package com.db.LibraryApp.AddBorrower;

import com.db.LibraryApp.UtilityClasses.LibraryAppException;
import com.db.LibraryApp.UtilityClasses.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: Rahul Nair
 */
public class InsertNewBorrower {

    public String addBorrower(BorrowerFields borrowerFields) throws LibraryAppException {
        Connection con;
        PreparedStatement checkBorrowerExists;
        PreparedStatement insertBorrower;
        Statement generateCardNumber;
        Integer cardNumber = 0;
        String returnValue = "";
        try {
            con = MySQLConnection.getConnection();
            String checkBorrowerExistsQuery = "SELECT * FROM borrower WHERE fname=? AND lname=? AND address=?";
            checkBorrowerExists = con.prepareStatement(checkBorrowerExistsQuery);
            checkBorrowerExists.setString(1, borrowerFields.getFirstName());
            checkBorrowerExists.setString(2, borrowerFields.getLastName());
            checkBorrowerExists.setString(3, borrowerFields.getAddress());

            ResultSet resultSet = checkBorrowerExists.executeQuery();

            if (resultSet.next()) {
                returnValue = "Exists";
            } else {
                String cardNoQuery = "SELECT MAX(card_no) as card_no FROM borrower";
                String insertBorrowerQuery = "INSERT INTO borrower VALUES(?,?,?,?,?)";

                generateCardNumber = con.createStatement();
                ResultSet cardNumberResultSet = generateCardNumber.executeQuery(cardNoQuery);
                while (cardNumberResultSet.next()) {
                    cardNumber = cardNumberResultSet.getInt("card_no");
                }
                cardNumber++;

                insertBorrower = con.prepareStatement(insertBorrowerQuery);
                insertBorrower.setString(1, Integer.toString(cardNumber));
                insertBorrower.setString(2, borrowerFields.getFirstName());
                insertBorrower.setString(3, borrowerFields.getLastName());
                insertBorrower.setString(4, borrowerFields.getAddress());
                insertBorrower.setString(5, borrowerFields.getPhoneNumber());
                insertBorrower.executeUpdate();

                returnValue = cardNumber.toString();

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new LibraryAppException(e.getMessage());
        }
        return returnValue;
    }

}
