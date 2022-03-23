package jan.dhan.darshak.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.ImageView.VISIBLE
import android.widget.Toast
import androidx.core.os.bundleOf
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPhotoResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import jan.dhan.darshak.R
import jan.dhan.darshak.databinding.DetailsLayoutBinding


class DetailsFragment(
    private var list: ArrayList<PhotoMetadata>, private val place: Place,private var selectedMarkerLocation: LatLng?
    ) :
    BottomSheetDialogFragment() {

    private val formFragment = FormFragment()
    private var _binding: DetailsLayoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var placesClient: PlacesClient
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailsLayoutBinding.inflate(inflater, container, false)
        if(list.isNotEmpty()) {
            binding.hsvdetail.visibility= VISIBLE
            binding.ivdetail.visibility=ImageView.GONE
            for (i in 0..list.size - 1) {
                val imageView = ImageView(requireContext())
                imageView.setId(i)
                imageView.setPadding(2, 2, 2, 2)
                imageView.layoutParams =
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1000)
                val photoRequest = FetchPhotoRequest.builder(list.get(i))
                    .build()
                placesClient = Places.createClient(requireContext())
                placesClient.fetchPhoto(photoRequest)
                    .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->
                        val bitmap = fetchPhotoResponse.bitmap
                        imageView.setImageBitmap(bitmap)

                        imageView.setScaleType(ScaleType.CENTER_CROP)
                        binding.linear.addView(imageView)
                    }
            }
        }
            else{
            binding.hsvdetail.visibility= GONE
            binding.ivdetail.visibility=ImageView.VISIBLE


            }
        return binding.root

        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.headerText.text=place.name
        binding.tvLocation.text= place.address
        binding.tvTimer.text=if (!place.isOpen?.toString()
                .isNullOrEmpty()
        ) place.isOpen?.toString() else "Close"
        if(place.phoneNumber?.toString()
            .isNullOrEmpty())
        binding.tvPhone.text="No Number Provided"
        else
            binding.tvPhone.text=place.phoneNumber?.toString()

        binding.tvDirection.setOnClickListener{
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=${selectedMarkerLocation?.latitude},${selectedMarkerLocation?.longitude}")
            ).also {
                it.`package` = "com.google.android.apps.maps"
                if (it.resolveActivity(requireContext().packageManager) != null)
                    startActivity(it)
            }
        }

        binding.tvCalls.setOnClickListener {
            Toast.makeText(requireContext(), "Call Button", Toast.LENGTH_SHORT).show()
            if (!place.phoneNumber?.toString().isNullOrEmpty())
                startActivity(
                    Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:${place.phoneNumber?.toString()}")
                    )
                )
            else
                Toast.makeText(
                    requireContext(),
                    "Phone number not Provided.",
                    Toast.LENGTH_SHORT
                )
                    .show()
        }

        binding.tvFeedback.setOnClickListener {
            formFragment.arguments = bundleOf(
                "heading" to getString(R.string.feedback)
            )
            formFragment.show(parentFragmentManager, "feedback")
        }
    }


    }
