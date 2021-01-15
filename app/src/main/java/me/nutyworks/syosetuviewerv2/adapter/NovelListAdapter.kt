package me.nutyworks.syosetuviewerv2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import me.nutyworks.syosetuviewerv2.R
import me.nutyworks.syosetuviewerv2.databinding.RowNovelItemBinding
import me.nutyworks.syosetuviewerv2.ui.novellist.NovelListViewModel

class NovelListAdapter(private val viewModel: NovelListViewModel) :
    RecyclerView.Adapter<NovelListAdapter.NovelViewHolder>() {

    override fun getItemViewType(position: Int): Int = R.layout.row_novel_item

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NovelViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: RowNovelItemBinding =
            DataBindingUtil.inflate(layoutInflater, viewType, parent, false)

        return NovelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NovelViewHolder, position: Int) {
        holder.bind(viewModel, position)
    }

    override fun getItemCount(): Int {
        return viewModel.novels.value?.size ?: 0
    }

    class NovelViewHolder(
        private val binding: RowNovelItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: NovelListViewModel, position: Int) {
            binding.viewModel = viewModel
            binding.novel = viewModel.novels.value?.get(position)
        }
    }
}
