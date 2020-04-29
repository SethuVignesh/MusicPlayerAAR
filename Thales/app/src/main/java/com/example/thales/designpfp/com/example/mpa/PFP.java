package com.example.thales.designpfp.com.example.mpa;

import com.example.thales.designpfp.CHVerificationMethod;
import com.example.thales.designpfp.PaymentService;

public class PFP extends ContactlessPaymentListener {

    @Override
    public void onTransactionStarted() {

    }

    @Override
    public void onAuthenticationRequired(PaymentService activatedPaymentService, CHVerificationMethod cvm, long cvmResetTimeout) {

    }

    @Override
    public void onReadyToTap(PaymentService paymentService) {
        super.onReadyToTap(paymentService);

    }
}
