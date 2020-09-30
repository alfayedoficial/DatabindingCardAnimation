package com.promoclick.databindingcard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.promoclick.databindingcard.databinding.FragmentCardFrontBinding;
import com.promoclick.databindingcard.utils.FontTypeChange;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


public class CardFrontFragment extends Fragment {

    public FragmentCardFrontBinding binding;
    public FontTypeChange fontTypeChange;
    public CardActivity mActivity;

    public CardFrontFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_card_front, container, false);
        binding.setFragment(this);
        binding.setLifecycleOwner(this);

        mActivity = (CardActivity) getActivity();
        fontTypeChange = new FontTypeChange(getActivity());
        binding.tvCardNumber.setTypeface(fontTypeChange.get_fontface(3));
        binding.tvMemberName.setTypeface(fontTypeChange.get_fontface(3));


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mActivity.getName() != null) {

            if (!mActivity.getName().isEmpty()) {
                binding.tvMemberName.setText(mActivity.getName());
            } else {
                binding.tvMemberName.setText("CARD HOLDER");
            }
        }

        if (mActivity.getNumber() != null) {

            if (!mActivity.getName().isEmpty()) {
                binding.tvCardNumber.setText(mActivity.getNumber());
            } else {
                binding.tvCardNumber.setText("XXXX XXXX XXXX XXXX");
            }
        }

        if (mActivity.getDate() != null) {

            if (!mActivity.getDate().isEmpty()) {
                binding.tvValidity.setText(mActivity.getDate());
            } else {
                binding.tvValidity.setText("MM/YY");
            }
        }



    }

    public TextView getNumber() {
        return binding.tvCardNumber;
    }

    public void setNumber(String number) {
        binding.tvCardNumber.setText(number);
    }

    public TextView getName() {
        return binding.tvMemberName;
    }

    public TextView getValidity() {
        return binding.tvValidity;
    }
}