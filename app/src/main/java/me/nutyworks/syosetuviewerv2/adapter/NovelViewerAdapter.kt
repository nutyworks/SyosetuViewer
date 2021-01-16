package me.nutyworks.syosetuviewerv2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import me.nutyworks.syosetuviewerv2.R
import me.nutyworks.syosetuviewerv2.databinding.RowNovelItemBinding
import me.nutyworks.syosetuviewerv2.databinding.RowTranslationWrapperBinding
import me.nutyworks.syosetuviewerv2.ui.NovelViewerViewModel
import me.nutyworks.syosetuviewerv2.ui.novellist.NovelViewModel

class NovelViewerAdapter(private val viewModel: NovelViewerViewModel) :
    RecyclerView.Adapter<NovelViewerAdapter.TranslationWrapperViewHolder>() {

    lateinit var context: Context
    override fun getItemViewType(position: Int): Int = R.layout.row_translation_wrapper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslationWrapperViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: RowTranslationWrapperBinding =
            DataBindingUtil.inflate(layoutInflater, viewType, parent, false)

        return TranslationWrapperViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TranslationWrapperViewHolder, position: Int) {
        holder.bind(viewModel, position)
    }

    override fun getItemCount(): Int {
        return viewModel.novelMainText.value?.size ?: 0
    }

    class TranslationWrapperViewHolder(
        private val binding: RowTranslationWrapperBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: NovelViewerViewModel, position: Int) {
            binding.viewModel = viewModel
            binding.position = position
        }
    }
}
