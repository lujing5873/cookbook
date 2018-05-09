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

import org.w3c.dom.Text;

import java.net.PortUnreachableException;
import java.util.List;

/**
 * Created by MDM on 2018/5/9.
 */

public class SearchAdpater extends RecyclerView.Adapter<SearchAdpater.MyHolder> {
    private Context context;
    private List<Food> foods;

    public SearchAdpater(Context context, List<Food> foods) {
        this.context = context;
        this.foods = foods;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_search,null);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.title.setText(foods.get(position).getFoodName());
        holder.content.setText(foods.get(position).getMaterial());
        Glide.with(context).load(foods.get(position).getImage().getUrl()).into(holder.iv);
    }

    @Override
    public int getItemCount() {
        return foods.size();
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
                    onItemClickLisentener.onItemClick((Integer) itemView.getTag());
                }
            });
        }
    }
    private OnItemClickLisentener onItemClickLisentener;
    public void setOnItemClickLinsentener(OnItemClickLisentener onItemClickLinsentener){
        this.onItemClickLisentener=onItemClickLinsentener;
    }
    public interface OnItemClickLisentener{
        void onItemClick(int position);
    }
}
