package com.coognet.tksstudio.entities;

public class JobProposal extends QueryObject {

    private final int jobPropID;
    private int custID;
    private String jobPropAddress;

    public JobProposal(int jobPropID, int custID, String jobPropAddress) {
        this.jobPropID = jobPropID;
        this.custID = custID;
        this.jobPropAddress = jobPropAddress;
    }

    public boolean editAccount(int custID, String jobPropAddress){

        setCustID(custID);
        setJobPropAddress(jobPropAddress);

        statement = "UPDATE Job_Proposal " +
                "SET " +
                "Cust_ID = " + this.getCustID() +  ", " +
                "JobProp_Address = '" + this.getJobPropAddress() + "' " +
                "WHERE JobProp_ID = " + this.getJobPropID();

        return query(statement);
    }

    public boolean addAccount(int custID, String jobPropAddress){
        statement = "INSERT INTO Job_Proposal (Cust_ID, JobProp_Address) VALUES (" +
                custID + ", '" + jobPropAddress +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Job_Proposal WHERE JobProp_ID = " +
                        this.getJobPropID();
        return query(statement);
    }

    public int getJobPropID() {
        return jobPropID;
    }

    public int getCustID() {
        return custID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public String getJobPropAddress() {
        return jobPropAddress;
    }

    public void setJobPropAddress(String jobPropAddress) {
        this.jobPropAddress = jobPropAddress;
    }
}
