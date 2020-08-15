package com.coognet.tksstudio.entities;

public class Customer extends QueryObject {

    private final int custID;
    private String custFirstName;
    private String custLastName;
    private String custAddress;

    public Customer(int custID, String custFirstName, String custLastName, String custAddress) {
        this.custID = custID;
        this.custFirstName = custFirstName;
        this.custLastName = custLastName;
        this.custAddress = custAddress;
    }

    public boolean editAccount(String custFirstName, String custLastName, String custAddress){

        setCustFirstName(custFirstName);
        setCustLastName(custLastName);
        setCustAddress(custAddress);

        statement = "UPDATE Customer " +
                "SET " +
                "Cust_FirstName = '" + this.getCustFirstName() +  "', " +
                "Cust_LastName = '" + this.getCustLastName() +  "', " +
                "Cust_Address = '" + this.getCustAddress() + "' " +
                "WHERE Cust_ID = " + this.getCustID();

        return query(statement);
    }

    public boolean addAccount(String custFirstName, String custLastName, String custAddress){
        statement = "INSERT INTO Customer (Cust_FirstName, Cust_LastName, Cust_Address) VALUES ('" +
                custFirstName + "', '" + custLastName + "', '" + custAddress +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Customer WHERE Cust_ID = " +
                        this.getCustID();
        return query(statement);
    }

    public int getCustID() {
        return custID;
    }

    public String getCustFirstName() {
        return custFirstName;
    }

    public void setCustFirstName(String custFirstName) {
        this.custFirstName = custFirstName;
    }

    public String getCustLastName() {
        return custLastName;
    }

    public void setCustLastName(String custLastName) {
        this.custLastName = custLastName;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }
}
