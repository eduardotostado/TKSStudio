package com.coognet.tksstudio.entities;

public class TaskProblem extends QueryObject {

    private int taskID;
    private int prblmID;

    public TaskProblem(int taskID, int prblmID) {
        this.taskID = taskID;
        this.prblmID = prblmID;
    }

    public boolean editAccount(int taskID, int prblmID){

        statement = "UPDATE Task_Problem " +
                "SET " +
                "Task_ID = " + taskID +  ", " +
                "Prblm_ID = " + prblmID +  " " +
                "WHERE Task_ID = " + this.getTaskID() + " AND Prblm_ID = " + this.getPrblmID();

        setTaskID(taskID);
        setTaskID(prblmID);

        return query(statement);
    }

    public boolean addAccount(int taskID, int prblmID){
        statement = "INSERT INTO Task_Problem (Task_ID, Prblm_ID) VALUES (" +
                taskID + ", " + prblmID +
                ")";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Task_Problem WHERE Task_ID = " + this.getTaskID() + " AND Prblm_ID = " + this.getPrblmID();
        return query(statement);
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public int getPrblmID() {
        return prblmID;
    }

    public void setPrblmID(int prblmID) {
        this.prblmID = prblmID;
    }
}
