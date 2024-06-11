package com.ps.mui3.tip

import android.os.Bundle
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import com.pluscubed.recyclerfastscroll.RecyclerFastScroller
import com.ps.mui3.tip.icons.IconsAdapter
import com.ps.mui3.tip.icons.IconsHelper
import com.ps.mui3.tip.utils.DebounceJob

class MainActivity : AppCompatActivity() {

    private val searchBar: SearchBar by lazy { findViewById(R.id.search_bar) }
    private val searchView: SearchView by lazy { findViewById(R.id.search_view) }
    private val noSearchResultsView: TextView by lazy { findViewById(R.id.no_search_results) }
    private val searchResultCount: TextView by lazy { findViewById(R.id.search_result_count) }
    private val recyclerView: RecyclerView by lazy { findViewById(R.id.recycler_view) }
    private val searchRecyclerView: RecyclerView by lazy { findViewById(R.id.search_recycler_view) }
    private val fastScroller: RecyclerFastScroller by lazy { findViewById(R.id.fast_scroller) }

    private val debounceJob = DebounceJob()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val allIcons = IconsHelper.getIconsList(this, true)
        val allIconsAdapter = IconsAdapter(this, allIcons)
        val searchIconsAdapter = IconsAdapter(this, allIcons)

        recyclerView.layoutManager = GridLayoutManager(this, GRID_COUNT)
        recyclerView.adapter = allIconsAdapter

        searchRecyclerView.layoutManager = GridLayoutManager(this, GRID_COUNT)
        searchRecyclerView.adapter = searchIconsAdapter
        searchIconsAdapter.updateList("")

        searchBar.hint = getString(R.string.search_bar_hint, allIcons.size)

        searchView.setupWithSearchBar(searchBar)
        fastScroller.attachRecyclerView(recyclerView)

        searchView.editText.doOnTextChanged { text, _, _, _ ->
            fun performSearch(filter: String) {
                val filteredCount = searchIconsAdapter.updateList(filter)
                noSearchResultsView.isVisible = filteredCount == 0
                searchResultCount.text = getString(R.string.search_result_count, filteredCount)
            }

            if (text.isNullOrEmpty()) {
                performSearch("")
            } else {
                debounceJob.debounce(DEBOUNCE_DELAY, lifecycleScope) {
                    performSearch(text.toString())
                }.invoke()
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (searchView.isShowing) searchView.hide()
                else finish()
            }
        })
    }

    companion object {
        private const val DEBOUNCE_DELAY = 500L
        private const val GRID_COUNT = 4
    }
}