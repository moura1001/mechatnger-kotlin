package kodilone.app.mechatnger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.w3c.dom.Text

class RegisterActivity : AppCompatActivity() {
    companion object{
        val TAG = "RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val buttonRegister = findViewById<Button>(R.id.registerButtonRegister)
        buttonRegister.setOnClickListener {
            val username = findViewById<EditText>(R.id.usernameEditTextRegister).text.toString()
            val email = findViewById<EditText>(R.id.emailEditTextRegister).text.toString()
            val password = findViewById<EditText>(R.id.passwordEditTextRegister).text.toString()

            Log.d(TAG, "Username: $username")
            Log.d(TAG, "Email: $email")
            Log.d(TAG, "Password: $password")
        }

        val alreadyHaveAccount = findViewById<TextView>(R.id.alreadyHaveAccountTextView)
        alreadyHaveAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}