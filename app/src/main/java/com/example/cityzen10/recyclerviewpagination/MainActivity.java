package com.example.cityzen10.recyclerviewpagination;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.cityzen10.recyclerviewpagination.api.Client;
import com.example.cityzen10.recyclerviewpagination.api.Service;
import com.example.cityzen10.recyclerviewpagination.model.Person;
import com.example.cityzen10.recyclerviewpagination.model.PersonResponse;
import com.example.cityzen10.recyclerviewpagination.utils.PaginationScrollListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 4;
    private int currentPage = PAGE_START;
    private Service personService;
    int cacheSize=10*1024*1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = (RecyclerView) findViewById(R.id.main_recycler);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        adapter = new PaginationAdapter(this);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 3000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        //init service and load data
        personService = Client.getClient().create(Service.class);
        loadFirstPage();
    }

    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        callTopPersons().enqueue(new Callback<PersonResponse>() {
            @Override
            public void onResponse(Call<PersonResponse> call, Response<PersonResponse> response) {
                // Got data. Send it to adapter

                List<Person> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES)
                    adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<PersonResponse> call, Throwable t) {
                t.printStackTrace();

            }
        });

    }

    private List<Person> fetchResults(Response<PersonResponse> response) {
        PersonResponse topPersons = response.body();
        assert topPersons != null;
        return topPersons.getData();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);


        callTopPersons().enqueue(new Callback<PersonResponse>() {
            @Override
            public void onResponse(Call<PersonResponse> call, Response<PersonResponse> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                List<Person> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<PersonResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    /**
     * Performs a Retrofit call to the top persons API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    private Call<PersonResponse> callTopPersons() {
        return personService.getUsers(
                currentPage
        );
    }
}