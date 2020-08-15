package com.coognet.tksstudio.entities;

public class Task extends QueryObject {

    private final int taskID;
    private int jobID;
    private int statusID;
    private String taskName;

    public Task(int taskID, int jobID, int statusID, String taskName) {
        this.taskID = taskID;
        this.jobID = jobID;
        this.statusID = statusID;
        this.taskName = taskName;
    }

    public boolean editAccount(int jobID, int statusID, String taskName){

        setJobID(jobID);
        setStatusID(statusID);
        setTaskName(taskName);

        statement = "UPDATE Task " +
                "SET " +
                "Job_ID = " + this.getJobID() +  ", " +
                "Status_ID = " + this.getStatusID() +  ", " +
                "Task_Name = '" + this.getTaskName() + "' " +
                "WHERE Task_ID = " + this.getTaskID();

        return query(statement);
    }

    public boolean addAccount(int jobID, int statusID, String taskName){
        statement = "INSERT INTO Task (Job_ID, Status_ID, Task_Name) VALUES (" +
                jobID + ", " + statusID + ", '" + taskName +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Task WHERE Task_ID = " +
                        this.getTaskID();
        return query(statement);
    }

    public int getTaskID() {
        return taskID;
    }

    public int getJobID() {
        return jobID;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
