package com.coognet.tksstudio.entities;

import java.math.BigDecimal;

public class MaterialSource extends QueryObject {

    private final int materialSourceID;
    private int supplierID;
    private int materialID;
    private BigDecimal materialCost;

    public MaterialSource(int materialSourceID, int supplierID, int materialID, BigDecimal materialCost) {
        this.materialSourceID = materialSourceID;
        this.supplierID = supplierID;
        this.materialID = materialID;
        this.materialCost = materialCost.setScale(2);
    }

    public boolean editAccount(int supplierID, int materialID, BigDecimal materialCost){

        setSupplierID(supplierID);
        setMaterialID(materialID);
        setMaterialCost(materialCost);

        statement = "UPDATE Material_Source " +
                "SET " +
                "Supplier_ID = " + this.getSupplierID() +  ", " +
                "Material_ID = " + this.getMaterialID() +  ", " +
                "Material_Cost = " + this.getMaterialCost() + " " +
                "WHERE MaterialSource_ID = " + this.getMaterialSourceID();

        return query(statement);
    }

    public boolean addAccount(int supplierID, int materialID, BigDecimal materialCost){
        statement = "INSERT INTO Material_Source (Supplier_ID, Material_ID, Material_Cost) VALUES (" +
                supplierID + ", " + materialID + ", " + materialCost +
                ")";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Material_Source WHERE MaterialSource_ID = " +
                        this.getMaterialSourceID();
        return query(statement);
    }

    public int getMaterialSourceID() {
        return materialSourceID;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public int getMaterialID() {
        return materialID;
    }

    public void setMaterialID(int materialID) {
        this.materialID = materialID;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }
}
