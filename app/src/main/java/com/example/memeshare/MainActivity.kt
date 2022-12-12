package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    private lateinit var ivMeme : ImageView
    var currentImageUrl : String? = null
    private lateinit var progressbar : ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = ""
        loadMeme()
    }

    private fun loadMeme() {
        ivMeme = findViewById(R.id.ivMemeImage)
        progressbar = findViewById(R.id.progressbar)
        val url = "https://meme-api.com/gimme/programmingmemes"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                currentImageUrl = response.getString("url")
                Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        progressbar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        progressbar.visibility = View.GONE
                        return false
                    }

                }).into(ivMeme)
            }, {
                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            })
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }

    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey, Check this cool meme !!! $currentImageUrl")
        val chooser = Intent.createChooser(intent, "Share this meme using....")
        startActivity(chooser)
        Animatoo.animateSlideUp(this)
    }
}