package me.nutyworks.syosetuviewerv2.ui.main.fragment.novel.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import me.nutyworks.syosetuviewerv2.R
import me.nutyworks.syosetuviewerv2.databinding.RowNovelItemBinding
import me.nutyworks.syosetuviewerv2.ui.main.fragment.novel.NovelViewModel

class NovelListAdapter(private val viewModel: NovelViewModel) :
    RecyclerView.Adapter<NovelListAdapter.NovelViewHolder>() {

    lateinit var context: Context
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

    fun deleteItem(position: Int) {
        viewModel.deleteNovelIndex(position)
    }

    class NovelViewHolder(
        private val binding: RowNovelItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: NovelViewModel, position: Int) {
            binding.viewModel = viewModel
            binding.position = position
        }
    }
}
