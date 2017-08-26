package arkratos.gamedev.com.colirfy;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by AkhilRaja on 22/08/17.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
private String[][] mDataset;
private int selectedPos = 0;

    public CustomAdapter(String s[][])
    {
        mDataset = s;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View circle1;
        public View circle2;
        public View circle3;
        public View circle4;
        public View circle5;

        public ViewHolder(View itemView) {
            super(itemView);
            circle1 = itemView.findViewById(R.id.circle1);
            circle2 = itemView.findViewById(R.id.circle2);
            circle3 = itemView.findViewById(R.id.circle3);
            circle4 = itemView.findViewById(R.id.circle4);
            circle5 = itemView.findViewById(R.id.circle5);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.listview, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CustomAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.palette_selected = i;
                MainActivity.changeBoom();
                Log.d("RecyclerView ","Touched");

                notifyItemChanged(selectedPos);
                selectedPos = i;
                notifyItemChanged(selectedPos);
                //viewHolder.itemView.setSelected(true);

            }
        });
        viewHolder.itemView.setBackgroundColor((selectedPos == i)
                ? viewHolder.itemView.getResources().getColor(R.color.cardview_dark_background)
                : viewHolder.itemView.getResources().getColor(R.color.White));

        viewHolder.circle1.setBackgroundColor(Color.parseColor(mDataset[i][0]));
        viewHolder.circle2.setBackgroundColor(Color.parseColor(mDataset[i][1]));
        viewHolder.circle3.setBackgroundColor(Color.parseColor(mDataset[i][2]));
        viewHolder.circle4.setBackgroundColor(Color.parseColor(mDataset[i][3]));
        viewHolder.circle5.setBackgroundColor(Color.parseColor(mDataset[i][4]));
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
