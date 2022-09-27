package me.chronick.sqlite_17_n

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.chronick.sqlite_17_n.databinding.ActivityMainBinding
import me.chronick.sqlite_17_n.db.MyAdapter
import me.chronick.sqlite_17_n.db.MyDBManager

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private val myDBManager = MyDBManager(this)
    private val myAdapter = MyAdapter(ArrayList(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initSearchView()

        binding.fabtnAdd.setOnClickListener {
            val i = Intent(this, EditActivity::class.java)
            startActivity(i)
        }

    }

    private fun init() {
        binding.rvView.layoutManager = LinearLayoutManager(this)
        val swapHelper = getSwapManager()
        swapHelper.attachToRecyclerView(binding.rvView)
        binding.rvView.adapter = myAdapter
    }

    private fun initSearchView(){
     binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{  // full search
         override fun onQueryTextSubmit(p0: String?): Boolean { // function update BD witch Picture
             return true
         }

         override fun onQueryTextChange(newText: String?): Boolean {
             val list = myDBManager.readDBData(newText!!)
             myAdapter.updateAdapter(list)
             Log.d("MyLog", "New Text: $newText")
            return true
         }
     })
    }

    private fun fillAdapter() {
        val list = myDBManager.readDBData("")
        myAdapter.updateAdapter(list)
        if (list.size > 0) {
            binding.tvNoElements.visibility = View.GONE
        } else {
            binding.tvNoElements.visibility = View.VISIBLE
        }
    }



    private fun getSwapManager(): ItemTouchHelper{
        return ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove( // передвижение
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { // свайп , получает ViewHolder по каждой позиции
                myAdapter.removeItem(viewHolder.adapterPosition, myDBManager)

            }
        })
    }


    override fun onResume() {
        super.onResume()
        myDBManager.openDB()
        fillAdapter()
    }

    override fun onDestroy() {
        myDBManager.closeDB()
        super.onDestroy()
    }


}