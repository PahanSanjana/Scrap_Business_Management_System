
package com.mycompany.scrap.management.system.model;

public class Purchase {

    // =========================================================
    // FIELDS
    // =========================================================

    private int id;

    private String purchaseCode;

    private int supplierId;

    private String purchaseDate;

    private String invoiceNumber;

    private String paymentMethod;

    private String paymentStatus;

    private double totalAmount;

    private String notes;

    private int createdBy;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public Purchase() {

    }

    // =========================================================
    // CONSTRUCTOR WITHOUT ID
    // =========================================================

    public Purchase(
            String purchaseCode,
            int supplierId,
            String purchaseDate,
            String invoiceNumber,
            String paymentMethod,
            String paymentStatus,
            double totalAmount,
            String notes,
            int createdBy
    ) {

        this.purchaseCode = purchaseCode;

        this.supplierId = supplierId;

        this.purchaseDate = purchaseDate;

        this.invoiceNumber = invoiceNumber;

        this.paymentMethod = paymentMethod;

        this.paymentStatus = paymentStatus;

        this.totalAmount = totalAmount;

        this.notes = notes;

        this.createdBy = createdBy;
    }

    // =========================================================
    // FULL CONSTRUCTOR
    // =========================================================

    public Purchase(
            int id,
            String purchaseCode,
            int supplierId,
            String purchaseDate,
            String invoiceNumber,
            String paymentMethod,
            String paymentStatus,
            double totalAmount,
            String notes,
            int createdBy
    ) {

        this.id = id;

        this.purchaseCode = purchaseCode;

        this.supplierId = supplierId;

        this.purchaseDate = purchaseDate;

        this.invoiceNumber = invoiceNumber;

        this.paymentMethod = paymentMethod;

        this.paymentStatus = paymentStatus;

        this.totalAmount = totalAmount;

        this.notes = notes;

        this.createdBy = createdBy;
    }

    // =========================================================
    // GETTER AND SETTER - ID
    // =========================================================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // =========================================================
    // GETTER AND SETTER - PURCHASE CODE
    // =========================================================

    public String getPurchaseCode() {
        return purchaseCode;
    }

    public void setPurchaseCode(String purchaseCode) {
        this.purchaseCode = purchaseCode;
    }

    // =========================================================
    // GETTER AND SETTER - SUPPLIER ID
    // =========================================================

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    // =========================================================
    // GETTER AND SETTER - PURCHASE DATE
    // =========================================================

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    // =========================================================
    // GETTER AND SETTER - INVOICE NUMBER
    // =========================================================

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    // =========================================================
    // GETTER AND SETTER - PAYMENT METHOD
    // =========================================================

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // =========================================================
    // GETTER AND SETTER - PAYMENT STATUS
    // =========================================================

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    // =========================================================
    // GETTER AND SETTER - TOTAL AMOUNT
    // =========================================================

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    // =========================================================
    // GETTER AND SETTER - NOTES
    // =========================================================

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // =========================================================
    // GETTER AND SETTER - CREATED BY
    // =========================================================

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    // =========================================================
    // TO STRING
    // =========================================================

    @Override
    public String toString() {

        return "Purchase{"
                + "id=" + id
                + ", purchaseCode='" + purchaseCode + '\''
                + ", supplierId=" + supplierId
                + ", purchaseDate='" + purchaseDate + '\''
                + ", invoiceNumber='" + invoiceNumber + '\''
                + ", paymentMethod='" + paymentMethod + '\''
                + ", paymentStatus='" + paymentStatus + '\''
                + ", totalAmount=" + totalAmount
                + ", notes='" + notes + '\''
                + ", createdBy=" + createdBy
                + '}';
    }
}