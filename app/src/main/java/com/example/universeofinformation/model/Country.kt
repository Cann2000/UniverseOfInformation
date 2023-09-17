package com.example.universeofinformation.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Country(

    @ColumnInfo(name = "CountryName")
    @SerializedName("name")
    val countryName:String,

    @ColumnInfo(name = "Capital")
    @SerializedName("capital")
    val capital:String,

    @ColumnInfo(name = "Year")
    @SerializedName("year")
    val year:String?,

    @ColumnInfo(name = "CountryFounder")
    @SerializedName("founder")
    val countryFounder:String?,

    @ColumnInfo(name = "Language")
    @SerializedName("language")
    val language:String?,

    @ColumnInfo(name = "Population")
    @SerializedName("population")
    val population:String,

    @ColumnInfo(name = "ImageUrl")
    @SerializedName("ImageUrl")
    val imageUrl:String,

    @ColumnInfo(name = "Starred")
    var starred:Boolean = false

    ) {

    @PrimaryKey(autoGenerate = true)
    var uuid:Int? = 0
}