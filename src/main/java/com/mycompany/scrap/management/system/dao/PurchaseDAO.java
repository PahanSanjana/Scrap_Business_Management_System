
package com.mycompany.scrap.management.system.dao;

import com.mycompany.scrap.management.system.database.DatabaseConnection;
import com.mycompany.scrap.management.system.model.Purchase;
import com.mycompany.scrap.management.system.model.PurchaseItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class PurchaseDAO {

    // =========================================================
    // SAVE PURCHASE WITH ITEMS
    // =========================================================

    public boolean savePurchase(
            Purchase purchase,
            List<PurchaseItem> purchaseItems
    ) throws SQLException {

        String purchaseSql = """
                INSERT INTO purchases (
                    purchase_code,
                    supplier_id,
                    purchase_date,
                    invoice_number,
                    payment_method,
                    payment_status,
                    total_amount,
                    notes,
                    created_by
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        String itemSql = """
                INSERT INTO purchase_items (
                    purchase_id,
                    material_id,
                    quantity,
                    unit_price,
                    total_price
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection()
        ) {

            connection.setAutoCommit(false);

            try {

                int purchaseId;

                // =================================================
                // INSERT PURCHASE
                // =================================================

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(
                                        purchaseSql,
                                        Statement.RETURN_GENERATED_KEYS
                                )
                ) {

                    statement.setString(
                            1,
                            purchase.getPurchaseCode()
                    );

                    statement.setInt(
                            2,
                            purchase.getSupplierId()
                    );

                    statement.setString(
                            3,
                            purchase.getPurchaseDate()
                    );

                    statement.setString(
                            4,
                            purchase.getInvoiceNumber()
                    );

                    statement.setString(
                            5,
                            purchase.getPaymentMethod()
                    );

                    statement.setString(
                            6,
                            purchase.getPaymentStatus()
                    );

                    statement.setDouble(
                            7,
                            purchase.getTotalAmount()
                    );

                    statement.setString(
                            8,
                            purchase.getNotes()
                    );

                    statement.setInt(
                            9,
                            purchase.getCreatedBy()
                    );

                    statement.executeUpdate();

                    try (
                            ResultSet generatedKeys =
                                    statement.getGeneratedKeys()
                    ) {

                        if (!generatedKeys.next()) {

                            throw new SQLException(
                                    "Failed to retrieve purchase ID."
                            );
                        }

                        purchaseId =
                                generatedKeys.getInt(1);

                        purchase.setId(purchaseId);
                    }
                }

                // =================================================
                // INSERT PURCHASE ITEMS
                // =================================================

                try (
                        PreparedStatement statement =
                                connection.prepareStatement(itemSql)
                ) {

                    for (
                            PurchaseItem item : purchaseItems
                    ) {

                        statement.setInt(
                                1,
                                purchaseId
                        );

                        statement.setInt(
                                2,
                                item.getMaterialId()
                        );

                        statement.setDouble(
                                3,
                                item.getQuantity()
                        );

                        statement.setDouble(
                                4,
                                item.getUnitPrice()
                        );

                        statement.setDouble(
                                5,
                                item.getTotalPrice()
                        );

                        statement.addBatch();
                    }

                    statement.executeBatch();
                }

                // =================================================
                // COMMIT TRANSACTION
                // =================================================

                connection.commit();

                return true;

            } catch (SQLException exception) {

                connection.rollback();

                throw exception;

            } finally {

                connection.setAutoCommit(true);
            }
        }
    }

    // =========================================================
    // GET ALL PURCHASES
    // =========================================================

    public List<Purchase> getAllPurchases()
            throws SQLException {

        List<Purchase> purchases =
                new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    purchase_code,
                    supplier_id,
                    purchase_date,
                    invoice_number,
                    payment_method,
                    payment_status,
                    total_amount,
                    notes,
                    created_by
                FROM purchases
                ORDER BY id DESC
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Purchase purchase =
                        new Purchase();

                purchase.setId(
                        resultSet.getInt("id")
                );

                purchase.setPurchaseCode(
                        resultSet.getString("purchase_code")
                );

                purchase.setSupplierId(
                        resultSet.getInt("supplier_id")
                );

                purchase.setPurchaseDate(
                        resultSet.getString("purchase_date")
                );

                purchase.setInvoiceNumber(
                        resultSet.getString("invoice_number")
                );

                purchase.setPaymentMethod(
                        resultSet.getString("payment_method")
                );

                purchase.setPaymentStatus(
                        resultSet.getString("payment_status")
                );

                purchase.setTotalAmount(
                        resultSet.getDouble("total_amount")
                );

                purchase.setNotes(
                        resultSet.getString("notes")
                );

                purchase.setCreatedBy(
                        resultSet.getInt("created_by")
                );

                purchases.add(purchase);
            }
        }

        return purchases;
    }

    // =========================================================
    // GET PURCHASE BY ID
    // =========================================================

    public Purchase getPurchaseById(
            int purchaseId
    ) throws SQLException {

        String sql = """
                SELECT
                    id,
                    purchase_code,
                    supplier_id,
                    purchase_date,
                    invoice_number,
                    payment_method,
                    payment_status,
                    total_amount,
                    notes,
                    created_by
                FROM purchases
                WHERE id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    purchaseId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    Purchase purchase =
                            new Purchase();

                    purchase.setId(
                            resultSet.getInt("id")
                    );

                    purchase.setPurchaseCode(
                            resultSet.getString("purchase_code")
                    );

                    purchase.setSupplierId(
                            resultSet.getInt("supplier_id")
                    );

                    purchase.setPurchaseDate(
                            resultSet.getString("purchase_date")
                    );

                    purchase.setInvoiceNumber(
                            resultSet.getString("invoice_number")
                    );

                    purchase.setPaymentMethod(
                            resultSet.getString("payment_method")
                    );

                    purchase.setPaymentStatus(
                            resultSet.getString("payment_status")
                    );

                    purchase.setTotalAmount(
                            resultSet.getDouble("total_amount")
                    );

                    purchase.setNotes(
                            resultSet.getString("notes")
                    );

                    purchase.setCreatedBy(
                            resultSet.getInt("created_by")
                    );

                    return purchase;
                }
            }
        }

        return null;
    }

    // =========================================================
    // GET PURCHASE ITEMS
    // =========================================================

    public List<PurchaseItem> getPurchaseItems(
            int purchaseId
    ) throws SQLException {

        List<PurchaseItem> items =
                new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    purchase_id,
                    material_id,
                    quantity,
                    unit_price,
                    total_price
                FROM purchase_items
                WHERE purchase_id = ?
                ORDER BY id ASC
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    purchaseId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    PurchaseItem item =
                            new PurchaseItem();

                    item.setId(
                            resultSet.getInt("id")
                    );

                    item.setPurchaseId(
                            resultSet.getInt("purchase_id")
                    );

                    item.setMaterialId(
                            resultSet.getInt("material_id")
                    );

                    item.setQuantity(
                            resultSet.getDouble("quantity")
                    );

                    item.setUnitPrice(
                            resultSet.getDouble("unit_price")
                    );

                    item.setTotalPrice(
                            resultSet.getDouble("total_price")
                    );

                    items.add(item);
                }
            }
        }

        return items;
    }

    // =========================================================
    // UPDATE PURCHASE STATUS
    // =========================================================

    public boolean updatePaymentStatus(
            int purchaseId,
            String paymentStatus
    ) throws SQLException {

        String sql = """
                UPDATE purchases
                SET
                    payment_status = ?,
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    paymentStatus
            );

            statement.setInt(
                    2,
                    purchaseId
            );

            return statement.executeUpdate() > 0;
        }
    }

    // =========================================================
    // DELETE PURCHASE
    // =========================================================

    public boolean deletePurchase(
            int purchaseId
    ) throws SQLException {

        String sql = """
                DELETE FROM purchases
                WHERE id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    purchaseId
            );

            return statement.executeUpdate() > 0;
        }
    }
    
    
    // =========================================================
    // UPDATE PURCHASE WITH ITEMS
    // =========================================================

    public boolean updatePurchase(
            Purchase purchase,
            List<PurchaseItem> purchaseItems
    ) throws SQLException {

        String updatePurchaseSql = """
                UPDATE purchases
                SET purchase_code = ?,
                    supplier_id = ?,
                    purchase_date = ?,
                    invoice_number = ?,
                    payment_method = ?,
                    payment_status = ?,
                    total_amount = ?,
                    notes = ?,
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;

        String deleteItemsSql = """
                DELETE FROM purchase_items
                WHERE purchase_id = ?
                """;

        String insertItemSql = """
                INSERT INTO purchase_items (
                    purchase_id,
                    material_id,
                    quantity,
                    unit_price,
                    total_price
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement statement =
                             connection.prepareStatement(updatePurchaseSql)) {

                    statement.setString(1, purchase.getPurchaseCode());
                    statement.setInt(2, purchase.getSupplierId());
                    statement.setString(3, purchase.getPurchaseDate());
                    statement.setString(4, purchase.getInvoiceNumber());
                    statement.setString(5, purchase.getPaymentMethod());
                    statement.setString(6, purchase.getPaymentStatus());
                    statement.setDouble(7, purchase.getTotalAmount());
                    statement.setString(8, purchase.getNotes());
                    statement.setInt(9, purchase.getId());

                    if (statement.executeUpdate() == 0) {
                        connection.rollback();
                        return false;
                    }
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(deleteItemsSql)) {
                    statement.setInt(1, purchase.getId());
                    statement.executeUpdate();
                }

                try (PreparedStatement statement =
                             connection.prepareStatement(insertItemSql)) {

                    for (PurchaseItem item : purchaseItems) {
                        statement.setInt(1, purchase.getId());
                        statement.setInt(2, item.getMaterialId());
                        statement.setDouble(3, item.getQuantity());
                        statement.setDouble(4, item.getUnitPrice());
                        statement.setDouble(5, item.getTotalPrice());
                        statement.addBatch();
                    }

                    statement.executeBatch();
                }

                connection.commit();
                return true;

            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            }
        }
    }

}