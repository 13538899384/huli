package com.ygxy.xqm.huli.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ygxy.xqm.huli.R;
import com.ygxy.xqm.huli.bean.Practice;
import com.ygxy.xqm.huli.PracticeItemActivity;

import java.util.List;

/**
 * Created by XQM on 2017/3/17.
 */

public class PracticeAdapter extends RecyclerView.Adapter<PracticeAdapter.ViewHolder>{
    private Context mContext;
    private List<Practice> practiceList;
    private LayoutInflater inflater;

    public PracticeAdapter(Context context,List<Practice> practiceList){
        inflater = LayoutInflater.from(context);
        this.practiceList = practiceList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = inflater.inflate(R.layout.practice_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PracticeItemActivity.class);
                mContext.startActivity(intent);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Practice practice = practiceList.get(position);
        holder.mTvPracticeName.setText("无菌技术");
        holder.mTvPracticeLevel.setText("初  中  高");
        holder.getmTvPracticeNameLevel.setText("初");
    }

    @Override
    public int getItemCount() {
        return practiceList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView mImageView;
        TextView mTvPracticeName;
        TextView mTvPracticeLevel;
        TextView getmTvPracticeNameLevel;
        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            mTvPracticeName = (TextView) view.findViewById(R.id.practice_name);
            mTvPracticeLevel = (TextView) view.findViewById(R.id.practice_level);
            mImageView = (ImageView) view.findViewById(R.id.practice_image);
            getmTvPracticeNameLevel = (TextView) view.findViewById(R.id.practice_name_level);
        }
    }
}
