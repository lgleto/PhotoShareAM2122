package ipca.example.photoshare.ui.photo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import ipca.example.photoshare.databinding.FragmentPhotoBinding
import java.io.File
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import ipca.example.photoshare.models.Photo
import java.io.ByteArrayOutputStream
import java.util.*


class PhotoFragment : Fragment() {

    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!

    private var bitmap : Bitmap? = null

    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabSend.visibility = View.GONE
        binding.fabSend.setOnClickListener {
            // Create a new user with a first and last name

            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val storage = Firebase.storage
            var storageRef = storage.reference
            val filename = "${UUID.randomUUID()}.jpg"
            val mountainImagesRef = storageRef.child("images/${Firebase.auth.currentUser?.uid}/$filename")

            var uploadTask = mountainImagesRef.putBytes(data)
            uploadTask.continueWithTask {task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                mountainImagesRef.downloadUrl
            }
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads

            }.addOnSuccessListener { task ->
                val downloadUri = task.uploadSessionUri?.toString()?:""

                Log.d(TAG, "DocumentSnapshot added with ID: ${uploadTask.result.toString()}")
                val photo = Photo(
                    binding.editTextPhotoDescription.text.toString(),
                    downloadUri
                )
                db.collection("photos")
                    .add(photo.toHash())
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
            }



        }
        binding.fabTakePhoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //val intent  = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(intent, CAMERA_PIC_REQUEST)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                CAMERA_PIC_REQUEST -> {
                    val bm : Bitmap? = data?.extras?.get("data") as? Bitmap
                    bm?.let {
                        binding.imageViewPhoto.setImageBitmap(it)
                        binding.fabSend.visibility = View.VISIBLE
                        bitmap = bm
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        val CAMERA_PIC_REQUEST = 1001
        val TAG = "PhotoFragement"
    }
}