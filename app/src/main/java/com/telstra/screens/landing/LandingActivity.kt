package com.telstra.screens.landing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.shimmer.ShimmerFrameLayout
import com.telstra.R
import com.telstra.helper.CommonUtils
import com.telstra.models.Base
import com.telstra.models.Rows
import kotlinx.android.synthetic.main.activity_landing.*

class LandingActivity : AppCompatActivity(), LandingView {
    private lateinit var factListAdapter: FactsListAdapter
    private lateinit var shimmerContainer: ShimmerFrameLayout
    private var landingPresenter: LandingPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        init()
    }

    override fun init() {
        shimmerContainer = shimmer_view_container as ShimmerFrameLayout
        shimmerContainer.startShimmerAnimation()
        landingPresenter = LandingPresenter(this)
        factListAdapter = FactsListAdapter(ArrayList<Rows>(), this)
        val mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(applicationContext)
        factsRecyclerView.layoutManager = mLayoutManager
        factsRecyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        factsRecyclerView.adapter = factListAdapter

        swipeToRefresh.setOnRefreshListener {
            landingPresenter!!.getFactData()
        }

        setTitle("")
        landingPresenter!!.getFactData()
    }

    private fun initListView(factList: ArrayList<Rows>) {
        factListAdapter.setList(factList)
        shimmerContainer.stopShimmerAnimation()
        if (swipeToRefresh.isRefreshing) {
            swipeToRefresh.isRefreshing = false
            CommonUtils.showToast(this, "Refreshed")
        }
    }

    override fun onSuccess(factList: Base) {
        setTitle(factList.title)
        initListView(factList.rows as ArrayList<Rows>)
    }

    override fun onError(error: String) {
        CommonUtils.showToast(this, error)

        shimmerContainer.stopShimmerAnimation()
        if (swipeToRefresh.isRefreshing) {
            swipeToRefresh.isRefreshing = false
        }
    }
}
