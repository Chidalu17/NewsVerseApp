package com.example.newsverse.fragment;

import android.view.View;

import com.example.newsverse.NewsPreferences;
import com.example.newsverse.R;
import com.example.newsverse.adapter.Adapter;
import com.example.newsverse.api.ApiClient;
import com.example.newsverse.api.EntertainmentApiInterface;
import com.example.newsverse.api.ScienceApiInterface;
import com.example.newsverse.models.News;
import com.example.newsverse.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScienceFragment extends BaseArticleFragment {
    public void LoadJson(final String keyword) {

        progressBar.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);

        ScienceApiInterface apiInterface = ApiClient.getApiClient().create(ScienceApiInterface.class);

        String country = NewsPreferences.getPreferredCountry(getContext());

        Call<News> call;


        call = apiInterface.getNews(country, API_KEY);


        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticle() != null) {

                    if (!articles.isEmpty()) {
                        articles.clear();
                    }

                    articles = response.body().getArticle();
                    adapter = new Adapter(articles, getActivity());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    initListener();

                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);


                } else {

                    progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);

                    String errorCode;
                    switch (response.code()) {
                        case 404:
                            errorCode = "404 not found";
                            break;
                        case 500:
                            errorCode = "500 server broken";
                            break;
                        default:
                            errorCode = "unknown error";
                            break;
                    }

                    showErrorMessage(
                            R.drawable.no_result,
                            "No Result",
                            "Please Try Again!\n" +
                                    errorCode);

                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                showErrorMessage(
                        R.drawable.oops,
                        "Oops..",
                        "Network failure, Please Try Again\n" +
                                t.toString());
            }
        });

    }
}