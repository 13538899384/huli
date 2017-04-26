package com.ygxy.xqm.huli.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ygxy.xqm.huli.R;
import com.ygxy.xqm.huli.bean.NotiHistory;

import java.util.List;

/**
 * Created by XQM on 2017/4/20.
 */

public class NotificationHistoryAdapter extends BaseAdapter{
    private List<NotiHistory> dataList;
    private LayoutInflater inflater;
    private Context mContext;
    private ViewHolder viewHolder;
    public NotificationHistoryAdapter(Context context,List<NotiHistory> dataList){
        mContext = context;
        inflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }
    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (viewHolder == null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.simple_list_item,null);//layout文件转化为convertView
            viewHolder.content = (TextView) convertView.findViewById(R.id.text_item);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
    static class ViewHolder{
        public TextView content;
    }
}
