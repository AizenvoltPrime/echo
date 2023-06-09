package com.example.echo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.textfield.TextInputEditText
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import android.text.method.PasswordTransformationMethod
import android.text.InputType
import android.widget.EditText
import com.google.firebase.database.FirebaseDatabase

private const val TAG = "RegisterActivity"

fun isValidPassword(password: String): Boolean {
    val passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$".toRegex()
    return passwordRegex.matches(password)
}

class Register : AppCompatActivity() {

    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextUsername: TextInputEditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextRepeatPassword: EditText
    private lateinit var buttonReg: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var textView: TextView

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editTextEmail = findViewById(R.id.email)
        editTextPassword = findViewById(R.id.password)
        editTextUsername = findViewById(R.id.username)
        editTextRepeatPassword = findViewById(R.id.repeat_password)
        buttonReg = findViewById(R.id.btn_register)
        progressBar = findViewById(R.id.progressBar)
        auth = FirebaseAuth.getInstance()
        textView = findViewById(R.id.loginNow)

        textView.setOnClickListener {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }

        buttonReg.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val username = editTextUsername.text.toString()
            val repeatPassword = editTextRepeatPassword.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(
                    this,
                    "Enter email",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Enter password",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (username.isEmpty()) {
                Toast.makeText(
                    this,
                    "Enter username",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (repeatPassword.isEmpty()) {
                Toast.makeText(
                    this,
                    "Enter repeat password",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (!isValidPassword(password)) {
                // Password is not valid, display error message to user
                Toast.makeText(
                    this,
                    "Password does not meet requirements",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener // Add this line to prevent further execution
            }

            if (password == repeatPassword) {
                // Passwords match, continue with registration
            } else {
                // Passwords do not match, display error message to user
                Toast.makeText(
                    this,
                    "Passwords do not match",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        // Get the current user
                        val user = auth.currentUser

                        // Get the reference to the users node in the Firebase Realtime Database using the correct database URL for your region.
                        val database =
                            FirebaseDatabase.getInstance("https://echo-37e0c-default-rtdb.europe-west1.firebasedatabase.app")
                        val usersRef = database.getReference("users")

                        // Create a map to store the user information.
                        val userInfo = mapOf(
                            "email" to email,
                            "username" to username,
                        )

                        // Save the user information in the Firebase Realtime Database.
                        usersRef.child(user!!.uid).setValue(userInfo)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Account created.", Toast.LENGTH_SHORT).show()
                                val intent = Intent(applicationContext, Login::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { exception ->
                                // Handle the exception appropriately.
                                Log.w(TAG, "setValue:failure", exception)
                                Toast.makeText(
                                    this,
                                    "Failed to save user information: ${exception.message}",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener(this) { exception ->
                    // Handle the exception appropriately.
                    Log.w(TAG, "createUserWithEmail:failure", exception)
                    Toast.makeText(
                        this,
                        "Authentication failed: ${exception.message}",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
        }

        // Set input type and transformation method for password fields.
        editTextPassword.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()

        editTextRepeatPassword.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        editTextRepeatPassword.transformationMethod = PasswordTransformationMethod.getInstance()
    }
}
