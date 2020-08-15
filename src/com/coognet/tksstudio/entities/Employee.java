package com.coognet.tksstudio.entities;

import java.math.BigDecimal;

public class Employee extends QueryObject {

    private final int empID;
    private int deptID;
    private int empTypeCode;
    private String empFirstName;
    private String empLastName;
    private BigDecimal empSalary;

    public Employee(int empID, int deptID, int empTypeCode, String empFirstName, String empLastName, BigDecimal empSalary) {
        this.empID = empID;
        this.deptID = deptID;
        this.empTypeCode = empTypeCode;
        this.empFirstName = empFirstName;
        this.empLastName = empLastName;
        this.empSalary = empSalary.setScale(2);
    }

    public boolean editAccount(int deptID, int empTypeCode, String empFirstName, String empLastName, BigDecimal empSalary){

        setDeptID(deptID);
        setEmpTypeCode(empTypeCode);
        setEmpFirstName(empFirstName);
        setEmpLastName(empLastName);
        setEmpSalary(empSalary);

        statement = "UPDATE Employee " +
                "SET " +
                "Dept_ID = " + this.getDeptID() +  ", " +
                "EmpType_Code = " + this.getEmpTypeCode() +  ", " +
                "Emp_FirstName = '" + this.getEmpFirstName() +  "', " +
                "Emp_LastName = '" + this.getEmpLastName() +  "', " +
                "Emp_Salary = " + this.getEmpSalary() + " " +
                "WHERE Emp_ID = " + this.getEmpID();

        return query(statement);
    }

    public boolean addAccount(int deptID, int empTypeCode, String empFirstName, String empLastName, BigDecimal empSalary){
        statement = "INSERT INTO Employee (Dept_ID, EmpType_Code, Emp_FirstName, Emp_LastName, Emp_Salary) VALUES (" +
                deptID + ", " + empTypeCode + ", '" + empFirstName + "', '" + empLastName + "', " + empSalary +
                ")";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Employee WHERE Emp_ID = " +
                        this.getEmpID();
        return query(statement);
    }

    public int getEmpID() {
        return empID;
    }

    public int getDeptID() {
        return deptID;
    }

    public void setDeptID(int deptID) {
        this.deptID = deptID;
    }

    public int getEmpTypeCode() {
        return empTypeCode;
    }

    public void setEmpTypeCode(int empTypeCode) {
        this.empTypeCode = empTypeCode;
    }

    public String getEmpFirstName() {
        return empFirstName;
    }

    public void setEmpFirstName(String empFirstName) {
        this.empFirstName = empFirstName;
    }

    public String getEmpLastName() {
        return empLastName;
    }

    public void setEmpLastName(String empLastName) {
        this.empLastName = empLastName;
    }

    public BigDecimal getEmpSalary() {
        return empSalary;
    }

    public void setEmpSalary(BigDecimal empSalary) {
        this.empSalary = empSalary;
    }
}
