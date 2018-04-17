package com.kaugirls.dalal.theedc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomShelterAdapter extends ArrayAdapter<Shelter_item> {

    Context mContext;
    private ArrayList<Shelter_item> dataSet;
    private int lastPosition = -1;

    public CustomShelterAdapter(ArrayList<Shelter_item> data, Context context) {
        super(context, R.layout.assembly_item, data);
        this.dataSet = data;
        this.mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Shelter_item dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        final View result;
        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.shelter_item, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.Name);
            viewHolder.txtEnable = convertView.findViewById(R.id.Enable);
            viewHolder.txtcapacity = convertView.findViewById(R.id.capacity);
            viewHolder.txtStatus = convertView.findViewById(R.id.status);
            viewHolder.txtdescription = convertView.findViewById(R.id.description);
            viewHolder.txttype = convertView.findViewById(R.id.type_shelter);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.txtName.setText(dataModel.shelter_name);
        viewHolder.txttype.setText(dataModel.shelter_type);
        viewHolder.txtStatus.setText(dataModel.shelter_status);
        viewHolder.txtcapacity.setText(dataModel.shelter_capacity);
        viewHolder.txtdescription.setText(dataModel.shelter_description);

        if (dataModel.shelter_isEnable.equals("1")) {
            viewHolder.txtEnable.setBackgroundResource(R.drawable.enable);
        } else {
            viewHolder.txtEnable.setBackgroundResource(R.drawable.disable);
        }

        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txttype;
        TextView txtStatus;
        TextView txtcapacity;
        TextView txtdescription;
        TextView txtEnable;
    }
}