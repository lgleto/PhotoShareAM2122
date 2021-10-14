package ipca.example.photoshare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ipca.example.photoshare.databinding.ActivityLoginBinding
import ipca.example.photoshare.models.User
import ipca.example.photoshare.ui.photo.PhotoFragment

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.editTextEmail.setText(Preferences(this).loginPref)

        binding.buttonLogin.setOnClickListener {
            val email : String = binding.editTextEmail.text.toString()
            val password : String = binding.editTextTextPassword.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                            Preferences(this).loginPref = email
                            var user = User("","",email)
                            val db = Firebase.firestore

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java ))

                        db.collection("users")
                            .add(user.toHash())
                            .addOnSuccessListener { documentReference ->
                                Log.d(PhotoFragment.TAG, "DocumentSnapshot added with ID: ${documentReference.id}")

                            }
                            .addOnFailureListener { e ->
                                Log.w(PhotoFragment.TAG, "Error adding document", e)
                            }



                    } else {
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }

        }



    }
}