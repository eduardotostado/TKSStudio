package com.coognet.tksstudio.entities;

public class SurveyPicture extends QueryObject {

    private final int pictureID;
    private int surveyID;
    private String pictureDesc;
    private String pictureURL;

    public SurveyPicture(int pictureID, int surveyID, String pictureDesc, String pictureURL) {
        this.pictureID = pictureID;
        this.surveyID = surveyID;
        this.pictureDesc = pictureDesc;
        this.pictureURL = pictureURL;
    }

    public boolean editAccount(int surveyID, String pictureDesc, String pictureURL){

        setSurveyID(surveyID);
        setPictureDesc(pictureDesc);
        setPictureURL(pictureURL);

        statement = "UPDATE Survey_Picture " +
                "SET " +
                "Survey_ID = " + this.getSurveyID() +  ", " +
                "Picture_Desc = '" + this.getPictureDesc() +  "', " +
                "Picture_URL = '" + this.getPictureURL() + "' " +
                "WHERE Picture_ID = " + this.getPictureID();

        return query(statement);
    }

    public boolean addAccount(int surveyID, String pictureDesc, String pictureURL){
        statement = "INSERT INTO Survey_Picture (Survey_ID, Picture_Desc, Picture_URL) VALUES (" +
                surveyID + ", '" + pictureDesc + "', '" + pictureURL +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Survey_Picture WHERE Picture_ID = " +
                        this.getPictureID();
        return query(statement);
    }

    public int getPictureID() {
        return pictureID;
    }

    public int getSurveyID() {
        return surveyID;
    }

    public void setSurveyID(int surveyID) {
        this.surveyID = surveyID;
    }

    public String getPictureDesc() {
        return pictureDesc;
    }

    public void setPictureDesc(String pictureDesc) {
        this.pictureDesc = pictureDesc;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }
}
