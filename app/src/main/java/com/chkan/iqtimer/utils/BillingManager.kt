package com.chkan.iqtimer.utils

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import kotlinx.coroutines.*

class BillingManager (context: Context, private val activity : Activity, private val applicationScope: CoroutineScope, private val result: ((MyResult)->Unit)) {

    private val purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                for (purchase in purchases) {
                    handlePurchase(purchase)
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
                result.invoke(MyResult.Error(billingResult.toString()))
            } else {
                if(billingResult.responseCode == BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED){
                    result.invoke(MyResult.Success)
                } else {
                    result.invoke(MyResult.Error(billingResult.toString()))
                }
                // Handle any other error codes
            }
        }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {

            result.invoke(MyResult.Success)

            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.purchaseToken)

                applicationScope.launch {
                    val ackPurchaseResult = withContext(Dispatchers.IO) {
                        billingClient.acknowledgePurchase(acknowledgePurchaseParams.build())
                    }
                }
            }
        }
    }

    private var billingClient = BillingClient.newBuilder(context)
        .setListener(purchasesUpdatedListener)
        .enablePendingPurchases()
        .build()

    fun start(){
        
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode ==  BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    //перед началом покупки, проверяем не куплен ли уже товар
                    checkPurchased()
                }
            }
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    private fun checkPurchased() {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)

        applicationScope.launch(Dispatchers.IO) {
            billingClient.queryPurchasesAsync(params.build()) { billingResult, purchasesList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    if (purchasesList.isNotEmpty() && purchasesList.first().purchaseState == Purchase.PurchaseState.PURCHASED) {
                        result.invoke(MyResult.Success)
                    } else {
                        getProductDetails()
                    }
                } else {
                    getProductDetails()
                }
            }
        }
    }

    private fun getProductDetails() {
        val listProducts = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("sku_access_achivements")
                .setProductType(BillingClient.ProductType.INAPP)
                .build())

        val query =
            QueryProductDetailsParams.newBuilder()
                .setProductList(listProducts)
                .build()

        billingClient.queryProductDetailsAsync(query) {
                billingResult,
                productDetailsList ->
            // check billingResult
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                startBillingFlow(productDetailsList)
            }
            // process returned productDetailsList
        }
    }

    private fun startBillingFlow(productDetails: List<ProductDetails>) {

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails.first())
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()
        //result get in purchasesUpdatedListener
        val billingResult = billingClient.launchBillingFlow(activity, billingFlowParams)
    }

}