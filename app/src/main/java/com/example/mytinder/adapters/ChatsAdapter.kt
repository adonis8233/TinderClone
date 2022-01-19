package com.example.mytinder.adapters

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mytinder.R
import com.example.mytinder.activities.ChatActivity
import com.example.mytinder.util.Chat

class ChatsAdapter(private var chats: ArrayList<Chat>): RecyclerView.Adapter<ChatsAdapter.
ChatsViewHolder>() {

    fun addElement(chat: Chat) {
        chats.add(chat)
        notifyDataSetChanged()
    }

    class ChatsViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

        private var layout = view.findViewById<View>(R.id.chatLayout)
        private var image = view.findViewById<ImageView>(R.id.chatPictureIV)
        private var name = view.findViewById<TextView>(R.id.chatNameTV)

        fun bind(chat: Chat) {
            name.text = chat.name
            if (image != null) {
                Glide.with(view)
                    .load(chat.imageUrl)
                    .into(image)
            }
            layout.setOnClickListener {
                val intent = ChatActivity.newIntent(view.context, chat.chatId, chat.userId,
                chat.imageUrl, chat.otherUserId)
                view.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder
    = ChatsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent,
        false))

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.bind(chats[position])
    }

    override fun getItemCount() = chats.size

}