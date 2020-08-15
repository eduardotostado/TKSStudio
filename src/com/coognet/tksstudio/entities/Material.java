package com.coognet.tksstudio.entities;

public class Material extends QueryObject {

    private final int materialID;
    private int matTypeCode;
    private String materialName;
    private String materialCostCode;

    public Material(int materialID, int matTypeCode, String materialName, String materialCostCode) {
        this.materialID = materialID;
        this.matTypeCode = matTypeCode;
        this.materialName = materialName;
        this.materialCostCode = materialCostCode;
    }

    public boolean editAccount(int matTypeCode, String materialName, String materialCostCode){

        setMatTypeCode(matTypeCode);
        setMaterialName(materialName);
        setMaterialCostCode(materialCostCode);

        statement = "UPDATE Material " +
                "SET " +
                "MatType_Code = " + this.getMatTypeCode() +  ", " +
                "Material_Name = '" + this.getMaterialName() +  "', " +
                "Material_CostCode = '" + this.getMaterialCostCode() + "' " +
                "WHERE Material_ID = " + this.getMaterialID();

        return query(statement);
    }

    public boolean addAccount(int matTypeCode, String materialName, String materialCostCode){
        statement = "INSERT INTO Material (MatType_Code, Material_Name, Material_CostCode) VALUES (" +
                matTypeCode + ", '" + materialName + "', '" + materialCostCode +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Material WHERE Material_ID = " +
                        this.getMaterialID();
        return query(statement);
    }

    public int getMaterialID() {
        return materialID;
    }

    public int getMatTypeCode() {
        return matTypeCode;
    }

    public void setMatTypeCode(int matTypeCode) {
        this.matTypeCode = matTypeCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialCostCode() {
        return materialCostCode;
    }

    public void setMaterialCostCode(String materialCostCode) {
        this.materialCostCode = materialCostCode;
    }
}
