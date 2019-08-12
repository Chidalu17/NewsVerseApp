package com.example.newsverse;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.newsverse.api.ApiClient;
import com.example.newsverse.api.ApiInterface;
import com.example.newsverse.models.News;

import retrofit2.Call;

import static com.example.newsverse.fragment.BaseArticleFragment.API_KEY;

public final class NewsPreferences {

    /**
     * Get PreferredCountry based on stored SharedPreferences.
     * @param context Context used to access SharedPreferences
     * @return Country
     */
    public static String getPreferredCountry(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);

        // Get the information from SharedPreferences and check for the value associated with the key
        String Country = sharedPrefs.getString(
                context.getString(R.string.settings_select_your_country_key),
                context.getString(R.string.settings_country_by_default));

        return Country;
    }

}
