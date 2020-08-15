package com.coognet.tksstudio.entities;

public class MaterialType extends QueryObject {

    private final int matTypeCode;
    private String matTypeName;

    public MaterialType(int matTypeCode, String matTypeName) {
        this.matTypeCode = matTypeCode;
        this.matTypeName = matTypeName;
    }

    public boolean editAccount(String matTypeName){

        setMatTypeName(matTypeName);

        statement = "UPDATE Material_Type " +
                "SET " +
                "MatType_Name = '" + this.getMatTypeName() +  "' " +
                "WHERE MatType_Code = " + this.getMatTypeCode();

        return query(statement);
    }

    public boolean addAccount(String matTypeName){
        statement = "INSERT INTO Material_Type (MatType_Name) VALUES ('" +
                matTypeName +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Material_Type WHERE MatType_Code = " +
                        this.getMatTypeCode();
        return query(statement);
    }

    public int getMatTypeCode() {
        return matTypeCode;
    }

    public String getMatTypeName() {
        return matTypeName;
    }

    public void setMatTypeName(String matTypeName) {
        this.matTypeName = matTypeName;
    }
}
