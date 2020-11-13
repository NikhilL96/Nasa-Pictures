package com.example.nasapictures.model


import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageDetailsModel(
    @Json(name = "copyright")
    var copyright: String? = null,
    @Json(name = "date")
    var date: String? = null,
    @Json(name = "explanation")
    var explanation: String? = null,
    @Json(name = "hdurl")
    var hdurl: String? = null,
    @Json(name = "media_type")
    var mediaType: String? = null,
    @Json(name = "service_version")
    var serviceVersion: String? = null,
    @Json(name = "title")
    var title: String? = null,
    @Json(name = "url")
    var url: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(copyright)
        parcel.writeString(date)
        parcel.writeString(explanation)
        parcel.writeString(hdurl)
        parcel.writeString(mediaType)
        parcel.writeString(serviceVersion)
        parcel.writeString(title)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageDetailsModel> {
        override fun createFromParcel(parcel: Parcel): ImageDetailsModel {
            return ImageDetailsModel(parcel)
        }

        override fun newArray(size: Int): Array<ImageDetailsModel?> {
            return arrayOfNulls(size)
        }
    }
}