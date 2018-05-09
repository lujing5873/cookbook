package com.zxx.cookbook.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zxx.cookbook.R;
import com.zxx.cookbook.bean.CookBook;
import com.zxx.cookbook.interfaces.IRecyclerViewItemClick;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * cookbook Adapter
 */

public class CookbookAdapter extends ListBaseAdapter<CookBook> {
    public CookbookAdapter(Context context, List<CookBook> list, IRecyclerViewItemClick listener) {
        this.mContext = context;
        this.mList = list;
        this.mListener = listener;
        this.mInfalter = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInfalter.inflate(R.layout.item_cookbook, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)  {
        ViewHolder viewHolder= (ViewHolder) holder;
        CookBook cookBook=mList.get(position);
        viewHolder.cookbookItemName.setText(cookBook.getCookbookName());
        viewHolder.cookbookItemNumber.setText(String.valueOf(cookBook.getNumber()));
        Glide.with(mContext).load(cookBook.getCookbookImage()==null?R.mipmap.default_img:cookBook.getCookbookImage().getUrl()).into(viewHolder.cookbookItemImg);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cookbook_item_img)
        ImageView cookbookItemImg;
        @BindView(R.id.cookbook_item_number)
        TextView cookbookItemNumber;
        @BindView(R.id.cookbook_item_name)
        TextView cookbookItemName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(v,getAdapterPosition());
                }
            });
        }
    }

}
