package com.mycompany.scrap.management.system.model;

/**
 * Supplier model class.
 *
 * Represents a supplier in the Scrap Business Management System.
 */
public class Supplier {

    // =========================================================
    // FIELDS
    // =========================================================

    private int id;

    private String supplierCode;

    private String supplierName;

    private String contactPerson;

    private String phoneNumber;

    private String email;

    private String address;

    private String paymentTerms;

    private boolean active;

    private String createdAt;

    private String updatedAt;

    // =========================================================
    // CONSTRUCTORS
    // =========================================================

    /**
     * Default constructor.
     */
    public Supplier() {

    }

    /**
     * Constructor for creating a new supplier.
     *
     * @param supplierCode supplier reference code
     * @param supplierName supplier or company name
     * @param contactPerson main contact person
     * @param phoneNumber supplier phone number
     * @param email supplier email
     * @param address supplier address
     * @param paymentTerms supplier payment terms
     */
    public Supplier(
            String supplierCode,
            String supplierName,
            String contactPerson,
            String phoneNumber,
            String email,
            String address,
            String paymentTerms
    ) {

        this.supplierCode = supplierCode;

        this.supplierName = supplierName;

        this.contactPerson = contactPerson;

        this.phoneNumber = phoneNumber;

        this.email = email;

        this.address = address;

        this.paymentTerms = paymentTerms;

        this.active = true;
    }

    // =========================================================
    // GETTERS AND SETTERS
    // =========================================================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    // =========================================================
    // TO STRING
    // =========================================================

    @Override
    public String toString() {

        return supplierName == null
                ? ""
                : supplierName;
    }
}