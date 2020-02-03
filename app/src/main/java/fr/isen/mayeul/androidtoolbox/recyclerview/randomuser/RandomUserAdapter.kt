package fr.isen.mayeul.androidtoolbox.recyclerview.randomuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.isen.mayeul.androidtoolbox.R
import fr.isen.mayeul.androidtoolbox.randomuser.RandomUser

class RandomUserAdapter(private val users: ArrayList<RandomUser>) : RecyclerView.Adapter<RandomUserHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomUserHolder {
        return RandomUserHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.activity_random_user_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: RandomUserHolder, position: Int) {
        holder.bindValue(users[position])
    }
}
