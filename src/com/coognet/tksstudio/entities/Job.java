package com.coognet.tksstudio.entities;

public class Job extends QueryObject {

    private final int jobID;
    private int jobPropID;
    private int projectID;
    private String jobName;

    public Job(int jobID, int jobPropID, int projectID, String jobName) {
        this.jobID = jobID;
        this.jobPropID = jobPropID;
        this.projectID = projectID;
        this.jobName = jobName;
    }

    public boolean editAccount(int jobPropID, int projectID, String jobName){

        setJobPropID(jobPropID);
        setProjectID(projectID);
        setJobName(jobName);

        statement = "UPDATE Job " +
                "SET " +
                "JobProp_ID = " + this.getJobPropID() +  ", " +
                "Project_ID = " + this.getProjectID() +  ", " +
                "Job_Name = '" + this.getJobName() + "' " +
                "WHERE Job_ID = " + this.getJobID();

        return query(statement);
    }

    public boolean addAccount(int jobPropID, int projectID, String jobName){
        statement = "INSERT INTO Job (JobProp_ID, Project_ID, Job_Name) VALUES (" +
                jobPropID + ", " + projectID + ", '" + jobName +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Job WHERE Job_ID = " +
                        this.getJobID();
        return query(statement);
    }

    public int getJobID() {
        return jobID;
    }

    public int getJobPropID() {
        return jobPropID;
    }

    public void setJobPropID(int jobPropID) {
        this.jobPropID = jobPropID;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}
