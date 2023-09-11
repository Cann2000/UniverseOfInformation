package com.example.universeofinformation.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class GeographicEvent(

    @ColumnInfo(name = "EventName")
    @SerializedName("EventName")
    val eventName:String?,

    @ColumnInfo(name = "EventDate")
    @SerializedName("Date")
    val eventDate:String?,

    @ColumnInfo(name = "EventLocation")
    @SerializedName("Location")
    val eventLocation:String?,

    @ColumnInfo(name = "EventInformation")
    @SerializedName("Description")
    val eventInformation: String?,

    @ColumnInfo(name = "ImageUrl")
    @SerializedName("ImageUrl")
    val imageUrl:String
) {

    @PrimaryKey(autoGenerate = true)
    var uuid : Int? = 0
}