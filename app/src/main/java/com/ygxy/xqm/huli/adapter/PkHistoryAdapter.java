package com.ygxy.xqm.huli.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ygxy.xqm.huli.R;
import com.ygxy.xqm.huli.bean.History;

import java.util.List;

/**
 * Created by XQM on 2017/3/30.
 */

public class PkHistoryAdapter extends RecyclerView.Adapter<PkHistoryAdapter.ViewHolder>{
    private Context mContext;
    private LayoutInflater inflater;
    private List<History> historyList;

    public PkHistoryAdapter(Context mContext,List<History> historyList){
        inflater = LayoutInflater.from(mContext);
        this.historyList = historyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = inflater.inflate(R.layout.pk_history_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        History history = historyList.get(position);
        holder.mTvName.setText("李思思");
        holder.mTvReferee1.setText("张三");
        holder.mTvReferee2.setText("李四");
        holder.mTvGrade1.setText("100分");
        holder.mTvGrade2.setText("98分");
        holder.mTvResult.setText("赢");
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTvName,mTvReferee1,mTvReferee2,mTvGrade1,mTvGrade2,mTvResult;
        public ViewHolder(View view) {
            super(view);
            mTvName = (TextView) view.findViewById(R.id.pk_history_name);
            mTvReferee1 = (TextView) view.findViewById(R.id.pk_history_grade1);
            mTvGrade1 = (TextView) view.findViewById(R.id.pk_history_grade1);
            mTvReferee2 = (TextView) view.findViewById(R.id.pk_history_referee2);
            mTvGrade2 = (TextView) view.findViewById(R.id.pk_history_grade2);
            mTvResult = (TextView) view.findViewById(R.id.pk_history_result);

        }
    }
}
