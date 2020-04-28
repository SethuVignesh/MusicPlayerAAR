package com.example.thales.designpfp.com.gemalto.mfs.mwsdk.payment;

import com.example.thales.designpfp.CHVerificationMethod;
import com.example.thales.designpfp.PaymentService;
import com.example.thales.designpfp.PaymentServiceErrorCode;
import com.example.thales.designpfp.TransactionContext;

public interface PaymentServiceListener{
    void onAuthenticationRequired(PaymentService
                                          activatedPaymentService, CHVerificationMethod cvm, long
                                          cvmResetTimeout);

    void onError(TransactionContext ctx, PaymentServiceErrorCode
            error, String msg);
}
