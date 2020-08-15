package com.coognet.tksstudio.entities;

public class Problem extends QueryObject {

    private final int prblmID;
    private String prblmDesc;

    public Problem(int prblmID, String prblmDesc) {
        this.prblmID = prblmID;
        this.prblmDesc = prblmDesc;
    }

    public boolean editAccount(String prblmDesc){

        setPrblmDesc(prblmDesc);

        statement = "UPDATE Problem " +
                "SET " +
                "Prblm_Desc = '" + this.getPrblmDesc() +  "' " +
                "WHERE Prblm_ID = " + this.getPrblmID();

        return query(statement);
    }

    public boolean addAccount(String prblmDesc){
        statement = "INSERT INTO Problem (Prblm_Desc) VALUES ('" +
                prblmDesc +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Problem WHERE Prblm_ID = " +
                        this.getPrblmID();
        return query(statement);
    }

    public int getPrblmID() {
        return prblmID;
    }

    public String getPrblmDesc() {
        return prblmDesc;
    }

    public void setPrblmDesc(String prblmDesc) {
        this.prblmDesc = prblmDesc;
    }
}
