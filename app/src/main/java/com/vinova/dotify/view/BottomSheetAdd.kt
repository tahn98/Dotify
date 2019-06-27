package com.vinova.dotify.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vinova.dotify.R
import com.vinova.dotify.adapter.MusicAdapter
import com.vinova.dotify.model.Music
import com.vinova.dotify.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.bottom_sheet_fragment.*

class BottomSheetAdd(var music : Music, var adapter: MusicAdapter, var list : MutableList<Music>) : BottomSheetDialogFragment() {

    private var mViewModel: UserViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        return inflater.inflate(R.layout.bottom_sheet_fragment_add, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bts_add.setOnClickListener {
            Toast.makeText(context, "Add ${music.name}", Toast.LENGTH_SHORT).show()
            mViewModel?.likeMusic("HkWQty0QRTh9eEaBdCngJQuU1uf2", music, false)
            this.dismiss()
        }

        bts_share.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.putExtra(Intent.EXTRA_SUBJECT, music.name)
            share.putExtra(Intent.EXTRA_TEXT, music.musicURL)
            startActivity(Intent.createChooser(share, "Share link"))
            this.dismiss()
        }
    }
}