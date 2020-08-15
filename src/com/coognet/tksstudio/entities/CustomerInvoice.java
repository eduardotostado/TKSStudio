package com.coognet.tksstudio.entities;

import java.math.BigDecimal;

public class CustomerInvoice extends QueryObject {

    private int custID;
    private int jobID;
    private BigDecimal invoiceAmt;

    public CustomerInvoice(int custID, int jobID, BigDecimal invoiceAmt) {
        this.custID = custID;
        this.jobID = jobID;
        this.invoiceAmt = invoiceAmt.setScale(2);
    }

    public boolean editAccount(int custID, int jobID, BigDecimal invoiceAmt){
        setInvoiceAmt(invoiceAmt);

        statement = "UPDATE Customer_Invoice " +
                "SET " +
                "Cust_ID = " + custID + ", " +
                "Job_ID = " + jobID + ", " +
                "Invoice_amt = " + this.getInvoiceAmt() + " " +
                "WHERE Cust_ID = " + this.getCustID() + " AND  Job_ID = " + this.getJobID();

        setCustID(custID);
        setJobID(jobID);

        return query(statement);
    }

    public boolean addAccount(int custID, int jobID, BigDecimal invoiceAmt){
        statement = "INSERT INTO Customer_Invoice (Cust_ID, Job_ID, Invoice_amt) VALUES (" +
                custID + ", " + jobID + ", " + invoiceAmt +
                ")";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Customer_Invoice WHERE Cust_ID = " +
                        this.getCustID() +
                        " AND Job_ID = " + this.getJobID();
        return query(statement);
    }

    public int getCustID() {
        return custID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public int getJobID() {
        return jobID;
    }

    public BigDecimal getInvoiceAmt() {
        return invoiceAmt;
    }

    public void setInvoiceAmt(BigDecimal invoiceAmt) {
        this.invoiceAmt = invoiceAmt;
    }
}
