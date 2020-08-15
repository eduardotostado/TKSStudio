package com.coognet.tksstudio.entities;

public class EmployeeType extends QueryObject {

    private final int empTypeCode;
    private String empTypeName;

    public EmployeeType(int empTypeCode, String empTypeName) {
        this.empTypeCode = empTypeCode;
        this.empTypeName = empTypeName;
    }

    public boolean editAccount(String empTypeName){

        setEmpTypeName(empTypeName);

        statement = "UPDATE Employee_Type " +
                "SET " +
                "EmpType_Name = '" + this.getEmpTypeName() +  "' " +
                "WHERE EmpType_Code = " + this.getEmpTypeCode();

        return query(statement);
    }

    public boolean addAccount(String empTypeName){
        statement = "INSERT INTO Employee_Type (EmpType_Name) VALUES ('" +
                empTypeName +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Employee_Type WHERE EmpType_Code = " +
                        this.getEmpTypeCode();
        return query(statement);
    }

    public int getEmpTypeCode() {
        return empTypeCode;
    }

    public String getEmpTypeName() {
        return empTypeName;
    }

    public void setEmpTypeName(String empTypeName) {
        this.empTypeName = empTypeName;
    }
}
