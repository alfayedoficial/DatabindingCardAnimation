package com.promoclick.databindingcard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.promoclick.databindingcard.databinding.FragmentCardBackBinding;
import com.promoclick.databindingcard.utils.FontTypeChange;


public class CardBackFragment extends Fragment {

    public FragmentCardBackBinding binding;
    public CardActivity mActivity;
    public CardBackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater ,R.layout.fragment_card_back, container, false);
        binding.setFragment(this);
        binding.setLifecycleOwner(this);

        mActivity = (CardActivity) getActivity();
        FontTypeChange fontTypeChange = new FontTypeChange(getActivity());
        binding.tvCvv.setTypeface(fontTypeChange.get_fontface(1));


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mActivity.getCvv() != null) {

            if (!mActivity.getCvv().isEmpty()) {
                binding.tvCvv.setText(mActivity.getCvv());
            } else {
                binding.tvCvv.setText(" ");
            }
        }
    }
}