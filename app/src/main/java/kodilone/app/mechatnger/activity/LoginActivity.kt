package kodilone.app.mechatnger.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import kodilone.app.mechatnger.R
import kodilone.app.mechatnger.utils.BoxAlertDialog

class LoginActivity : AppCompatActivity() {
    companion object{
        val TAG = "LoginScreen"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val buttonLogin = findViewById<Button>(R.id.loginButtonLogin)
        buttonLogin.setOnClickListener {
            performLogin()
        }

        val registerNewAccount = findViewById<TextView>(R.id.registerNewAccountTextView)
        registerNewAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin() {
        val email = findViewById<EditText>(R.id.emailEditTextLogin).text.toString()
        val password = findViewById<EditText>(R.id.passwordEditTextLogin).text.toString()

        if(email.isEmpty() || password.isEmpty()){
            val msg = "Please enter text in email/password"
            BoxAlertDialog(msg).show(supportFragmentManager, "ERROR_ALERT_DIALOG")
            return
        }

        Log.d(TAG, "Email: $email")
        Log.d(TAG, "Password: $password")

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener

                Log.d(TAG, "Successfully logged user with uid: ${it.result?.user?.uid}")

                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                val msg = "Failed to authenticate user:\n\n${it.message}"
                BoxAlertDialog(msg).show(supportFragmentManager, "ERROR_ALERT_DIALOG")
                Log.d(TAG, msg)
            }
    }
}