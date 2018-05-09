package com.zxx.cookbook.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zxx.cookbook.R;
import com.zxx.cookbook.bean.Food;
import com.zxx.cookbook.interfaces.IRecyclerViewItemClick;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MDM on 2018/5/9.
 */

public class FoodManagerAdapter extends ListBaseAdapter<Food> {
    private boolean isManager;
    private static final int IS_MANAGER = 1003;

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public FoodManagerAdapter(Context context, List<Food> foods, IRecyclerViewItemClick listener) {
        this.mContext = context;
        this.mList = foods;
        this.mListener = listener;
        this.mInfalter = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.size() <= 0) {
            return EMPTY;
        } else if (isManager) {
            return IS_MANAGER;
        }
        return NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("onCreateViewHolder");
        if (viewType == EMPTY) {
            View view = mInfalter.inflate(R.layout.lay_empty, parent, false);
            return new EmptyHolder(view);
        } else if (viewType == IS_MANAGER) {
            View view = mInfalter.inflate(R.layout.item_manager, null);
            return new ViewHolder(view);
        }
        View view = mInfalter.inflate(R.layout.item_search, null);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        System.out.println("onBindViewHolder");
        if (holder instanceof EmptyHolder) {
            System.out.println("空数据");
            return;
        }
        final Food food=mList.get(position);
        if(holder instanceof ViewHolder){
            ViewHolder viewHolder= (ViewHolder) holder;

            viewHolder.itemView.setTag(position);
            viewHolder.title.setText(food.getFoodName());
            viewHolder.content.setText(food.getMaterial());
            Glide.with(mContext).load(food.getImage().getUrl()).into(viewHolder.iv);
            viewHolder.checkBox.setChecked(food.isCheck());
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(food.isCheck()){
                        food.setCheck(false);
                    }else{
                        food.setCheck(true);
                    }
                }
            });
            return;
        }
        MyHolder myHolder = (MyHolder) holder;
        myHolder.itemView.setTag(position);
        myHolder.title.setText(mList.get(position).getFoodName());
        myHolder.content.setText(mList.get(position).getMaterial());
        Glide.with(mContext).load(mList.get(position).getImage().getUrl()).into(myHolder.iv);
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView title, content;
        private ImageView iv;

        public MyHolder(final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_search_title);
            content = itemView.findViewById(R.id.item_search_content);
            iv = itemView.findViewById(R.id.item_search_iv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener == null) {
                        return;
                    }
                    mListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }

     class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_search_iv)
        ImageView iv;
        @BindView(R.id.item_search_title)
        TextView title;
        @BindView(R.id.item_search_check)
        CheckBox checkBox;
        @BindView(R.id.item_search_content)
        TextView content;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
