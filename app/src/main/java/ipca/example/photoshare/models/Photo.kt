package ipca.example.photoshare.models

import android.graphics.Bitmap
import com.google.firebase.firestore.QueryDocumentSnapshot

class Photo {

    var description : String = ""
    var imageURl :  String = ""
    var bitmap : Bitmap? = null
    var id : String? =  null

    constructor(description: String, imageURl: String) {
        this.description = description
        this.imageURl = imageURl
    }

    fun toHash() : HashMap<String, Any>{
        var hashMap = HashMap<String, Any>()
        hashMap.put("description", description)
        hashMap.put("imageURl"   , imageURl)
        return hashMap
    }

    companion object {
        fun fromHash(hashMap: QueryDocumentSnapshot) : Photo {
            val photo = Photo(
                hashMap["description"] as String,
                hashMap["imageURl"] as String
            )
            return photo
        }
    }
}