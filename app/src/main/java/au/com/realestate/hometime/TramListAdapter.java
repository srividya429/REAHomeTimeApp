package au.com.realestate.hometime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import au.com.realestate.hometime.models.TramDetails;

class TramListAdapter extends ArrayAdapter<TramDetails> {

    private List<TramDetails> dataSet;
    Context mContext;

    public TramListAdapter(List<TramDetails> northValues, Context context) {
        super(context, R.layout.list_item, northValues);
        this.dataSet = northValues;
        this.mContext=context;
    }
    // View lookup cache
    private static class ViewHolder {
        TextView txtTime;
        TextView txtRouteNum;
        TextView txtDestination;
        ImageView info;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TramDetails tramData = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.txtTime = convertView.findViewById(R.id.time);
            viewHolder.txtRouteNum = convertView.findViewById(R.id.routeNum);
            viewHolder.txtDestination= convertView.findViewById(R.id.txtDestination);

            viewHolder.info = convertView.findViewById(R.id.imgTram);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.txtTime.setText(tramData.getTime()+" Min");
        viewHolder.txtRouteNum.setText(tramData.getRouteNum());
        viewHolder.txtDestination.setText("To "+tramData.getDestination());
        viewHolder.info.setTag(position);

        // Return the completed view to render on screen
        return convertView;
    }
}
