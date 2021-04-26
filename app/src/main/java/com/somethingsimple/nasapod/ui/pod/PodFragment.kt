package com.somethingsimple.nasapod.ui.pod

import android.content.Intent
import android.os.Bundle
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
        binding.chipRandom.setOnClickListener { viewModel.getRandom() }
        binding.chipYesterday.setOnClickListener { viewModel.getYesterday() }
        binding.chipToday.setOnClickListener { viewModel.sendServerRequest() }
    }

    private fun renderData(data: PictureOfTheDayResponse) {
        when (data) {
            is PictureOfTheDayResponse.Success -> {
                data.serverResponse.let {
                    val url = it.url
                    if (url.isNullOrEmpty()) {
                        //Отобразите ошибку
                        //showError("Сообщение, что ссылка пустая")
                    } else {
                        //Отобразите фото
                        //showSuccess()
                        //Coil в работе: достаточно вызвать у нашего ImageView
                        //нужную extension-функцию и передать ссылку и заглушки для placeholder
                        binding.imageView.load(url) {
                            lifecycle(this@PodFragment)
                            error(R.drawable.ic_load_error_24)
                            placeholder(R.drawable.ic_baseline_image_not_supported_24)
                        }
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
}