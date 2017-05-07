package com.ygxy.xqm.huli.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Intermediate intermediate = list.get(position);
        holder.textView.setText(intermediate.getNumber());
        Glide.with(mContext).load(intermediate.getUrl1()).into(holder.imageView1);
        Glide.with(mContext).load(intermediate.getUrl2()).into(holder.imageView2);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView textView;
        ImageView imageView1, imageView2;
        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            textView = (TextView) itemView.findViewById(R.id.intermediate_first_text);
            imageView1 = (ImageView) itemView.findViewById(R.id.intermediate_first_answer);
            imageView2 = (ImageView) itemView.findViewById(R.id.intermediate_first_respondence);
        }
    }
}
