package com.ygxy.xqm.huli;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 中级阶段
 * Created by XQM on 2017/4/24.
 */

public class Pharmaceutical_Preparations extends AppCompatActivity{
    @BindView(R.id.gridView_preparations)GridView gridView;
    private int[] imageId = new int[] { R.mipmap.icon_logo, R.mipmap.icon_logo,
            R.mipmap.icon_logo,R.mipmap.icon_logo,R.mipmap.icon_logo,R.mipmap.icon_logo}; // 定义并初始化保存图片id的数组
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharmaceutical_preparations);
        ButterKnife.bind(this);

        GridViewAdapter gridViewAdapter = new GridViewAdapter();
        gridView.setAdapter(gridViewAdapter);
        // 为GridView设定监听器
//        gridView.setOnItemClickListener(new gridViewListener());
    }

    private class GridViewAdapter extends BaseAdapter{

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageview; // 声明ImageView的对象
            if (convertView == null) {
                imageview = new ImageView(Pharmaceutical_Preparations.this); // 实例化ImageView的对象
                imageview.setScaleType(ImageView.ScaleType.CENTER_INSIDE); // 设置缩放方式
                imageview.setPadding(5, 5, 5, 5); // 设置ImageView的内边距
            } else {
                imageview = (ImageView) convertView;
            }
            imageview.setImageResource(imageId[position]); // 为ImageView设置要显示的图片
            return imageview; // 返回ImageView
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return imageId.length;
        }
    }
}
