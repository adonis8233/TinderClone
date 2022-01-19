package com.example.mytinder.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.mytinder.R
import com.example.mytinder.util.DATA_USERS
import com.example.mytinder.util.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_userinfo.*

class UserinfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userinfo)

        val userId = intent.extras?.getString(PARAM_USER_ID, "")
        if (userId.isNullOrEmpty()) {
            finish()
        }

        val userDatabase = FirebaseDatabase.getInstance().reference.child(DATA_USERS)
        userDatabase.child(userId!!).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                userInfoName.text = user?.name
                userInfoAge.text = user?.age
                if (user?.imageUrl != null) {
                    Glide.with(this@UserinfoActivity)
                        .load(user.imageUrl)
                        .into(userInfoIV)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    companion object {
        private val PARAM_USER_ID = "User id"

        fun newIntent(context: Context, userId: String?): Intent {
            val intent = Intent(context, UserinfoActivity::class.java)
            intent.putExtra(PARAM_USER_ID, userId)
            return intent
        }
    }
}