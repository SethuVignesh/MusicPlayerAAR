package com.example.thales.designpfp.com.example.mpa;

public class PaymentFactory {
    boolean enablePFP = false;
    public static PaymentFactory paymentFactory = new   PaymentFactory();

    private PaymentFactory() {

    }

    public static PaymentFactory getInstance() {
        return paymentFactory;
    }

    public PaymentWalletModule getPaymentModule(boolean enablePFP) {
        if (enablePFP) {
            return new PFP();
        } else {
            return ContactlessPaymentListener();
        }
    }
}
