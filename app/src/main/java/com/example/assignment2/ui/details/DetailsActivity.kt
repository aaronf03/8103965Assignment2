package com.example.assignment2.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment2.databinding.ActivityDetailsBinding

/**
 * Shows every field of the selected entity, including the description,
 * which is deliberately excluded from the Dashboard's summary view.
 */
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    @Suppress("UNCHECKED_CAST", "DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val entity = intent.getSerializableExtra(EXTRA_ENTITY) as? HashMap<String, String>

        binding.textDetails.text = if (entity.isNullOrEmpty()) {
            "No details available."
        } else {
            entity.entries.joinToString(separator = "\n\n") { (key, value) ->
                "${key.replaceFirstChar { c -> c.uppercase() }}:\n$value"
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_ENTITY = "extra_entity"
    }
}
