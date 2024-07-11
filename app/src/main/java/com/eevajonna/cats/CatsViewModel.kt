package com.eevajonna.cats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eevajonna.cats.data.CatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatsViewModel
    @Inject
    constructor(
        private val catsRepository: CatsRepository,
    ) : ViewModel() {
        private var _cats = MutableStateFlow(emptySet<String>())
        val cats = _cats.asStateFlow()

        private var _selectedCat = MutableStateFlow<String?>(null)
        val selectedCat = _selectedCat.asStateFlow()

        init {
            getCats()
        }

        private fun getCats() {
            viewModelScope.launch {
                catsRepository.catIdsFlow.collect {
                    when (it.size) {
                        0 -> {
                            _cats.value = emptySet()
                            _selectedCat.value = null
                        }
                        1 -> {
                            _selectedCat.value = it.first()
                        }
                        else -> {
                            _cats.value = it.take(it.size - 1).toSet()
                            _selectedCat.value = it.last()
                        }
                    }
                }
            }
        }

        fun getNewCat() {
            viewModelScope.launch {
                catsRepository.getNewCat()
                getCats()
            }
        }

        fun deleteCat(id: String) {
            viewModelScope.launch {
                delay(500)
                catsRepository.deleteCatId(id = id)
                getCats()
            }
        }
    }
