package me.nutyworks.syosetuviewerv2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import me.nutyworks.syosetuviewerv2.R
import me.nutyworks.syosetuviewerv2.databinding.RowChapterItemBinding
import me.nutyworks.syosetuviewerv2.databinding.RowEpisodeItemBinding
import me.nutyworks.syosetuviewerv2.ui.novellist.NovelViewModel

class NovelDetailAdapter(private val viewModel: NovelViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val CHAPTER = R.layout.row_chapter_item
        private const val EPISODE = R.layout.row_episode_item
    }

    override fun getItemViewType(position: Int): Int =
        when (viewModel.selectedNovelBodies.value?.get(position)?.isChapter) {
            true -> CHAPTER
            else -> EPISODE
        }

    class ChapterViewHolder(
        private val binding: RowChapterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: NovelViewModel, position: Int) {
            binding.viewModel = viewModel
            binding.position = position
        }
    }

    class EpisodeViewHolder(
        private val binding: RowEpisodeItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: NovelViewModel, position: Int) {
            binding.viewModel = viewModel
            binding.position = position
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            CHAPTER -> ChapterViewHolder(
                DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
            )
            EPISODE -> EpisodeViewHolder(
                DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
            )
            else -> throw IllegalStateException("viewType is neither CHAPTER nor EPISODE")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            CHAPTER -> (holder as ChapterViewHolder).bind(viewModel, position)
            EPISODE -> (holder as EpisodeViewHolder).bind(viewModel, position)
            else -> throw IllegalStateException("viewType is neither CHAPTER nor EPISODE")
        }
    }

    override fun getItemCount(): Int {
        return viewModel.selectedNovelBodies.value?.size ?: 0
    }
}
