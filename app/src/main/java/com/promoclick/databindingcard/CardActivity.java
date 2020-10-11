package com.promoclick.databindingcard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import com.mastercard.gateway.android.sdk.Gateway;
import com.mastercard.gateway.android.sdk.Gateway3DSecureCallback;
import com.mastercard.gateway.android.sdk.GatewayCallback;
import com.mastercard.gateway.android.sdk.GatewayMap;
import com.promoclick.databindingcard.databinding.ActivityCardBinding;
import com.promoclick.databindingcard.payment.ApiPayment;
import com.promoclick.databindingcard.utils.CreditCardEditText;
import com.promoclick.databindingcard.utils.CreditCardUtils;
import com.promoclick.databindingcard.utils.TakeFocus;

import java.util.Objects;
import java.util.UUID;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
//    public static final String MERCHANT_URL = "https://qnbalahli.gateway.mastercard.com/";

public class CardActivity extends AppCompatActivity {

    private ActivityCardBinding binding;
    private CardFrontFragment cardFrontFragment;
    private CardBackFragment cardBackFragment;
    private boolean etNumber = true;
    private boolean isDelete;
    private String sessionId, apiVersion, threeDSecureId, orderId, transactionId;
    private ApiPayment apiController = ApiPayment.getInstance();
    private Gateway gateway;
//    public static final String MERCHANT_URL = "https://heart-attack-promo-clicks.herokuapp.com/";
        public static final String MERCHANT_URL = "https://qnbalahli.gateway.mastercard.com/";
    //    public static final String MERCHANT_ID = "TESTQNBAATEST001";
    public static final String MERCHANT_ID = "HEARTATTACK";

//    merchant: HEARTATTACK:
//    password : eef2f897a15f452b8cc4d52036b4a782
//    Base Url : https://qnbalahli.gateway.mastercard.com/api/rest/version/43/merchant/HEARTATTACK/session

    public static final String TAG = "TEST_API";
    public static final String CARD_NUMBER = "XXXX XXXX XXXX XXXX";
    // static for demo
    public static final String AMOUNT = "1.00";
    public static final String CURRENCY = "EGP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_card);
        binding.setActivity(this);

        cardFrontFragment = new CardFrontFragment();
        cardBackFragment = new CardBackFragment();

        fireCard(cardFrontFragment);

        onTextWatcher();

        initGateWay();
    }

    private void initGateWay() {
        // init api controller
        apiController.setMerchantServerUrl(MERCHANT_URL);
        // init gateway
        gateway = new Gateway();
        gateway.setMerchantId(MERCHANT_ID);
        try {
            Gateway.Region region = Gateway.Region.MTF;
            gateway.setRegion(region);
        } catch (Exception e) {
            Log.e(TAG, "Invalid Gateway region value provided", e);
        }

        createSession();
    }

    private void createSession() {

        apiController.createSession(new CreateSessionCallback());
    }

    private void onTextWatcher() {
        // Number Watcher
        binding.etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isDelete = before != 0;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i(TAG, "" + etNumber);
                if (etNumber) {
                    String source = editable.toString();
                    int length = source.length();

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(source);

                    if (length > 0 && length % 5 == 0) {
                        if (isDelete)
                            stringBuilder.deleteCharAt(length - 1);
                        else
                            stringBuilder.insert(length - 1, " ");
                        binding.etNumber.setText(stringBuilder);
                        binding.etNumber.setSelection(Objects.requireNonNull(binding.etNumber.getText()).length());

                    }
                    if (length == 0)
                        cardFrontFragment.binding.tvCardNumber.setText(CARD_NUMBER);
                    else
                        cardFrontFragment.binding.tvCardNumber.setText(stringBuilder);

                }

            }
        });

        // Name Watcher
        binding.etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etNumber) {
                    if (TextUtils.isEmpty(editable.toString().trim()))
                        cardFrontFragment.binding.tvMemberName.setText("CARD HOLDER");
                    else
                        cardFrontFragment.binding.tvMemberName.setText(editable.toString());
                }

            }
        });

        // date Watcher
        binding.etValidity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isDelete = before != 0;
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (etNumber) {
                    String source = editable.toString();
                    int length = source.length();

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(source);

                    if (length == 3) {
                        if (isDelete)
                            stringBuilder.deleteCharAt(length - 1);
                        else
                            stringBuilder.insert(length - 1, "/");
                        binding.etValidity.setText(stringBuilder);
                        binding.etValidity.setSelection(binding.etValidity.getText().length());
                    }

                    if (length == 0)
                        cardFrontFragment.binding.tvValidity.setText("MM/YY");
                    else
                        cardFrontFragment.binding.tvValidity.setText(stringBuilder);
                }
            }
        });

        // cvv Watcher
        binding.etCvv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!etNumber) {
                    if (TextUtils.isEmpty(editable.toString().trim()))
                        cardBackFragment.binding.tvCvv.setText("XXX");
                    else
                        cardBackFragment.binding.tvCvv.setText(editable.toString());
                }

            }
        });

        TextOnTouchFront(binding.etNumber);
        TextOnTouchFront(binding.etName);
        TextOnTouchFront(binding.etValidity);
        TextOnTouchBack(binding.etCvv);

    }

    @SuppressLint("ClickableViewAccessibility")
    private void TextOnTouchFront(CreditCardEditText etNumber) {
        etNumber.setOnTouchListener((arg0, arg1) -> {
            if (!this.etNumber) {
                fireCard(cardFrontFragment);
                this.etNumber = true;
            }
            return false;
        });

        etNumber.setOnClickListener(view -> {
            if (!this.etNumber) {
                fireCard(cardFrontFragment);
                this.etNumber = true;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void TextOnTouchBack(CreditCardEditText etNumber) {
        etNumber.setOnTouchListener((arg0, arg1) -> {
            if (this.etNumber) {
                fireCard(cardBackFragment);
                this.etNumber = false;
            }
            return false;
        });

        etNumber.setOnClickListener(view -> {
            if (this.etNumber) {
                fireCard(cardBackFragment);
                this.etNumber = false;
            }
        });

    }

    public void btnNext() {
        String cardName = getName();
        String cardNumber = getNumber();
        String cardValidity = getDate();
        String cardCVV = getCvv();
        String expiryMonth, expiryYear;

        if (TextUtils.isEmpty(cardName)) {
            Toast.makeText(this, "Enter Valid Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cardValidity) || !CreditCardUtils.isValidDate(cardValidity)) {
            Toast.makeText(this, "Enter correct validity", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cardCVV) || cardCVV.length() < 3) {
            Toast.makeText(this, "Enter valid security number", Toast.LENGTH_SHORT).show();
        } else {
            expiryMonth = cardValidity.substring(0, 2);
            expiryYear = cardValidity.substring(3, 5);
            updateSession(cardName, cardNumber, expiryMonth, expiryYear, cardCVV);
        }

    }

    public void fireCard(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)
                .replace(R.id.cardViewContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void updateSession(String name, String number, String expiryMonth, String expiryYear, String cvv) {

        GatewayMap request = new GatewayMap()
                .set("sourceOfFunds.provided.card.nameOnCard", name)
                .set("sourceOfFunds.provided.card.number", number)
                .set("sourceOfFunds.provided.card.securityCode", cvv)
                .set("sourceOfFunds.provided.card.expiry.month", expiryMonth)
                .set("sourceOfFunds.provided.card.expiry.year", expiryYear);

        gateway.updateSession(sessionId, apiVersion, request, new UpdateSessionCallback());
    }

    public String getNumber() {

        return Objects.requireNonNull(binding.etNumber.getText()).toString();
    }

    public String getName() {

        return Objects.requireNonNull(binding.etName.getText()).toString();
    }

    public String getDate() {
        return Objects.requireNonNull(binding.etValidity.getText()).toString();
    }

    public String getCvv() {

        return Objects.requireNonNull(binding.etCvv.getText()).toString();
    }

    public void takeFocus() {

        TakeFocus.hideKeyboardFrom(this, binding.lyParent);
    }

    private void check3dsEnrollment() {
        String threeDSId = UUID.randomUUID().toString();
        threeDSId = threeDSId.substring(0, threeDSId.indexOf('-'));

        apiController.check3DSecureEnrollment(sessionId, AMOUNT, CURRENCY, threeDSId, new Check3DSecureEnrollmentCallback());
    }

    private void processPayment() {
        Log.i(TAG, "Payment Order");
        apiController.completeSession(sessionId, orderId, transactionId, AMOUNT, CURRENCY, threeDSecureId, false, new CompleteSessionCallback());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // handle the 3DSecure lifecycle
        if (Gateway.handle3DSecureResult(requestCode, resultCode, data, new ThreeDSecureCallback())) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class CreateSessionCallback implements ApiPayment.CreateSessionCallback {
        @Override
        public void onSuccess(String sessionId, String apiVersion) {
            Log.i(TAG, "Session established");
            Log.i(TAG, "sessionId :" + sessionId);
            Log.i(TAG, "apiVersionId :" + apiVersion);

            CardActivity.this.sessionId = sessionId;
            CardActivity.this.apiVersion = apiVersion;

        }

        @Override
        public void onError(Throwable throwable) {
            Log.e(TAG, throwable.getMessage(), throwable);

            Toast.makeText(CardActivity.this, "Error :" + throwable.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    private class UpdateSessionCallback implements GatewayCallback {
        @Override
        public void onSuccess(GatewayMap response) {
            Log.i(TAG, "Successfully updated session");
            Toast.makeText(CardActivity.this, "Successfully updated session", Toast.LENGTH_LONG).show();
            check3dsEnrollment();

        }

        @Override
        public void onError(Throwable throwable) {
            Log.e(TAG, throwable.getMessage(), throwable);
            Toast.makeText(CardActivity.this, "Error :" + throwable.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    private class Check3DSecureEnrollmentCallback implements ApiPayment.Check3DSecureEnrollmentCallback {
        @Override
        public void onSuccess(GatewayMap response) {
            int apiVersionInt = Integer.parseInt(apiVersion);
            String threeDSecureId = (String) response.get("gatewayResponse.3DSecureID");

            String html = null;
            if (response.containsKey("gatewayResponse.3DSecure.authenticationRedirect.simple.htmlBodyContent")) {
                html = (String) response.get("gatewayResponse.3DSecure.authenticationRedirect.simple.htmlBodyContent");
            }

            // for API versions <= 46, you must use the summary status field to determine next steps for 3DS
            if (apiVersionInt <= 46) {
                String summaryStatus = (String) response.get("gatewayResponse.3DSecure.summaryStatus");
                if ("CARD_ENROLLED".equalsIgnoreCase(summaryStatus)) {
                    Gateway.start3DSecureActivity(CardActivity.this, html);
                    return;
                }

                CardActivity.this.threeDSecureId = null;
                // for these 2 cases, you still provide the 3DSecureId with the pay operation
                if ("CARD_NOT_ENROLLED".equalsIgnoreCase(summaryStatus) || "AUTHENTICATION_NOT_AVAILABLE".equalsIgnoreCase(summaryStatus)) {
                    CardActivity.this.threeDSecureId = threeDSecureId;
                }
                processPayment();
            }

            // for API versions >= 47, you must look to the gateway recommendation and the presence of 3DS info in the payload
            else {
                String gatewayRecommendation = (String) response.get("gatewayResponse.response.gatewayRecommendation");
                // if DO_NOT_PROCEED returned in recommendation, should stop transaction
                if ("DO_NOT_PROCEED".equalsIgnoreCase(gatewayRecommendation)) {
                    Toast.makeText(CardActivity.this, "Error DO_NOT_PROCEED", Toast.LENGTH_LONG).show();
                    return;
                }

                // if PROCEED in recommendation, and we have HTML for 3ds, perform 3DS
                if (html != null) {
                    Gateway.start3DSecureActivity(CardActivity.this, html);
                    return;
                }

                CardActivity.this.threeDSecureId = threeDSecureId;
                processPayment();
            }
        }

        @Override
        public void onError(Throwable throwable) {
            Log.e(TAG, throwable.getMessage(), throwable);
            Toast.makeText(CardActivity.this, "Error :" + throwable.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private class ThreeDSecureCallback implements Gateway3DSecureCallback {
        @Override
        public void on3DSecureCancel() {
            showError();
        }

        @Override
        public void on3DSecureComplete(GatewayMap result) {
            int apiVersionInt = Integer.parseInt(apiVersion);
            Log.e(TAG, "on3DSecure is Complete");

            if (apiVersionInt <= 46) {
                if ("AUTHENTICATION_FAILED".equalsIgnoreCase((String) result.get("3DSecure.summaryStatus"))) {
                    showError();
                    return;
                }
            } else { // version >= 47
                if ("DO_NOT_PROCEED".equalsIgnoreCase((String) result.get("response.gatewayRecommendation"))) {
                    showError();
                    return;
                }
            }

            CardActivity.this.threeDSecureId = threeDSecureId;

            processPayment();
        }

        void showError() {
            Log.e(TAG, "on3DSecure is Failed");
            Toast.makeText(CardActivity.this, "Error : on3DSecureComplete", Toast.LENGTH_LONG).show();
        }
    }

    private class CompleteSessionCallback implements ApiPayment.CompleteSessionCallback {
        @Override
        public void onSuccess(String result) {
            Log.e(TAG, result);
            Toast.makeText(CardActivity.this, "Success", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(Throwable throwable) {
            Log.e(TAG, throwable.getMessage(), throwable);
            Toast.makeText(CardActivity.this, "Success Error", Toast.LENGTH_LONG).show();
        }
    }


}