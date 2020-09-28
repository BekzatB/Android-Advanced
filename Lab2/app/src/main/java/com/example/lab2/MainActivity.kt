package com.example.lab2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.lab2.ui.NowPlayingMoviesAdapter
import com.example.lab2.ui.PopularMoviesAdapter
import com.example.lab2.utils.OnItemClickListener
import com.example.lab2.utils.PaginationListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpAdapter()
        setData()
    }


    private lateinit var popularMoviesRecyclerView: RecyclerView
    private lateinit var nowPlayingMoviesRecyclerView: RecyclerView
    private lateinit var nowPlayingMoviesProgressBar: ProgressBar
    private lateinit var popularMoviesProgressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private var popularMoviesAdapter: PopularMoviesAdapter? = null
    private var nowPlayingMoviesAdapter: NowPlayingMoviesAdapter? = null

    private var popularMoviesCurrentPage = PaginationListener.PAGE_START
    private var popularMoviesIsLastPage = false
    private var popularMoviesIsLoading = false
    private var popularMoviesItemCount = 0

    private var nowPlayingMoviesCurrentPage = PaginationListener.PAGE_START
    private var nowPlayingMoviesIsLastPage = false
    private var nowPlayingMoviesIsLoading = false
    private var nowPlayingMoviesItemCount = 0

    private fun onCreateComponent() {
        popularMoviesAdapter = PopularMoviesAdapter()
        nowPlayingMoviesAdapter = NowPlayingMoviesAdapter()
    }


    private fun bindView()  {
        popularMoviesRecyclerView = findViewById(R.id.popularMoviesRecyclerView)
        nowPlayingMoviesRecyclerView = findViewById(R.id.nowPlayingMoviesRecyclerView)
        nowPlayingMoviesProgressBar = findViewById(R.id.nowPlayingMoviesProgressBar)
        popularMoviesProgressBar = findViewById(R.id.popularMoviesProgressBar)
        swipeRefreshLayout = findViewById(R.id.moviesFragmentSFL)

        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            popularMoviesProgressBar.visibility = View.VISIBLE
            nowPlayingMoviesProgressBar.visibility = View.VISIBLE
            popularMoviesAdapter?.clearAll()
            nowPlayingMoviesAdapter?.clearAll()
        }
    }

    private fun setUpAdapter() {
        val popularMoviesLinearLayoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        )
        popularMoviesRecyclerView.layoutManager = popularMoviesLinearLayoutManager

        popularMoviesRecyclerView.setHasFixedSize(true)
        popularMoviesRecyclerView.addOnScrollListener(object :
                PaginationListener(popularMoviesLinearLayoutManager) {
            override fun loadMoreItems() {
                popularMoviesIsLoading = true
                popularMoviesCurrentPage++
                moviesFragmentViewModel.getPopularMovies(page = popularMoviesCurrentPage)
            }

            override fun isLoading(): Boolean = popularMoviesIsLoading
            override fun isLastPage(): Boolean = popularMoviesIsLastPage
        })
        popularMoviesAdapter = PopularMoviesAdapter()
        popularMoviesRecyclerView.adapter = popularMoviesAdapter
        val nowPlayingMoviesLinearLayoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.HORIZONTAL,
                false
        )
        nowPlayingMoviesRecyclerView.layoutManager = nowPlayingMoviesLinearLayoutManager
        nowPlayingMoviesRecyclerView.addOnScrollListener(object :
                PaginationListener(nowPlayingMoviesLinearLayoutManager) {
            override fun loadMoreItems() {
                nowPlayingMoviesIsLoading = true
                nowPlayingMoviesCurrentPage++
                moviesFragmentViewModel.getNowPlayingMovies(page = nowPlayingMoviesCurrentPage)
            }

            override fun isLastPage(): Boolean = nowPlayingMoviesIsLastPage
            override fun isLoading(): Boolean = nowPlayingMoviesIsLoading
        })
        nowPlayingMoviesAdapter = NowPlayingMoviesAdapter()
        nowPlayingMoviesRecyclerView.adapter = nowPlayingMoviesAdapter

    }

    private fun setData() {
        popularMoviesAdapter?.clearAll()
        nowPlayingMoviesAdapter?.clearAll()

        moviesFragmentViewModel.liveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is MoviesFragmentViewModel.State.ShowLoading -> {
                    nowPlayingMoviesProgressBar.visibility = View.VISIBLE
                    popularMoviesProgressBar.visibility = View.VISIBLE
                }
                is MoviesFragmentViewModel.State.HideLoading -> {
                    nowPlayingMoviesProgressBar.visibility = View.GONE
                    popularMoviesProgressBar.visibility = View.GONE
                }
                is MoviesFragmentViewModel.State.PopularMovies -> {
                    popularMoviesItemCount = result.result.size
                    if (popularMoviesCurrentPage != PaginationListener.PAGE_START) {
                        popularMoviesAdapter?.removeLoading()
                    }
                    popularMoviesAdapter?.addItems(result.result)
                    if (popularMoviesCurrentPage < result.totalPages) {
                        popularMoviesAdapter?.addLoading()
                    } else {
                        popularMoviesIsLastPage = true
                    }
                    popularMoviesIsLoading = false
                }
                is MoviesFragmentViewModel.State.NowPlayingMovies -> {
                    nowPlayingMoviesItemCount = result.result.size
                    if (nowPlayingMoviesCurrentPage != PaginationListener.PAGE_START) {
                        nowPlayingMoviesAdapter?.removeLoading()
                    }
                    nowPlayingMoviesAdapter?.addItems(result.result)
                    if (nowPlayingMoviesCurrentPage < result.totalPages) {
                        nowPlayingMoviesAdapter?.addLoading()
                    } else {
                        nowPlayingMoviesIsLastPage = true
                    }
                    nowPlayingMoviesIsLoading = false
                }
            }
        })
    }
}