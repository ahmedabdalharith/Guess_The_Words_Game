package com.pyramid.questions.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pyramid.questions.domain.model.Player
import com.pyramid.questions.data.local.GamePreferencesManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PlayerViewModel(
    private val prefs: GamePreferencesManager
) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player> = _player

    init {
        loadPlayerData()
    }

    private fun loadPlayerData() {
        viewModelScope.launch(Dispatchers.IO) {
            val localPlayer = prefs.getPlayer()
            _player.postValue(localPlayer)

            val userId = prefs.getUsernameId() ?: return@launch

            try {
                val snapshot = firestore.collection("players").document(userId).get().await()
                if (snapshot.exists()) {
                    val firebasePlayer = snapshot.toObject(Player::class.java)
                    firebasePlayer?.let {
                        _player.postValue(it)
                        prefs.savePlayer(it)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updatePlayer(player: Player) {
        _player.value = player
        prefs.savePlayer(player)

        val userId = prefs.getUsernameId() ?: return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                firestore.collection("players").document(userId).set(player).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addCoins(amount: Int) {
        val current = _player.value ?: return
        val updated = current.copy(coins = current.coins + amount)
        updatePlayer(updated)
    }

    fun spendCoins(amount: Int): Boolean {
        val current = _player.value ?: return false
        return if (current.coins >= amount) {
            val updated = current.copy(coins = current.coins - amount)
            updatePlayer(updated)
            true
        } else false
    }

    fun addStars(amount: Int) {
        val current = _player.value ?: return
        val updated = current.copy(stars = current.stars + amount)
        updatePlayer(updated)
    }

    fun increaseXp(xpAmount: Int) {
        val current = _player.value ?: return
        val newXp = current.xp + xpAmount
        val totalXp = current.totalXp
        val levelUp = newXp >= totalXp
        val updated = if (levelUp) {
            current.copy(
                xp = newXp - totalXp,
                currentLevel = current.currentLevel + 1,
                totalXp = totalXp + 50  // increase required XP for next level
            )
        } else {
            current.copy(xp = newXp)
        }
        updatePlayer(updated)
    }
}
