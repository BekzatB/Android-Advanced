package com.example.lab3.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.lab2.ui.NowPlayingMoviesAdapter
import com.example.lab2.ui.PopularMoviesAdapter
import com.example.lab3.R
import com.example.lab3.utils.PaginationListener
import kotlinx.android.synthetic.main.fragment_movies.*
import org.koin.android.ext.android.inject

class MoviesFragment : Fragment() {

    private val viewModel: MoviesFragmentViewModel by inject()


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateComponent()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setListeners()
        setData()
    }

    private fun setAdapter() {
        val popularMoviesLinearLayoutManager = LinearLayoutManager(
            context,
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
                viewModel.getPopularMovies(page = popularMoviesCurrentPage)
            }

            override fun isLoading(): Boolean = popularMoviesIsLoading
            override fun isLastPage(): Boolean = popularMoviesIsLastPage
        })
        popularMoviesAdapter = PopularMoviesAdapter()
        popularMoviesRecyclerView.adapter = popularMoviesAdapter

        val nowPlayingMoviesLinearLayoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        nowPlayingMoviesRecyclerView.layoutManager = nowPlayingMoviesLinearLayoutManager
        nowPlayingMoviesRecyclerView.addOnScrollListener(object :
            PaginationListener(nowPlayingMoviesLinearLayoutManager) {
            override fun loadMoreItems() {
                nowPlayingMoviesIsLoading = true
                nowPlayingMoviesCurrentPage++
                viewModel.getNowPlayingMovies(page = nowPlayingMoviesCurrentPage)
            }

            override fun isLastPage(): Boolean = nowPlayingMoviesIsLastPage
            override fun isLoading(): Boolean = nowPlayingMoviesIsLoading
        })
        nowPlayingMoviesAdapter = NowPlayingMoviesAdapter()
        nowPlayingMoviesRecyclerView.adapter = nowPlayingMoviesAdapter

    }

    private fun setListeners() {
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            popularMoviesProgressBar.visibility = View.VISIBLE
            nowPlayingMoviesProgressBar.visibility = View.VISIBLE
            popularMoviesAdapter?.clearAll()
            nowPlayingMoviesAdapter?.clearAll()
            viewModel.getNowPlayingMovies()
            viewModel.getPopularMovies()
        }
    }

    private fun setData() {
        popularMoviesAdapter?.clearAll()
        nowPlayingMoviesAdapter?.clearAll()

        viewModel.getNowPlayingMovies()
        viewModel.getPopularMovies()

        viewModel.liveData.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is State.ShowLoading -> {
                    nowPlayingMoviesProgressBar.visibility = View.VISIBLE
                    popularMoviesProgressBar.visibility = View.VISIBLE
                }
                is State.HideLoading -> {
                    nowPlayingMoviesProgressBar.visibility = View.GONE
                    popularMoviesProgressBar.visibility = View.GONE
                }
                is State.PopularMoviesResult -> {
                    popularMoviesItemCount = result.result.movies.size
                    if (popularMoviesCurrentPage != PaginationListener.PAGE_START) {
                        popularMoviesAdapter?.removeLoading()
                    }
                    popularMoviesAdapter?.addItems(result.result.movies)
                    if (popularMoviesCurrentPage < result.result.totalPages) {
                        popularMoviesAdapter?.addLoading()
                    } else {
                        popularMoviesIsLastPage = true
                    }
                    popularMoviesIsLoading = false
                }
                is State.NowPlayingMoviesResult -> {
                    nowPlayingMoviesItemCount = result.result.movies.size
                    if (nowPlayingMoviesCurrentPage != PaginationListener.PAGE_START) {
                        nowPlayingMoviesAdapter?.removeLoading()
                    }
                    nowPlayingMoviesAdapter?.addItems(result.result.movies)
                    if (nowPlayingMoviesCurrentPage < result.result.totalPages) {
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