package com.example.notesappnodejsrender.adapters

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.notesappnodejsrender.R
import com.example.notesappnodejsrender.interfaces.MyApiService
import com.example.notesappnodejsrender.models.Note
import com.example.notesappnodejsrender.models.NoteCreate
import com.example.notesappnodejsrender.models.PostNoteResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NoteAdapter(private val notes: List<Note>, private val context: Context) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val BASE_URL = "https://notes-nodejs-server-badri.onrender.com"

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.txt_title)
        private val contentTextView: TextView = itemView.findViewById(R.id.txt_content)
        private val idTextView: TextView = itemView.findViewById(R.id.txt_id)
        private val userIdTextView: TextView = itemView.findViewById(R.id.txt_userId)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val note = notes[position]
                    showCustomDeleteDialog(note.id)
                }
            }
        }

        fun bind(note: Note) {
            titleTextView.text = note.title
            contentTextView.text = note.content
            idTextView.text = note.id
            userIdTextView.text = note.userid
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    private fun showCustomDeleteDialog(id: String) {
        val view: View = LayoutInflater.from(context).inflate(R.layout.delete_note_dialog, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)

        val idEditText = view.findViewById<EditText>(R.id.et_id)
        idEditText.setText(id) // Set the text of the EditText to the provided ID
        idEditText.isEnabled = false

        builder.setPositiveButton("Delete") { dialog, which ->
            deleteData(id)
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
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
                    Toast.makeText(context,responseData?.message, Toast.LENGTH_SHORT).show()
                    // Handle the response data here
                } else {
                    // Handle the error
                    Toast.makeText(context,"API response Error", Toast.LENGTH_SHORT).show()
                    Log.e("API Response", "Request failed with code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<PostNoteResponse>, t: Throwable) {
                // Handle network errors here
                Toast.makeText(context,"Network Error", Toast.LENGTH_SHORT).show()
                Log.e("API Response", "Request failed: ${t.message}")
            }
        })
    }
}
