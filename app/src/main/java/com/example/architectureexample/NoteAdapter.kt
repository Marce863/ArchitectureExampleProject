package com.example.architectureexample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/*
Adapter is how we get our data from Note objects into the RecyclerView

Adapter is needed to use a recycler view.
The adapter acts as a bridge between your data source and the RecyclerView.
It manages the creation of individual items views, binds the data to each view,
and handles user interaction events.

LayoutManager is responsible for positioning and measuring the items views within
the RecyclerView. It determines how to items are arranged, such as in a linear
vertical list or a grid.
 */
class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteHolder>() {
    // Our list of Notes is initialize to avoid null pointers
    var notes: List<Note> = arrayListOf()
        set(value) {
            field = value
            // Main activity observes the live data, on change we get past a list of
            // notes. Need a way to get the list of notes in our recycler view
            // TODO: Will be removed later and replace
            notifyDataSetChanged()
        }

    fun getNoteAt(position: Int): Note {
        return notes[position]
    }

    /*
    ViewHolder is a class that is used to hold references to the views in a
    RecyclerView item. Instead of creating a new view for each item, the ViewHolder
    recycles teh existing views.
    The ViewHolder acts as a container for views in a RecyclerView
     */
    class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView
        val textViewDescription: TextView
        val textViewPriority: TextView

        init {
            textViewTitle = itemView.findViewById(R.id.text_view_title)
            textViewDescription = itemView.findViewById(R.id.text_view_description)
            textViewPriority = itemView.findViewById(R.id.text_view_priority)
        }
    }

    // We create and return a NoteHolder, the layout we want to use for our single
    // items in our recycler view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        // parent is just our recycler view, which is in the main activity so it can
        // get the context
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)

        return NoteHolder(itemView)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    // Get data from Note object into the views of our NoteHolder.
    // The holder is the view holder at this position.
    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val currentNote: Note = notes[position]
        holder.textViewTitle.text = currentNote.title
        holder.textViewDescription.text = currentNote.description
        holder.textViewPriority.text = currentNote.priority.toString()
    }
}