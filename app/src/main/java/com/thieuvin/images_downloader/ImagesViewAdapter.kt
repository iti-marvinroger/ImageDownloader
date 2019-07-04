package com.thieuvin.images_downloader

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_image.view.imageView


class ImagesViewAdapter(context: Context, images: List<String>) : ArrayAdapter<String>(context, 0, images) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        // Get the data item for this position
        val imageUrl = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false)
        }

        Picasso.get().load(imageUrl).into(convertView!!.imageView)


        return convertView
    }
}