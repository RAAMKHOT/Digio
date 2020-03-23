package com.telstra

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.telstra.helper.CommonUtils
import com.telstra.models.Rows
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.net.ssl.SSLHandshakeException

class LandingActivity : AppCompatActivity(), ItemListView {

    private lateinit var factListAdapter: ItemListAdapter
    private lateinit var shimmerContainer: ShimmerFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        shimmerContainer = shimmer_view_container as ShimmerFrameLayout
        shimmerContainer.startShimmerAnimation()

        factListAdapter = ItemListAdapter(ArrayList<Rows>())
        val mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(applicationContext)
        factsRecyclerView.layoutManager = mLayoutManager
        factsRecyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        factsRecyclerView.adapter = factListAdapter

        swipeToRefresh.setOnRefreshListener {
            getFactData()
        }

        setTitle("")
        getFactData()
    }

    private fun getFactData() {
        val repository = CommonUtils.getSOService()

        CompositeDisposable().add(
            repository.getFacts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    { result ->
                        if (result != null) {
                            setTitle(result.title)
                            initListView(result.rows as ArrayList<Rows>)
                        }
                    },
                    { error ->
                        if (error is SSLHandshakeException) {
                            Toast.makeText(
                                this,
                                "We are getting SSL Handshake Exception, We need to add SSL cert in Server side \n Please use the lower version device.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        shimmerContainer.stopShimmerAnimation()
                        if (swipeToRefresh.isRefreshing) {
                            swipeToRefresh.isRefreshing = false
                        }
                    })
        )
    }

    private fun initListView(factList: ArrayList<Rows>) {
        factListAdapter.setList(factList)

        shimmerContainer.stopShimmerAnimation()

        if (swipeToRefresh.isRefreshing) {
            swipeToRefresh.isRefreshing = false
            Toast.makeText(
                this,
                "Refreshed", Toast.LENGTH_SHORT
            ).show()
        }
    }
}
