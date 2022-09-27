package me.chronick.sqlite_17_n.db

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.chronick.sqlite_17_n.EditActivity
import me.chronick.sqlite_17_n.R

class MyAdapter(listMy: ArrayList<ListItem>, mainContext: Context) : RecyclerView.Adapter<MyAdapter.MyHolder>(){
    private var listArray =listMy
    var context = mainContext

    class MyHolder(itemView: View, vHContext: Context) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle:TextView = itemView.findViewById(R.id.tvTitle)
        private val context = vHContext

        fun setData(item: ListItem){
            tvTitle.text=item.title
            itemView.setOnClickListener{
                val intent = Intent(context, EditActivity::class.java).apply {  // откуда куда
                    putExtra(MyIntentConstants.INTENT_TITLE_KEY, item.title)
                    putExtra(MyIntentConstants.INTENT_DESC_KEY, item.desc)
                    putExtra(MyIntentConstants.INTENT_URI_KEY, item.uri)
                }

                context.startActivity(intent) // что хотим запустить и что передаем в активити
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder { //рисуется шаблон.  создаем шаблон, преобразуем xml в экран
        val inflater = LayoutInflater.from(parent.context) // взяли из вьюгрупп, надуваем xml
        return MyHolder(inflater.inflate(R.layout.rc_item,parent, false), context)
    }

    override fun getItemCount(): Int { // сколько эл-то нужно подключить к ресайклер вью(список из какого числа эл-тов)
        return listArray.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int){
        // заполняется шаблон. передаем holder и itemcount для заполнения шаблона (данные массива к шаблону для рисования), отрисовывается каждый раз новая позиция
        holder.setData(listArray[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(listItems: List<ListItem>){
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(position: Int, dbManager : MyDBManager){
        dbManager.removeItemFromDB(listArray[position].id.toString())
        listArray.removeAt(position)
        notifyItemRangeChanged(0,listArray.size) // delete position and update
        notifyItemRemoved(position) // what Item delete
    }
}
