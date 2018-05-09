package com.zxx.cookbook.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.zxx.cookbook.R;
import com.zxx.cookbook.interfaces.IRecyclerViewItemClick;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public  abstract  class ListBaseAdapter<T> extends RecyclerView.Adapter {
    protected Context mContext;
    protected LayoutInflater mInfalter;
    protected List<T> mList = new ArrayList<>();
    protected IRecyclerViewItemClick mListener;
    protected final int EMPTY=1001;
    protected final int NORMAL=1002;
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public List<T> getDataList() {
        return mList;
    }

    public void setDataList(Collection<T> list) {
        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addAll(Collection<T> list) {
        int lastIndex = this.mList.size();
        if (this.mList.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }



    public void remove(int position) {
        if(this.mList.size() > 0) {
            mList.remove(position);
            notifyItemRemoved(position);
        }

    }


    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }
}
