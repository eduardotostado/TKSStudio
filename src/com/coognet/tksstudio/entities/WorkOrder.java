package com.coognet.tksstudio.entities;

public class WorkOrder extends QueryObject {

    private int contID;
    private int projectID;

    public WorkOrder(int contID, int projectID) {
        this.contID = contID;
        this.projectID = projectID;
    }

    public boolean editAccount(int contID, int projectID){

        statement = "UPDATE Work_Order " +
                "SET " +
                "Cont_ID = " + contID+  ", " +
                "Project_ID = " + projectID +  " " +
                "WHERE Cont_ID = " + this.getContID() + " AND Project_ID = " + this.getProjectID();

        setContID(contID);
        setProjectID(projectID);

        return query(statement);
    }

    public boolean addAccount(int contID, int projectID){
        statement = "INSERT INTO Work_Order (Cont_ID, Project_ID) VALUES (" +
                contID + ", " + projectID +
                ")";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Work_Order WHERE Cont_ID = " + this.getContID() + " AND Project_ID = " + this.getProjectID();
        return query(statement);
    }

    public int getContID() {
        return contID;
    }

    public void setContID(int contID) {
        this.contID = contID;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }
}
