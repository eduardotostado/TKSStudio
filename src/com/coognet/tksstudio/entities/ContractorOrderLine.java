package com.coognet.tksstudio.entities;

public class ContractorOrderLine extends QueryObject {

    private final int cOLID;
    private int cPOID;
    private int materialSourceID;
    private int cOLQuantity;

    public ContractorOrderLine(int cOLID, int cPOID, int materialSourceID, int cOLQuantity) {
        this.cOLID = cOLID;
        this.cPOID = cPOID;
        this.materialSourceID = materialSourceID;
        this.cOLQuantity = cOLQuantity;
    }

    public boolean editAccount(int cPOID, int materialSourceID, int cOLQuantity){
        setcPOID(cPOID);
        setMaterialSourceID(materialSourceID);
        setcOLQuantity(cOLQuantity);

        statement = "UPDATE Contractor_Order_Line " +
                "SET " +
                "CPO_ID = " + this.getCPOID() +  ", " +
                "MaterialSource_ID = " + this.getMaterialSourceID() +  ", " +
                "COL_Quantity = " + this.getCOLQuantity() + " " +
                "WHERE COL_ID = " + this.getCOLID();

        return query(statement);
    }

    public boolean addAccount(int cPOID, int materialSourceID, int cOLQuantity){
        statement = "INSERT INTO Contractor_Order_Line (CPO_ID, MaterialSource_ID, COL_Quantity) VALUES (" +
                cPOID + ", " + materialSourceID + ", " + cOLQuantity +
                ")";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Contractor_Order_Line WHERE COL_ID = " +
                        this.getCOLID();
        return query(statement);
    }

    public int getCOLID() {
        return cOLID;
    }

    public int getCPOID() {
        return cPOID;
    }

    public void setcPOID(int cPOID) {
        this.cPOID = cPOID;
    }

    public int getMaterialSourceID() {
        return materialSourceID;
    }

    public void setMaterialSourceID(int materialSourceID) {
        this.materialSourceID = materialSourceID;
    }

    public int getCOLQuantity() {
        return cOLQuantity;
    }

    public void setcOLQuantity(int cOLQuantity) {
        this.cOLQuantity = cOLQuantity;
    }
}
