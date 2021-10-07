package ipca.example.photoshare.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ipca.example.photoshare.R
import ipca.example.photoshare.databinding.FragmentHomeBinding
import ipca.example.photoshare.models.Photo

class HomeFragment : Fragment() {

    var photos = arrayListOf<Photo>()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var mLayoutManager: LinearLayoutManager? = null

    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recycleViewPhotos.layoutManager = mLayoutManager
        mAdapter = PhotosAdapter()
        binding.recycleViewPhotos.itemAnimator = DefaultItemAnimator()
        binding.recycleViewPhotos.adapter = mAdapter


        db.collection("photos")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val photo = Photo.fromHash(document)
                    photos.add(photo)

                }
                mAdapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    inner class PhotosAdapter : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

        inner class ViewHolder(val v: View) : RecyclerView.ViewHolder(v)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.row_photo, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.v.findViewById<TextView>(R.id.textViewDescription).text = photos[position].description

            holder.v.apply {
                val textViewDescription = findViewById<TextView>(R.id.textViewDescription)
                textViewDescription.text = photos[position].description

                val imageViewPhoto = findViewById<ImageView>(R.id.imageViewPhoto)
                Glide.with(this).load(photos[position].imageURl).into(imageViewPhoto);
            }
        }

        override fun getItemCount(): Int {
            return photos.size
        }
    }


    companion object {
        val TAG = "HomeFragment"
    }
}