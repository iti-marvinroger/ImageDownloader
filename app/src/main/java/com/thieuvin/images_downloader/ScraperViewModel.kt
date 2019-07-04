package com.thieuvin.images_downloader

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thieuvin.images_downloader.entities.ScrapingResult
import com.thieuvin.images_downloader.services.ScraperService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ScraperViewModel : ViewModel() {
    private val job = Job()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    private val scraperService = ScraperService()

    val urlQueue = ArrayList<String>()
    var queueRunning = false

    val queueSizeLiveData = MutableLiveData<Int>()
    val progressLiveData = MutableLiveData<Int>()
    val resultLiveData = MutableLiveData<ScrapingResult>()

    // start processing the queue, notifying the UIs as needed
    fun start() {
        val url = urlQueue.removeAt(0)
        queueSizeLiveData.postValue(urlQueue.size)

        ioScope.launch {
            queueRunning = true
            val imageUrls = scraperService.extractImagesURL(url)

            scraperService.fetchAllImages(imageUrls, progressLiveData)

            resultLiveData.postValue(ScrapingResult(url, imageUrls))
            queueRunning = false

            if (urlQueue.size > 0) {
                start()
            }
        }
    }

    // add URLs to the queue
    fun addToQueue(url: String) {
       urlQueue.add(url)

        queueSizeLiveData.postValue(urlQueue.size)

        if (!queueRunning) {
            start()
        }
    }
}