package me.nutyworks.syosetuviewerv2.ui.searchresult

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import me.nutyworks.syosetuviewerv2.R
import me.nutyworks.syosetuviewerv2.databinding.RowSearchResultBinding

class SearchResultAdapter(val viewModel: SearchResultViewModel) :
    RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {

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

            viewModel.searchResults.value?.get(position)?.keywords?.forEach { (_, translated) ->
                binding.flKeywordsWrapper.addView(
                    TextView(binding.root.context).apply {
                        text = translated
                        background = ContextCompat.getDrawable(
                            binding.root.context,
                            R.drawable.keyword_border
                        )
                    }
                )
            }
        }
    }
}
