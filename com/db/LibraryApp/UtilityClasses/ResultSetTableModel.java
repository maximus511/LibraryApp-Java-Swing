package com.db.LibraryApp.UtilityClasses;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created with IntelliJ IDEA.
 * User: Rahul Nair
 */
public class ResultSetTableModel extends AbstractTableModel {
    private final ResultSetMetaData resultSetMetaData;
    private final String[] searchBookCols = new String[]{"Book ID", "Branch ID", "Total Copies", "Available Copies"};
    private final String[] checkInCols = new String[]{"Book ID", "Branch ID", "Card No.", "Date Out", "Due Date"};

    private List<ResultRecord> resultRecords;

    private String sourceScreen = null;

    public ResultSetTableModel(ResultSetMetaData resultSetMetaData, List<ResultRecord> resultRecords) {
        this.resultSetMetaData = resultSetMetaData;
        if (resultRecords != null) {
            this.resultRecords = resultRecords;
        } else {
            this.resultRecords = new ArrayList<ResultRecord>();
        }

    }

    public int getRowCount() {
        return resultRecords.size();
    }

    public int getColumnCount() {
        try {
            return resultSetMetaData.getColumnCount();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Object getValue(int row, int column) {
        return resultRecords.get(row).getValue(column);
    }

    public String getColumnName(int col) {
        if ("SearchBooks".equals(getSourceScreen())) {
            return searchBookCols[col];
        }
        return checkInCols[col];
    }

    public Class<?> getColumnClass(int col) {
        String className = "";
        try {
            className = resultSetMetaData.getColumnClassName(col + 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return className.getClass();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex > resultRecords.size()) {
            return null;
        }
        return resultRecords.get(rowIndex).getValue(columnIndex);
    }

    public List<ResultRecord> getResultRecords() {
        return this.resultRecords;
    }

    public void setResultRecords(List<ResultRecord> resultRecords) {
        this.resultRecords = resultRecords;
    }

    public String getSourceScreen() {
        return sourceScreen;
    }

    public void setSourceScreen(String sourceScreen) {
        this.sourceScreen = sourceScreen;
    }

    public void deleteRow(int[] rows) {
        ListIterator<ResultRecord> recordIterator = resultRecords.listIterator();
        int deletedRowCount=0;
        while(recordIterator.hasNext())
        {
            for(int row: rows)
            {
                if(recordIterator.nextIndex()==row)
                {
                    recordIterator.next();
                    recordIterator.remove();
                    deletedRowCount++;
                }
            }
            if(deletedRowCount==rows.length)
            {
                break;
            }
        }
        fireTableDataChanged();
    }
}
