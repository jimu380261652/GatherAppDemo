package com.jimu.dev;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by Ljh on 2020/12/29.
 * Description:本地语音播报
 */
public class VoiceUtils {
    private static final String TAG = "VoiceUtils";
    //
    private static VoiceUtils mInstance;
    private static MediaPlayer player;

    //
    public static VoiceUtils getInstance() {
        if (mInstance == null) {
            synchronized (VoiceUtils.class) {
                if (mInstance == null) {
                    mInstance = new VoiceUtils();
                    return mInstance;
                }
            }
        }
        return mInstance;
    }

    public VoiceUtils() {
        init();
    }

    private void init() {
        Log.d(TAG, "init");
        //SystemPropertyUtil.set("sys.aud.usb", "1");
        //SystemPropertyUtil.set("use_nuplayer", true);
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d(TAG, "onCompletion");
                //SystemPropertyUtil.set("use_nuplayer", false);
            }
        });
    }

    public void startPlay(Context context, VoiceTypeEnum typeEnum) {
        Log.d(TAG, "startPlay:");
        if (player == null) {
            init();
        }
        try {
            player.reset();
            AssetFileDescriptor file = context.getResources().openRawResourceFd(typeEnum.rawId);
            player.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            player.setLooping(false);
            player.prepareAsync();
            player.setOnPreparedListener(preparedListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MediaPlayer.OnPreparedListener preparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if (player != null) {
                player.start();
            }
        }
    };

    public void stopPlay() {
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
            player = null;
        }
    }

    public enum VoiceTypeEnum {
        VOICE_COOK_FINISH(R.raw.cook_finish),
        VOICE_COOK_STOP(R.raw.cook_stop),
        VOICE_COOK_YR(R.raw.yure_finish),
        VOICE_ADD_WATER(R.raw.jiashui);
        public int rawId;

        VoiceTypeEnum(int rawId) {
            this.rawId = rawId;
        }
    }
}

