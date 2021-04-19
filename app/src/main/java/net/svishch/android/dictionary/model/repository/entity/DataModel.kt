package net.svishch.android.dictionary.model.repository.entity

import com.google.gson.annotations.Expose

class DataModel(
    @Expose val text: String?,
    @Expose val meanings: List<Meanings>?
)
