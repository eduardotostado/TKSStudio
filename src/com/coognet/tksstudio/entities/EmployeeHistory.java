package com.coognet.tksstudio.entities;

import java.math.BigDecimal;

public class EmployeeHistory extends QueryObject {

    private String dateAssign;
    private int empID;
    private BigDecimal empSalary;

    public EmployeeHistory(String dateAssign, int empID, BigDecimal empSalary) {
        this.dateAssign = dateAssign;
        this.empID = empID;
        this.empSalary = empSalary.setScale(2);
    }

    public boolean editAccount(String dateAssign, int empID, BigDecimal empSalary){
        setEmpSalary(empSalary);

        statement = "UPDATE Employee_History " +
                "SET " +
                "Date_Assign = '" + dateAssign +  "', " +
                "Emp_Id = " + empID +  ", " +
                "Emp_Salary = " + this.getEmpSalary() + " " +
                "WHERE Date_assign = '" + this.getDateAssign() + "' AND Emp_Id = " + this.getEmpID();

        setDateAssign(dateAssign);
        setEmpID(empID);

        return query(statement);
    }

    public boolean addAccount(String dateAssign, int empID, BigDecimal empSalary){
        statement = "INSERT INTO Employee_History (Date_Assign, Emp_Id, Emp_Salary) VALUES ('" +
                dateAssign + "', " + empID + ", " + empSalary +
                ")";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Employee_History WHERE Date_Assign = '" +
                        this.getDateAssign() +
                        "' AND Emp_Id = " + this.getEmpID();
        return query(statement);
    }

    public String getDateAssign() {
        return dateAssign;
    }

    public void setDateAssign(String dateAssign) {
        this.dateAssign = dateAssign;
    }

    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public BigDecimal getEmpSalary() {
        return empSalary;
    }

    public void setEmpSalary(BigDecimal empSalary) {
        this.empSalary = empSalary;
    }
}
