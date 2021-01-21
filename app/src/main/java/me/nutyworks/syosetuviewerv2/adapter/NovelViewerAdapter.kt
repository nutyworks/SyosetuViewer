package me.nutyworks.syosetuviewerv2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import me.nutyworks.syosetuviewerv2.R
import me.nutyworks.syosetuviewerv2.databinding.RowTranslationWrapperBinding
import me.nutyworks.syosetuviewerv2.ui.NovelViewerViewModel

class NovelViewerAdapter(private val viewModel: NovelViewerViewModel) :
    RecyclerView.Adapter<NovelViewerAdapter.TranslationWrapperViewHolder>() {

    companion object {
        private const val TAG = "NovelViewerAdapter"
    }

    lateinit var context: Context
    override fun getItemViewType(position: Int): Int = R.layout.row_translation_wrapper

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TranslationWrapperViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: RowTranslationWrapperBinding =
            DataBindingUtil.inflate(layoutInflater, viewType, parent, false)

        return TranslationWrapperViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TranslationWrapperViewHolder, position: Int) {
        holder.bind(viewModel, position)

        holder.binding.root.setOnClickListener {
            viewModel.toggleViewerSettings()
        }

        holder.binding.root.setOnLongClickListener {
            viewModel.toggleTextLanguageType(position)
            holder.binding.invalidateAll()
            true
        }
    }

    override fun getItemCount(): Int {
        return viewModel.novelBody.get()?.mainTextWrappers?.size ?: 0
    }

    class TranslationWrapperViewHolder(
        val binding: RowTranslationWrapperBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: NovelViewerViewModel, position: Int) {
            binding.viewModel = viewModel
            binding.position = position
        }
    }
}
