package com.coognet.tksstudio.entities;

public class Dependent extends QueryObject {

    private final int depID;
    private int empID;
    private String depFirstName;
    private String depLastName;

    public Dependent(int depID, int empID, String depFirstName, String depLastName) {
        this.depID = depID;
        this.empID = empID;
        this.depFirstName = depFirstName;
        this.depLastName = depLastName;
    }

    public boolean editAccount(int empID, String depFirstName, String depLastName){
        setEmpID(empID);
        setDepFirstName(depFirstName);
        setDepLastName(depLastName);

        statement = "UPDATE Dependent " +
                "SET " +
                "Emp_ID = " + this.getEmpID() + ", " +
                "Dep_FirstName = '" + this.getDepFirstName() + "', " +
                "Dep_LastName = '" + this.getDepLastName() + "' " +
                "WHERE Dep_ID = " + this.getDepID();

        return query(statement);
    }

    public boolean addAccount(int empID, String depFirstName, String depLastName){
        statement = "INSERT INTO Dependent (Emp_ID, Dep_FirstName, Dep_LastName) VALUES (" +
                empID + ", '" + depFirstName + "', '" + depLastName +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Dependent WHERE Dep_ID = " +
                        this.getDepID();
        return query(statement);
    }

    public int getDepID() {
        return depID;
    }

    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public String getDepFirstName() {
        return depFirstName;
    }

    public void setDepFirstName(String depFirstName) {
        this.depFirstName = depFirstName;
    }

    public String getDepLastName() {
        return depLastName;
    }

    public void setDepLastName(String depLastName) {
        this.depLastName = depLastName;
    }
}
