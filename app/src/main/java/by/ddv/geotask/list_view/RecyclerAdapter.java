package by.ddv.geotask.list_view;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import by.ddv.geotask.fragments.FromFragment;
import by.ddv.geotask.R;
import by.ddv.geotask.fragments.ToFragment;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<GeocogingListItem> listItems;
    Context context;

    ViewGroup callingParent;


    public RecyclerAdapter(Context context, List<GeocogingListItem> listItems) {
        this.inflater = LayoutInflater.from(context);
        this.listItems = listItems;
    }


    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        callingParent = parent;

        View view = inflater.inflate(R.layout.list_item_object, parent, false);
        return new RecyclerAdapter.ViewHolder(view, context, listItems);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        GeocogingListItem listItem = listItems.get(position);
        holder.address.setText(listItem.getAddress());
        holder.coordinates.setText(listItem.getCoordinates());

        //set Background Color of recyclerview 's item
        if (position % 2 == 0){
            holder.itemView.setBackgroundColor(Color.parseColor("#E6EBDE"));
        } else holder.itemView.setBackgroundColor(Color.parseColor("#FFF3DE"));

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView address, coordinates;
        List<GeocogingListItem> listItems = new ArrayList<>();
        Context context;

        public ViewHolder(View view, Context context, List<GeocogingListItem> listItems){
            super(view);
            this.listItems = listItems;
            this.context = context;
            view.setOnClickListener(this);
            address = (TextView) view.findViewById(R.id.tvAddressObject);
            coordinates = (TextView) view.findViewById(R.id.tvCoordinatesObject);

            //get the height of recyclerview 's item
            //view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            //FromFragment.heightListItem = view.getMeasuredHeight();
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            GeocogingListItem geocogingListItem = this.listItems.get(position);

            if (callingParent.getTag().equals("FromFragment")){
                FromFragment.addPoint(geocogingListItem.getLat(), geocogingListItem.getLng(), geocogingListItem.getAddress());
            } else {
                ToFragment.addPoint(geocogingListItem.getLat(), geocogingListItem.getLng(), geocogingListItem.getAddress());
            }
        }
    }


}
