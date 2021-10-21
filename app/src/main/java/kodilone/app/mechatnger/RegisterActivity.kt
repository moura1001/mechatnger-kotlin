package kodilone.app.mechatnger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    companion object{
        val TAG = "RegisterScreen"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val buttonRegister = findViewById<Button>(R.id.registerButtonRegister)
        buttonRegister.setOnClickListener {
            performRegister()
        }

        val alreadyHaveAccount = findViewById<TextView>(R.id.alreadyHaveAccountTextView)
        alreadyHaveAccount.setOnClickListener {
            finish()
        }
    }

    private fun performRegister() {
        val username = findViewById<EditText>(R.id.usernameEditTextRegister).text.toString()
        val email = findViewById<EditText>(R.id.emailEditTextRegister).text.toString()
        val password = findViewById<EditText>(R.id.passwordEditTextRegister).text.toString()

        if(email.isEmpty() || password.isEmpty()){
            val msg = "Please enter text in username/email/password"
            BoxAlertDialog(msg).show(supportFragmentManager, "ERROR_ALERT_DIALOG")
            return
        }

        Log.d(TAG, "Username: $username")
        Log.d(TAG, "Email: $email")
        Log.d(TAG, "Password: $password")

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener

                Log.d(TAG, "Successfully created user with uid: ${it.result?.user?.uid}")
            }
            .addOnFailureListener {
                val msg = "Failed to create user:\n\n${it.message}"
                BoxAlertDialog(msg).show(supportFragmentManager, "ERROR_ALERT_DIALOG")
                Log.d(TAG, msg)
            }
    }
}