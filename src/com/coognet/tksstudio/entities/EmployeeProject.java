package com.coognet.tksstudio.entities;

public class EmployeeProject extends QueryObject {

    private int empID;
    private int projectID;

    public EmployeeProject(int empID, int projectID) {
        this.empID = empID;
        this.projectID = projectID;
    }

    public boolean editAccount(int empID, int projectID){
        statement = "UPDATE Employee_Project " +
                "SET " +
                "Emp_ID = " + empID +  ", " +
                "Project_ID = " + projectID + " " +
                "WHERE Emp_ID = " + this.getEmpID() + " AND Project_ID = " + this.getProjectID();

        setEmpID(empID);
        setProjectID(projectID);

        return query(statement);
    }

    public boolean addAccount(int empID, int projectID){
        statement = "INSERT INTO Employee_Project (Emp_ID, Project_ID) VALUES (" +
                empID + ", " + projectID +
                ")";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Employee_Project " +
                        "WHERE Emp_ID = " + this.getEmpID() +
                        " AND Project_ID = " + this.getProjectID();
        return query(statement);
    }

    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }
}
