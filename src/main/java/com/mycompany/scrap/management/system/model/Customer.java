
package com.mycompany.scrap.management.system.model;

/**
 * Customer model.
 *
 * Represents a customer in the Scrap Business Management System.
 */
public class Customer {

    // =========================================================
    // FIELDS
    // =========================================================

    private int id;

    private String customerCode;

    private String fullName;

    private String nicNumber;

    private String phoneNumber;

    private String address;

    private boolean active;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public Customer() {

        this.active = true;
    }

    // =========================================================
    // CONSTRUCTOR WITHOUT ID
    // =========================================================

    public Customer(
            String customerCode,
            String fullName,
            String nicNumber,
            String phoneNumber,
            String address
    ) {

        this.customerCode = customerCode;
        this.fullName = fullName;
        this.nicNumber = nicNumber;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.active = true;
    }

    // =========================================================
    // CONSTRUCTOR WITH ID
    // =========================================================

    public Customer(
            int id,
            String customerCode,
            String fullName,
            String nicNumber,
            String phoneNumber,
            String address,
            boolean active
    ) {

        this.id = id;
        this.customerCode = customerCode;
        this.fullName = fullName;
        this.nicNumber = nicNumber;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.active = active;
    }

    // =========================================================
    // GET ID
    // =========================================================

    public int getId() {

        return id;
    }

    // =========================================================
    // SET ID
    // =========================================================

    public void setId(int id) {

        this.id = id;
    }

    // =========================================================
    // GET CUSTOMER CODE
    // =========================================================

    public String getCustomerCode() {

        return customerCode;
    }

    // =========================================================
    // SET CUSTOMER CODE
    // =========================================================

    public void setCustomerCode(String customerCode) {

        this.customerCode = customerCode;
    }

    // =========================================================
    // GET FULL NAME
    // =========================================================

    public String getFullName() {

        return fullName;
    }

    // =========================================================
    // SET FULL NAME
    // =========================================================

    public void setFullName(String fullName) {

        this.fullName = fullName;
    }

    // =========================================================
    // GET NIC NUMBER
    // =========================================================

    public String getNicNumber() {

        return nicNumber;
    }

    // =========================================================
    // SET NIC NUMBER
    // =========================================================

    public void setNicNumber(String nicNumber) {

        this.nicNumber = nicNumber;
    }

    // =========================================================
    // GET PHONE NUMBER
    // =========================================================

    public String getPhoneNumber() {

        return phoneNumber;
    }

    // =========================================================
    // SET PHONE NUMBER
    // =========================================================

    public void setPhoneNumber(String phoneNumber) {

        this.phoneNumber = phoneNumber;
    }

    // =========================================================
    // GET ADDRESS
    // =========================================================

    public String getAddress() {

        return address;
    }

    // =========================================================
    // SET ADDRESS
    // =========================================================

    public void setAddress(String address) {

        this.address = address;
    }

    // =========================================================
    // CHECK ACTIVE STATUS
    // =========================================================

    public boolean isActive() {

        return active;
    }

    // =========================================================
    // SET ACTIVE STATUS
    // =========================================================

    public void setActive(boolean active) {

        this.active = active;
    }

    // =========================================================
    // TO STRING
    // =========================================================

    @Override
    public String toString() {

        return "Customer{"
                + "id=" + id
                + ", customerCode='" + customerCode + '\''
                + ", fullName='" + fullName + '\''
                + ", nicNumber='" + nicNumber + '\''
                + ", phoneNumber='" + phoneNumber + '\''
                + ", address='" + address + '\''
                + ", active=" + active
                + '}';
    }
}