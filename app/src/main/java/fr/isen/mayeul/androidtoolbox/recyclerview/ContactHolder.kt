package fr.isen.mayeul.androidtoolbox.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import fr.isen.mayeul.androidtoolbox.Contact
import kotlinx.android.synthetic.main.activity_contact_item.view.*

class ContactHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindValue(contact: Contact) {
        itemView.contactName.text = contact.name
        itemView.contactNumber.text = contact.number
    }
}
