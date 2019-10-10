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
import java.util.Set;

import pulkit.com.torontowastewizard.Model.Waste;
import pulkit.com.torontowastewizard.R;

public class FavWasteAdapter extends RecyclerView.Adapter<FavWasteAdapter.FavWasteAdapterViewHolder> {

    ArrayList<Waste> wasteArrayList = new ArrayList<>();

    public FavWasteAdapter(ArrayList<Waste> al){
        wasteArrayList = al;
    }


    @NonNull
    @Override
    public FavWasteAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.waste_search_list_layout, parent, false);

        return new FavWasteAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavWasteAdapterViewHolder holder, int position) {

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

        holder.fav.setImageResource(R.drawable.fav_selected);

    }

    @Override
    public int getItemCount() {
        return wasteArrayList.size();
    }

    public class FavWasteAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView binImage, fav;
        TextView binName, title, desc;

        public FavWasteAdapterViewHolder(@NonNull View itemView) {
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
                        Log.d("FavWasteAdapter", "yo this should not have happened!!!");
                    } else {
                        Log.d("adapter", "not in Null part");
                        if (stringSet.contains(title)){
                            Log.d("FavWasteAdapter", "stringSet Contains title");
                            stringSet.remove(title);
                            fav.setImageResource(R.drawable.fav);
                        } else {
                            Log.d("FavWasteAdapter", "Why it is not in the sharedpref why?????");
                        }
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putStringSet("fav", stringSet);
                        editor.commit();
                    }

                    break;
            }
        }
    }
}
