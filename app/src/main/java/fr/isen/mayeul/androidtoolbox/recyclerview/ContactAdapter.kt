package fr.isen.mayeul.androidtoolbox.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.isen.mayeul.androidtoolbox.Contact
import fr.isen.mayeul.androidtoolbox.R

class ContactAdapter(private val contacts: ArrayList<Contact>) : RecyclerView.Adapter<ContactHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        return ContactHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_contact_item, parent, false))
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bindValue(contacts[position])
    }
}
