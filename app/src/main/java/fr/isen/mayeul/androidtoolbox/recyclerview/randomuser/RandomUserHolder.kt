package fr.isen.mayeul.androidtoolbox.recyclerview.randomuser

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.mayeul.androidtoolbox.randomuser.RandomUser
import fr.isen.mayeul.androidtoolbox.utils.CircleTransform
import kotlinx.android.synthetic.main.activity_random_user_item.view.*

class RandomUserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindValue(randomUser: RandomUser) {
        itemView.randomName.text = ("${randomUser.name.first} ${randomUser.name.last}")
        itemView.randomAddress.text =
            ("${randomUser.location.street.number} ${randomUser.location.street.name} ${randomUser.location.city}")
        itemView.randomEmail.text = randomUser.email

        Picasso.get().load(randomUser.picture.large).transform(CircleTransform()).into(itemView.randomImage)
    }
}