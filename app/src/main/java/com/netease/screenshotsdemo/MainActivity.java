package com.netease.screenshotsdemo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends ActionBarActivity {
    /**
     *  全屏截图按钮
     */
    private Button mBtn;
    /**
     * 某个view的截图
     */
    private Button mViewShotBtn;
    private Button mLocalVideoShotBtn;

    private ImageView mImg;
    private VideoView mVideoView;

    private Uri mVideoUri;
    private String mVideoPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        setListeners();

        playLocalVideo();
    }

    /**
     * view初始化
     */
    private void initView(){
        mBtn = (Button)findViewById(R.id.btn);
        mViewShotBtn = (Button)findViewById(R.id.btn_shot_view);
        mLocalVideoShotBtn = (Button)findViewById(R.id.btn_shot_video_local);
        mImg = (ImageView)findViewById(R.id.img);
        mVideoView = (VideoView)findViewById(R.id.videoview);
    }

    private void initData(){
        mVideoUri = Uri.parse("http://v.cctv.com/flash/mp4video28/TMS/2013/05/06/265114d5f2e641278098503f1676d017_h264418000nero_aac32-1.mp4");
        mVideoPath = "/sdcard/test.mp4";
    }

    /**
     * 设置监听
     */
    private void setListeners(){
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenShot.shoot(MainActivity.this);
            }
        });

        mViewShotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBitmapByView(mImg);
            }
        });

        mLocalVideoShotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotLocalVideo();
            }
        });
    }

    /**
     * 播放网络的视频
     */
    private void playRemoteVideo(){
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.setVideoURI(mVideoUri);
        mVideoView.requestFocus();
        mVideoView.start();
    }

    /**
     * 播放本地视频
     */
    private void playLocalVideo(){
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.setVideoPath(mVideoPath);
        mVideoView.requestFocus();
        mVideoView.start();
    }

    /**
     * 截取Imageview的屏幕显示
     * **/
    public static Bitmap getBitmapByView(ImageView imageView) {
        Bitmap bitmap = null;

        // 创建对应大小的bitmap
        bitmap = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(),
                Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        imageView.draw(canvas);
        // 测试输出
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("/sdcard/screen_view_shot.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (null != out) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 本地视频播放截屏
     * @return
     */
    private Bitmap shotLocalVideo(){
        MediaMetadataRetriever rev = new MediaMetadataRetriever();
        rev.setDataSource(mVideoPath);
        Bitmap bitmap = rev.getFrameAtTime(mVideoView.getCurrentPosition() * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        mImg.setImageBitmap(bitmap);
        return bitmap;
    }

}
