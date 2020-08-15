package com.coognet.tksstudio.entities;

import java.math.BigDecimal;

public class ContractorPurchaseOrder extends QueryObject {

    private final int cPOID;
    private int contID;
    private int accountsPayableID;
    private BigDecimal cPOTotal;
    private String cPODate;

    public ContractorPurchaseOrder(int cPOID, int contID, int accountsPayableID, BigDecimal cPOTotal, String cPODate) {
        this.cPOID = cPOID;
        this.contID = contID;
        this.accountsPayableID = accountsPayableID;
        this.cPOTotal = cPOTotal.setScale(2);
        this.cPODate = cPODate;
    }

    public boolean editAccount(int contID, int accountsPayableID, BigDecimal cPOTotal, String cPODate){
        setContID(contID);
        setAccountsPayableID(accountsPayableID);
        setCPOTotal(cPOTotal);
        setCPODate(cPODate);

        statement = "UPDATE Contractor_Purchase_Order " +
                "SET " +
                "Cont_ID = " + this.getContID() +  ", " +
                "AccountsPayable_ID = " + this.getAccountsPayableID() +  ", " +
                "CPO_Total = " + this.getCPOTotal() + ", " +
                "CPO_Date = '" + this.getCPODate() + "' " +
                "WHERE CPO_ID = " + this.getCPOID();

        return query(statement);
    }

    public boolean addAccount(int contID, int accountsPayableID, BigDecimal cPOTotal, String cPODate){
        statement = "INSERT INTO Contractor_Purchase_Order (Cont_ID, AccountsPayable_ID, CPO_Total, CPO_Date) VALUES (" +
                contID + ", " + accountsPayableID + ", " + cPOTotal + ", '" + cPODate +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Contractor_Purchase_Order WHERE CPO_ID = " +
                        this.getCPOID();
        return query(statement);
    }

    public int getCPOID() {
        return cPOID;
    }

    public int getContID() {
        return contID;
    }

    public void setContID(int contID) {
        this.contID = contID;
    }

    public int getAccountsPayableID() {
        return accountsPayableID;
    }

    public void setAccountsPayableID(int accountsPayableID) {
        this.accountsPayableID = accountsPayableID;
    }

    public BigDecimal getCPOTotal() {
        return cPOTotal;
    }

    public void setCPOTotal(BigDecimal cPOTotal) {
        this.cPOTotal = cPOTotal;
    }

    public String getCPODate() {
        return cPODate;
    }

    public void setCPODate(String cPODate) {
        this.cPODate = cPODate;
    }
}
