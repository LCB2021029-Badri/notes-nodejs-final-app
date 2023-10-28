package com.example.notesappnodejsrender

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesappnodejsrender.adapters.NoteAdapter
import com.example.notesappnodejsrender.databinding.ActivityMainBinding
import com.example.notesappnodejsrender.interfaces.MyApiService
import com.example.notesappnodejsrender.models.Note
import com.example.notesappnodejsrender.models.NoteCreate
import com.example.notesappnodejsrender.models.PostNoteResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView:RecyclerView
    private val BASE_URL = "https://notes-nodejs-server-badri.onrender.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        binding.btnGet.setOnClickListener {
            fetchData()
        }

        binding.btnCreate.setOnClickListener {
            showCustomCreateDialog()
        }

        binding.btnSearch.setOnClickListener {
            filterFromUserId(binding.etSearch.text.toString())
        }

        binding.btnDelete.setOnClickListener {
            showCustomDeleteDialog()
        }


    }

    private fun fetchData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(MyApiService::class.java)

        apiService.fetchData().enqueue(object : Callback<List<Note>> {
            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                if (response.isSuccessful) {
                    val notes = response.body()
                    notes?.let {
                        val adapter = NoteAdapter(it,this@MainActivity)
                        recyclerView.adapter = adapter
                    }
                } else {
                    Log.e("API Response", "Request failed with code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                Log.e("API Response", "Request failed: ${t.message}")
            }
        })
    }

    private fun showCustomCreateDialog() {
        val view: View = layoutInflater.inflate(R.layout.create_note_dialog, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)

        // Handle "Post" button click within the custom dialog
        builder.setPositiveButton("Post") { dialog, which ->
            val idEditText = view.findViewById<EditText>(R.id.et_id)
            val useridEditText = view.findViewById<EditText>(R.id.et_userId)
            val titleEditText = view.findViewById<EditText>(R.id.et_title)
            val contentEditText = view.findViewById<EditText>(R.id.et_content)

            val id = idEditText.text.toString()
            val userid = useridEditText.text.toString()
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            // You can now use the collected data (id, userid, title, content) to send a POST request
            postData(id, userid, title, content)
        }

        // Handle "Cancel" button click
        builder.setNegativeButton("Cancel") { dialog, which ->
            // Dismiss the dialog
            dialog.dismiss()
        }

        builder.show()
    }

    private fun postData(id: String, userid: String, title: String, content: String) {
        // Retrofit POST request code (similar to previous example)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(MyApiService::class.java)

        val postData = NoteCreate(id, userid, title, content)

        val call = apiService.postData(postData)

        call.enqueue(object : Callback<PostNoteResponse> {
            override fun onResponse(call: Call<PostNoteResponse>, response: Response<PostNoteResponse>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    // Handle the response data here
                    Toast.makeText(this@MainActivity,responseData?.message,Toast.LENGTH_SHORT).show()
                } else {
                    // Handle the error
                    Toast.makeText(this@MainActivity,"API response Error",Toast.LENGTH_SHORT).show()
                    Log.e("API Response", "Request failed with code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PostNoteResponse>, t: Throwable) {
                // Handle network errors here
                Toast.makeText(this@MainActivity,"Network Error",Toast.LENGTH_SHORT).show()
                Log.e("API Response", "Request failed: ${t.message}")
            }
        })
    }

    private fun filterFromUserId(userid: String){
        // Retrofit POST request code (similar to previous example)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(MyApiService::class.java)

        val postData = NoteCreate("xxx",userid, "xxx", "xxx")

        val call = apiService.filterUserId(postData)

        call.enqueue(object : Callback<List<Note>> {
            override fun onResponse(call: Call<List<Note>>, response: Response<List<Note>>) {
                if (response.isSuccessful) {
                    val notes = response.body()
                    // Handle the response data here
                    notes?.let {
                        val adapter = NoteAdapter(it,this@MainActivity)
                        recyclerView.adapter = adapter
                    }
                } else {
                    // Handle the error
                    Toast.makeText(this@MainActivity,"API response Error",Toast.LENGTH_SHORT).show()
                    Log.e("API Response", "Request failed with code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Note>>, t: Throwable) {
                // Handle network errors here
                Toast.makeText(this@MainActivity,"Network Error",Toast.LENGTH_SHORT).show()
                Log.e("API Response", "Request failed: ${t.message}")
            }
        })
    }

    private fun showCustomDeleteDialog(){
        val view: View = layoutInflater.inflate(R.layout.delete_note_dialog, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view)

        // Handle "Post" button click within the custom dialog
        builder.setPositiveButton("Delete") { dialog, which ->
            val idEditText = view.findViewById<EditText>(R.id.et_id)
            val id = idEditText.text.toString()
            // You can now use the collected data (id, userid, title, content) to send a POST request
            deleteData(id)
        }

        // Handle "Cancel" button click
        builder.setNegativeButton("Cancel") { dialog, which ->
            // Dismiss the dialog
            dialog.dismiss()
        }

        builder.show()
    }

    private fun deleteData(id:String){
        // Retrofit POST request code (similar to previous example)
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(MyApiService::class.java)

        val postData = NoteCreate(id,"xxx", "xxx", "xxx")

        val call = apiService.deleteById(postData)

        call.enqueue(object : Callback<PostNoteResponse> {
            override fun onResponse(call: Call<PostNoteResponse>, response: Response<PostNoteResponse>) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    Toast.makeText(this@MainActivity,responseData?.message,Toast.LENGTH_SHORT).show()
                    // Handle the response data here
                } else {
                    // Handle the error
                    Toast.makeText(this@MainActivity,"API response Error",Toast.LENGTH_SHORT).show()
                    Log.e("API Response", "Request failed with code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PostNoteResponse>, t: Throwable) {
                // Handle network errors here
                Toast.makeText(this@MainActivity,"Network Error",Toast.LENGTH_SHORT).show()
                Log.e("API Response", "Request failed: ${t.message}")
            }
        })
    }

}