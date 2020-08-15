package com.coognet.tksstudio.entities;

public class JobMaterial extends QueryObject {

    private int jobID;
    private int materialID;

    public JobMaterial(int jobID, int materialID) {
        this.jobID = jobID;
        this.materialID = materialID;
    }

    public boolean editAccount(int jobID, int materialID){

        statement = "UPDATE Job_Material " +
                "SET " +
                "Job_ID = " + jobID +  ", " +
                "Material_ID = " + materialID + " " +
                "WHERE Job_ID = " + this.getJobID() + " AND  Material_ID = " + this.getMaterialID();

        setJobID(jobID);
        setMaterialID(materialID);

        return query(statement);
    }

    public boolean addAccount(int jobID, int materialID){
        statement = "INSERT INTO Job_Material (Job_ID, Material_ID) VALUES (" +
                jobID + ", " + materialID +
                ")";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Job_Material WHERE Job_ID = " + this.getJobID() + " AND  Material_ID = " + this.getMaterialID();
        return query(statement);
    }

    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public int getMaterialID() {
        return materialID;
    }

    public void setMaterialID(int materialID) {
        this.materialID = materialID;
    }
}
