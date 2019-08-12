package com.example.newsverse.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.newsverse.EmptyRecyclerView;
import com.example.newsverse.NewsDetailActivity;
import com.example.newsverse.NewsPreferences;
import com.example.newsverse.R;
import com.example.newsverse.adapter.Adapter;
import com.example.newsverse.api.ApiClient;
import com.example.newsverse.api.ApiInterface;
import com.example.newsverse.models.Article;
import com.example.newsverse.models.News;
import com.example.newsverse.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BaseArticleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String API_KEY = "aaa79c03abf448539fe599146b6fd17d";
    public EmptyRecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    public List<Article> articles = new ArrayList<>();
    public Adapter adapter;
    private String TAG = BaseArticleFragment.class.getSimpleName();
    public SwipeRefreshLayout swipeRefreshLayout;
    public RelativeLayout errorLayout;
    public View progressBar;
    private ImageView errorImage;
    private TextView errorTitle, errorMessage;
    private Button btnRetry;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);


        recyclerView = rootView.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        onLoadingSwipeRefresh("");

        errorLayout = rootView.findViewById(R.id.errorLayout);
        errorImage = rootView.findViewById(R.id.errorImage);
        errorTitle = rootView.findViewById(R.id.errorTitle);
        errorMessage = rootView.findViewById(R.id.errorMessage);
        btnRetry = rootView.findViewById(R.id.btnRetry);
        progressBar = rootView.findViewById(R.id.loading_indicator);

        return rootView;
    }

    public void LoadJson(final String keyword){

        errorLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(true);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        String country = NewsPreferences.getPreferredCountry(getContext());
        String language = Utils.getLanguage();



        Call<News> call;

        if (keyword.length() > 0 ){
            call = apiInterface.getNewsSearch(keyword, language, "publishedAt", API_KEY);
        } else {
            call = apiInterface.getNews(country, API_KEY);
        }

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticle() != null){

                    if (!articles.isEmpty()){
                        articles.clear();
                    }
                    Log.d("d","getting response.....\n" + response);
                    articles = response.body().getArticle();
                    adapter = new Adapter(articles, getContext());
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
                            "Please Try Again!\n"+
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
                        "Network failure, Please Try Again\n"+
                                t.toString());
            }
        });

    }



    public void initListener(){

        adapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImageView imageView = view.findViewById(R.id.img);
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);

                Article article = articles.get(position);
                intent.putExtra("url", article.getUrl());
                intent.putExtra("title", article.getTitle());
                intent.putExtra("img",  article.getUrlToImage());
                intent.putExtra("date",  article.getPublishedAt());
                intent.putExtra("source",  article.getSource().getName());
                intent.putExtra("author",  article.getAuthor());

                Pair<View, String> pair = Pair.create((View)imageView, ViewCompat.getTransitionName(imageView));
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        pair
                );


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    startActivity(intent, optionsCompat.toBundle());
                }else {
                    startActivity(intent);
                }

            }
        });

    }



    @Override
    public void onRefresh() {
        LoadJson("");
    }

    private void onLoadingSwipeRefresh(final String keyword){

        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        LoadJson(keyword);
                    }
                }
        );

    }

    public void showErrorMessage(int imageView, String title, String message){

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
        }

        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoadingSwipeRefresh("");
            }
        });

    }

}
