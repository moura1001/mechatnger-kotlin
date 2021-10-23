package kodilone.app.mechatnger

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    companion object{
        val TAG = "RegisterScreen"
    }

    private val selectPhotoActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        onActivityResult(result)
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

        val selectPhotoButton = findViewById<Button>(R.id.selectPhotoButtonRegister)
        selectPhotoButton.setOnClickListener {
            performSelectPhoto()
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

    private fun performSelectPhoto(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        selectPhotoActivity.launch(intent)
    }

    private fun onActivityResult(result: ActivityResult){
        if(result.resultCode == Activity.RESULT_OK && result.data != null){
            Log.d(TAG, "Photo was selected")

            val selectPhotoButton = findViewById<Button>(R.id.selectPhotoButtonRegister)

            val uri = result.data!!.data

            try {
                val bitmap = if(Build.VERSION.SDK_INT < 28) {
                                MediaStore.Images.Media.getBitmap(contentResolver, uri)
                            }else{
                                val source = ImageDecoder.createSource(contentResolver, uri!!)
                                ImageDecoder.decodeBitmap(source)
                            }

                val bitmapDrawable = BitmapDrawable(resources, bitmap)
                selectPhotoButton.background = bitmapDrawable
            }catch (e:Throwable){
                Log.d(TAG,"$e")
            }
        }
    }
}