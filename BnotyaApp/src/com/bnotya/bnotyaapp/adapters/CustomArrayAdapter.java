package com.bnotya.bnotyaapp.adapters;

import com.bnotya.bnotyaapp.R;
import com.bnotya.bnotyaapp.models.ListItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter<ListItem>
{
	Context _context; 
    int _layoutResourceId;    
    ListItem[] _data;
    
    public CustomArrayAdapter(Context context, int layoutResourceId, ListItem[] data) {
        super(context, layoutResourceId, data);
        _layoutResourceId = layoutResourceId;
        _context = context;
        _data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {        
        ItemHolder holder = null;
        
        if(convertView == null)
        {   
        	convertView = LayoutInflater.from(getContext()).inflate(_layoutResourceId, null);
            
            holder = new ItemHolder();            
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.icon);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            
            convertView.setTag(holder);
        }
        else
        {
            holder = (ItemHolder)convertView.getTag();
        }
        
        ListItem itemData = _data[position];
        holder.imgIcon.setImageResource(itemData.getIcon());
        holder.txtTitle.setText(itemData.getTitle());        
        
        return convertView;
    }
    
    static class ItemHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}
