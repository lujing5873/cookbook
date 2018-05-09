package com.zxx.cookbook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zxx.cookbook.R;

import java.util.List;

/**
 * Created by dell on 2018/5/9.
 */

public class ListPopupWindowAdapter extends BaseAdapter {
    private List<String> mList;
    private Context mContext;
    public ListPopupWindowAdapter(Context context,List<String> mList){
        this.mContext=context;
        this.mList=mList;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView =  LayoutInflater.from(mContext).inflate(R.layout.item_popup, parent, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.item_popup);
            convertView.setTag(viewHolder);//讲ViewHolder存储在View中

        }else{
            viewHolder = (ViewHolder) convertView.getTag();//重获取viewHolder
        }
        viewHolder.textView.setText(mList.get(position));
        return convertView;
    }
    //内部类
    class ViewHolder{
        TextView textView;
    }
}
