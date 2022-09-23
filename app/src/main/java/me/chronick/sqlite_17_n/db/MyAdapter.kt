package me.chronick.sqlite_17_n.db

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.chronick.sqlite_17_n.R

class MyAdapter(listMy: ArrayList<String>) : RecyclerView.Adapter<MyAdapter.MyHolder>(){
    var listArray =listMy

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)

        fun setData(title: String){
            tvTitle.text=title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder { //рисуется шаблон.  создаем шаблон, преобразуем xml в экран
        val inflater = LayoutInflater.from(parent.context) // взяли из вьюгрупп, надуваем xml
        return MyHolder(inflater.inflate(R.layout.rc_item,parent, false))
    }

    override fun getItemCount(): Int { // сколько эл-то нужно подключить к ресайклер вью(список из какого числа эл-тов)
        return listArray.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int){
        // заполняется шаблон. передаем holder и itemcount для заполнения шаблона (данные массива к шаблону для рисования), отрисовывается каждый раз новая позиция
        holder.setData(listArray[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(listItems: List<String>){
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }
}
