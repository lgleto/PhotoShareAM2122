package ipca.example.photoshare

import android.R
import android.content.Context

import android.content.SharedPreferences




class Preferences {

    var loginPref : String
        get() = sharedPreferences.getString("LOGIN_PREF", "") as String
        set(value) {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("LOGIN_PREF", value)
            editor.apply()
        }

    lateinit var sharedPreferences: SharedPreferences
    constructor(context: Context){
         sharedPreferences =
            context.
            getSharedPreferences("PHOTO_PREF", Context.MODE_PRIVATE)
    }


}