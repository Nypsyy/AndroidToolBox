package fr.isen.mayeul.androidtoolbox.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import fr.isen.mayeul.androidtoolbox.RandomUser
import kotlinx.android.synthetic.main.activity_random_user_item.view.*

class RandomUserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindValue(randomUser: RandomUser) {
        itemView.randomName.text = randomUser.name
        itemView.randomAddress.text = randomUser.address
        itemView.randomEmail.text = randomUser.email
    }
}