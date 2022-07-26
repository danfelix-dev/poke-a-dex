package com.zshock.pokeadex.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.zshock.pokeadex.data.model.pokemon.Sprites
import com.zshock.pokeadex.data.model.pokemon.Type
import com.zshock.pokeadex.data.model.pokemon.TypeX
import com.zshock.pokeadex.data.model.species.Species

class Converters {

    @TypeConverter
    fun fromType(value: List<Type>?): String? {
        return value?.joinToString(separator = ",") { it.type.name }
    }

    @TypeConverter
    fun typeToType(value: String?): List<Type>? {
        return value?.split(",")?.map { Type(0, TypeX(it, "")) }
    }

    @TypeConverter
    fun speciesToGson(value: Species?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun speciesFrom(jsonString: String?): Species? {
        return Gson().fromJson(jsonString, Species::class.java)
    }

    @TypeConverter
    fun spritesToGson(value: Sprites?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun spritesFrom(jsonString: String?): Sprites? {
        return Gson().fromJson(jsonString, Sprites::class.java)
    }
}