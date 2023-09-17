package com.example.universeofinformation.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Favorite(

    @ColumnInfo(name = "Title")
    val title: String?,

    @ColumnInfo(name = "Subtitle")
    val subTitle:String?,

    @ColumnInfo(name = "ImageUrl")
    val imageUrl:String?

) {
    @PrimaryKey(autoGenerate = true)
    var uuid: Int? = null
}

