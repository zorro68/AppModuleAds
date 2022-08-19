package com.ads.control.funtion;

public interface PurchaseListioner {
    void onProductPurchased(String productId, String transactionDetails);
    void displayErrorMessage(String errorMsg );
    void onUserCancelBilling( );
}
