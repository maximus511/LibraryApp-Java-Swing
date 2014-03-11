package com.db.LibraryApp.SearchBook;


import com.db.LibraryApp.UtilityClasses.MySQLConnection;
import com.db.LibraryApp.UtilityClasses.ResultRecord;
import com.db.LibraryApp.UtilityClasses.ResultSetTableModel;

import javax.swing.JTable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rahul Nair
 */
public class BookAvailabilityResults {

    JTable resultTable = null;
    ResultSet resultSet = null;

    public BookAvailabilityResults(JTable resultTable, BookSearchFields bookSearchFields) {
        this.resultTable = resultTable;
        fetchSearchResults(bookSearchFields);
    }

    public void fetchSearchResults(BookSearchFields bookSearchFields) {
        try {

            Connection con = MySQLConnection.getConnection();

            StringBuilder query = new StringBuilder("SELECT a.book_id, a.branch_id, no_of_copies," +
                    " (no_of_copies - IFNULL(b.num_out, 0)) AS num_avail " +
                    "from (SELECT bc.book_id, bc.branch_id, no_of_copies FROM book_copies bc " +
                    "LEFT OUTER JOIN book_loans bl ON bl.book_id = bc.book_id AND bl.branch_id = bc.branch_id " +
                    "GROUP BY bc.book_id , bc.branch_id) a LEFT JOIN (SELECT bl.book_id, bl.branch_id, " +
                    "COUNT(*) AS num_out FROM book_loans bl JOIN book_copies bc ON bl.book_id = bc.book_id " +
                    "AND bl.branch_id = bc.branch_id GROUP BY bl.book_id , bl.branch_id) b ON a.book_id = b.book_id " +
                    "AND a.branch_id = b.branch_id ");
            if ((null != bookSearchFields.getBookId() && !"".equals(bookSearchFields.getBookId())) ||
                    (null != bookSearchFields.getTitle() && !"".equals(bookSearchFields.getTitle()))) {
                query.append("JOIN book c ON c.book_id = a.book_id ");
            }
            if (null != bookSearchFields.getAuthorName() && !"".equals(bookSearchFields.getAuthorName())) {
                query.append("JOIN book_authors d ON a.book_id=d.book_id ");
            }
            query.append("where ");

            boolean isPrevCondition = false;
            int index = 0;
            if (null != bookSearchFields.getBookId() && !"".equals(bookSearchFields.getBookId())) {
                query.append("a.book_id = ? ");
                isPrevCondition = true;
                index++;
            }
            if (null != bookSearchFields.getAuthorName() && !"".equals(bookSearchFields.getAuthorName())) {
                if (isPrevCondition) {
                    query.append("and ");
                }
                query.append("d.author_name like ? ");
                isPrevCondition = true;
                index++;
            }
            if (null != bookSearchFields.getTitle() && !"".equals(bookSearchFields.getTitle())) {
                if (isPrevCondition) {
                    query.append("and ");
                }
                query.append("c.title like ? ");
                index++;
            }
            PreparedStatement st = con.prepareStatement(query.toString());
            isPrevCondition = false;
            if (null != bookSearchFields.getBookId() && !"".equals(bookSearchFields.getBookId())) {
                st.setString(1, bookSearchFields.getBookId());
                isPrevCondition = true;
            }
            if (null != bookSearchFields.getAuthorName() && !"".equals(bookSearchFields.getAuthorName())) {
                if (isPrevCondition) {
                    st.setString(2, "%" + bookSearchFields.getAuthorName() + "%");
                } else {
                    st.setString(1, "%" + bookSearchFields.getAuthorName() + "%");
                }
            }
            if (null != bookSearchFields.getTitle() && !"".equals(bookSearchFields.getTitle())) {
                st.setString(index, "%" + bookSearchFields.getTitle() + "%");
            }

            resultSet = st.executeQuery();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    protected JTable bindResultSetWithTable() throws Exception {
        List<ResultRecord> resultRecords = new ArrayList<ResultRecord>();
        Object[] values;
        while (resultSet.next()) {
            values = new Object[4];
            values[0] = resultSet.getString("book_Id");
            values[1] = resultSet.getString("branch_id");
            values[2] = resultSet.getString("no_of_copies");
            values[3] = resultSet.getString("num_avail");
            ResultRecord resultRecord = new ResultRecord(values);
            resultRecords.add(resultRecord);
        }
        bindWithTableModel(resultRecords);
        return this.resultTable;
    }

    protected void bindWithTableModel(List<ResultRecord> chunks) {
        ResultSetTableModel tableModel = (this.resultTable.getModel() instanceof ResultSetTableModel ? (ResultSetTableModel) this.resultTable.getModel() : null);
        if (tableModel == null) {
            try {
                tableModel = new ResultSetTableModel(resultSet.getMetaData(), chunks);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (tableModel != null) {
                tableModel.setSourceScreen("SearchBooks");
                this.resultTable.setModel(tableModel);
            }
        } else {
            tableModel.getResultRecords().clear();
            tableModel.getResultRecords().addAll(chunks);
        }
        if (tableModel != null) {
            tableModel.fireTableDataChanged();
        }
    }
}
