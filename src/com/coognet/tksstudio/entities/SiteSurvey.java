package com.coognet.tksstudio.entities;

public class SiteSurvey extends QueryObject {

    private final int surveyID;
    private int jobPropID;
    private String surveyNotes;
    private String surveyDate;

    public SiteSurvey(int surveyID, int jobPropID, String surveyDate, String surveyNotes) {
        this.surveyID = surveyID;
        this.jobPropID = jobPropID;
        this.surveyNotes = surveyNotes;
        this.surveyDate = surveyDate;
    }

    public boolean editAccount(int jobPropID, String surveyDate, String surveyNotes){

        setJobPropID(jobPropID);
        setSurveyNotes(surveyNotes);
        setSurveyDate(surveyDate);

        statement = "UPDATE Site_Survey " +
                "SET " +
                "JobProp_ID = " + this.getJobPropID() +  ", " +
                "Survey_Notes = '" + this.getSurveyNotes() +  "', " +
                "Survey_Date = '" + this.getSurveyDate() + "' " +
                "WHERE Survey_ID = " + this.getSurveyID();

        return query(statement);
    }

    public boolean addAccount(int jobPropID, String surveyDate, String surveyNotes){
        statement = "INSERT INTO Site_Survey (JobProp_ID, Survey_Notes, Survey_Date) VALUES (" +
                jobPropID + ", '" + surveyNotes + "', '" + surveyDate +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Site_Survey WHERE Survey_ID = " +
                        this.getSurveyID();
        return query(statement);
    }

    public int getSurveyID() {
        return surveyID;
    }

    public int getJobPropID() {
        return jobPropID;
    }

    public void setJobPropID(int jobPropID) {
        this.jobPropID = jobPropID;
    }

    public String getSurveyNotes() {
        return surveyNotes;
    }

    public void setSurveyNotes(String surveyNotes) {
        this.surveyNotes = surveyNotes;
    }

    public String getSurveyDate() {
        return surveyDate;
    }

    public void setSurveyDate(String surveyDate) {
        this.surveyDate = surveyDate;
    }
}
