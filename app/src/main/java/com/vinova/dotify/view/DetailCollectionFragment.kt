package com.vinova.dotify.view


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.vinova.dotify.R
import com.vinova.dotify.adapter.MusicAdapter
import com.vinova.dotify.databinding.FragmentDetailCollectionBinding
import com.vinova.dotify.model.Music
import com.vinova.dotify.model.MusicCollection
import com.vinova.dotify.utils.BaseConst
import com.vinova.dotify.viewmodel.UserViewModel
import jp.wasabeef.glide.transformations.BlurTransformation


class DetailCollectionFragment : Fragment() {
    private lateinit var collection: MusicCollection
    private var type: String? = null
    private var listMusic: MutableList<Music> = arrayListOf()
    private lateinit var musicAdapter: MusicAdapter
    private var mViewModel: UserViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //get Arguments
        val bundle = this.arguments
        if (bundle != null) {
            collection = bundle.getSerializable(BaseConst.passMusicCollection) as MusicCollection
            type = bundle.getString("Type")
            listMusic = collection.listMusic?.values?.toList() as MutableList<Music>
        }
        mViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        val binding = DataBindingUtil.inflate<FragmentDetailCollectionBinding>(
            inflater,
            R.layout.fragment_detail_collection,
            container,
            false
        )
        Glide
            .with(this)
            .load(collection.photoURL)
            .fitCenter()
            .apply(RequestOptions.bitmapTransform(BlurTransformation(15, 3)))
            .thumbnail(0.1f)
            .into(binding.albumBackgroundImd)

        Glide
            .with(this)
            .load(collection.photoURL)
            .centerCrop()
            .into(binding.photoAlbumImg)
        binding.type.text = type
        binding.albumName.text = collection.name
        var action = false

        mViewModel?.isLike("HkWQty0QRTh9eEaBdCngJQuU1uf2", collection, type!!)
            ?.observe(this, Observer<Boolean> { data ->
                run {
                    action = if (data) {
                        binding.likeButton.setImageResource(R.drawable.ic_heart_song_btn_selected)
                        false
                    } else {
                        binding.likeButton.setImageResource(R.drawable.ic_heart_song_btn)
                        true
                    }
                }
            })
        binding.likeButton.setOnClickListener {
            action = if (action) {
                binding.likeButton.setImageResource(R.drawable.ic_heart_song_btn_selected)
                false
            } else {
                binding.likeButton.setImageResource(R.drawable.ic_heart_song_btn)
                true
            }
            mViewModel?.likeCollection("HkWQty0QRTh9eEaBdCngJQuU1uf2", collection, type!!, action)

        }
        binding.shareButton.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.putExtra(Intent.EXTRA_SUBJECT, collection.name)
            share.putExtra(Intent.EXTRA_TEXT, collection.listMusic!!.values.toList()[0].musicURL)
            startActivity(Intent.createChooser(share, "Share link"))
        }
        binding.listMusicContainer.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.playButton.setOnClickListener {
            (activity as MainScreen).play(0, listMusic)
        }
        musicAdapter = MusicAdapter(context!!, listMusic) { music: Music, pos: Int ->
            itemClicked(music, pos)
        }

        binding.listMusicContainer.adapter = musicAdapter
        musicAdapter.notifyDataSetChanged()
        binding.imgAlbumBack.setOnClickListener {
            (activity as MainScreen).onBackPressed()
        }
        setupToolBar()
        return binding.root
    }

    private fun itemClicked(music: Music, pos: Int?) {
        (activity as MainScreen).play(pos!! - 1, listMusic)
    }

    private fun setupToolBar() {
        (activity as MainScreen).hideToolbar()
    }

}
