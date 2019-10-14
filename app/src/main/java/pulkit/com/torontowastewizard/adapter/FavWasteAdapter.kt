package pulkit.com.torontowastewizard.adapter

import android.content.Context
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pulkit.com.torontowastewizard.R
import pulkit.com.torontowastewizard.model.Waste

class FavWasteAdapter() : RecyclerView.Adapter<FavWasteAdapter.FavWasteAdapterViewHolder>() {

    private var wasteArrayList = ArrayList<Waste>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavWasteAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.waste_search_list_layout, parent, false)

        return FavWasteAdapterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavWasteAdapterViewHolder, position: Int) {

        val sTitle = wasteArrayList[position].keywords
        val sDesc = wasteArrayList[position].body
        val sBinName = wasteArrayList[position].category
        var sBinImage = R.drawable.garbagebin
        when (sBinName) {
            "Garbage" -> sBinImage = R.drawable.garbagebin
            "Blue Bin" -> sBinImage = R.drawable.bluebin
            "Oversize" -> sBinImage = R.drawable.oversize
            "HHW" -> sBinImage = R.drawable.hhw
            "Not Accepted" -> sBinImage = R.drawable.notaccepted
            "Depot" -> sBinImage = R.drawable.dropoff
            "Metal Items" -> sBinImage = R.drawable.metal
            "Yard Waste" -> sBinImage = R.drawable.yardwaste
            "Electronic Waste" -> sBinImage = R.drawable.electronic
            "Green Bin" -> sBinImage = R.drawable.greenbin
            "Christmas Tree" -> sBinImage = R.drawable.ctree
            else -> sBinImage = R.drawable.garbagebin
        }

        holder.title.text = sTitle
        holder.desc.text = Html.fromHtml(sDesc).toString()
        holder.binName.text = sBinName

        holder.binImage.setImageResource(sBinImage)

        holder.fav.setImageResource(R.drawable.fav_selected)

    }

    override fun getItemCount(): Int {
        return wasteArrayList.size
    }

    fun submitList(wasteList: List<Waste>) {
        this.wasteArrayList.clear()
        this.wasteArrayList.addAll(wasteList)
        notifyDataSetChanged()
    }

    inner class FavWasteAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var binImage: ImageView
        internal var fav: ImageView
        internal var binName: TextView
        internal var title: TextView
        internal var desc: TextView

        init {

            binImage = itemView.findViewById(R.id.search_bin_image)
            fav = itemView.findViewById(R.id.search_fav)
            binName = itemView.findViewById(R.id.search_bin_text)
            title = itemView.findViewById(R.id.search_title)
            desc = itemView.findViewById(R.id.search_desc)

            fav.setOnClickListener(this)
        }


        override fun onClick(v: View) {
            when (v.id) {
                R.id.search_fav -> {
                    //Do something
                    val title = wasteArrayList[adapterPosition].keywords
                    val sharedPreferences = v.context.getSharedPreferences("FavSharedPreferences", Context.MODE_PRIVATE)
                    val stringSet = sharedPreferences.getStringSet("fav", null)
                    if (stringSet == null) {
                        //Doing first time click
                        Log.d("FavWasteAdapter", "yo this should not have happened!!!")
                    } else {
                        Log.d("adapter", "not in Null part")
                        if (stringSet.contains(title)) {
                            Log.d("FavWasteAdapter", "stringSet Contains title")
                            stringSet.remove(title)
                            fav.setImageResource(R.drawable.fav)
                        } else {
                            Log.d("FavWasteAdapter", "Why it is not in the sharedpref why?????")
                        }
                        val editor = sharedPreferences.edit()
                        editor.putStringSet("fav", stringSet)
                        editor.commit()
                    }
                }
            }
        }
    }
}
