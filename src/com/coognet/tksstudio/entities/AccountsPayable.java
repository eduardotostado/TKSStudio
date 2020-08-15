package com.coognet.tksstudio.entities;

import java.math.BigDecimal;

public class AccountsPayable extends QueryObject {

    private final int accountsPayableID;
    private int accountID;
    private String accountName;
    private BigDecimal balance;
    private String dueDate;


    public AccountsPayable(int accountsPayableID, int accountID, String accountName, BigDecimal balance, String dueDate) {
        this.accountsPayableID = accountsPayableID;
        this.accountID = accountID;
        this.accountName = accountName;
        this.balance = balance.setScale(2);
        this.dueDate = dueDate;
    }

    public boolean editAccount(int accountID, BigDecimal balance, String dueDate){
        setAccountID(accountID);
        setBalance(balance);
        setDueDate(dueDate);

        statement = "UPDATE Accounts_Payable " +
                "SET " +
                "Account_ID = " + this.getAccountID() +  ", " +
                "Balance = " + this.getBalance() +  ", " +
                "DueDate = '" + this.getDueDate() + "' " +
                "WHERE AccountsPayable_ID = " + this.getAccountsPayableID();

        return query(statement);
    }

    public boolean addAccount(int accountID, BigDecimal balance, String dueDate){
        statement = "INSERT INTO Accounts_Payable (Account_ID, Balance, DueDate) VALUES (" +
                accountID + ", " + balance + ", '" + dueDate +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Accounts_Payable WHERE AccountsPayable_ID = " +
                        this.getAccountsPayableID();
        return query(statement);
    }

    public int getAccountsPayableID() {
        return accountsPayableID;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getAccountName() {
        return accountName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
