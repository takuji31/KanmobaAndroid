package jp.takuji31.kanmoba.num21

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.media.ExifInterface
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import jp.takuji31.kanmoba.R
import java.io.File

class ExifInterfaceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exif_interface)
        val recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        val granted = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (granted == PackageManager.PERMISSION_GRANTED) {
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = Adapter()
            recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.first() == PackageManager.PERMISSION_GRANTED) {
            val recyclerView = findViewById(R.id.recyclerView) as RecyclerView
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = Adapter()
            recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        }
    }

    class Adapter() : RecyclerView.Adapter<ViewHolder>() {
        val items : List<String> by lazy {
            val path = File( Environment.getExternalStorageDirectory(), IMAGES_PATH)
            val files = path.listFiles()
            files.map {
                it.absolutePath
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ViewHolder(inflater.inflate(R.layout.recycler_row_exif, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val exif = ExifInterface(items[position])
            holder.imageView.setImageBitmap(exif.thumbnailBitmap)
            holder.textView.text = ATTRIBUTES.map {
                it + " : " + exif.getAttribute(it)
            }.joinToString("\n")
        }

        override fun getItemCount(): Int {
            return items.size
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView by lazy {
            itemView.findViewById(R.id.imageView) as ImageView
        }
        val textView by lazy {
            itemView.findViewById(R.id.textView) as TextView
        }
    }

    companion object {
        val IMAGES_PATH = "Kanmoba21/"
        val ATTRIBUTES = arrayOf(
                ExifInterface.TAG_DATETIME,
                ExifInterface.TAG_MODEL,
                ExifInterface.TAG_APERTURE_VALUE,
                ExifInterface.TAG_F_NUMBER,
                ExifInterface.TAG_SHUTTER_SPEED_VALUE,
                ExifInterface.TAG_EXPOSURE_TIME,
                ExifInterface.TAG_FOCAL_LENGTH,
                ExifInterface.TAG_ISO_SPEED_RATINGS
        )
    }
}
