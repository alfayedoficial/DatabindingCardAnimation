package com.promoclick.databindingcard.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by ( Eng Ali Al Fayed)
 * Class do :
 * Date 9/30/2020 - 10:56 AM
 */
public class TakeFocus {

    public static void hideKeyboardFrom(Activity mActivity, View view) {
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
