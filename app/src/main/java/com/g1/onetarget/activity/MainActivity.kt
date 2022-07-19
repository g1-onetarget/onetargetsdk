package com.g1.onetarget.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.g1.onetarget.R
import com.g1.onetarget.adapter.AnswersAdapter
import com.g1.onetargetsdk.ApiUtils
import com.g1.onetargetsdk.SOService
import com.g1.onetargetsdk.model.SOAnswersResponse
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private fun log(msg: String) {
        Log.d(javaClass.simpleName, msg)
    }

    private var mService: SOService? = null
    private var mAdapter: AnswersAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
    }

    private fun setupViews() {
        setupActionBar()
        mService = ApiUtils.sOService
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.let { actionBar ->
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
            toolbar.title = getString(R.string.app_name)
            toolbar.setTitleTextColor(Color.WHITE)
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }

        btTestTracking.setOnClickListener {
            track()
        }

        mAdapter = AnswersAdapter(
            mItems = ArrayList(0),
            mItemListener = object : AnswersAdapter.PostItemListener {
                override fun onPostClick(id: Long) {

                }
            }
        )

        rvAnswers.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = mAdapter
            this.setHasFixedSize(true)
            val itemDecoration: RecyclerView.ItemDecoration =
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            this.addItemDecoration(itemDecoration)
        }

    }

    private fun track() {
        loadAnswers()
    }

    private fun loadAnswers() {
        log("loadAnswers")
        mService?.answers?.enqueue(object : Callback<SOAnswersResponse> {
            override fun onResponse(
                call: Call<SOAnswersResponse>,
                response: Response<SOAnswersResponse>
            ) {
                log("onResponse ${response.body()}")
                if (response.isSuccessful) {
                    response.body()?.items?.let {
                        mAdapter?.updateAnswers(it)
                    }
                } else {
                    val statusCode = response.code()
                    print(statusCode)
                    // handle request errors depending on status code
                }
            }

            override fun onFailure(call: Call<SOAnswersResponse>, t: Throwable) {
                log("onFailure $t")
            }
        })
    }
}
