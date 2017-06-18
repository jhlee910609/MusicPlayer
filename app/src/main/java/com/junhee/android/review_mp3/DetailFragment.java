package com.junhee.android.review_mp3;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.junhee.android.review_mp3.domain.Music;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    public static final int CHANGE_SEEKBAR = 99;
    static final String ARG1 = "position";
    public int position = -1;
    ViewHolder viewHolder = null;

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHANGE_SEEKBAR:
                    viewHolder.setSeekBarPosition(msg.arg1);
                    viewHolder.currentTime.setText(msg.arg1 + "");
                    break;
            }
        }
    };

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(int position) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG1, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pager, container, false);
        Bundle bundle = getArguments();
        position = bundle.getInt(ARG1);
        viewHolder = new ViewHolder(view, position);

        return view;

    }

    public List<Music.Item> getdatas() {
        Music music = Music.getInstance();
        music.loader(getContext());

        return music.getItems();
    }


    public class ViewHolder implements View.OnClickListener {

        ViewPager viewPager;
        ImageButton btnPrev, btnNext;
        Button btnPause;
        RelativeLayout layoutController;
        TextView title, artist, duration, currentTime;
        SeekBar seekBar;

        public ViewHolder(View view, int position) {

            viewPager = (ViewPager) view.findViewById(R.id.viewPager);
            btnPause = (Button) view.findViewById(R.id.btnPause);
            btnNext = (ImageButton) view.findViewById(R.id.btnNext);
            btnPrev = (ImageButton) view.findViewById(R.id.btnPrev);
            layoutController = (RelativeLayout) view.findViewById(R.id.layoutController);
            seekBar = (SeekBar) view.findViewById(R.id.seekBar);
            title = (TextView) view.findViewById(R.id.title);
            artist = (TextView) view.findViewById(R.id.detail_artist);
            duration = (TextView) view.findViewById(R.id.duration);
            currentTime = (TextView) view.findViewById(R.id.current);

            moveSeekBar();
            setOnClickListenter();
            setViewPager(position);
        }

        public void moveSeekBar() {

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (Player.playerStatus == Player.PLAY) {
                        if (fromUser)
                            Player.player.seekTo(progress);
                            currentTime.setText(Player.getCurrent() + "");
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        }

        public void setOnClickListenter() {

            btnPrev.setOnClickListener(this);
            btnPause.setOnClickListener(this);
            btnNext.setOnClickListener(this);

        }

        public void setViewPager(int position) {
            DetailAdapter adapter = new DetailAdapter(getdatas());
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(position);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // TODO 바로 앞, 뒤 음악만 됨
                // TODO 사진 함께 안 바뀜
                case R.id.btnPrev:

                    if (!getdatas().get(position - 1).equals("")) {
                        Player.play(getdatas().get(position - 1).musicUri, v.getContext());
                        position--;
                    } else {
                        Toast.makeText(v.getContext(), "이전 재생곡이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.btnPause:

                    Uri musicUri = getdatas().get(position).musicUri;
                    Player.play(musicUri, v.getContext());
                    duration.setText(Player.player.getDuration() + "");
                    // seekBar 의 최대길이를 지정
                    Log.d("DetailFragment", "duration=" + Player.getDuration());
                    seekBar.setMax(Player.getDuration());
                    // seekBar를 변경해주는 thread
                    new SeekBarThread(handler).start();
                    break;

                case R.id.btnNext:

                    if (!getdatas().get(position + 1).equals("")) {
                        Player.play(getdatas().get(position + 1).musicUri, v.getContext());
                        position++;
                    } else {
                        Toast.makeText(v.getContext(), "다음 재생곡이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }

        public void setSeekBarPosition(int current) {
            seekBar.setProgress(current);
        }
    }
}

class SeekBarThread extends Thread {

    Handler handler;
    boolean runFlag = true;

    public SeekBarThread(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {

        while (runFlag) {

            int current = Player.getCurrent();

            Message msg = new Message();
            msg.what = DetailFragment.CHANGE_SEEKBAR;
            msg.arg1 = current;
            handler.sendMessage(msg);

            if (current >= Player.getDuration())
                runFlag = false;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
