package me.chronick.sqlite_17_n

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import me.chronick.sqlite_17_n.databinding.ActivityMainBinding
import me.chronick.sqlite_17_n.db.MyAdapter
import me.chronick.sqlite_17_n.db.MyDBManager

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val myDBManager = MyDBManager(this)
    private val myAdapter = MyAdapter(ArrayList(), this )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        binding.fabtnAdd.setOnClickListener{
            val i = Intent(this,EditActivity::class.java)
            startActivity(i)
        }

    }

    private fun init(){
        binding.rvView.layoutManager =LinearLayoutManager(this)
        binding.rvView.adapter = myAdapter
    }

    private fun fillAdapter(){
        myAdapter.updateAdapter(myDBManager.readDBData())
    }


    override fun onResume() {
        super.onResume()
        myDBManager.openDB()
        fillAdapter()
        if (!myDBManager.readDBData().isEmpty()){
            binding.tvNoElements.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        myDBManager.closeDB()
        super.onDestroy()
    }


}