package danielfhauge.dk.todo_app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import danielfhauge.dk.todo_app.TodoAdapter.OnItemClickListener

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TodoAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var todosList: ArrayList<Entity>
    private lateinit var addBut: Button
    private lateinit var addText: EditText
    private lateinit var api: Api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        api = Api()

        createTodoList()
        buildRecyclerView()
        SetAdd()

    }

    private fun insertTodo(){
        val text = addText.text.toString()
        if (text.isNotBlank()){
            val todo = Todo( text, "", "")
            Thread(Runnable {
                try {
                    val entity = api.AddTodo(todo)
                    todosList.add(entity)
                    this@MainActivity.runOnUiThread { adapter.notifyItemInserted(todosList.size-1) }
                }catch (error: Error){
                    println("Could not add stuff")
                }
            }).start()
        }
    }

    private fun removeTodo(pos: Int){
        todosList.removeAt(pos)
        adapter.notifyItemRemoved(pos)
    }


    private fun createTodoList() {
        todosList = ArrayList()
        val todo = Todo( "Loading", "", "")
        todosList.add(Entity(1, todo))
        updateTodo()
    }

    private fun updateTodo(){
        Thread(Runnable {
            val results = api.GetTodos()
            todosList.clear()
            todosList.addAll(results)
            this@MainActivity.runOnUiThread { adapter.notifyDataSetChanged() }
        }).start()
    }

    private fun SetAdd(){
        addBut = findViewById(R.id.button)
        addText = findViewById(R.id.insert)

        addBut.setOnClickListener { insertTodo() }
    }

    private fun buildRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        adapter = TodoAdapter(todosList)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                Thread(Runnable {
                    try {
                        api.RemoveTodo(todosList[position].id)
                        this@MainActivity.runOnUiThread { removeTodo(position) }
                    }catch (error: Error) {
                        println(error.message)
                    }
                }).start()
            }
        })
    }
}
