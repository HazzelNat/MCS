package com.example.petadoptioncenter

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity: AppCompatActivity() {

    private lateinit var usernameET: EditText
    private lateinit var passwordET: EditText
    private lateinit var emailET: EditText
    private lateinit var phoneET: EditText
    private lateinit var maleRB: RadioButton
    private lateinit var femaleRB: RadioButton
    private lateinit var registerButton: Button
    private lateinit var loginRE: TextView
    private var checkUser: Boolean = false

    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        db = Firebase.firestore

        usernameET = findViewById(R.id.username)
        passwordET = findViewById(R.id.password)
        emailET = findViewById(R.id.email)
        phoneET = findViewById(R.id.phone)
        maleRB = findViewById(R.id.radio_male)
        femaleRB = findViewById(R.id.radio_female)
        registerButton = findViewById(R.id.register)
        loginRE = findViewById(R.id.loginRE)
        var isMale: Boolean

        registerButton.setOnClickListener {
            val username = usernameET.text.toString()
            val password = passwordET.text.toString()
            val email = emailET.text.toString()
            val phone = phoneET.text.toString()

            isMale = maleRB.isChecked == true

            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

//            if (usernameExists(username)) {
//                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }

            if (password.length < 8) {
                Toast.makeText(this, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (username.length < 5) {
                Toast.makeText(this, "Password must be at least 5 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!email.endsWith("@puff.com")) {
                Toast.makeText(this, "Email must end with '@puff.com'", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (phone.length !in 11..13) {
                Toast.makeText(this, "Phone number length must be between 11 and 13 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
            addUser(username, password, email, phone, isMale)

            startActivity(Intent(this, LoginActivity::class.java))
            finish()

        }

        loginRE.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }

//    private fun usernameExists(username: String): Boolean {
//        checkUser(username)
//
//        if (checkUser){
//            return false
//        }
//
//        return true
//    }

    private fun addUser (username:String, password:String, email:String, phonenumber:String, isMale:Boolean){
        val userInfo = userInfo(username, password, email, phonenumber, isMale)
        db.collection("User")
            .add(userInfo)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

//    private fun checkUser (username: String) {
//        db.collection("User").get().addOnSuccessListener { querySnapshot ->
//            for (document in querySnapshot){
//                if (username == document.get("username").toString()){
//                    checkUser = false
//                }
//            }
//        }
//    }
}