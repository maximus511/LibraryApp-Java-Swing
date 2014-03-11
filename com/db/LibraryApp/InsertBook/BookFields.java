package com.db.LibraryApp.InsertBook;

/**
 * Created with IntelliJ IDEA.
 * User: Rahul Nair
 */
public class BookFields {

    String bookId;
    String title;
    String[] authorNames;
    String branchId;
    Integer numOfCopies;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getAuthorNames() {
        return authorNames;
    }

    public void setAuthorNames(String[] authorNames) {
        this.authorNames = authorNames;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public Integer getNumOfCopies() {
        return numOfCopies;
    }

    public void setNumOfCopies(Integer numOfCopies) {
        this.numOfCopies = numOfCopies;
    }

}
