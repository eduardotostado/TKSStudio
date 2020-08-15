package com.coognet.tksstudio.entities;


public class Account extends QueryObject {

    private final int accountID;
    private int deptID;
    private String deptName;
    private String accountName;
    private String accountType;


    public Account(int accountID, int deptID, String deptName, String accountName, String accountType){
        this.accountID = accountID;
        this.deptID = deptID;
        this.deptName = deptName;
        this.accountName = accountName;
        this.accountType = accountType;
    }

    public Account(int accountID, String accountName) {
        this.accountID = accountID;
        this.accountName = accountName;
    }

    public boolean editAccount(int deptID, String accountName, String accountType) {

        setDeptID(deptID);
        setAccountName(accountName);
        setAccountType(accountType);


        statement = "UPDATE Account " +
                "SET " +
                "Dept_ID = " + this.getDeptID() +  ", " +
                "Account_Name = '" + this.getAccountName() +  "', " +
                "Account_Type = '" + this.getAccountType() + "' " +
                "WHERE Account_ID = " + this.getAccountID();

        return query(statement);
    }

    public boolean addAccount(int deptID, String accountName, String accountType){
            statement = "INSERT INTO Account (Dept_ID, Account_Name, Account_Type) VALUES ("
                    + deptID + ", '" + accountName + "', '" + accountType +
                            "')";

            return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Account WHERE Account_ID = " +
                        this.getAccountID();
        return query(statement);
    }

    public int getAccountID() {
        return accountID;
    }

    public int getDeptID() {
        return deptID;
    }

    public void setDeptID(int deptID) {
        this.deptID = deptID;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getDeptName() {
        return deptName;
    }

    public String toString(){
        return this.getAccountName();
    }
}