package com.thieuvin.images_downloader.services

import androidx.lifecycle.MutableLiveData
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import org.jsoup.Jsoup
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.floor

fun normalizeURL(mainUrl: String, imageUrl: String): String {
    // we try to normalize our URLs
    // so that we can have absolute URLs
    if (
        imageUrl.startsWith("//") ||
        imageUrl.startsWith("https://") ||
        imageUrl.startsWith("http://")
    ) {
        return imageUrl
    }

    val baseUrl = URL(mainUrl)
    val finalUrl = URL(baseUrl, imageUrl)

    return finalUrl.toString()
}

class ScraperService {
    private val httpClient = OkHttpClient()


    // extract all images href from a given URL
    fun extractImagesURL(url: String): List<String> {
        val doc = Jsoup.connect(url).get()
        return doc.select("img")
            .mapNotNull { col -> col.attr("src") }
            .map { normalizeURL(url, it) }
    }

    // fetch all the images URLs, load them into the Picasa cache
    suspend fun fetchAllImages(urls: List<String>, progressLiveData: MutableLiveData<Int>): Boolean =
        suspendCoroutine { cont ->

            var done = 0
            val total = urls.size

            if (total == 0) {
                cont.resume(true)
            }

            fun onSuccess() {
                done++

                val percentage = floor(100f * done / total).toInt()

                progressLiveData.postValue(percentage)

                // if we've done all URLs, we're done, resume the func
                if (done == urls.size) {
                    cont.resume(true)
                }
            }

            fun onError(e: Exception) {
                cont.resumeWithException(e)
            }

            urls.forEach {
                Picasso.get().load(it).fetch(object: com.squareup.picasso.Callback {
                    override fun onSuccess() {
                        onSuccess()
                    }
                    override fun onError(e: Exception) {
                        onError(e)
                    }
                })
            }
        }

}