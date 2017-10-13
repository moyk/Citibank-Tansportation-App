package com.citi.cititransit;

import android.location.Address;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import android.view.ViewGroup;
import android.widget.LinearLayout;

import android.widget.Toast;

import com.mastercard.mp.checkout.Amount;
import com.mastercard.mp.checkout.CheckoutResponseConstants;
import com.mastercard.mp.checkout.CheckoutSummaryItem;
import com.mastercard.mp.checkout.CryptoOptions;
import com.mastercard.mp.checkout.MasterpassButton;
import com.mastercard.mp.checkout.MasterpassCheckoutCallback;
import com.mastercard.mp.checkout.MasterpassCheckoutRequest;
import com.mastercard.mp.checkout.MasterpassError;
import com.mastercard.mp.checkout.MasterpassInitCallback;
import com.mastercard.mp.checkout.MasterpassMerchant;
import com.mastercard.mp.checkout.MasterpassMerchantConfiguration;
import com.mastercard.mp.checkout.NetworkType;
import com.mastercard.mp.checkout.ShippingSummaryItem;
import com.mastercard.mp.checkout.Tokenization;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * Created by nick on 10/13/17.
 */

public class PayActivity extends AppCompatActivity implements MasterpassInitCallback,
        MasterpassCheckoutCallback{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String signature = "LOCAL_TESTING";  // will be provided by Masterpass, use "LOCAL_TESTING" to test integration

        MasterpassMerchantConfiguration sampleConfig = new MasterpassMerchantConfiguration.Builder()
                .setContext(this.getApplicationContext())                       // context
                .setEnvironment(MasterpassMerchantConfiguration.SANDBOX)        // environment
                .setLocale(new Locale("en-US"))                                 // locale
                .setAnalyticsEnabled(false)                                     // analytics on / off
                .setSignature(signature)                                        // will be provided by Masterpass
                .build();

        try {
            MasterpassMerchant.initialize(sampleConfig, this);
        } catch (IllegalStateException e) {
            // in case SDK already initialized
            this.onInitSuccess();
        }
    }

    @Override
    public MasterpassCheckoutRequest getCheckoutRequest() {
        Amount total = new Amount(1000, Currency.getInstance(Locale.US).getCurrencyCode());
        ArrayList<NetworkType> allowedNetworkTypes = new ArrayList<>();
        allowedNetworkTypes.add(new NetworkType(NetworkType.MASTER));
        Tokenization tokenization = getSampleTokenizationObject();
        return new MasterpassCheckoutRequest.Builder()
                .setCheckoutId("c71d7dddda6a4db3af6121346de08ad9").setCartId("bb9410f9-e5a7-4f14-9c99-ea557d6fe2e8")
                .setAmount(total).setAllowedNetworkTypes(allowedNetworkTypes)
                .setTokenization(tokenization)
                .isShippingRequired(false).build();
    }

    @Override
    public void onCheckoutComplete(Bundle bundle) {
        String transactionId = bundle.getString(CheckoutResponseConstants.TRANSACTION_ID);
        Toast.makeText(this, "Here is the transaction ID:" + transactionId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCheckoutError(MasterpassError masterpassError) {
        Toast.makeText(this, masterpassError.message(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public MasterpassCheckoutRequest getUpdatedCheckoutData(List<CheckoutSummaryItem> list,
                                                            List<ShippingSummaryItem> list1,
                                                            ShippingSummaryItem shippingSummaryItem,
                                                            Address address, Amount amount) {
        return null;
    }

    @Override
    public void onInitSuccess() {
        MasterpassButton masterpassButton =
                MasterpassMerchant.getMasterpassButton(MasterpassButton.WEB_FLOW_ENABLED, this);
        ViewGroup.LayoutParams layout = masterpassButton.getLayoutParams();

        this.addContentView(masterpassButton,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onInitError(MasterpassError masterpassError) {
        Toast.makeText(this, masterpassError.message(), Toast.LENGTH_SHORT).show();
    }

    private Tokenization getSampleTokenizationObject() {
        try {
            ArrayList<String> format = new ArrayList<>();
            CryptoOptions.Mastercard mastercard = new CryptoOptions.Mastercard();
            CryptoOptions cryptoOptions = new CryptoOptions();
            format.add("UCAF");
            mastercard.setFormat(format);
            cryptoOptions.setMastercard(mastercard);
            String unpreditableNumber = Base64.encodeToString(Integer.toString(10000).getBytes("UTF-8")
                    , Base64.NO_WRAP);
            return new Tokenization(unpreditableNumber, cryptoOptions);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
