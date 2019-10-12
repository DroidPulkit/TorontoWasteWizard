package pulkit.com.torontowastewizard.model

import android.os.Parcel
import android.os.Parcelable

//class Waste : Serializable {
//    var body: String? = null
//    var category: String? = null
//    var title: String? = null
//    var keywords: String? = null
//
//    constructor(body: String, category: String, title: String, keywords: String) {
//        this.body = body
//        this.category = category
//        this.title = title
//        this.keywords = keywords
//    }
//
//    constructor() {
//
//    }
//}

data class Waste(
  val body: String? = null,
  val category: String? = null,
  val title: String? = null,
  val keywords: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(body)
        parcel.writeString(category)
        parcel.writeString(title)
        parcel.writeString(keywords)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Waste> {
        override fun createFromParcel(parcel: Parcel): Waste {
            return Waste(parcel)
        }

        override fun newArray(size: Int): Array<Waste?> {
            return arrayOfNulls(size)
        }
    }
}