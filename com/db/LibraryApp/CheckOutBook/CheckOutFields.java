package com.db.LibraryApp.CheckOutBook;

/**
 * Created with IntelliJ IDEA.
 * User: Rahul Nair
 */
public class CheckOutFields {
    public String bookId;
    public String cardNumber;
    public String branchId;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }
}
