package com.example.universeofinformation.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Literature(

    @ColumnInfo(name = "WorkName")
    @SerializedName("work_name")
    val workName:String?,

    @ColumnInfo(name = "WorkType")
    @SerializedName("work_type")
    val workType:String?,

    @ColumnInfo(name = "Author")
    @SerializedName("author")
    val author:String?,

    @ColumnInfo(name = "Period")
    @SerializedName("period")
    val period:String?,

    @ColumnInfo(name = "Summary")
    @SerializedName("summary")
    val summary:String?,

    @ColumnInfo(name = "ImageUrl")
    @SerializedName("ImageUrl")
    val imageUrl:String,

    @ColumnInfo(name = "Starred")
    var starred:Boolean = false

) {

    @PrimaryKey(autoGenerate = true)
    var uuid:Int? = 0
}