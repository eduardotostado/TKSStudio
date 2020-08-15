package com.coognet.tksstudio.entities;

import java.math.BigDecimal;


public class Maintenance extends QueryObject {

    private final int maintenanceID;
    private int vehicleID;
    private String maintenanceDesc;
    private BigDecimal maintenanceCost;
    private String maintenanceDate;

    public Maintenance(int maintenanceID, int vehicleID, String maintenanceDesc, BigDecimal maintenanceCost, String maintenanceDate) {
        this.maintenanceID = maintenanceID;
        this.vehicleID = vehicleID;
        this.maintenanceDesc = maintenanceDesc;
        this.maintenanceCost = maintenanceCost.setScale(2);
        this.maintenanceDate = maintenanceDate;
    }

    public boolean editAccount(int vehicleID, String maintenanceDesc, BigDecimal maintenanceCost, String maintenanceDate){

        setVehicleID(vehicleID);
        setMaintenanceDesc(maintenanceDesc);
        setMaintenanceCost(maintenanceCost);
        setMaintenanceDate(maintenanceDate);

        statement = "UPDATE Maintenance " +
                "SET " +
                "Vehicle_ID = " + this.getVehicleID() +  ", " +
                "Maintenance_Desc = '" + this.getMaintenanceDesc() +  "', " +
                "Maintenance_Cost = " + this.getMaintenanceCost() +  ", " +
                "Maintenance_Date = '" + this.getMaintenanceDate() + "' " +
                "WHERE Maintenance_ID = " + this.getMaintenanceID();

        return query(statement);
    }

    public boolean addAccount(int vehicleID, String maintenanceDesc, BigDecimal maintenanceCost, String maintenanceDate){
        statement = "INSERT INTO Maintenance (Vehicle_ID, Maintenance_Desc, Maintenance_Cost, Maintenance_Date) VALUES (" +
                vehicleID + ", '" + maintenanceDesc + "', " + maintenanceCost + ", '" + maintenanceDate +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Maintenance WHERE Maintenance_ID = " +
                        this.getMaintenanceID();
        return query(statement);
    }

    public int getMaintenanceID() {
        return maintenanceID;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getMaintenanceDesc() {
        return maintenanceDesc;
    }

    public void setMaintenanceDesc(String maintenanceDesc) {
        this.maintenanceDesc = maintenanceDesc;
    }

    public BigDecimal getMaintenanceCost() {
        return maintenanceCost;
    }

    public void setMaintenanceCost(BigDecimal maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
    }

    public String getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(String maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }
}
