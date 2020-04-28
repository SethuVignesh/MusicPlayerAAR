package com.example.thales.designpfp.com.example.mpa;

import android.app.Application;
import android.content.Context;

import com.example.thales.designpfp.CHVerificationMethod;
import com.example.thales.designpfp.PaymentService;
import com.example.thales.designpfp.PaymentServiceErrorCode;
import com.example.thales.designpfp.TransactionContext;
import com.example.thales.designpfp.com.gemalto.mfs.mwsdk.payment.ContactlessPaymentServiceListener;

public class ContactlessPaymentListener implements ContactlessPaymentServiceListener {

    private Context context;

    public ContactlessPaymentListener(Application app) {
        this.context = app.getApplicationContext();
        // Show UI screen asking user to do second tap
    }

    @Override
    public void onReadyToTap(PaymentService paymentService) {

    }

    @Override
    public void onTransactionCompleted(TransactionContext ctx) {

    }

    @Override
    public void onTransactionStarted() {

    }

    @Override
    public void onAuthenticationRequired(PaymentService paymentService, CHVerificationMethod cvm, long cvmResetTimeout) {

    }

    @Override
    public void onError(TransactionContext ctx, PaymentServiceErrorCode error, String msg) {

    }
}
