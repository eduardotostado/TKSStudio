package com.coognet.tksstudio.entities;

public class Department extends QueryObject {

    private final int deptID;
    private String deptName;

    public Department(int deptID, String deptName) {
        this.deptID = deptID;
        this.deptName = deptName;
    }

    public boolean editAccount(String deptName){
        setDeptName(deptName);

        statement = "UPDATE Department " +
                "SET " +
                "Dept_Name = '" + this.getDeptName() + "' " +
                "WHERE Dept_ID = " + this.getDeptID();

        return query(statement);
    }

    public boolean addAccount(String deptName){
        statement = "INSERT INTO Department (Dept_Name) VALUES ('" +
                deptName +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Department WHERE Dept_ID = " +
                        this.getDeptID();
        return query(statement);
    }

    public int getDeptID() {
        return deptID;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return deptName;
    }
}
