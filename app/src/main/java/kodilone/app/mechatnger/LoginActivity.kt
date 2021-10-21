package kodilone.app.mechatnger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    companion object{
        val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val buttonLogin = findViewById<Button>(R.id.loginButtonLogin)
        buttonLogin.setOnClickListener {
            val email = findViewById<EditText>(R.id.emailEditTextLogin).text.toString()
            val password = findViewById<EditText>(R.id.passwordEditTextLogin).text.toString()

            Log.d(TAG, "Email: $email")
            Log.d(TAG, "Password: $password")
        }

        val registerNewAccount = findViewById<TextView>(R.id.registerNewAccountTextView)
        registerNewAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}