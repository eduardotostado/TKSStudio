package com.coognet.tksstudio.entities;

public class Supplier extends QueryObject {

    private final int supplierID;
    private String supplierName;

    public Supplier(int supplierID, String supplierName) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
    }

    public boolean editAccount(String supplierName){

        setSupplierName(supplierName);

        statement = "UPDATE Supplier " +
                "SET " +
                "Supplier_Name = '" + this.getSupplierName() +  "' " +
                "WHERE Supplier_ID = " + this.getSupplierID();

        return query(statement);
    }

    public boolean addAccount(String supplierName){
        statement = "INSERT INTO Supplier (Supplier_Name) VALUES ('" +
                supplierName +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Supplier WHERE Supplier_ID = " +
                        this.getSupplierID();
        return query(statement);
    }

    public int getSupplierID() {
        return supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
