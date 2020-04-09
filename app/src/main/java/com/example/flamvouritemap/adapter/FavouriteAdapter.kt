package com.example.flamvouritemap.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.example.flamvouritemap.R
import com.example.flamvouritemap.placeModel.Result
import com.example.flamvouritemap.util.DebugLogger


class FavouriteAdapter(
    private val favouriteLocal: List<Result>,
    private val context: Context,
    private val userClickListener: UserClickListener
) : RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>() {

    interface UserClickListener {
        fun unfavouriteButton(result: Result)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourite_list_item, parent, false)
        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        holder.favouriteName.text = favouriteLocal[position].name
        DebugLogger.logDebug("Bindview holder names: "+favouriteLocal[position].name)
        Glide.with(context)
            .asBitmap()
            .load(favouriteLocal[position].icon)
            .placeholder(R.drawable.ic_broken_image_black_24dp)
            .into(holder.imageView)
        holder.unfavouriteButton.setOnClickListener {
            userClickListener.unfavouriteButton(
                favouriteLocal[position]
            )
        }
    }


    override fun getItemCount(): Int {
        return favouriteLocal.size
    }

    inner class FavouriteViewHolder(itemView: View) :
        ViewHolder(itemView) {
        @BindView(R.id.favouriteName)
        lateinit var favouriteName: TextView
        @BindView(R.id.imageView)
        lateinit var imageView: ImageView
        @BindView(R.id.unfavouriteButton)
        lateinit var unfavouriteButton:Button

        init {
            ButterKnife.bind(this, itemView)
        }
    }




}