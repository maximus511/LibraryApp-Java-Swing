package com.db.LibraryApp.UtilityClasses;

/**
 * Created with IntelliJ IDEA.
 * User: Rahul Nair
 */
public class ResultRecord {
    private final Object[]  values;

    public ResultRecord(Object[] values) {
        this.values = values;
    }

    public int getSize() {
        return values.length;
    }

    public Object getValue(int i) {
        return values[i];
    }
}
