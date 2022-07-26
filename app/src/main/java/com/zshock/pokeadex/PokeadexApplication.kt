package com.zshock.pokeadex

import android.app.Application
import com.zshock.pokeadex.data.network.NetworkLiveData

class PokeadexApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NetworkLiveData.init(this)
    }

}