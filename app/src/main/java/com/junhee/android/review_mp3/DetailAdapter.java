package com.junhee.android.review_mp3;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.junhee.android.review_mp3.domain.Music;

import java.util.List;

/**
 * Created by JunHee on 2017. 6. 17..
 */

public class DetailAdapter extends PagerAdapter {

    List<Music.Item> datas;

    public DetailAdapter(List<Music.Item> datas){
        this.datas = datas;
    }


    @Override
    public int getCount() {
        return datas.size();
    }

    // ListAdapter의 onBindViewHolder 같은 개념
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_pager_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.detail_albumArt);
        TextView title = (TextView) view.findViewById(R.id.detail_title);
        TextView artist = (TextView) view.findViewById(R.id.detail_artist);

        Glide
                .with(container.getContext())
                .load(datas.get(position).albumArt)
                .placeholder(R.drawable.default_album)
                .into(imageView);

        title.setText(datas.get(position).title);
        artist.setText(datas.get(position).artist);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    // instantiateItem 에서 리턴한 Object 가 View 가 맞는지를 확인한다.
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
