package com.innovatelabs.news


import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.*


class MainActivity : AppCompatActivity(), NewsItemclicked{

    private lateinit var mAdapter: NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.newsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        dataIteams()
        mAdapter = NewsAdapter(this)
        recyclerView.adapter = mAdapter
    }
    private fun dataIteams() {
        val url = "https://newsdata.io/api/1/news?apikey=pub_6231cfbc2f85618fd88ae8ca41b451f1d8d1&country=in"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            {
                val newsJsonArray = it.getJSONArray("results")
                val jsonArray = ArrayList<News>()
                for(i in 0 until  newsJsonArray.length()){
                    val jsonArrayObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        jsonArrayObject.getString("title"),
                        jsonArrayObject.getString("image_url"),
                        jsonArrayObject.getString("link"),
                        jsonArrayObject.getString("source_id")
                    )
                    jsonArray.add(news)
                }
                mAdapter.updatedNews(jsonArray)
            },
            { error ->
                // TODO: Handle error
            },
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }


    override fun onIteamClicked(item: News) {
        val builder:CustomTabsIntent.Builder =  CustomTabsIntent.Builder();
        val customTabsIntent : CustomTabsIntent  = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.link));
    }
}