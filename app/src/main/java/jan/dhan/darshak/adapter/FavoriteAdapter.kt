package jan.dhan.darshak.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import jan.dhan.darshak.FavoriteBottomFragment.Favoritefragment
import jan.dhan.darshak.R
import jan.dhan.darshak.database.Favoriteentity
import jan.dhan.darshak.databinding.FavoriteBottomFragmentBinding
import jan.dhan.darshak.ui.MainActivity


class FavoriteAdapter(private val items:ArrayList<Favoriteentity>,
                      private val mContext: Context?,
):RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

     class ViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView){

         val clSinglePlace: ConstraintLayout = itemView.findViewById(R.id.clFavoritePlace)
         val tvResultHeading: TextView = itemView.findViewById(R.id.tvHeading)
         val tvAddress: TextView = itemView.findViewById(R.id.tvfavoriteAddress)
         val tvTimings: TextView = itemView.findViewById(R.id.tvfavTimings)
         val ivSpeak: ImageView = itemView.findViewById(R.id.ivfavSpeak)
         val ivDirectionIcon: ImageView = itemView.findViewById(R.id.ivfavDirectionIcon)
         val ivCallIcon: ImageView = itemView.findViewById(R.id.ivfavCallIcon)
         val ivShareIcon: ImageView = itemView.findViewById(R.id.ivfavShareIcon)
         val rbRatings: RatingBar = itemView.findViewById(R.id.rbfavRatings)
         val tvRatingCount: TextView = itemView.findViewById(R.id.tvfavRatingCount)
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.favorite_location, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item =items[position]
        Log.d("dsf", "onBindViewHolder: "+items)
        holder.tvResultHeading.text=item.name
        holder.tvAddress.text=item.address
        holder.rbRatings.rating=item.rating?.toFloat() ?:0F
        holder.tvRatingCount.text=item.ratingCount
        val open = if (item.open.toBoolean())
            "<font color=\"${
                mContext?.resources?.getColor(
                    R.color.green_color,
                    mContext.theme
                )
            }\">Open Now</font>"
        else
            "<font color=\"${
                mContext?.resources?.getColor(
                    R.color.navigationSelected,
                    mContext.theme
                )
            }\">Closed</font>"
        val compatOpen = if (item.open.toBoolean()) "Open Now" else "Closed"
        val close = item.close
        val timings = "$open $close"
        val compatTimings = "$compatOpen $close"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvTimings.setText(
                Html.fromHtml(item.timings, HtmlCompat.FROM_HTML_MODE_LEGACY),
                TextView.BufferType.SPANNABLE
            )
        } else {
            holder.tvTimings.text = compatTimings
        }



        holder.ivSpeak.setOnClickListener {
            (mContext as MainActivity).sayOutLoud("${item.name}")
        }

        holder.ivShareIcon.setOnClickListener {
            Intent(Intent.ACTION_SEND).also {
                it.type = "text/plain"
                it.putExtra(Intent.EXTRA_SUBJECT, "Location")
                it.putExtra(
                    Intent.EXTRA_TEXT,
                    "${item.name}\n$item.address\nhttps://www.google.co.id/maps/@$item.latitude,$item.longitude"
                )
                mContext!!.startActivity(Intent.createChooser(it, "Share using:"))
            }
        }

        holder.clSinglePlace.setOnClickListener {
            (mContext as MainActivity).zoomToCurrentSelectedPlace(LatLng(item.latitude!!, item.longitude!!))
        }

        holder.ivCallIcon.setOnClickListener {
            if (!item.phoneNumber.isNullOrEmpty())
                mContext!!.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${item.phoneNumber}")))
            else
                Toast.makeText(mContext, "Phone number not Provided.", Toast.LENGTH_SHORT).show()
        }

        holder.ivDirectionIcon.setOnClickListener {
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=${item.latitude},${item.longitude}")
            ).also {
                it.`package` = "com.google.android.apps.maps"
                if (it.resolveActivity(mContext!!.packageManager) != null)
                    mContext.startActivity(it)
            }
        }
    }

    override fun getItemCount() = items.size


    fun removeAll() {
        items.clear()
        notifyDataSetChanged()
    }
    }
