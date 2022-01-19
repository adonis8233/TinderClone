package com.example.mytinder.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytinder.R
import com.example.mytinder.activities.TinderCallback
import com.example.mytinder.adapters.ChatsAdapter
import com.example.mytinder.util.Chat
import com.example.mytinder.util.DATA_MATCHES
import com.example.mytinder.util.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_matches.*


class MatchesFragment : Fragment() {

    private lateinit var userId: String
    private lateinit var userDatabase: DatabaseReference
    private lateinit var chatDatabase: DatabaseReference
    private var callback: TinderCallback? = null

    private val chatsAdapter = ChatsAdapter(ArrayList())

    fun setCallback(callback: TinderCallback) {
        this.callback = callback
        userId = callback.onGetUserId()
        userDatabase = callback.getUserDatabase()
        chatDatabase = callback.getChatDatabase()

        fetchData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matches, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        matchesRV.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
            adapter = chatsAdapter
        }
    }

    fun fetchData() {
        userDatabase.child(userId).child(DATA_MATCHES).addListenerForSingleValueEvent(
            object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChildren()) {
                        snapshot.children.forEach { child ->
                            val matchId = child.key
                            val chatId = child.value.toString()
                            if (!matchId.isNullOrEmpty()) {
                                userDatabase.child(matchId).addListenerForSingleValueEvent(object:
                                ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val user = snapshot.getValue(User::class.java)
                                        if (user != null) {
                                            val chat = Chat(chatId, matchId, user.uid, user.name,
                                                user.imageUrl)
                                            chatsAdapter.addElement(chat)
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }

                                })
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }


}