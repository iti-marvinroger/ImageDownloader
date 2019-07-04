package com.thieuvin.images_downloader

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thieuvin.images_downloader.entities.ScrapingResult
import kotlinx.android.synthetic.main.item_result.view.*

class ResultViewAdapter(private val dataset: List<ScrapingResult>) :
    RecyclerView.Adapter<ResultViewAdapter.ViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_result, parent, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = dataset[position]
        holder.view.textView.text = result.url
        holder.view.imagesList.adapter = ImagesViewAdapter(holder.view.context, result.imageUrls)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataset.size
}
