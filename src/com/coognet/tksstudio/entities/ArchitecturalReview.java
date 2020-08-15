package com.coognet.tksstudio.entities;

public class ArchitecturalReview extends QueryObject {

    private final int archRvwID;
    private int surveyID;
    private int rvwTypeID;
    private int rvwStatusID;

    public ArchitecturalReview(int archRvwID, int surveyID, int rvwTypeID, int rvwStatusID) {
        this.archRvwID = archRvwID;
        this.surveyID = surveyID;
        this.rvwTypeID = rvwTypeID;
        this.rvwStatusID = rvwStatusID;
    }

    public boolean editAccount(int surveyID, int rvwTypeID, int rvwStatusID){
        setSurveyID(surveyID);
        setRvwTypeID(rvwTypeID);
        setRvwStatusID(rvwStatusID);

        statement = "UPDATE Architectural_Review " +
                "SET " +
                "Survey_ID = " + this.getSurveyID() +  ", " +
                "Rvw_TypeID = " + this.getRvwTypeID() +  ", " +
                "RvwStatus_ID = " + this.getRvwStatusID() + " " +
                "WHERE ArchRvw_ID = " + this.getArchRvwID();

        return query(statement);
    }

    public boolean addAccount(int surveyID, int rvwTypeID, int rvwStatusID){
        statement = "INSERT INTO Architectural_Review (Survey_ID, Rvw_TypeID, RvwStatus_ID) VALUES (" +
                surveyID + ", " + rvwTypeID + ", " + rvwStatusID +
                ")";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Architectural_Review WHERE ArchRvw_ID = " +
                        this.getArchRvwID();
        return query(statement);
    }

    public int getArchRvwID() {
        return archRvwID;
    }

    public int getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(int surveyID) {
        this.surveyID = surveyID;
    }

    public int getRvwTypeID() {
        return rvwTypeID;
    }

    public void setRvwTypeID(int rvwTypeID) {
        this.rvwTypeID = rvwTypeID;
    }

    public int getRvwStatusID() {
        return rvwStatusID;
    }

    public void setRvwStatusID(int rvwStatusID) {
        this.rvwStatusID = rvwStatusID;
    }
}
