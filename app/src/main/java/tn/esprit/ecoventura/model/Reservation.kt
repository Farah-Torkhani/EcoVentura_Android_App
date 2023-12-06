package tn.esprit.ecoventura.model

import android.os.Parcel
import android.os.Parcelable

data class Reservation(
    val id: String,
    val guideId: String,
    val userId: String,
    val location: String,
    val hoursBooked: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt() // Corrected to read as Int
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(guideId)
        parcel.writeString(userId)
        parcel.writeString(location)
        parcel.writeInt(hoursBooked) // Corrected to write as Int
    }

    companion object CREATOR : Parcelable.Creator<Reservation> {
        override fun createFromParcel(parcel: Parcel): Reservation {
            return Reservation(parcel)
        }

        override fun newArray(size: Int): Array<Reservation?> {
            return arrayOfNulls(size)
        }
    }
}

data class ReservationRequest(
    val userId: String,
    val hoursBooked: Int,
    val location: String
)

data class ReservationResponse(
    val statusCode: Int,
    val message: String,
    val reservation: Reservation
)
