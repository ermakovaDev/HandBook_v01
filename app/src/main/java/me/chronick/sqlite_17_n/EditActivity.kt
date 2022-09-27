package me.chronick.sqlite_17_n

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import me.chronick.sqlite_17_n.databinding.ActivityMainBinding
import me.chronick.sqlite_17_n.databinding.EditActivityBinding
import me.chronick.sqlite_17_n.db.MyDBManager
import me.chronick.sqlite_17_n.db.MyIntentConstants

class EditActivity : AppCompatActivity() {
    private val imageRequestCode = 10
    var tempImageUri = "empty"
    private val myDBManager = MyDBManager(this)
    lateinit var binding: EditActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getMyIntents()

        binding.fabtnAddImage.setOnClickListener {
            binding.clMyImageLayout.visibility = View.VISIBLE
            binding.fabtnAddImage.visibility = View.GONE
        }

        binding.ibtnDeleteImage.setOnClickListener {
            binding.clMyImageLayout.visibility = View.GONE
            binding.fabtnAddImage.visibility = View.VISIBLE
        }

        binding.ibtnEditImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT) // постоянная ссылка
            intent.type = "image/*"
            startActivityForResult(intent, imageRequestCode)
        }

        binding.fabtnSave.setOnClickListener {
            val myTitle = binding.etTitle.text.toString()
            val myDesc = binding.etContent.text.toString()
            if (myTitle != "" && myDesc != "") {
                myDBManager.insertToDB(myTitle, myDesc, tempImageUri)
                finish()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == imageRequestCode) {
            binding.ivPicture.setImageURI(data?.data)
            tempImageUri = data?.data.toString()
            contentResolver.takePersistableUriPermission(data?.data!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    override fun onResume() {
        super.onResume()
        myDBManager.openDB()
    }

    override fun onDestroy() {
        myDBManager.closeDB()
        super.onDestroy()
    }

    private fun getMyIntents() { // ++Intent
        val intentToDo = intent
        if (intentToDo != null) {
            Log.d ("MyLog", "Data received : " + intentToDo.getStringExtra(MyIntentConstants.INTENT_TITLE_KEY))
            if (intentToDo.getStringExtra(MyIntentConstants.INTENT_TITLE_KEY) != null) {
                binding.fabtnAddImage.visibility = View.GONE

                binding.etTitle.setText(intentToDo.getStringExtra(MyIntentConstants.INTENT_TITLE_KEY))
                binding.etContent.setText(intentToDo.getStringExtra(MyIntentConstants.INTENT_DESC_KEY))

                if (intentToDo.getStringExtra(MyIntentConstants.INTENT_URI_KEY) != "empty") {
                    binding.clMyImageLayout.visibility = View.VISIBLE
                    binding.ivPicture.setImageURI(
                        Uri.parse(
                            intentToDo.getStringExtra(
                                MyIntentConstants.INTENT_URI_KEY
                            )
                        )
                    )
                    binding.ibtnDeleteImage.visibility = View.GONE
                    binding.ibtnEditImage.visibility = View.GONE
                }
            }
        }
    }

}