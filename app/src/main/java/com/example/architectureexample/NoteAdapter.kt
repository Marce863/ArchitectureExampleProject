package com.example.architectureexample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
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

NoteAdapter will help with animations as it'll let us know what exact view is
being modified, we just need to override two methods and Use teh DiffUtil.
LiveData also gets handled through it's submitList() method
 */
class NoteAdapter : ListAdapter<Note, NoteAdapter.NoteViewHolder>(NoteDiffUtil()) {

    class NoteDiffUtil : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return (oldItem.id == newItem.id);
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.title == newItem.title
                    && oldItem.description == newItem.description
                    && oldItem.priority == newItem.priority
        }
    }

    private lateinit var listener: OnItemClickListener

    fun getNoteAt(position: Int): Note {
        return getItem(position)
    }

    /*
    ViewHolder is a class that is used to hold references to the views in a
    RecyclerView item. Instead of creating a new view for each item, the ViewHolder
    recycles teh existing views.
    The ViewHolder acts as a container for views in a RecyclerView
     */
    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.text_view_title)
        val textViewDescription: TextView = itemView.findViewById(R.id.text_view_description)
        val textViewPriority: TextView = itemView.findViewById(R.id.text_view_priority)

        init {
            itemView.setOnClickListener {
                val position: Int = adapterPosition
                // The new ListAdapter provides a getter method
                listener.onItemClick(getItem(position))
            }
        }
    }

    // We create and return a NoteHolder, the layout we want to use for our single
    // items in our recycler view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        // parent is just our recycler view, which is in the main activity so it can
        // get the context
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)

        return NoteViewHolder(itemView)
    }

    // Get data from Note object into the views of our NoteHolder.
    // The holder is the view holder at this position.
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        // ListAdapter providers a getter for Note at given position
        val currentNote: Note = getItem(position)
        holder.textViewTitle.text = currentNote.title
        holder.textViewDescription.text = currentNote.description
        holder.textViewPriority.text = currentNote.priority.toString()
    }

    interface OnItemClickListener {
        fun onItemClick(note: Note);
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}