package pulkit.com.torontowastewizard.Adapter

import android.content.Context
import android.content.SharedPreferences
import androidx.recyclerview.widget.RecyclerView
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import java.util.ArrayList
import java.util.HashSet

import pulkit.com.torontowastewizard.Model.Waste
import pulkit.com.torontowastewizard.R

class WasteAdapter(al: ArrayList<Waste>) : RecyclerView.Adapter<WasteAdapter.WasteAdapterViewHolder>() {

    internal var wasteArrayList = ArrayList<Waste>()

    init {
        wasteArrayList = al
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WasteAdapterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.waste_search_list_layout, parent, false)

        return WasteAdapterViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WasteAdapterViewHolder, position: Int) {
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

        //If anyone asks me in future what kind of things I am doing here, probably god know by then,
        //sorry on a thigh deadline to make apps so no comments
        val sharedPreferences = holder.itemView.context.getSharedPreferences("FavSharedPreferences", Context.MODE_PRIVATE)
        val stringSet = sharedPreferences.getStringSet("fav", null)
        if (stringSet == null) {
            holder.fav.setImageResource(R.drawable.fav)
        } else {
            Log.d("onLoad", stringSet.toString())
            if (stringSet.contains(sTitle)) {
                holder.fav.setImageResource(R.drawable.fav_selected)
            } else {
                holder.fav.setImageResource(R.drawable.fav)
            }
        }
    }

    override fun getItemCount(): Int {
        return wasteArrayList.size
    }

    inner class WasteAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
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
                    val title = wasteArrayList[adapterPosition].keywords ?: ""
                    val sharedPreferences = v.context.getSharedPreferences("FavSharedPreferences", Context.MODE_PRIVATE)
                    val stringSet = sharedPreferences.getStringSet("fav", null)
                    if (stringSet == null) {
                        //Doing first time click
                        Log.d("adapter", "In Null part")
                        val editor = sharedPreferences.edit()
                        val addStringSet = HashSet<String>()
                        addStringSet.add(title)
                        editor.putStringSet("fav", addStringSet)
                        editor.apply()
                        fav.setImageResource(R.drawable.fav_selected)
                    } else {
                        Log.d("adapter", "not in Null part")
                        if (stringSet.contains(title)) {
                            stringSet.remove(title)
                            fav.setImageResource(R.drawable.fav)
                        } else {
                            stringSet.add(title)
                            fav.setImageResource(R.drawable.fav_selected)
                        }
                        val editor = sharedPreferences.edit()
                        editor.putStringSet("fav", stringSet)
                        editor.apply()
                    }
                }
            }
        }
    }
}
