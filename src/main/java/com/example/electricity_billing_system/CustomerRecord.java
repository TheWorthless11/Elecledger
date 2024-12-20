package com.example.electricity_billing_system;

public class CustomerRecord {

    private String meterNo;
    private String name;
    private String address;
    private String city;
    private String state;
    private String email;
    private String phone;

    public CustomerRecord(String meterNo, String name, String address, String city, String state, String email, String phone) {
        this.meterNo = meterNo;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.email = email;
        this.phone = phone;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
