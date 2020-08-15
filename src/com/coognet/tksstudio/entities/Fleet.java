package com.coognet.tksstudio.entities;

public class Fleet extends QueryObject {

    private final int fleetID;
    private int accountsPayableID;
    private int fleetVehicleCnt;

    public Fleet(int fleetID, int accountsPayableID, int fleetVehicleCnt) {
        this.fleetID = fleetID;
        this.accountsPayableID = accountsPayableID;
        this.fleetVehicleCnt = fleetVehicleCnt;
    }

    public boolean editAccount(int accountsPayableID, int fleetVehicleCnt){

        setAccountsPayableID(accountsPayableID);
        setFleetVehicleCnt(fleetVehicleCnt);

        statement = "UPDATE Fleet " +
                "SET " +
                "AccountsPayable_ID = " + this.getAccountsPayableID() +  ", " +
                "Fleet_VehicleCnt = " + this.getFleetVehicleCnt() + " " +
                "WHERE Fleet_ID = " + this.getFleetID();

        return query(statement);
    }

    public boolean addAccount(int accountsPayableID, int fleetVehicleCnt){
        statement = "INSERT INTO Fleet (AccountsPayable_ID, Fleet_VehicleCnt) VALUES (" +
                accountsPayableID + ", " + fleetVehicleCnt +
                ")";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Fleet WHERE Fleet_ID = " +
                        this.getFleetID();
        return query(statement);
    }

    public int getFleetID() {
        return fleetID;
    }

    public int getAccountsPayableID() {
        return accountsPayableID;
    }

    public void setAccountsPayableID(int accountsPayableID) {
        this.accountsPayableID = accountsPayableID;
    }

    public int getFleetVehicleCnt() {
        return fleetVehicleCnt;
    }

    public void setFleetVehicleCnt(int fleetVehicleCnt) {
        this.fleetVehicleCnt = fleetVehicleCnt;
    }
}
