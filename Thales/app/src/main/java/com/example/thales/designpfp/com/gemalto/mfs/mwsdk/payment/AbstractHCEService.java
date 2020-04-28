package com.example.thales.designpfp.com.gemalto.mfs.mwsdk.payment;

import android.os.Bundle;

public abstract class AbstractHCEService  implements PaymentServiceListener {

    public abstract PaymentServiceListener setupListener();

    public abstract void setupPluginRegistration();

    public abstract boolean setupCardActivation();

    final void onDeactivated(final int reason) {

    }

    public byte[] processCommandApdu(final byte[] apdu, final Bundle
            bundle) {
        return new byte[1];
    }
}
