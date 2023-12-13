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
     val price: Double,
     val availability: List<String> = emptyList(),

     ): Parcelable {
     constructor(parcel: Parcel) : this(
         parcel.readString() ?: "",
         parcel.readString() ?: "",
         parcel.readString() ?: "",
         parcel.readString() ?: "",
         parcel.readString() ?: "",
         parcel.readString() ?: "",
         parcel.readDouble(),
         parcel.createStringArrayList() ?: emptyList()
     )

     override fun describeContents(): Int {
         return 0
     }


     override fun writeToParcel(parcel: Parcel, flags: Int) {
         parcel.writeString(_id)
         parcel.writeString(fullname)
         parcel.writeString(location)
         parcel.writeString(image)
         parcel.writeString(description)
         parcel.writeString(reviews)
         parcel.writeDouble(price)
         parcel.writeStringList(availability)
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
    val guides: List<Guide>
)


