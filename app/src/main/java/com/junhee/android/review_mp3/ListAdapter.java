package com.junhee.android.review_mp3;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.junhee.android.review_mp3.ListFragment.OnListFragmentInteractionListener;
import com.junhee.android.review_mp3.domain.Music;
import com.junhee.android.review_mp3.dummy.DummyContent.DummyItem;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    private Context context = null;
    private final List<Music.Item> datas;
    private final OnListFragmentInteractionListener mListener;

    public ListAdapter(List<Music.Item> items, OnListFragmentInteractionListener listener) {

        datas = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null)
            context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.position = position;
        holder.musicUri = datas.get(position).musicUri;
        holder.mIdView.setText(datas.get(position).id);
        holder.mContentView.setText(datas.get(position).title);

        Glide
                .with(context)
                .load(datas.get(position).albumArt)
                .placeholder(R.drawable.default_album)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(holder.imgAlbum);

        if(datas.get(position).itemClicked) {
            holder.btnPause.setVisibility(View.VISIBLE);
        } else {
            holder.btnPause.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void goDetail(int position){
        mListener.goDetailInteration(position);
    }

    public void setItemClicked(int position){
        for(Music.Item item : datas){
            item.itemClicked = false;
        }
        datas.get(position).itemClicked = true;
        // adapter 초기화 한번 해준다.
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Uri musicUri;
        public int position;
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView imgAlbum;
        public final ImageButton btnPause;


        public ViewHolder(View view) {

            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            imgAlbum = (ImageView) view.findViewById(R.id.imgAlbum);
            btnPause = (ImageButton) view.findViewById(R.id.btnPause);

            // 플레이
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setItemClicked(position);
                    Player.play(musicUri, mView.getContext());
                    btnPause.setImageResource(android.R.drawable.ic_media_pause);
                    // btnPause.setVisibility(View.VISIBLE);
                }
            });

            // 상세보기로 이동 -> 뷰페이저로 이동
            mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    goDetail(position);
                    return true; // 롱클릭 후 온클릭이 실행되지 않도록 한다.
                }
            });

            // pause 버튼 클릭
            btnPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch(Player.playerStatus){
                        case Player.PLAY:
                            Player.pause();
                            // pause 가 클릭되면 이미지 모양이 play 로 바뀐다.
                            btnPause.setImageResource(android.R.drawable.ic_media_play);
                            break;
                        case Player.PAUSE:
                            Player.replay();
                            btnPause.setImageResource(android.R.drawable.ic_media_pause);
                            break;
                    }
                }
            });
        }
    }
}
