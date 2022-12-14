package me.chronick.sqlite_17_n

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.chronick.sqlite_17_n.databinding.ActivityMainBinding
import me.chronick.sqlite_17_n.db.MyAdapter
import me.chronick.sqlite_17_n.db.MyDBManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val myDBManager = MyDBManager(this)
    private val myAdapter = MyAdapter(ArrayList(), this)
    private var job: Job? = null // Coroutine stop

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

    private fun initSearchView() {
        binding.svSearch.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {  // full search
            override fun onQueryTextSubmit(query: String?): Boolean { // function update BD witch Picture
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                fillAdapter(newText!!)
                return true
            }
        })
    }

    private fun fillAdapter(text: String) {

        job?.cancel() // null or not null

        job = CoroutineScope(Dispatchers.Main).launch { // thread MAIN
            val list = myDBManager.readDBData(text)
            myAdapter.updateAdapter(list)
            if (list.size > 0) {
                binding.tvNoElements.visibility = View.GONE
            } else {
                binding.tvNoElements.visibility = View.VISIBLE
            }
        }

    }


    private fun getSwapManager(): ItemTouchHelper {
        return ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove( // ????????????????????????
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) { // ?????????? , ???????????????? ViewHolder ???? ???????????? ??????????????
                myAdapter.removeItem(viewHolder.adapterPosition, myDBManager)

            }
        })
    }


    override fun onResume() {
        super.onResume()
        myDBManager.openDB()
        fillAdapter("")
    }

    override fun onDestroy() {
        myDBManager.closeDB()
        super.onDestroy()
    }


}