package com.coognet.tksstudio.entities;

public class Status extends QueryObject {

    private final int statusID;
    private String statusName;

    public Status(int statusID, String statusName) {
        this.statusID = statusID;
        this.statusName = statusName;
    }

    public boolean editAccount(String statusName){

        setStatusName(statusName);

        statement = "UPDATE Status " +
                "SET " +
                "Status_Name = '" + this.getStatusName() +  "' " +
                "WHERE Status_ID = " + this.getStatusID();

        return query(statement);
    }

    public boolean addAccount(String statusName){
        statement = "INSERT INTO Status (Status_Name) VALUES ('" +
                statusName +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Status WHERE Status_ID = " +
                        this.getStatusID();
        return query(statement);
    }

    public int getStatusID() {
        return statusID;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
