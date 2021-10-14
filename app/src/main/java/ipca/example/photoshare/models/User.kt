package ipca.example.photoshare.models

import android.graphics.Bitmap
import com.google.firebase.firestore.QueryDocumentSnapshot

class User {

    var name        : String = ""
    var photoURl    : String = ""
    var email       : String = ""
    var photoBitmap : Bitmap? = null
    var id          : String? =  null

    constructor(
                name        : String,
                photoURl    : String,
                email       : String
    ) {
        this.name      = name
        this.photoURl  = photoURl
        this.email     = email
    }

    fun toHash() : HashMap<String, Any>{
        var hashMap = HashMap<String, Any>()
        hashMap.put("name"     , name    )
        hashMap.put("photoURl" , photoURl)
        hashMap.put("email"    , email   )
        return hashMap
    }

    companion object {
        fun fromHash(hashMap: QueryDocumentSnapshot) : User {
            val photo = User(
                hashMap["name"    ] as String,
                hashMap["photoURl"] as String,
                hashMap["email"   ] as String
            )
            return photo
        }
    }
}