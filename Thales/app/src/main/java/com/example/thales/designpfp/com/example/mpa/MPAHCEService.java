package com.example.thales.designpfp.com.example.mpa;

import android.os.Bundle;

import com.example.thales.designpfp.CHVerificationMethod;
import com.example.thales.designpfp.PaymentService;
import com.example.thales.designpfp.PaymentServiceErrorCode;
import com.example.thales.designpfp.TransactionContext;
import com.example.thales.designpfp.com.gemalto.mfs.mwsdk.payment.AbstractHCEService;
import com.example.thales.designpfp.com.gemalto.mfs.mwsdk.payment.PaymentServiceListener;

public class MPAHCEService extends AbstractHCEService implements PaymentWalletModule {
    PaymentWalletModule walletModule;
    PaymentFactory paymentFactory;

    MPAHCEService() {
        paymentFactory = PaymentFactory.getInstance();


    }

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

//CLA | INS | P1 | P2 | Lc | CData | Le
//        byte[] buffer = apdu.getBuffer();
        byte cla = bytes[ISO7816.OFFSET_CLA];
        byte ins = bytes[ISO7816.OFFSET_INS];
        byte[] p1 = bytes[ISO7816.OFFSET_P1];
        boolean PFPenabled = (p1[p1.length - 1] == 1);

        walletModule = paymentFactory.getPaymentModule(PFPenabled);
        byte[] responseAPDU = super.processCommandApdu(bytes, bundle);



//        firstTapLogic
//        walletModule with falgs based on APDU


        //secondTapLogic
        //walletModule with flag disbled to get Factory



        if (null != responseAPDU) {

        } else {

        }

//        contactlessPaymentServiceListener.onTransactionStarted();
//
//        contactlessPaymentServiceListener.onAuthenticationRequired();
//        contactlessPaymentServiceListener.onReadyToTap();
//
//        contactlessPaymentServiceListener.onTransactionCompleted();
        return responseAPDU;
    }


    @Override
    public void firstTapCompleted() {
        walletModule = paymentFactory.getPaymentModule(false);
    }
}
