package com.junhee.android.review_mp3;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * Created by JunHee on 2017. 6. 17..
 */

public class Player {

    public static final int STOP = 0;
    public static final int PLAY = 1;
    public static final int PAUSE = 2;
    public static MediaPlayer player = null;
    public static int playerStatus = STOP;

    public static void play(Uri musicUri, Context context) {

        if (player != null) {
            player.release();
        }
        player = MediaPlayer.create(context, musicUri);
        player.setLooping(false);
        player.start();
        playerStatus = PLAY;

    }

    public static void pause() {

        if (player != null) {
            player.pause();
        }
        playerStatus = PAUSE;
    }

    public static void replay() {

        if (player != null) {
            player.start();
        }
        playerStatus = PLAY;
    }

    public static int getDuration() {
        if (player != null)
            return player.getDuration();

        return 0;
    }

    public static int getCurrent() {
        if (player != null)
            return player.getCurrentPosition();

        return 0;
    }
}



