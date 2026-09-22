
package com.mycompany.scrap.management.system.model;

public class PurchaseItem {

    // =========================================================
    // FIELDS
    // =========================================================

    private int id;

    private int purchaseId;

    private int materialId;

    private double quantity;

    private double unitPrice;

    private double totalPrice;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public PurchaseItem() {

    }

    // =========================================================
    // CONSTRUCTOR WITHOUT ID
    // =========================================================

    public PurchaseItem(
            int purchaseId,
            int materialId,
            double quantity,
            double unitPrice,
            double totalPrice
    ) {

        this.purchaseId = purchaseId;

        this.materialId = materialId;

        this.quantity = quantity;

        this.unitPrice = unitPrice;

        this.totalPrice = totalPrice;
    }

    // =========================================================
    // FULL CONSTRUCTOR
    // =========================================================

    public PurchaseItem(
            int id,
            int purchaseId,
            int materialId,
            double quantity,
            double unitPrice,
            double totalPrice
    ) {

        this.id = id;

        this.purchaseId = purchaseId;

        this.materialId = materialId;

        this.quantity = quantity;

        this.unitPrice = unitPrice;

        this.totalPrice = totalPrice;
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
    // GETTER AND SETTER - PURCHASE ID
    // =========================================================

    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    // =========================================================
    // GETTER AND SETTER - MATERIAL ID
    // =========================================================

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    // =========================================================
    // GETTER AND SETTER - QUANTITY
    // =========================================================

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    // =========================================================
    // GETTER AND SETTER - UNIT PRICE
    // =========================================================

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    // =========================================================
    // GETTER AND SETTER - TOTAL PRICE
    // =========================================================

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    // =========================================================
    // TO STRING
    // =========================================================

    @Override
    public String toString() {

        return "PurchaseItem{"
                + "id=" + id
                + ", purchaseId=" + purchaseId
                + ", materialId=" + materialId
                + ", quantity=" + quantity
                + ", unitPrice=" + unitPrice
                + ", totalPrice=" + totalPrice
                + '}';
    }
}