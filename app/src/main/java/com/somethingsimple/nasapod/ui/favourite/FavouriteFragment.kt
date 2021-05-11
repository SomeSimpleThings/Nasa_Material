package com.somethingsimple.nasapod.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.somethingsimple.nasapod.databinding.FavouriteFragmentBinding

class FavouriteFragment : Fragment() {

    companion object {
        fun newInstance() = FavouriteFragment()
    }

    private var _binding: FavouriteFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { FavouriteAdapter() }

    private val viewModel: FavouriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FavouriteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView?.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = adapter
        }
        viewModel.getAllFavourites()
            .observe(viewLifecycleOwner, {
                adapter.setFavourites(it)
            })
    }

}