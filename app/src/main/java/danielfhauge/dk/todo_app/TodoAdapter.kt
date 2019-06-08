package danielfhauge.dk.todo_app

import android.icu.text.SimpleDateFormat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import java.util.*
import java.util.concurrent.TimeUnit


class TodoAdapter(var todos : ArrayList<Entity>): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private lateinit var mListner: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListner = listener
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        var current = todos[position]
        holder.body.text = current.data.body
        if (current.data.date.isNotEmpty()){
            val currentDate = Date()
            val expire = SimpleDateFormat("yyyy-MM-dd").parse(current.data.date)
            val diff = expire.time - currentDate.time
            val daysDiff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
            holder.expire.text = "$daysDiff Days"
        }

    }

    override fun onCreateViewHolder(parrent: ViewGroup, type: Int): TodoViewHolder {
        var v = LayoutInflater.from(parrent.context).inflate(R.layout.recycler_item, parrent, false)
        return TodoViewHolder(v, mListner)
    }


    class TodoViewHolder(itemView: View, listener: OnItemClickListener): ViewHolder(itemView){
        var body: TextView = itemView.findViewById(R.id.text)
        var expire: TextView = itemView.findViewById(R.id.text2)
        var delete: ImageView = itemView.findViewById(R.id.image_delete)

        init {
            delete.setOnClickListener {
                if (listener != null){
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION){
                        listener.onItemClick(pos)
                    }
                }
            }
        }

    }


}