package com.kaugirls.dalal.theedc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

// subclass extends from arraylist with the DataModel as the object

// CustomAdapter that populates the DataModel into the ListView
public class CustomAdapter extends ArrayAdapter<Assembly_item> {
    Context mContext;
    //The data model that is contained in the ArrayList is shown in class assembly item.
    private ArrayList<Assembly_item> dataSet;
    private int lastPosition = -1;

    //the first parameter is context, second parameter is Xml Layout for the row and the third parameter is the id of the TextView within the layout resource to be populated and last parameter is the array containing values.
    public CustomAdapter(ArrayList<Assembly_item> data, Context context) {
        super(context, R.layout.assembly_item, data);
        this.dataSet = data;
        this.mContext = context;
    }

    @Override
    //getView() is the method that returns the actual view used as a row within the ListView at a particular position.
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Assembly_item dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.assembly_item, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.Name);
            viewHolder.txtEnable = convertView.findViewById(R.id.Enable);
            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        viewHolder.txtName.setText(dataModel.assembly_name);
        if (dataModel.assembly_isEnable.equals("1")) {
            viewHolder.txtEnable.setBackgroundResource(R.drawable.enable);
        } else {
            viewHolder.txtEnable.setBackgroundResource(R.drawable.disable);
        }

        return convertView;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtEnable;
    }
}