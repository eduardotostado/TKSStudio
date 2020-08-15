package com.coognet.tksstudio.entities;

import java.math.BigDecimal;

public class EmployeePurchaseOrder extends QueryObject {

    private final int ePOID;
    private int empID;
    private int accountsPayableID;
    private BigDecimal ePOTotal;
    private String ePODate;

    public EmployeePurchaseOrder(int ePOID, int empID, int accountsPayableID, BigDecimal ePOTotal, String ePODate) {
        this.ePOID = ePOID;
        this.empID = empID;
        this.accountsPayableID = accountsPayableID;
        this.ePOTotal = ePOTotal.setScale(2);
        this.ePODate = ePODate;
    }

    public boolean editAccount(int empID, int accountsPayableID, BigDecimal ePOTotal, String ePODate){
        setEmpID(empID);
        setAccountsPayableID(accountsPayableID);
        setEPOTotal(ePOTotal);
        setEPODate(ePODate);

        statement = "UPDATE Employee_Purchase_Order " +
                "SET " +
                "Emp_ID = " + this.getEmpID() +  ", " +
                "AccountsPayable_ID = " + this.getAccountsPayableID() +  ", " +
                "EPO_Total = " + this.getEPOTotal() +  ", " +
                "EPO_Date = '" + this.getEPODate() + "' " +
                "WHERE EPO_ID = " + this.getEPOID();

        return query(statement);
    }

    public boolean addAccount(int empID, int accountsPayableID, BigDecimal ePOTotal, String ePODate){
        statement = "INSERT INTO Employee_Purchase_Order (Emp_ID, AccountsPayable_ID, EPO_Total, EPO_Date) VALUES (" +
                empID + ", " + accountsPayableID + ", " + ePOTotal + ", '" + ePODate +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Employee_Purchase_Order WHERE EPO_ID = " +
                        this.getEPOID();
        return query(statement);
    }

    public int getEPOID() {
        return ePOID;
    }

    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public int getAccountsPayableID() {
        return accountsPayableID;
    }

    public void setAccountsPayableID(int accountsPayableID) {
        this.accountsPayableID = accountsPayableID;
    }

    public BigDecimal getEPOTotal() {
        return ePOTotal;
    }

    public void setEPOTotal(BigDecimal ePOTotal) {
        this.ePOTotal = ePOTotal;
    }

    public String getEPODate() {
        return ePODate;
    }

    public void setEPODate(String ePODate) {
        this.ePODate = ePODate;
    }
}
