package com.example.assignment2.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment2.databinding.ItemEntityBinding

/**
 * Displays a summary for each entity. Tapping a row invokes [onItemClick]
 * with the full entity map so the Details screen can show every field,
 * including the description.
 */
class EntityAdapter(
    private val entities: List<Map<String, String>>,
    private val onItemClick: (Map<String, String>) -> Unit
) : RecyclerView.Adapter<EntityAdapter.EntityViewHolder>() {

    inner class EntityViewHolder(val binding: ItemEntityBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        val binding = ItemEntityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EntityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntityViewHolder, position: Int) {
        val entity = entities[position]
        val summary = entity.entries
            .filter { !it.key.equals("description", ignoreCase = true) }
            .joinToString(separator = "\n") { "${it.key}: ${it.value}" }

        holder.binding.textSummary.text = summary.ifBlank { "Item ${position + 1}" }
        holder.itemView.setOnClickListener { onItemClick(entity) }
    }

    override fun getItemCount(): Int = entities.size
}
