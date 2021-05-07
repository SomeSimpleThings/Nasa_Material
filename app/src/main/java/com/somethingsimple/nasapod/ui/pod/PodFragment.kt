package com.somethingsimple.nasapod.ui.pod

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.transition.*
import android.view.*
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.api.load
import com.somethingsimple.nasapod.R
import com.somethingsimple.nasapod.WIKI_URL
import com.somethingsimple.nasapod.data.PictureOfTheDayResponse
import com.somethingsimple.nasapod.databinding.FragmentPodBinding


class PodFragment : Fragment() {

    private val viewModel: PodViewModel by viewModels()
    private var _binding: FragmentPodBinding? = null
    private var liked = false

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = PodFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData()
            .observe(viewLifecycleOwner, { renderData(it) })
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = "$WIKI_URL${binding.inputEditText.text.toString()}".toUri()
            })
        }
        setupChipsBehavior()

    }

    private fun setupChipsBehavior() {
//        binding.chipGroup.apply {
//            setOnCheckedChangeListener { chipGroup: ChipGroup, i: Int ->
//                when (i) {
//                    binding.chipRandom.id -> viewModel.getRandom()
//                    binding.chipYesterday.id -> viewModel.getYesterday()
//                    binding.chipToday.id -> viewModel.getToday()
//                }
//            }
//        }
        binding.chipRandom.setOnClickListener {
            binding.chipYesterday.isChecked = false
            binding.chipToday.isChecked = false
            viewModel.getRandom()
        }
        binding.chipYesterday.setOnClickListener {
            binding.chipRandom.isChecked = false
            binding.chipToday.isChecked = false
            viewModel.getYesterday()
        }
        binding.chipToday.setOnClickListener {
            binding.chipRandom.isChecked = false
            binding.chipYesterday.isChecked = false
            viewModel.getToday()
        }
        binding.favIcon.apply {
            setOnClickListener {
                if (liked) setBackgroundResource(R.drawable.fav_click_like)
                else setBackgroundResource(R.drawable.fav_click_dislike)
                liked = !liked
                val animation: AnimationDrawable = background as AnimationDrawable
                animation.start()
            }
        }
    }

    private fun renderData(data: PictureOfTheDayResponse) {
        var visible = false
        setTextVisibility(visible)
        binding.imageView.animate().alpha(0.1f)
            .setDuration(300)
        when (data) {
            is PictureOfTheDayResponse.Success -> {
                data.serverResponse.let {
                    val url = it.url
                    if (!url.isNullOrEmpty()) {
                        binding.imageView.load(url) {
                            lifecycle(this@PodFragment)
                            error(R.drawable.ic_load_error_24)
                            placeholder(R.drawable.ic_baseline_image_not_supported_24)
                        }
                        binding.imageView.animate().alpha(1f)
                            .setDuration(300)
                        visible = !visible;
                        setTextVisibility(visible)
                        binding.bottomSheetDescriptionHeader.text = it.title
                        binding.bottomSheetDescription.text = it.explanation
                    }
                }

            }
            is PictureOfTheDayResponse.Loading -> {
                binding.imageView.setImageResource(R.drawable.ic_baseline_update_24)
            }

            is PictureOfTheDayResponse.Error -> {
                binding.imageView.setImageResource(R.drawable.ic_load_error_24)
            }
        }
    }

    private fun setTextVisibility(visible: Boolean) {
        TransitionManager.beginDelayedTransition(
            binding.mainMotionContainer,
            Slide(Gravity.BOTTOM)
        )
        binding.bottomSheetDescriptionHeader.setVisibility(if (visible) View.VISIBLE else View.GONE)
        binding.favIcon.setVisibility(if (visible) View.VISIBLE else View.GONE)
        binding.bottomSheetDescription.setVisibility(if (visible) View.VISIBLE else View.GONE)
    }
}