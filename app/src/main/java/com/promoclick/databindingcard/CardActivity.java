package com.promoclick.databindingcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.promoclick.databindingcard.databinding.ActivityCardBinding;
import com.promoclick.databindingcard.utils.CreditCardEditText;
import com.promoclick.databindingcard.utils.CreditCardUtils;
import com.promoclick.databindingcard.utils.TakeFocus;

public class CardActivity extends AppCompatActivity {

    private ActivityCardBinding binding;
    private CardFrontFragment cardFrontFragment;
    private CardBackFragment cardBackFragment;
    private boolean  etNumber = true;
    private boolean isDelete;
    private  String cardNumber, cardCVV, cardValidity, cardName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this , R.layout.activity_card);
        binding.setActivity(this);

        cardFrontFragment = new CardFrontFragment();
        cardBackFragment = new CardBackFragment();
        fireCard(cardFrontFragment);

        onTextWatcher();
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
                Log.i("TSDADADS" ,"" +etNumber);
                if (etNumber){
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
                        binding.etNumber.setSelection(binding.etNumber.getText().length());

                    }
                    if (length == 0)
                        cardFrontFragment.binding.tvCardNumber.setText("XXXX XXXX XXXX XXXX");
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
                if (etNumber){
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
               if (etNumber){
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
                       binding.etValidity.setSelection( binding.etValidity.getText().length());

                       // Log.d("test"+s.toString(), "afterTextChanged: append "+length);
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
                if (!etNumber){
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
            if (!this.etNumber){
                fireCard(cardFrontFragment);
                this.etNumber = true;
            }
            return false;
        });

        etNumber.setOnClickListener(view -> {
            if (!this.etNumber){
                fireCard(cardFrontFragment);
                this.etNumber = true;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void TextOnTouchBack(CreditCardEditText etNumber) {
        etNumber.setOnTouchListener((arg0, arg1) -> {
            if (this.etNumber){
                fireCard(cardBackFragment);
                this.etNumber = false;
            }
            return false;
        });

        etNumber.setOnClickListener(view -> {
            if (this.etNumber){
                fireCard(cardBackFragment);
                this.etNumber = false;
            }
        });

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

    public String getNumber(){
        return binding.etNumber.getText().toString();
    }
    public String getName(){
        return binding.etName.getText().toString();
    }
    public String getDate(){
        return binding.etValidity.getText().toString();
    }
    public String getCvv(){
        return binding.etCvv.getText().toString();
    }

    public void btnNext(){
        cardName = getName();
        cardNumber = getName();
        cardValidity = getDate();
        cardCVV = getCvv();

        if (TextUtils.isEmpty(cardName)) {
            Toast.makeText(this, "Enter Valid Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cardNumber) || !CreditCardUtils.isValid(cardNumber.replace(" ",""))) {
            Toast.makeText(this, "Enter Valid card number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cardValidity)||!CreditCardUtils.isValidDate(cardValidity)) {
            Toast.makeText(this, "Enter correct validity", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cardCVV)||cardCVV.length()<3) {
            Toast.makeText(this, "Enter valid security number", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Your card is added", Toast.LENGTH_SHORT).show();
    }

    public void takeFocus(){
        TakeFocus.hideKeyboardFrom(this , binding.lyParent);
    }
}