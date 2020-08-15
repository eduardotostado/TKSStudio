package com.coognet.tksstudio.entities;

public class Project extends QueryObject {

    private final int projectID;
    private String projectName;

    public Project(int projectID, String projectName) {
        this.projectID = projectID;
        this.projectName = projectName;
    }

    public boolean editAccount(String projectName){

        setProjectName(projectName);

        statement = "UPDATE Project " +
                "SET " +
                "Project_Name = '" + this.getProjectName() +  "' " +
                "WHERE Project_ID = " + this.getProjectID();

        return query(statement);
    }

    public boolean addAccount(String projectName){
        statement = "INSERT INTO Project (Project_Name) VALUES ('" +
                projectName +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Project WHERE Project_ID = " +
                        this.getProjectID();
        return query(statement);
    }

    public int getProjectID() {
        return projectID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
