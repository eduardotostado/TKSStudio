package com.coognet.tksstudio.entities;

public class ReviewStatus extends QueryObject {

    private final int rvwStatusID;
    private String rvwStatus_Name;

    public ReviewStatus(int rvwStatusID, String rvwStatus_Name) {
        this.rvwStatusID = rvwStatusID;
        this.rvwStatus_Name = rvwStatus_Name;
    }

    public boolean editAccount(String rvwStatus_Name){

        setRvwStatus_Name(rvwStatus_Name);

        statement = "UPDATE Review_Status " +
                "SET " +
                "RvwStatus_Name = '" + this.getRvwStatus_Name() +  "' " +
                "WHERE RvwStatus_ID = " + this.getRvwStatusID();

        return query(statement);
    }

    public boolean addAccount(String rvwStatus_Name){
        statement = "INSERT INTO Review_Status (RvwStatus_Name) VALUES ('" +
                rvwStatus_Name +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Review_Status WHERE RvwStatus_ID = " +
                        this.getRvwStatusID();
        return query(statement);
    }

    public int getRvwStatusID() {
        return rvwStatusID;
    }

    public String getRvwStatus_Name() {
        return rvwStatus_Name;
    }

    public void setRvwStatus_Name(String rvwStatus_Name) {
        this.rvwStatus_Name = rvwStatus_Name;
    }
}
