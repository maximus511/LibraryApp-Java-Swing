package com.db.LibraryApp.InsertBook;

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
public class InsertNewBook {
    public void insertNewBook(BookFields bookFields) throws LibraryAppException {
        Connection con = null;
        PreparedStatement checkForExistingBook;
        PreparedStatement checkForExistingBranch;
        PreparedStatement insertNewBook;
        PreparedStatement insertNewBookWithAuthor;
        PreparedStatement insertNumOfCopies;
        try {
            con = MySQLConnection.getConnection();
            String existingBookQuery = "SELECT count(*) as count FROM book WHERE book_id=?";
            checkForExistingBook = con.prepareStatement(existingBookQuery);
            checkForExistingBook.setString(1, bookFields.getBookId());

            ResultSet existingBooks = checkForExistingBook.executeQuery();
            if (existingBooks.next() && existingBooks.getInt("count") > 0) {
                throw new LibraryAppException("This book (Book ID - " + bookFields.getBookId() + ") already exists in the system");
            }

            String existingBranchQuery = "SELECT count(*) as count FROM library_branch WHERE branch_id=?";
            checkForExistingBranch = con.prepareStatement(existingBranchQuery);
            checkForExistingBranch.setString(1, bookFields.getBranchId());

            ResultSet existingBranch = checkForExistingBranch.executeQuery();
            if (existingBranch.next() && existingBranch.getInt("count") == 0) {
                throw new LibraryAppException("This branch (Branch ID - " + bookFields.getBranchId() + ") does not exist in the system");
            }

            String insertNewBookQuery = "INSERT INTO book VALUES(?,?)";
            insertNewBook = con.prepareStatement(insertNewBookQuery);
            insertNewBook.setString(1, bookFields.getBookId());
            insertNewBook.setString(2, bookFields.getTitle());
            insertNewBook.executeUpdate();


            String insertNewBookEntryInAuthors = "INSERT INTO book_authors VALUES(?,?)";
            for (String authorName : bookFields.getAuthorNames()) {
                insertNewBookWithAuthor = con.prepareStatement(insertNewBookEntryInAuthors);
                insertNewBookWithAuthor.setString(1, bookFields.getBookId());
                insertNewBookWithAuthor.setString(2, authorName.trim());
                insertNewBookWithAuthor.executeUpdate();
            }

            String insertNumOfCopiesQuery = "INSERT INTO book_copies VALUES(?,?,?)";
            insertNumOfCopies = con.prepareStatement(insertNumOfCopiesQuery);
            insertNumOfCopies.setString(1, bookFields.getBookId());
            insertNumOfCopies.setString(2, bookFields.getBranchId());
            insertNumOfCopies.setInt(3, bookFields.getNumOfCopies());
            insertNumOfCopies.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}