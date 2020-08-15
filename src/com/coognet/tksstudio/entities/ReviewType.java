package com.coognet.tksstudio.entities;

public class ReviewType extends QueryObject {

    private final int rvwTypeID;
    private String rvwTypeName;
    private String rvwRequirements;

    public ReviewType(int rvwTypeID, String rvwTypeName, String rvwRequirements) {
        this.rvwTypeID = rvwTypeID;
        this.rvwTypeName = rvwTypeName;
        this.rvwRequirements = rvwRequirements;
    }

    public boolean editAccount(String rvwTypeName, String rvwRequirements){

        setRvwTypeName(rvwTypeName);
        setRvwRequirements(rvwRequirements);

        statement = "UPDATE Review_Type " +
                "SET " +
                "Rvw_TypeName = '" + this.getRvwTypeName() +  "', " +
                "Rvw_Requirements = '" + this.getRvwRequirements() +  "' " +
                "WHERE Rvw_TypeID = " + this.getRvwTypeID();

        return query(statement);
    }

    public boolean addAccount(String rvwTypeName, String rvwRequirements){
        statement = "INSERT INTO Review_Type (Rvw_TypeName, Rvw_Requirements) VALUES ('" +
                rvwTypeName + "', '" + rvwRequirements +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Review_Type WHERE Rvw_TypeID = " +
                        this.getRvwTypeID();
        return query(statement);
    }

    public int getRvwTypeID() {
        return rvwTypeID;
    }

    public String getRvwTypeName() {
        return rvwTypeName;
    }

    public void setRvwTypeName(String rvwTypeName) {
        this.rvwTypeName = rvwTypeName;
    }

    public String getRvwRequirements() {
        return rvwRequirements;
    }

    public void setRvwRequirements(String rvwRequirements) {
        this.rvwRequirements = rvwRequirements;
    }
}
