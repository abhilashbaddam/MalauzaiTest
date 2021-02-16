package com.malauzai.android.app.test.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.malauzai.android.app.test.R
import com.malauzai.android.app.test.model.PhotosItem
import com.squareup.picasso.Picasso

internal class SearchListAdapter(private val ctx: Context, private val list: List<PhotosItem>): RecyclerView.Adapter<SearchListAdapter.SearchViewHolder>() {
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {

        with(list[position]){
            holder.name.text = rover?.name
            holder.launchDate.text = rover?.launch_date
            holder.landingDate.text = rover?.landing_date

            //Hack: Replacing https with http.
            var img_url = img_src?.replace("http", "https")

            Picasso.with(ctx).load(img_url)
                .placeholder(R.drawable.place_holder)
                .into(holder.image)
            Picasso.with(ctx).setLoggingEnabled(true)
            holder.itemView.setOnClickListener{ Toast.makeText(ctx, ctx.getString(R.string.item_click_message), Toast.LENGTH_SHORT).show() }
        }
    }

    override fun getItemCount(): Int {
          return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemView = LayoutInflater.from(ctx).inflate(R.layout.search_list_item, parent, false)
        return SearchViewHolder(itemView)
    }


    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.image)
        var name: TextView = itemView.findViewById(R.id.rover_name)
        var launchDate: TextView = itemView.findViewById(R.id.rover_launch_date)
        var landingDate: TextView = itemView.findViewById(R.id.rover_landing_date)
    }

}