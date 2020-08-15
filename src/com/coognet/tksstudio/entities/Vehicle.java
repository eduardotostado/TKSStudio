package com.coognet.tksstudio.entities;

public class Vehicle extends QueryObject {

    private final int vehicleID;
    private int fleetID;
    private String vehicleMake;
    private String vehicleModel;
    private String vehicleColor;

    public Vehicle(int vehicleID, int fleetID, String vehicleMake, String vehicleModel, String vehicleColor) {
        this.vehicleID = vehicleID;
        this.fleetID = fleetID;
        this.vehicleMake = vehicleMake;
        this.vehicleModel = vehicleModel;
        this.vehicleColor = vehicleColor;
    }

    public boolean editAccount(int fleetID, String vehicleMake, String vehicleModel, String vehiclecolor){

        setFleetID(fleetID);
        setVehicleMake(vehicleMake);
        setVehicleModel(vehicleModel);
        setVehicleColor(vehiclecolor);

        statement = "UPDATE Vehicle " +
                "SET " +
                "Fleet_ID = " + this.getFleetID() +  ", " +
                "Vehicle_Make = '" + this.getVehicleMake() +  "', " +
                "Vehicle_Model = '" + this.getVehicleModel() +  "', " +
                "Vehicle_Color = '" + this.getVehicleColor() + "' " +
                "WHERE Vehicle_ID = " + this.getVehicleID();

        return query(statement);
    }

    public boolean addAccount(int fleetID, String vehicleMake, String vehicleModel, String vehiclecolor){
        statement = "INSERT INTO Vehicle (Fleet_ID, Vehicle_Make, Vehicle_Model, Vehicle_Color) VALUES (" +
                fleetID + ", '" + vehicleMake + "', '" + vehicleModel + "', '" + vehiclecolor +
                "')";

        return query(statement);
    }

    public boolean deleteAccount(){
        statement =
                "DELETE FROM Vehicle WHERE Vehicle_ID = " +
                        this.getVehicleID();
        return query(statement);
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public int getFleetID() {
        return fleetID;
    }

    public void setFleetID(int fleetID) {
        this.fleetID = fleetID;
    }

    public String getVehicleMake() {
        return vehicleMake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehicleMake = vehicleMake;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }
}
