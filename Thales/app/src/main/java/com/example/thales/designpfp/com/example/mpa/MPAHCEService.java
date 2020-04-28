package com.example.thales.designpfp.com.example.mpa;

import android.os.Bundle;

import com.example.thales.designpfp.CHVerificationMethod;
import com.example.thales.designpfp.PaymentService;
import com.example.thales.designpfp.PaymentServiceErrorCode;
import com.example.thales.designpfp.TransactionContext;
import com.example.thales.designpfp.com.gemalto.mfs.mwsdk.payment.AbstractHCEService;
import com.example.thales.designpfp.com.gemalto.mfs.mwsdk.payment.PaymentServiceListener;

public class MPAHCEService extends AbstractHCEService {

    @Override
    public PaymentServiceListener setupListener() {
        return null;
    }

    @Override
    public void setupPluginRegistration() {

    }

    @Override
    public boolean setupCardActivation() {
        return false;
    }

    @Override
    public void onAuthenticationRequired(PaymentService activatedPaymentService, CHVerificationMethod cvm, long cvmResetTimeout) {

    }

    @Override
    public void onError(TransactionContext ctx, PaymentServiceErrorCode error, String msg) {

    }

    @Override
    public byte[] processCommandApdu(byte[] bytes, Bundle bundle) {
        //        AppLogger.i(TAG, "APDU Received from POS::" +
        //                ByteUtils.toHexString(bytes));
        byte[] responseAPDU = super.processCommandApdu(bytes, bundle);
        if (null != responseAPDU) {
        //            AppLogger.i(TAG, "APDU sent to POS::" +
        //                    ByteUtils.toHexString(responseAPDU));
        } else {
        //            AppLogger.i(TAG, "APDU sent to POS:: null");
        }
        return responseAPDU;
    }
}
