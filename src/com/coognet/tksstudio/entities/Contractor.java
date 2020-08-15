package com.coognet.tksstudio.entities;

import java.math.BigDecimal;

public class Contractor extends QueryObject {

    private final int contID;
    private String contFirstName;
    private String contLastName;
    private BigDecimal contSalary;

    public Contractor(int contID, String contFirstName, String contLastName, BigDecimal contSalary) {
        this.contID = contID;
        this.contFirstName = contFirstName;
        this.contLastName = contLastName;
        this.contSalary = contSalary.setScale(2);
    }

    public boolean editAccount(String contFirstName, String contLastName, BigDecimal contSalary){
        setContFirstName(contFirstName);
        setContLastName(contLastName);
        setContSalary(contSalary);

        statement = "UPDATE Contractor " +
                "SET " +
                "Cont_FirstName = '" + this.getContFirstName() +  "', " +
                "Cont_LastName = '" + this.getContLastName() +  "', " +
                "Cont_Salary = " + this.getContSalary() + " " +
                "WHERE Cont_ID = " + this.getContID();

        return query(statement);
    }

    public boolean addAccount(String contFirstName, String contLastName, BigDecimal contSalary){
        statement = "INSERT INTO Contractor (Cont_FirstName, Cont_LastName, Cont_Salary) VALUES ('" +
                contFirstName + "', '" + contLastName + "', " + contSalary +
                ")";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Contractor WHERE Cont_ID = " +
                        this.getContID();
        return query(statement);
    }

    public int getContID() {
        return contID;
    }

    public String getContFirstName() {
        return contFirstName;
    }

    public void setContFirstName(String contFirstName) {
        this.contFirstName = contFirstName;
    }

    public String getContLastName() {
        return contLastName;
    }

    public void setContLastName(String contLastName) {
        this.contLastName = contLastName;
    }

    public BigDecimal getContSalary() {
        return contSalary;
    }

    public void setContSalary(BigDecimal contSalary) {
        this.contSalary = contSalary;
    }
}
