package com.coognet.tksstudio.entities;

public class EmployeeOrderLine  extends QueryObject {

    private final int eOLID;
    private int ePOID;
    private int materialSourceID;
    private int eOLQuantity;

    public EmployeeOrderLine(int eOLID, int ePOID, int materialSourceID, int eOLQuantity) {
        this.eOLID = eOLID;
        this.ePOID = ePOID;
        this.materialSourceID = materialSourceID;
        this.eOLQuantity = eOLQuantity;
    }

    public boolean editAccount(int ePOID, int materialSourceID, int eOLQuantity){
        setEPOID(ePOID);
        setMaterialSourceID(materialSourceID);
        setEOLQuantity(eOLQuantity);

        statement = "UPDATE Employee_Order_Line " +
                "SET " +
                "EPO_ID = " + this.getEPOID() +  ", " +
                "MaterialSource_ID = " + this.getMaterialSourceID() +  ", " +
                "EOL_Quantity = " + this.getEOLQuantity() + " " +
                "WHERE EOL_ID = " + this.getEOLID();

        return query(statement);
    }

    public boolean addAccount(int ePOID, int materialSourceID, int eOLQuantity){
        statement = "INSERT INTO Employee_Order_Line (EPO_ID, MaterialSource_ID, EOL_Quantity) VALUES (" +
                ePOID + ", " + materialSourceID + ", " + eOLQuantity +
                ")";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Employee_Order_Line WHERE EOL_ID = " +
                        this.getEOLID();
        return query(statement);
    }

    public int getEOLID() {
        return eOLID;
    }

    public int getEPOID() {
        return ePOID;
    }

    public void setEPOID(int ePOID) {
        this.ePOID = ePOID;
    }

    public int getMaterialSourceID() {
        return materialSourceID;
    }

    public void setMaterialSourceID(int materialSourceID) {
        this.materialSourceID = materialSourceID;
    }

    public int getEOLQuantity() {
        return eOLQuantity;
    }

    public void setEOLQuantity(int eOLQuantity) {
        this.eOLQuantity = eOLQuantity;
    }
}
