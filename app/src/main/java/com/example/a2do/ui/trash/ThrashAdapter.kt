package com.example.a2do.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.a2do.R
import com.example.a2do.databinding.TrashRowBinding
import com.example.a2do.model.Note

class ThrashAdapter: ListAdapter<Note, ThrashAdapter.ViewHolder>(DIFF_CALLBACK) {

    private lateinit var onNoteListClickListener: OnNoteListClickListener
    private lateinit var changeStatus:ChangeStatus


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.create(LayoutInflater.from(parent.context), parent, onNoteListClickListener,changeStatus)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),position)
    }

    fun setOnNoteListClickListener(onNoteListClickListener: OnNoteListClickListener) {
        this.onNoteListClickListener = onNoteListClickListener
    }


    class ViewHolder(val binding: TrashRowBinding, onNoteListClickListener: OnNoteListClickListener,changeStatus: ChangeStatus): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onNoteListClickListener.onClick(binding.trash!!)


                binding.rdSelect.setOnClickListener {
                    val lottie = it as LottieAnimationView
                    if(lottie.background!=null) {
                        lottie.background = null
                        lottie.setAnimation("check_anim.json")
                        lottie.playAnimation()
                        changeStatus.changeButton(true,binding.position)

                    }
                    else{
                        lottie.setImageResource(R.drawable.white_radius_background)
                        lottie.setBackgroundResource(R.drawable.checkbox_empty)
                        changeStatus.changeButton(false,binding.position)
                    }
                }
            }
        }
        companion object {
            fun create(inflater: LayoutInflater, parent: ViewGroup, onNoteListClickListener: OnNoteListClickListener,changeStatus: ChangeStatus): ViewHolder {
                val itemTrashBinding = TrashRowBinding.inflate(inflater, parent, false)
                return ViewHolder(itemTrashBinding,onNoteListClickListener,changeStatus)
            }
        }

        fun bind(note: Note,position: Int) {
            binding.position=position
            binding.trash = note
            binding.executePendingBindings()
        }


    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean =
                oldItem.baslik == newItem.baslik

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean =
                oldItem.note == newItem.note
        }
    }

    interface OnNoteListClickListener {
        fun onClick(note: Note)
    }


    interface ChangeStatus{
        fun changeButton(boolean: Boolean,position: Int)
    }
    fun changeStatus(changeStatus: ChangeStatus){
        this.changeStatus=changeStatus
    }




}