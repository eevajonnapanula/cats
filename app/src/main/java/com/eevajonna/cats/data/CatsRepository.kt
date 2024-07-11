package com.eevajonna.cats.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CatsRepository
    @Inject
    constructor(
        private val dataStore: DataStore<Preferences>,
        private val catsService: CatsService,
    ) {
        private val catIdsPreferenceKey = stringSetPreferencesKey("cats")

        val catIdsFlow: Flow<Set<String>> =
            dataStore.data
                .map {
                    it[catIdsPreferenceKey] ?: emptySet()
                }

        suspend fun deleteCatId(id: String) {
            dataStore.edit { preferences ->
                val oldVal = preferences[catIdsPreferenceKey]?.toMutableSet()

                oldVal?.remove(id)

                preferences[catIdsPreferenceKey] = oldVal ?: emptySet()
            }
        }

        suspend fun getNewCat() {
            try {
                val catResponse = catsService.getCat()
                val cat = catResponse.body()

                cat?.let {
                    addCatId(it._id)
                }
            } catch (e: Exception) {
                Log.e("CatsRepository", "$e")
            }
        }

        private suspend fun addCatId(id: String) {
            dataStore.edit { preferences ->
                val oldVal = preferences[catIdsPreferenceKey]?.toMutableSet()

                oldVal?.add(id)

                preferences[catIdsPreferenceKey] = oldVal ?: emptySet()
            }
        }
    }
