package kodilone.app.mechatnger.activity.entry

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kodilone.app.mechatnger.R
import kodilone.app.mechatnger.activity.message.LatestMessagesActivity
import kodilone.app.mechatnger.model.User
import kodilone.app.mechatnger.utils.BoxAlertDialog
import java.util.*

class RegisterActivity : AppCompatActivity() {
    companion object{
        val TAG = "RegisterScreen"
    }

    private val selectPhotoActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        onActivityResult(result)
    }

    private var selectedPhotoUri: Uri? = null

    private var registeredUserUid: String? = null
    private var registeredUserName: String? = null
    private var registeredUserUrl: String? = null

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

        if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
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

                registeredUserUid = it.result?.user?.uid
                registeredUserName = username

                Log.d(TAG, "Successfully created user with uid: ${it.result?.user?.uid}")

                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                val msg = "Failed to create user:\n\n${it.message}"
                BoxAlertDialog(msg).show(supportFragmentManager, "ERROR_ALERT_DIALOG")
                Log.d(TAG, msg)
            }
    }

    private fun uploadImageToFirebaseStorage(){
        if(selectedPhotoUri != null){
            val fileName = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$fileName")
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d(TAG, "Successfully uploaded image: ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d(TAG, "File location: $it")

                        registeredUserUrl = it.toString()

                        saveUserToFirebaseRealtimeDatabase()
                    }
                }
                .addOnFailureListener {
                    val msg = "Failed to upload image:\n\n${it.message}"
                    BoxAlertDialog(msg).show(supportFragmentManager, "ERROR_ALERT_DIALOG")
                    Log.d(TAG, msg)
                }
        }
    }

    private fun saveUserToFirebaseRealtimeDatabase(){
        val ref = FirebaseDatabase.getInstance().getReference("/users/$registeredUserUid")

        val user = User(registeredUserUid!!, registeredUserName!!, registeredUserUrl!!)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "Saved user to Firebase Realtime Database")

                val intent = Intent(this, LatestMessagesActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
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

            selectedPhotoUri = result.data!!.data

            try {
                val bitmap = if(Build.VERSION.SDK_INT < 28) {
                                MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
                            }else{
                                val source = ImageDecoder.createSource(contentResolver, selectedPhotoUri!!)
                                ImageDecoder.decodeBitmap(source)
                            }

                //val bitmapDrawable = BitmapDrawable(resources, bitmap)
                //selectPhotoButton.background = bitmapDrawable

                val circleImageView = findViewById<ImageView>(R.id.selectPhotoImageViewRegister)
                circleImageView.setImageBitmap(bitmap)

                selectPhotoButton.alpha = 0f

            }catch (e:Throwable){
                Log.d(TAG,"$e")
            }
        }
    }
}