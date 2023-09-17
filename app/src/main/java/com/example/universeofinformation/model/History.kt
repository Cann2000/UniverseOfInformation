package com.example.universeofinformation.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class History(

    @ColumnInfo(name = "WarName")
    @SerializedName("WarName")
    val warName:String?,

    @ColumnInfo(name = "WarHistory")
    @SerializedName("WarHistory")
    val warHistory:String?,

    @ColumnInfo(name = "WarInformation")
    @SerializedName("Information")
    val warInformation:String?,

    @ColumnInfo(name = "Countries")
    @SerializedName("Countries")
    val countries:String?,

    @ColumnInfo(name = "Winner")
    @SerializedName("Winner")
    val winner:String?,

    @ColumnInfo(name = "ImageUrl")
    @SerializedName("ImageUrl")
    val imageUrl:String?,

    @ColumnInfo(name = "Starred")
    var starred:Boolean = false

) {
    @PrimaryKey(autoGenerate = true)
    var uuid:Int? = 0
}