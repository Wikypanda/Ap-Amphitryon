package com.restaurant.amphitryon.presentation.chefcuisinier

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.restaurant.amphitryon.data.local.database.entities.PlatEntity
import com.restaurant.amphitryon.data.local.database.entities.PlatServiceEntity
import com.restaurant.amphitryon.data.repository.PlatRepository
import com.restaurant.amphitryon.domain.model.Service
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChefCuisinierViewModel @Inject constructor(
    private val platRepository: PlatRepository
) : ViewModel() {
    
    private val _plats = MutableStateFlow<List<PlatEntity>>(emptyList())
    val plats: StateFlow<List<PlatEntity>> = _plats.asStateFlow()
    
    private val _platServices = MutableStateFlow<List<PlatServiceEntity>>(emptyList())
    val platServices: StateFlow<List<PlatServiceEntity>> = _platServices.asStateFlow()
    
    init {
        loadPlats()
    }
    
    private fun loadPlats() {
        viewModelScope.launch {
            platRepository.getAllPlats().collect { platList ->
                _plats.value = platList
            }
        }
    }
    
    fun loadPlatServicesForService(date: String, service: Service) {
        viewModelScope.launch {
            platRepository.getPlatServicesForService(date, service).collect { platServiceList ->
                _platServices.value = platServiceList
            }
        }
    }
    
    fun loadPlatServicesForDate(date: String) {
        viewModelScope.launch {
            platRepository.getPlatServicesForDate(date).collect { platServiceList ->
                _platServices.value = platServiceList
            }
        }
    }
    
    fun insertPlat(plat: PlatEntity) {
        viewModelScope.launch {
            platRepository.insertPlat(plat)
        }
    }
    
    fun updatePlat(plat: PlatEntity) {
        viewModelScope.launch {
            platRepository.updatePlat(plat)
        }
    }
    
    fun deletePlat(plat: PlatEntity) {
        viewModelScope.launch {
            platRepository.deletePlat(plat)
        }
    }
    
    fun insertPlatService(platService: PlatServiceEntity) {
        viewModelScope.launch {
            platRepository.insertPlatService(platService)
        }
    }
    
    fun updatePlatService(platService: PlatServiceEntity) {
        viewModelScope.launch {
            platRepository.updatePlatService(platService)
        }
    }
    
    fun removePlatFromService(platId: Long, date: String, service: Service) {
        viewModelScope.launch {
            platRepository.removePlatFromService(platId, date, service)
        }
    }
    
    fun updateAvailableQuantity(platServiceId: Long, quantity: Int) {
        viewModelScope.launch {
            platRepository.updateAvailableQuantity(platServiceId, quantity)
        }
    }
}
