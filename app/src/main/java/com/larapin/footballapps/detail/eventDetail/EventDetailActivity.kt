package com.larapin.footballapps.detail.eventDetail

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.Menu
import android.widget.ProgressBar
import com.google.gson.Gson
import com.larapin.footballapps.R
import com.larapin.footballapps.api.repoApi
import com.larapin.footballapps.model.detailEvent
import com.larapin.footballapps.util.invisible
import com.larapin.footballapps.util.visible
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_event_detail.*
import okhttp3.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.find
import org.jetbrains.anko.support.v4.onRefresh
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class EventDetailActivity : AppCompatActivity(), EventDetailView {
    private lateinit var presenter: EventDetailPresenter
    private lateinit var details: detailEvent
    private lateinit var idEvent: String
    private var idHome: String = ""
    private var idAway: String = ""
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    val client = OkHttpClient()

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        supportActionBar?.title = "Match Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        progressBar = find(R.id.progress_bar_detail)
        swipeRefresh = find(R.id.swipe_refresh_detail)

        val intent = intent
        idEvent = intent.getStringExtra("id")
        idHome = intent.getStringExtra("idhome")
        idAway = intent.getStringExtra("idaway")

        val request = repoApi()
        val gson = Gson()
        presenter = EventDetailPresenter(this, request, gson)
        presenter.getEventDetail(idEvent)
        swipeRefresh.onRefresh {
            presenter.getEventDetail(idEvent)
        }
        val logo = arrayOf(idHome, idAway)
        getBadge(logo)
    }



    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    @SuppressLint("SimpleDateFormat")
    override fun showEventDetail(data: List<detailEvent>) {
        details = detailEvent(data[0].eventId,
                data[0].eventDate,
                data[0].eventTime,
                data[0].idHome,
                data[0].teamHome,
                data[0].scoreHome,
                data[0].formationHome,
                data[0].goalHome,
                data[0].shotHome,
                data[0].gkHome,
                data[0].defHome,
                data[0].midHome,
                data[0].forwHome,
                data[0].idAway,
                data[0].teamAway,
                data[0].scoreAway,
                data[0].formationAway,
                data[0].goalAway,
                data[0].shotAway,
                data[0].gkAway,
                data[0].defAway,
                data[0].midAway,
                data[0].forwAway)
        swipeRefresh.isRefreshing = false
        val tanggal = SimpleDateFormat("EEE, d MMM yyyy")
                .format(toGMTFormat(data[0].eventDate, data[0].eventTime))
        val waktu = SimpleDateFormat("HH:mm")
                .format(toGMTFormat(data[0].eventDate, data[0].eventTime))
        tv_date_detail.text = tanggal
        tv_time_detail.text = waktu
        tv_home_detail.text = data[0].teamHome
        tv_away_detail.text = data[0].teamAway
        if(data[0].scoreHome.isNullOrEmpty() && data[0].scoreAway.isNullOrEmpty()){
            tv_skor_detail.text = "0 vs 0"
        }else{
            tv_skor_detail.text = data[0].scoreHome+"  vs  "+data[0].scoreAway
        }
        tv_home_formation.text = data[0].formationHome
        tv_away_formation.text = data[0].formationAway
        tv_home_goals.text = data[0].goalHome?.replace(";", "\n")
        tv_away_goals.text = data[0].goalAway?.replace(";", "\n")
        tv_home_shots.text = data[0].shotHome
        tv_away_shots.text = data[0].shotAway
    }

    private fun getBadge(logo: Array<String>) {
        for(i in 0..1){
            val request = Request.Builder()
                    .url("https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id="+logo[i])
                    .build()
            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    var a = response.body()?.string()
                    runOnUiThread {
                        run(){
                            var json = JSONObject(a)
                            var badge = json.getJSONArray("teams").getJSONObject(0).getString("strTeamBadge")
                            if(i == 0) {
                                Picasso.with(ctx).load(badge).into(img_home)
                            }else{
                                Picasso.with(ctx).load(badge).into(img_away)
                            }
                        }
                    }
                }
            })
        }
    }

    private fun toGMTFormat(date: String?, time: String?): Date? {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val dateTime = "$date $time"
        return formatter.parse(dateTime)
    }

}