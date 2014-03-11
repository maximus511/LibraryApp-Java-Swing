package com.db.LibraryApp.UpdateBookCount;

import com.db.LibraryApp.InsertBook.BookFields;
import com.db.LibraryApp.UtilityClasses.LibraryAppException;
import com.db.LibraryApp.UtilityClasses.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: Rahul Nair
 */
public class UpdateBookCountResults {
    public void updateBookCount(BookFields bookFields) throws LibraryAppException {
        Connection con = null;
        PreparedStatement checkForExistingBook;
        PreparedStatement checkForExistingBranch;
        PreparedStatement updateCopies;
        try {
            con = MySQLConnection.getConnection();
            String existingBookQuery = "SELECT count(*) as count FROM book WHERE book_id=?";
            checkForExistingBook = con.prepareStatement(existingBookQuery);
            checkForExistingBook.setString(1, bookFields.getBookId());

            ResultSet existingBooks = checkForExistingBook.executeQuery();
            if (existingBooks.next() && existingBooks.getInt("count") == 0) {
                throw new LibraryAppException("This book (Book ID - " + bookFields.getBookId() + ") does not exist in the system");
            }

            String existingBranchQuery = "SELECT count(*) as count FROM library_branch WHERE branch_id=?";
            checkForExistingBranch = con.prepareStatement(existingBranchQuery);
            checkForExistingBranch.setString(1, bookFields.getBranchId());

            ResultSet existingBranch = checkForExistingBranch.executeQuery();
            if (existingBranch.next() && existingBranch.getInt("count") == 0) {
                throw new LibraryAppException("This branch (Branch ID - " + bookFields.getBranchId() + ") does not exist in the system");
            }

            String existingCount = "SELECT no_of_copies as count FROM book_copies WHERE book_id=? and branch_id=?";
            PreparedStatement count = con.prepareStatement(existingCount);
            count.setString(1, bookFields.getBookId());
            count.setString(2, bookFields.getBranchId());
            ResultSet rs = count.executeQuery();
            int existingCopies = 0;
            while (rs.next())
            {
                existingCopies = rs.getInt("count");
            }
            String updateBookCount = "UPDATE Book_Copies SET no_of_copies=? WHERE book_id=? and branch_id=? ";
            updateCopies = con.prepareStatement(updateBookCount);
            updateCopies.setInt(1, bookFields.getNumOfCopies() + existingCopies);
            updateCopies.setString(2, bookFields.getBookId());
            updateCopies.setString(3, bookFields.getBranchId());
            updateCopies.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
