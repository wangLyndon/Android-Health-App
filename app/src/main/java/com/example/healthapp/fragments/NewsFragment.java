package com.example.healthapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.healthapp.R;
import com.example.healthapp.activities.ChangeFrag;
import com.example.healthapp.adapter.NewsAdapter;
import com.example.healthapp.entities.News;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class NewsFragment extends Fragment {
    private String Api = "???";

    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;

    private TextView isLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        recyclerView = view.findViewById(R.id.newsList);

        isLoading = view.findViewById(R.id.isLoading);

        view.findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChangeFrag.class);
                intent.putExtra("fragName", "News");
                startActivity(intent);
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        searchNews();

        return view;
    }

    private void searchNews(){
        NewsApiClient newsApiClient = new NewsApiClient(Api);

        /*
            {
            "status": "ok",
            "totalResults": 6467,
            -"articles": [
                    -{
                    -"source": {
                    "id": "wired",
                    "name": "Wired"
                    },
                    "author": "Joel Khalili",
                    "title": "Peter Todd Was ‘Unmasked’ As Bitcoin Creator Satoshi Nakamoto. Now He’s In Hiding",
                    "description": "Peter Todd has gone underground after an HBO documentary named him as the creator of Bitcoin, Satoshi Nakamoto, whose real identity has long remained a mystery.",
                    "url": "https://www.wired.com/story/peter-todd-was-unmasked-as-bitcoin-creator-satoshi-nakamoto-now-hes-in-hiding/",
                    "urlToImage": "https://media.wired.com/photos/6716870e6874cb5feda0798e/191:100/w_1280,c_limit/102124-bitcoin-satoshi-an.jpg",
                    "publishedAt": "2024-10-22T11:33:59Z",
                    "content": "In the week before the documentary was released, online betting markets had Len Sassaman, a cryptographer who moved in similar online circles to Satoshi, as the most likely candidate to be revealed a… [+2075 chars]"
                    },
         */

        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("health")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        List<News> news = new ArrayList<>();

                        for (int i = 0; i < response.getArticles().size(); i++) {
                            news.add(new News(response.getArticles().get(i).getTitle(), response.getArticles().get(i).getDescription(),
                                    response.getArticles().get(i).getContent(), response.getArticles().get(i).getPublishedAt(),
                                    response.getArticles().get(i).getUrl()));
                        }

                        newsAdapter = new NewsAdapter(getContext(), news);
                        recyclerView.setAdapter(newsAdapter);

                        isLoading.setText("News are below");
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                    }
                }
        );
    }
}