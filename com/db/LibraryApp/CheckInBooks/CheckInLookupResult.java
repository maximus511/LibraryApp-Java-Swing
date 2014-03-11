package com.db.LibraryApp.CheckInBooks;


import com.db.LibraryApp.UtilityClasses.MySQLConnection;
import com.db.LibraryApp.UtilityClasses.ResultRecord;
import com.db.LibraryApp.UtilityClasses.ResultSetTableModel;

import javax.swing.JTable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rahul Nair
 */
public class CheckInLookupResult {
    JTable table = null;
    ResultSet resultSet = null;
    public static Connection con = null;

    public CheckInLookupResult(JTable table, CheckInLookupFields checkInLookupFields) {
        this.table = table;
        fetchSearchResults(checkInLookupFields);
    }

    public void fetchSearchResults(CheckInLookupFields checkInLookupFields) {
        try {

            con = MySQLConnection.getConnection();

            StringBuilder query = new StringBuilder("select l.book_id, l.branch_id, l.card_no, l.date_out, l.due_date " +
                    "from book_loans l , borrower b " +
                    "where l.card_no = b.card_no ");
            boolean isPrevCondition = false;
            int index = 0;
            if (null != checkInLookupFields.getBookId() && !"".equals(checkInLookupFields.getBookId())) {
                query.append("and l.book_id = ? ");
                index++;
            }
            if (null != checkInLookupFields.getCardNumber() && !"".equals(checkInLookupFields.getCardNumber())) {
                query.append("and l.card_no=? ");
                index++;
            }
            if (null != checkInLookupFields.getBorrowerName() && !"".equals(checkInLookupFields.getBorrowerName())) {
                query.append("and ( b.fname like ? or b.lname like ? )");
                index++;
            }
            PreparedStatement st = con.prepareStatement(query.toString());
            if (null != checkInLookupFields.getBookId() && !"".equals(checkInLookupFields.getBookId())) {
                st.setString(1, checkInLookupFields.getBookId());
                isPrevCondition = true;
            }
            if (null != checkInLookupFields.getCardNumber() && !"".equals(checkInLookupFields.getCardNumber())) {
                if (isPrevCondition) {
                    st.setString(2, checkInLookupFields.getCardNumber());
                } else {
                    st.setString(1, checkInLookupFields.getCardNumber());
                }
            }
            if (null != checkInLookupFields.getBorrowerName() && !"".equals(checkInLookupFields.getBorrowerName())) {
                st.setString(index, "%" + checkInLookupFields.getBorrowerName() + "%");
                st.setString(index + 1, "%" + checkInLookupFields.getBorrowerName() + "%");
            }

            resultSet = st.executeQuery();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    protected JTable bindResultsetWithTable() throws Exception {
        List<ResultRecord> resultRecords = new ArrayList<ResultRecord>();
        Object[] values;
        while (resultSet.next()) {
            values = new Object[5];
            values[0] = resultSet.getString("book_Id");
            values[1] = resultSet.getString("branch_id");
            values[2] = resultSet.getString("card_no");
            values[3] = resultSet.getDate("date_out");
            values[4] = resultSet.getDate("due_date");
            ResultRecord resultRecord = new ResultRecord(values);
            resultRecords.add(resultRecord);
        }
        process(resultRecords);
        return this.table;
    }

    protected void process(List<ResultRecord> chunks) {
        ResultSetTableModel tableModel = (this.table.getModel() instanceof ResultSetTableModel ? (ResultSetTableModel) this.table.getModel() : null);
        if (tableModel == null) {
            try {
                tableModel = new ResultSetTableModel(this.resultSet.getMetaData(), chunks);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (tableModel != null) {
                tableModel.setSourceScreen("CheckIn");
                this.table.setModel(tableModel);
            }
        } else {
            tableModel.getResultRecords().clear();
            tableModel.getResultRecords().addAll(chunks);
        }
        if (tableModel != null) {
            tableModel.fireTableDataChanged();
        }
    }


    public static void removeRecord(HashMap<Integer, String[]> parameters) throws SQLException {
        try {
            con = MySQLConnection.getConnection();
            for (int currentValue = 0; currentValue < parameters.keySet().size(); currentValue++) {
                String deleteQuery = "delete from book_loans where book_id=? and branch_id=? and card_no=?";
                PreparedStatement statement = con.prepareStatement(deleteQuery);
                statement.setString(1, parameters.get(currentValue)[0]);
                statement.setString(2, parameters.get(currentValue)[1]);
                statement.setString(3, parameters.get(currentValue)[2]);
                statement.executeUpdate();
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
