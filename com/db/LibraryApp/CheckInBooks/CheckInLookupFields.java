package com.db.LibraryApp.CheckInBooks;
/**
 * Created with IntelliJ IDEA.
 * User: Rahul Nair
 */
public class CheckInLookupFields {
    public String bookId;
    public String cardNumber;
    public String borrowerName;

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

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }
}

