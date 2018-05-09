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
import com.zxx.cookbook.bean.Food;
import com.zxx.cookbook.interfaces.IRecyclerViewItemClick;

import org.w3c.dom.Text;

import java.net.PortUnreachableException;
import java.util.List;

/**
 * Created by MDM on 2018/5/9.
 */

public class FoodAdapter extends ListBaseAdapter<Food>{
    public FoodAdapter(Context context, List<Food> foods, IRecyclerViewItemClick listener) {
        this.mContext = context;
        this.mList = foods;
        this.mListener=listener;
        this.mInfalter = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= mInfalter.inflate(R.layout.item_search, null);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder= (MyHolder) holder;
        myHolder.itemView.setTag(position);
        myHolder.title.setText(mList.get(position).getFoodName());
        myHolder.content.setText(mList.get(position).getMaterial());
        Glide.with(mContext).load(mList.get(position).getImage().getUrl()).into(myHolder.iv);
    }

    class  MyHolder extends RecyclerView.ViewHolder{
        private TextView title,content;
        private ImageView iv;
        public MyHolder(final View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.item_search_title);
            content=itemView.findViewById(R.id.item_search_content);
            iv=itemView.findViewById(R.id.item_search_iv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener==null){
                        return;
                    }
                    mListener.onItemClick(v,getAdapterPosition());
                }
            });
        }
    }

}
