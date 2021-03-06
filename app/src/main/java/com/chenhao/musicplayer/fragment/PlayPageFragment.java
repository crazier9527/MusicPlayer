package com.chenhao.musicplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chenhao.musicplayer.R;
import com.chenhao.musicplayer.activity.MainActivity;
import com.chenhao.musicplayer.bean.MusicInfo;
import com.chenhao.musicplayer.messagemgr.MediaPlayerObserver;
import com.chenhao.musicplayer.messagemgr.MessageID;
import com.chenhao.musicplayer.messagemgr.MessageManager;
import com.chenhao.musicplayer.mod.MediaPlayerManager;
import com.chenhao.musicplayer.utils.DateUtils;
import com.chenhao.musicplayer.utils.ObjectSaveUtil;
import com.chenhao.musicplayer.utils.ToastUtil;

import java.util.List;

/**
 * Created by chenhao on 2016/11/7.
 */

public class PlayPageFragment extends Fragment implements View.OnClickListener {

    private ImageView mBackImageView;
    private TextView mMusicName;
    private TextView mSingerName;
    private SeekBar mSeekBar;
    private TextView mMusicCurrentTime;
    private TextView mMusicTotalTime;
    private ImageView mPlayAndPause;
    private ImageView mFrontMusic;
    private ImageView mNextMusic;
    private ImageView mPlayModel;
    private ImageView mMusicList;
    private int mMusicDuration;

    private String mName;
    private String mArtist;
    private Call mCall = null;
    private Thread mThread = null;
    private int mProgress;

    private MediaPlayerObserver mObserver = new MediaPlayerObserver() {

        @Override
        public void startMusic(List<MusicInfo> infos, int position) {
            mMusicName.setText(infos.get(position).getName());
            mPlayAndPause.setImageResource(R.drawable.btn_pause);
            mMusicDuration = MediaPlayerManager.getInstance().getMusicDuration();
            mSeekBar.setMax(mMusicDuration);
            mMusicTotalTime.setText(DateUtils.millisecondFormat(mMusicDuration));
            if (mThread == null) {
                mCall = new Call();
                mThread = new Thread(mCall);
                mThread.start();
            }
        }

        @Override
        public void stopMusic() {
            mPlayAndPause.setImageResource(R.drawable.btn_play);
            mSeekBar.setProgress(0);
            mSeekBar.setSecondaryProgress(0);
        }

        @Override
        public void onPause() {
            mPlayAndPause.setImageResource(R.drawable.btn_play);
            if (mCall != null) {
                mCall.cancel();
                mThread = null;
            }
        }

        @Override
        public void onBuffering(int i) {
            int currentBuffer = mMusicDuration / 100 * i;
            mMusicDuration =  MediaPlayerManager.getInstance().getMusicDuration();
            mSeekBar.setSecondaryProgress(currentBuffer > mMusicDuration ? mMusicDuration:currentBuffer);
        }

        @Override
        public void onPrepared(List<MusicInfo> infos, int position) {
        }

        @Override
        public void onRefreshUI(List<MusicInfo> infos, int position) {
            mMusicName.setText(infos.get(position).getName());
            if(!TextUtils.isEmpty(infos.get(position).getArtist())){
                mSingerName.setText(infos.get(position).getArtist());
                mSingerName.setVisibility(View.VISIBLE);
            }else{
                mSingerName.setVisibility(View.GONE);
            }
        }
    };

    public static PlayPageFragment getInstance(String name,String artist) {
        PlayPageFragment f = new PlayPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("MusicName", name);
        bundle.putString("Artist", artist);
        f.setArguments(bundle);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_page, container, false);
        MessageManager.getInstance().attachMessage(MessageID.OBSERVER_MEDIA_PLAYER, mObserver);
        Bundle args = getArguments();
        if (args != null) {
            mName = args.getString("MusicName");
            mArtist = args.getString("Artist");
        }
        initView(view);
        setListener();
        init();
        return view;
    }

    private void init() {
        int mode = MediaPlayerManager.getInstance().getPlayMode();
        setPlayModePic(mode);
        int musicDuration = MediaPlayerManager.getInstance().getMusicDuration();
        int musicCurrentPosition = MediaPlayerManager.getInstance().getMusicCurrentPosition();
        Log.e("chenhaolog", "当前播放 : " + musicCurrentPosition);
        mSeekBar.setMax(musicDuration);
        mSeekBar.setProgress(musicCurrentPosition);
        mMusicTotalTime.setText(DateUtils.millisecondFormat(musicDuration));
        if (MediaPlayerManager.getInstance().isPlaying()) {
            mPlayAndPause.setImageResource(R.drawable.btn_pause);
            if (mThread == null) {
                mCall = new Call();
                mThread = new Thread(mCall);
                mThread.start();
            }
        }
        mMusicName.setText(mName);
        if(!TextUtils.isEmpty(mArtist)){
            mSingerName.setText(mArtist);
            mSingerName.setVisibility(View.VISIBLE);
        }else{
            mSingerName.setVisibility(View.GONE);
        }
    }

    private void setListener() {
        mBackImageView.setOnClickListener(this);
        mPlayAndPause.setOnClickListener(this);
        mFrontMusic.setOnClickListener(this);
        mNextMusic.setOnClickListener(this);
        mPlayModel.setOnClickListener(this);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mMusicCurrentTime.setText(DateUtils.millisecondFormat(progress));
                mProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mCall != null) {
                    mCall.cancel();
                    mCall = null;
                    mThread = null;
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mThread == null) {
                    mCall = new Call();
                    mThread = new Thread(mCall);
                    mThread.start();
                }
                MediaPlayerManager.getInstance().seekTo(mProgress);
            }
        });
    }

    private void initView(View view) {
        mBackImageView = (ImageView) view.findViewById(R.id.back_img);
        mMusicName = (TextView) view.findViewById(R.id.music_name_text);
        mSingerName = (TextView) view.findViewById(R.id.singer_name_text);
        mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
        mMusicCurrentTime = (TextView) view.findViewById(R.id.music_current_time);
        mMusicTotalTime = (TextView) view.findViewById(R.id.music_total_time);
        mPlayAndPause = (ImageView) view.findViewById(R.id.play_and_pause_img);
        mFrontMusic = (ImageView) view.findViewById(R.id.front_img);
        mNextMusic = (ImageView) view.findViewById(R.id.next_img);
        mPlayModel = (ImageView) view.findViewById(R.id.play_model_img);
        mMusicList = (ImageView) view.findViewById(R.id.music_list_img);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_img:
                MainActivity.getInstance().getSupportFragmentManager().popBackStack();
                break;
            case R.id.play_and_pause_img:
                if (MediaPlayerManager.getInstance().getMediaPlayState() == MediaPlayerManager.MEDIA_PLAY_DEFAULT) {
                    ToastUtil.showToast(getContext(),"当前无歌曲播放");
                    return;
                }
                MediaPlayerManager.getInstance().pauseOrPlay();
                break;
            case R.id.next_img:
                MediaPlayerManager.getInstance().playerNext();
                break;
            case R.id.front_img:
                MediaPlayerManager.getInstance().playerFront();
                break;
            case R.id.play_model_img:
                int i = MediaPlayerManager.getInstance().getPlayMode();
                int mode = (i + 1) % 3;
                MediaPlayerManager.getInstance().setPlayMode(mode);
                setPlayModePic(mode);
                ObjectSaveUtil.saveInteger(getContext(), "play_mode", mode);
                break;
        }
    }

    private void setPlayModePic(int mode){
        switch (mode){
            case MediaPlayerManager.PLAY_MODE_LIST_CYCLE:
                mPlayModel.setImageResource(R.drawable.btn_list);
                break;
            case MediaPlayerManager.PLAY_MODE_SINGLE_CYCLE:
                mPlayModel.setImageResource(R.drawable.btn_single);
                break;
            case MediaPlayerManager.PLAY_MODE_RANDOM_CYCLE:
                mPlayModel.setImageResource(R.drawable.btn_random);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MessageManager.getInstance().detachMessage(MessageID.OBSERVER_MEDIA_PLAYER, mObserver);
        if (mCall != null) {
            mCall.cancel();
        }
    }

    public class Call implements Runnable {

        private boolean flag = true;

        public void cancel() {
            this.flag = false;
        }

        @Override
        public void run() {
            //设置当前进度
            while (flag) {
                if (MediaPlayerManager.getInstance().getMediaPlayState() != MediaPlayerManager.MEDIA_PLAY_RESET) {
                    int p = MediaPlayerManager.getInstance().getMusicCurrentPosition();
                    mSeekBar.setProgress(p);
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.w("chenhaolog", Thread.currentThread().getId() + "运行结束");
        }
    }
}
