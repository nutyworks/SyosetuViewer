package me.nutyworks.syosetuviewerv2.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import me.nutyworks.syosetuviewerv2.R
import me.nutyworks.syosetuviewerv2.data.ImageWrapper
import me.nutyworks.syosetuviewerv2.data.TranslationWrapper
import me.nutyworks.syosetuviewerv2.databinding.RowImageWrapperBinding
import me.nutyworks.syosetuviewerv2.databinding.RowTranslationWrapperBinding
import me.nutyworks.syosetuviewerv2.ui.NovelViewerViewModel

class NovelViewerAdapter(private val viewModel: NovelViewerViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TAG = "NovelViewerAdapter"

        private const val TEXT = R.layout.row_translation_wrapper
        private const val IMAGE = R.layout.row_image_wrapper
    }

    lateinit var context: Context

    override fun getItemViewType(position: Int): Int =
        when (viewModel.novelBody.get()?.mainTextWrappers?.get(position)) {
            is TranslationWrapper -> TEXT
            is ImageWrapper -> IMAGE
            else -> throw IllegalStateException("Main text is neither text nor image")
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TEXT -> TranslationWrapperViewHolder(
                DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
            )
            IMAGE -> ImageWrapperViewHolder(
                DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
            )
            else -> throw IllegalStateException("Main text is neither text nor image")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TranslationWrapperViewHolder -> holder.bind(viewModel, position)
            is ImageWrapperViewHolder -> holder.bind(viewModel, position)
            else -> throw IllegalStateException("Main text is neither text nor image")
        }
    }

    override fun getItemCount(): Int {
        return viewModel.novelBody.get()?.mainTextWrappers?.size ?: 0
    }

    class TranslationWrapperViewHolder(
        val binding: RowTranslationWrapperBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: NovelViewerViewModel, position: Int) {
            binding.viewModel = viewModel
            binding.position = position

            binding.root.setOnClickListener {
                viewModel.toggleViewerSettings()
            }

            binding.root.setOnLongClickListener {
                viewModel.toggleTextLanguageType(position)
                binding.invalidateAll()
                true
            }
        }
    }

    class ImageWrapperViewHolder(
        val binding: RowImageWrapperBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: NovelViewerViewModel, position: Int) {
            val uri =
                (viewModel.novelBody.get()?.mainTextWrappers?.get(position) as ImageWrapper).uri
            binding.viewModel = viewModel
            binding.position = position

            binding.progressBar.visibility = View.GONE

            binding.btnLoad.setOnClickListener {
                binding.progressBar.visibility = View.VISIBLE
                binding.btnLoad.visibility = View.GONE
                Glide.with(binding.root)
                    .load("https:$uri")
                    .listener(
                        object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding.btnLoad.visibility = View.VISIBLE
                                binding.progressBar.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding.progressBar.visibility = View.GONE
                                return false
                            }
                        }
                    )
                    .centerInside()
                    .override(Target.SIZE_ORIGINAL)
                    .into(binding.imageHolder)
            }
        }
    }
}
