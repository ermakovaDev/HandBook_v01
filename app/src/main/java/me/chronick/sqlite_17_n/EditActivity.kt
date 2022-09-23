package me.chronick.sqlite_17_n

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import me.chronick.sqlite_17_n.databinding.ActivityMainBinding
import me.chronick.sqlite_17_n.databinding.EditActivityBinding
import me.chronick.sqlite_17_n.db.MyDBManager

class EditActivity : AppCompatActivity() {
    private val imageRequestCode = 10
    var tempImageUri = "empty"
    private val myDBManager = MyDBManager(this)
    lateinit var binding: EditActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabtnAddImage.setOnClickListener {
            binding.clMyImageLayout.visibility = View.VISIBLE
            binding.fabtnAddImage.visibility = View.GONE
        }

        binding.ibtnDeleteImage.setOnClickListener {
            binding.clMyImageLayout.visibility = View.GONE
            binding.fabtnAddImage.visibility = View.VISIBLE
        }

        binding.ibtnEditImage.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, imageRequestCode)
        }

        binding.fabtnSave.setOnClickListener{
            val myTitle = binding.etTitle.text.toString()
            val myDesc = binding.etContent.text.toString()
            if (myTitle!= "" && myDesc != ""){
                myDBManager.insertToDB(myTitle, myDesc, tempImageUri)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == imageRequestCode){
            binding.ivPicture.setImageURI(data?.data)
            tempImageUri = data?.data.toString()
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

}