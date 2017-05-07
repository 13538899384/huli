package com.ygxy.xqm.huli.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ygxy.xqm.huli.R;
import com.ygxy.xqm.huli.bean.Intermediate;

import java.util.List;

/**
 * Created by XQM on 2017/4/28.
 */

public class IntermediateAdapter extends RecyclerView.Adapter<IntermediateAdapter.ViewHolder>{
    private LayoutInflater inflater;
    private Context mContext;
    private List<Intermediate> list;
    public IntermediateAdapter(Context context,List<Intermediate>list){
        inflater = LayoutInflater.from(context);
        mContext = context;
        this.list = list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = inflater.inflate(R.layout.intermediate_once_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Intermediate intermediate = list.get(position);
        holder.textView.setText(list.get(position).getNumber());
        Glide.with(mContext).load(list.get(position).getUrl1()).into(holder.imageView1);
        Glide.with(mContext).load(list.get(position).getUrl2()).into(holder.imageView2);

        holder.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Intermediate intermediate1:list){
                    intermediate1.isSelect = false;
                }
                list.get(position).isSelect = true;

                if (list.get(position).isSelect)
                {
                    holder.select0.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext,"0",Toast.LENGTH_SHORT).show();
                } else {
                    holder.select0.setVisibility(View.GONE);
                }
                notifyDataSetChanged();
            }
        });
        holder.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Intermediate intermediate1:list){
                    intermediate1.isSelect = false;
                }
                list.get(position).isSelect = true;

                if (list.get(position).isSelect)
                {
                    holder.select0.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext,"1",Toast.LENGTH_SHORT).show();
                } else {
                    holder.select0.setVisibility(View.GONE);
                }
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView textView;
        ImageView imageView1, imageView2,select0,select1;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            textView = (TextView) itemView.findViewById(R.id.intermediate_first_text);
            imageView1 = (ImageView) itemView.findViewById(R.id.intermediate_first_answer);
            imageView2 = (ImageView) itemView.findViewById(R.id.intermediate_first_respondence);
            select0 = (ImageView) itemView.findViewById(R.id.select0);
            select1 = (ImageView) itemView.findViewById(R.id.select1);
        }
    }
}
