package tn.esprit.ecoventura.model
import android.os.Parcel
import android.os.Parcelable
 data class Guide(
    val _id: String,
    val fullname: String,
    val location: String,
    val image: String,
    val description: String,
    val reviews: String,
    val price: String
): Parcelable {
     constructor(parcel: Parcel) : this(
         parcel.readString() ?: "",
         parcel.readString() ?: "",
         parcel.readString() ?: "",
         parcel.readString() ?: "",
         parcel.readString() ?: "",
         parcel.readString() ?: "",
         parcel.readString() ?: "",
     )

     override fun describeContents(): Int {
         return 0
     }


     override fun writeToParcel(parcel: Parcel, flags: Int) {
         parcel.writeString(fullname)
         parcel.writeString(location)
         parcel.writeString(image)
         parcel.writeString(description)
         parcel.writeString(reviews)
         parcel.writeString(price)

     }
     companion object CREATOR : Parcelable.Creator<Guide> {
         override fun createFromParcel(parcel: Parcel): Guide {
             return Guide(parcel)
         }

         override fun newArray(size: Int): Array<Guide?> {
             return arrayOfNulls(size)
         }
     }
 }

data class GuideApiResponse(
    val statusCode: Int,
    val message: String,
    val guides: List<Guide>
)


