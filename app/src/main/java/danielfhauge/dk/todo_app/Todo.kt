package danielfhauge.dk.todo_app

data class Entity(val id: Int, val data: Todo)

data class Todo(val body: String, val date: String = "", val time: String = "")
