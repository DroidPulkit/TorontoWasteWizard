package pulkit.com.torontowastewizard.Adapter;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pulkit.com.torontowastewizard.Model.Waste;
import pulkit.com.torontowastewizard.R;

public class WasteAdapter extends RecyclerView.Adapter<WasteAdapter.WasteAdapterViewHolder> {

    ArrayList<Waste> wasteArrayList = new ArrayList<>();

    public  WasteAdapter(ArrayList<Waste> al){
        wasteArrayList = al;
    }

    @NonNull
    @Override
    public WasteAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.waste_search_list_layout, parent, false);

        return new WasteAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WasteAdapterViewHolder holder, int position) {
        String sTitle = wasteArrayList.get(position).getKeywords();
        String sDesc = wasteArrayList.get(position).getBody();
        String sBinName = wasteArrayList.get(position).getCategory();
        int sBinImage = R.drawable.garbagebin;
        switch (sBinName){
            case "Garbage":
                sBinImage = R.drawable.garbagebin;
                break;
            case "Blue Bin":
                sBinImage = R.drawable.bluebin;
                break;
            case "Oversize":
                sBinImage = R.drawable.oversize;
                break;
            case "HHW":
                sBinImage = R.drawable.hhw;
                break;
            case "Not Accepted":
                sBinImage = R.drawable.notaccepted;
                break;
            case "Depot":
                sBinImage = R.drawable.dropoff;
                break;
            case "Metal Items":
                sBinImage = R.drawable.metal;
                break;
            case "Yard Waste":
                sBinImage = R.drawable.yardwaste;
                break;
            case "Electronic Waste":
                sBinImage = R.drawable.electronic;
                break;
            case "Green Bin":
                sBinImage = R.drawable.greenbin;
                break;
            case "Christmas Tree":
                sBinImage = R.drawable.ctree;
                break;
            default:
                sBinImage = R.drawable.garbagebin;
                break;
        }

        holder.title.setText(sTitle);
        holder.desc.setText(Html.fromHtml(sDesc).toString());
        holder.binName.setText(sBinName);

        holder.binImage.setImageResource(sBinImage);

        //If anyone asks me in future what kind of things I am doing here, probably god know by then,
        //sorry on a thigh deadline to make apps so no comments
        SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("FavSharedPreferences", Context.MODE_PRIVATE);
        Set<String> stringSet = sharedPreferences.getStringSet("fav", null);
        if (stringSet == null){
            holder.fav.setImageResource(R.drawable.fav);
        } else {
            Log.d("onLoad", stringSet.toString());
            if (stringSet.contains(sTitle)){
                holder.fav.setImageResource(R.drawable.fav_selected);
            } else {
                holder.fav.setImageResource(R.drawable.fav);
            }
        }
    }

    @Override
    public int getItemCount() {
        return wasteArrayList.size();
    }

    public class WasteAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView binImage, fav;
        TextView binName, title, desc;
        public WasteAdapterViewHolder(View itemView){
            super(itemView);

            binImage = itemView.findViewById(R.id.search_bin_image);
            fav = itemView.findViewById(R.id.search_fav);
            binName = itemView.findViewById(R.id.search_bin_text);
            title = itemView.findViewById(R.id.search_title);
            desc = itemView.findViewById(R.id.search_desc);

            fav.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.search_fav:
                    //Do something
                    String title = wasteArrayList.get(getAdapterPosition()).getKeywords();
                    SharedPreferences sharedPreferences = v.getContext().getSharedPreferences("FavSharedPreferences", Context.MODE_PRIVATE);
                    Set<String> stringSet = sharedPreferences.getStringSet("fav", null);
                    if (stringSet == null){
                        //Doing first time click
                        Log.d("adapter", "In Null part");
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Set<String> addStringSet = new HashSet<>();
                        addStringSet.add(title);
                        editor.putStringSet("fav", addStringSet);
                        editor.apply();
                        fav.setImageResource(R.drawable.fav_selected);
                    } else {
                        Log.d("adapter", "not in Null part");
                        if (stringSet.contains(title)){
                            stringSet.remove(title);
                            fav.setImageResource(R.drawable.fav);
                        } else {
                            stringSet.add(title);
                            fav.setImageResource(R.drawable.fav_selected);
                        }
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putStringSet("fav", stringSet);
                        editor.apply();
                    }

                    break;
            }
        }
    }
}
