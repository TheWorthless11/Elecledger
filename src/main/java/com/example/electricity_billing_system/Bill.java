package com.example.electricity_billing_system;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Bill {

    private final StringProperty meterNo;
    private final StringProperty month;
    private final StringProperty units;
    private final StringProperty totalBill;
    private final StringProperty status;

    // Constructor
    public Bill(String meterNo, String month, String units, String totalBill, String status) {
        this.meterNo = new SimpleStringProperty(meterNo);
        this.month = new SimpleStringProperty(month);
        this.units = new SimpleStringProperty(units);
        this.totalBill = new SimpleStringProperty(totalBill);
        this.status = new SimpleStringProperty(status);
    }

    // Getters for properties
    public String getMeterNo() {
        return meterNo.get();
    }

    public void setMeterNo(String meterNo) {
        this.meterNo.set(meterNo);
    }

    public StringProperty meterNoProperty() {
        return meterNo;
    }

    public String getMonth() {
        return month.get();
    }

    public void setMonth(String month) {
        this.month.set(month);
    }

    public StringProperty monthProperty() {
        return month;
    }

    public String getUnits() {
        return units.get();
    }

    public void setUnits(String units) {
        this.units.set(units);
    }

    public StringProperty unitsProperty() {
        return units;
    }

    public String getTotalBill() {
        return totalBill.get();
    }

    public void setTotalBill(String totalBill) {
        this.totalBill.set(totalBill);
    }

    public StringProperty totalBillProperty() {
        return totalBill;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }
}
