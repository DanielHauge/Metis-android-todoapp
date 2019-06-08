package danielfhauge.dk.todo_app

import android.content.Context
import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.lang.Exception

class Api {

    private val client = OkHttpClient()
    private val url = "<URL TO STORRAGE + APIKEY>"

    fun GetTodos(): ArrayList<Entity>{
        val request = Request.Builder().url(url).get().build()

        var result: Array<Entity>


        val response = client.newCall(request).execute()
        if (response.isSuccessful){
            val json = response.body()?.string()
            println(json)
            val serialized = Klaxon().parseArray<Entity>(json.orEmpty())
            if (serialized != null) {
                result = serialized.run { toTypedArray() }
            } else{
                throw error("SERIALIZATION ERROR!")
            }
        }else{
            val errorMes = response.code().toString()+response.message()
            throw error(errorMes)
        }

        return result.toCollection(ArrayList())
    }

    fun AddTodo(newTodo: Todo): Entity{
        val postBody = Klaxon().toJsonString(newTodo)
        val requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),postBody)
        val request = Request.Builder().url(url).post(requestBody).build()

        var result: Int

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) throw error("Response was not succesful")

        val respBody = response.body()?.string()?.toInt() ?: throw error("Response was not succesful")

        return Entity(respBody, newTodo)
    }

    fun RemoveTodo(id: Int){
        val request = Request.Builder().url("$url/$id").delete().build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) throw error("Response was not succesful")
    }




}

