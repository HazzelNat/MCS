package com.example.petadoptioncenter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petadoptioncenter.HomeActivity
import com.example.petadoptioncenter.R
import com.example.petadoptioncenter.RegisterActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    private lateinit var usernameET: EditText
    private lateinit var passwordET: EditText
    private lateinit var loginButton: Button
    private lateinit var registerTV: TextView
    private var checkUser: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = Firebase.firestore

        usernameET = findViewById(R.id.username)
        passwordET = findViewById(R.id.password)
        loginButton = findViewById(R.id.login)
        registerTV = findViewById(R.id.register)

        loginButton.setOnClickListener {
            val username = usernameET.text.toString()
            val password = passwordET.text.toString()

            if (username.isEmpty()) {
                Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (username.length < 5) {
                Toast.makeText(this, "Username must be at least 5 character", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 8) {
                Toast.makeText(this, "Password must be at least 8 character", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            checkUser(username, password)
            if(checkUser == true){
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Username or Password is incorrect", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

        registerTV.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun checkUser (username: String, password: String) {
        db.collection("User").get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot){
                if (username == document.get("username").toString() && password == document.get("password").toString()){
                    checkUser = true
                }
            }
        }
    }
}