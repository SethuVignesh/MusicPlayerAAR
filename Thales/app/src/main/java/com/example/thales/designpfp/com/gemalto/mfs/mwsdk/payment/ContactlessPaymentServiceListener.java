package com.example.thales.designpfp.com.gemalto.mfs.mwsdk.payment;

import com.example.thales.designpfp.PaymentService;
import com.example.thales.designpfp.TransactionContext;

public interface ContactlessPaymentServiceListener extends PaymentServiceListener {
    void onReadyToTap(PaymentService activatedPaymentService);

    void onTransactionCompleted(TransactionContext ctx);

    void onTransactionStarted();
}
