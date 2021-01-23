package me.nutyworks.syosetuviewerv2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import me.nutyworks.syosetuviewerv2.R
import me.nutyworks.syosetuviewerv2.databinding.RowSearchResultBinding
import me.nutyworks.syosetuviewerv2.ui.SearchResultViewModel

class SearchResultAdapter(val viewModel: SearchResultViewModel) :
    RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {

    lateinit var context: Context
    override fun getItemViewType(position: Int): Int = R.layout.row_search_result

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: RowSearchResultBinding =
            DataBindingUtil.inflate(layoutInflater, viewType, parent, false)

        return SearchResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(viewModel, position)
    }

    override fun getItemCount(): Int {
        return viewModel.searchResults.value?.size ?: 0
    }

    class SearchResultViewHolder(
        private val binding: RowSearchResultBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: SearchResultViewModel, position: Int) {
            binding.viewModel = viewModel
            binding.position = position
        }
    }
}
