package com.example.echo

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

/* Class to reset both password and email. */
class ChangeCredentials : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var apply: Button
    lateinit var changemail: EditText
    lateinit var changepass: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credential_reset)

        // Initialising the email and password
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.logpass)
        apply = findViewById<Button>(R.id.apply)
        apply.setOnClickListener(View.OnClickListener {
            changeemail(
                email.text.toString(),
                password.text.toString()
            )
        })
        apply = findViewById<Button>(R.id.changepass)
        apply.setOnClickListener(View.OnClickListener {
            changepassw(
                email.text.toString(),
                password.text.toString()
            )
        })
    }

    private fun changeemail(email: String, password: String) {
        changemail = findViewById<EditText>(R.id.changemail)
        val user = FirebaseAuth.getInstance().currentUser

        /* Firebase needs reauthentication to change credentials */
        val credential =
            EmailAuthProvider.getCredential(email, password) // Current Login Credentials
        user!!.reauthenticate(credential).addOnCompleteListener {
            Log.d("value", "Authentication successful.")

            /* remove if (change...) {} else {}' if not working */
 /*           if (changemail == null) {

            } else { */
                val user = FirebaseAuth.getInstance().currentUser
                user!!.updateEmail(changemail.getText().toString()).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this@ChangeCredentials,
                            "Email changed successfully to " + changemail.getText().toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            //}
        }
    }

    /* remove if (change...) {} else {}' if not working */
    private fun changepassw(email: String, password: String) {
        changepass = findViewById<EditText>(R.id.changepass)
        val user = FirebaseAuth.getInstance().currentUser
        val credential =
            EmailAuthProvider.getCredential(email, password) // Current Login Credentials

  /*      if (changepass == null) {

        } else { */
            user!!.reauthenticate(credential).addOnCompleteListener {
                Log.d("value", "Authentication successful.")
                val user = FirebaseAuth.getInstance().currentUser
                user!!.updatePassword(changepass.getText().toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@ChangeCredentials,
                                "Password changed successfully",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
       // }
    }
}