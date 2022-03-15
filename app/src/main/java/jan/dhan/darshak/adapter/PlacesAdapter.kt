package jan.dhan.darshak.adapter

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import jan.dhan.darshak.R
import jan.dhan.darshak.adapter.PlacesAdapter.PlacesViewHolder


class PlacesAdapter(
    private val mContext: Context,
    private val places: ArrayList<HashMap<String?, String?>?>
) : RecyclerView.Adapter<PlacesViewHolder>() {

    var currentLocation: LatLng? = null

    inner class PlacesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clSinglePlace: ConstraintLayout = itemView.findViewById(R.id.clSinglePlace)
        val tvResultHeading: TextView = itemView.findViewById(R.id.tvResultHeading)
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        val tvTimings: TextView = itemView.findViewById(R.id.tvTimings)
        val ivSpeak: ImageView = itemView.findViewById(R.id.ivSpeak)
        val ivDirectionIcon: ImageView = itemView.findViewById(R.id.ivDirectionIcon)
        val ivCallIcon: ImageView = itemView.findViewById(R.id.ivCallIcon)
        val ivSaveIcon: ImageView = itemView.findViewById(R.id.ivSaveIcon)
        val ivShareIcon: ImageView = itemView.findViewById(R.id.ivShareIcon)
        val rbRatings: RatingBar = itemView.findViewById(R.id.rbRatings)
        val tvRatingCount: TextView = itemView.findViewById(R.id.tvRatingCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesViewHolder {
        return PlacesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.single_location, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        val place = places[position]
        val id = place?.get("id")
        val heading = place?.get("name")
        val address = place?.get("address")
        val latitude = place?.get("latitude")?.toDouble()
        val longitude = place?.get("longitude")?.toDouble()
        val rating = place?.get("rating")
        val ratingCount = "(${place?.get("ratingCount")})"
        val open = if (place?.get("open").toBoolean()) "Open Now" else "Closed"
        val close = place?.get("close")
        val timings = "$open $close"
        val phoneNumber = place?.get("phoneNumber")
        val website = place?.get("website")

        holder.tvResultHeading.text = heading
        holder.tvAddress.text = address
        holder.rbRatings.rating = rating?.toFloat() ?: 4F
        holder.tvRatingCount.text = ratingCount
        holder.tvTimings.text = timings

        holder.ivShareIcon.setOnClickListener {
            Toast.makeText(mContext, "Share Button Clicked", Toast.LENGTH_SHORT).show()
        }

        holder.ivSaveIcon.setOnClickListener {
            Toast.makeText(mContext, "Save Button Clicked", Toast.LENGTH_SHORT).show()
        }

        holder.ivSpeak.setOnClickListener {
            Toast.makeText(mContext, "Speak Button Clicked", Toast.LENGTH_SHORT).show()
        }

        holder.clSinglePlace.setOnClickListener {
            Toast.makeText(mContext, "Whole Clicked", Toast.LENGTH_SHORT).show()
        }

        holder.ivCallIcon.setOnClickListener {
            mContext.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber")))
        }

        holder.ivDirectionIcon.setOnClickListener {
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=$latitude,$longitude")
            ).also {
                it.`package` = "com.google.android.apps.maps"
                if (it.resolveActivity(mContext.packageManager) != null)
                    mContext.startActivity(it)
            }
        }
    }

    override fun getItemCount() = places.size

    fun updateList(newPlace: HashMap<String?, String?>?, location: LatLng, position: Int) {
        places.add(newPlace)
        currentLocation = location
        notifyItemInserted(position)
    }
}