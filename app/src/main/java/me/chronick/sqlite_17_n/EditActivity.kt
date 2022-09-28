package me.chronick.sqlite_17_n

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.chronick.sqlite_17_n.databinding.EditActivityBinding
import me.chronick.sqlite_17_n.db.MyDBManager
import me.chronick.sqlite_17_n.db.MyIntentConstants
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {
    private val imageRequestCode = 10
    private var tempImageUri = "empty"
    private var idItem = 0
    private var isEditState = false
    private val myDBManager = MyDBManager(this)
    private lateinit var binding: EditActivityBinding

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
            tempImageUri = "empty"
        }

        binding.ibtnEditImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT) // постоянная ссылка
            intent.type = "image/*"
            startActivityForResult(intent, imageRequestCode)
        }

        binding.fabtnSave.setOnClickListener {
            val myTitle = binding.etTitle.text.toString()
            val myDesc = binding.etDescription.text.toString()
            if (myTitle != "" && myDesc != "") {
                CoroutineScope(Dispatchers.Main).launch {
                    if (isEditState) {
                        myDBManager.updateItemFromDB( idItem, myTitle, myDesc, tempImageUri, getCurrentTime())
                    } else {
                        myDBManager.insertToDB(myTitle, myDesc, tempImageUri, getCurrentTime())
                    }
                    finish()
                }

            }
        }

        binding.fabtnEditItem.setOnClickListener {
            binding.etTitle.isEnabled = true
            binding.etDescription.isEnabled = true
            binding.fabtnEditItem.visibility = View.GONE
            binding.fabtnAddImage.visibility = View.VISIBLE
            if (tempImageUri == "empty") return@setOnClickListener
            binding.ibtnEditImage.visibility = View.VISIBLE
            binding.ibtnDeleteImage.visibility = View.VISIBLE

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

    private fun getMyIntents() { // ++Intent
        val intentToDo = intent
        binding.fabtnEditItem.visibility = View.GONE
        if (intentToDo != null) {
            Log.d("MyLog", "Data received : " + intentToDo.getStringExtra(MyIntentConstants.INTENT_TITLE_KEY))
            if (intentToDo.getStringExtra(MyIntentConstants.INTENT_TITLE_KEY) != null) {
                binding.fabtnAddImage.visibility = View.GONE
                binding.etTitle.setText(intentToDo.getStringExtra(MyIntentConstants.INTENT_TITLE_KEY))
                binding.etDescription.setText(intentToDo.getStringExtra(MyIntentConstants.INTENT_DESC_KEY))

                isEditState = true
                binding.etTitle.isEnabled = false
                binding.etDescription.isEnabled = false
                binding.fabtnEditItem.visibility = View.VISIBLE

                idItem = intentToDo.getIntExtra(MyIntentConstants.INTENT_ID_KEY, 0)

                if (intentToDo.getStringExtra(MyIntentConstants.INTENT_URI_KEY) != "empty") {
                    binding.clMyImageLayout.visibility = View.VISIBLE
                    tempImageUri = intentToDo.getStringExtra(MyIntentConstants.INTENT_URI_KEY)!!
                    binding.ivPicture.setImageURI(Uri.parse(tempImageUri))
                    binding.ibtnDeleteImage.visibility = View.GONE
                    binding.ibtnEditImage.visibility = View.GONE
                }
            }
        }
    }

    private fun getCurrentTime(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd.MM.yy kk:mm", Locale.getDefault())
        return formatter.format(time)
    }

    override fun onResume() {
        super.onResume()
        myDBManager.openDB()
    }

    override fun onDestroy() {
        myDBManager.closeDB()
        super.onDestroy()
    }
}