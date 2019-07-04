package com.thieuvin.images_downloader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.thieuvin.images_downloader.entities.ScrapingResult
import kotlinx.android.synthetic.main.activity_main.*
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog


val NO_ITEMS = "No items in queue"

class MainActivity : AppCompatActivity() {

    private lateinit var scraperViewModel: ScraperViewModel

    private val results: ArrayList<ScrapingResult> = arrayListOf()

    fun showDialog() {
        // let's build our dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add a website")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        // on click on "Add", we add the URL to the queue
        builder.setPositiveButton("Add") { dialog, which ->
            scraperViewModel.addToQueue(input.text.toString())
        }

        builder.setNegativeButton("Cancel") { dialog, which ->

        }

        builder.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val viewManager = LinearLayoutManager(this)
        val viewAdapter = ResultViewAdapter(results)


        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        scraperViewModel = ViewModelProviders.of(this).get(ScraperViewModel::class.java)

        button.setOnClickListener({ v -> showDialog() })
        textView.text = NO_ITEMS

        // listen to live events to update our UI
        scraperViewModel.progressLiveData.observe(this, Observer {
            progressBar.progress = it
        })

        scraperViewModel.queueSizeLiveData.observe(this, Observer {
            textView.text = if (it == 0) NO_ITEMS else "$it item(s) in queue"
        })

        scraperViewModel.resultLiveData.observe(this, Observer {
            results.add(it)
            viewAdapter.notifyDataSetChanged()
        })
    }
}
