package com.ygxy.xqm.huli.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ygxy.xqm.huli.R;
import com.ygxy.xqm.huli.bean.Heroes;

import java.util.List;

/**
 * Created by XQM on 2017/3/30.
 */

public class PkHeroesAdapter extends RecyclerView.Adapter<PkHeroesAdapter.ViewHolder>{
    private Context mContext;
    private List<Heroes> heroesList;
    private LayoutInflater inflater;

    public PkHeroesAdapter(Context context,List<Heroes> heroesList){
        inflater = LayoutInflater.from(context);
        this.heroesList = heroesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = inflater.inflate(R.layout.heros_range_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Heroes heroes = heroesList.get(position);
        holder.mTvRange.setText(heroes.getHeroes_range()+"");
        holder.mTvName.setText(heroes.getHeroes_name());
        holder.mTvGrade.setText(heroes.getHeroes_grade() + "分");
        holder.mTvNickname.setText(heroes.getHeroes_nickname());
    }

    @Override
    public int getItemCount() {
        return heroesList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTvRange,mTvGrade,mTvName,mTvNickname;
        public ViewHolder(View view) {
            super(view);
            mTvRange = (TextView) view.findViewById(R.id.pk_heroes_range);
            mTvGrade = (TextView) view.findViewById(R.id.pk_heroes_grade);
            mTvName = (TextView) view.findViewById(R.id.pk_heroes_name);
            mTvNickname = (TextView) view.findViewById(R.id.pk_heroes_nickname);
        }
    }
}
