package com.ygxy.xqm.huli.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.ygxy.xqm.huli.DaoNiaoAdvancedActivity;
import com.ygxy.xqm.huli.R;
import com.ygxy.xqm.huli.adapter.IntermediateAdapter;
import com.ygxy.xqm.huli.bean.Intermediate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 中级练习场
 * Created by XQM on 2017/4/28.
 */
public class DaoIntermediateFragment extends Fragment implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    @BindView(R.id.intermediate_first_right)
    VideoView videoView;
    @BindView(R.id.once_pass_listView)RecyclerView recyclerView;
    private Intermediate[] intermediates = {
            new Intermediate(false,"第一题：擦车顺序",R.drawable.icon_tiwei1_error,R.drawable
                    .icon_tiwei1_right),
            new Intermediate(false,"第二题：拿取无菌持物钳",R.drawable.icon_xiaodu2_right,R.drawable
                    .icon_xiaodu2_error),
            new Intermediate(false,"第三题：打开无菌治疗巾手法",R.drawable.icon_open_bao3_error,R.drawable
                    .icon_open_bao3_right),
            new Intermediate(false,"第四题：铺盘",R.drawable.icon_dongjin4_right,R.drawable
                    .icon_dongjin4_right),
            new Intermediate(false,"第五题：打开无菌盅",R.drawable.icon_shuinang6_error,R.drawable
                    .icon_shuinang6_right),
            new Intermediate(false,"第六题：倒无菌溶液",R.drawable.icon_biaoqian7_error,R.drawable
                    .icon_biaoqian7_right),
            new Intermediate(false,"第七题：封盘",R.drawable.icon_shoutao8_error,R.drawable
                    .icon_shoutao8_right)};
    private List<Intermediate> list = new ArrayList<>();
    private IntermediateAdapter adapter;
    private MediaController mediaController;
    @OnClick(R.id.intermediate_btn_submit)void intermediate_btn_submit(){
        Intent intent = new Intent(getActivity(), DaoNiaoAdvancedActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
    @OnClick(R.id.intermediate_first_right)void intermediate_first_right(){
//        videoView.start();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intermediate_once_pass,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mediaController = new MediaController(getActivity());
        //设置控制栏
        videoView.setMediaController(mediaController);
        //获取焦点
        videoView.requestFocus();
        Uri videoUri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/v1.mp4");
        videoView.setVideoURI(videoUri);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        list.clear();
        for (int i = 0;i < intermediates.length;i++){
//            Random random = new Random();
//            int index = random.nextInt(intermediates.length);
            list.add(intermediates[i]);
        }
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new IntermediateAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Toast.makeText(getActivity(),"获取视频失败",Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        videoView.start();
    }

//    /**
//     * 回调内部类
//     */
//    private class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {
//        @Override
//        public void onCompletion(MediaPlayer mp) {
//            Toast.makeText( getActivity(), "播放完成了", Toast.LENGTH_SHORT).show();
//            mp.start();
//        }
//    }
@Override
public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        //横屏
    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
        //竖屏
    }
}
}
