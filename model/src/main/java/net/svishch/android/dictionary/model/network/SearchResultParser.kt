package net.svishch.android.dictionary.model.network

import net.svishch.android.dictionary.model.repository.entity.DataModel
import net.svishch.android.dictionary.model.repository.entity.Meanings


fun parseSearchResults(state: net.svishch.android.dictionary.model.AppState): net.svishch.android.dictionary.model.AppState {
    val newSearchResults = arrayListOf<DataModel>()
    when (state) {
        is net.svishch.android.dictionary.model.AppState.Success -> {
            val searchResults = state.data
            if (!searchResults.isNullOrEmpty()) {
                for (searchResult in searchResults) {
                    parseResult(searchResult, newSearchResults)
                }
            }
        }
    }

    return net.svishch.android.dictionary.model.AppState.Success(newSearchResults)
}

private fun parseResult(dataModel: DataModel, newDataModels: ArrayList<DataModel>) {
    if (!dataModel.text.isNullOrBlank() && !dataModel.meanings.isNullOrEmpty()) {
        val newMeanings = arrayListOf<net.svishch.android.dictionary.model.repository.entity.Meanings>()
        for (meaning in dataModel.meanings) {
            if (meaning.translation != null && !meaning.translation.translation.isNullOrBlank()) {
                newMeanings.add(
                    net.svishch.android.dictionary.model.repository.entity.Meanings(
                        meaning.translation,
                        meaning.imageUrl
                    )
                )
            }
        }
        if (newMeanings.isNotEmpty()) {
            newDataModels.add(
                DataModel(
                    dataModel.text,
                    newMeanings
                )
            )
        }
    }
}

fun convertMeaningsToString(meanings: List<Meanings>): String {
    var meaningsSeparatedByComma = String()
    for ((index, meaning) in meanings.withIndex()) {
        meaningsSeparatedByComma += if (index + 1 != meanings.size) {
            String.format("%s%s", meaning.translation?.translation, ", ")
        } else {
            meaning.translation?.translation
        }
    }
    return meaningsSeparatedByComma
}
