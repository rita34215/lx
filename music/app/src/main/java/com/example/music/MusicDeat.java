package com.example.music;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;

public class MusicDeat extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_deat);
        setTitle("详情");

        ImageView iv_music = findViewById(R.id.iv_music);
        TextView tv_musci_name = findViewById(R.id.tv_musci_name);
        TextView tv_geshou_name = findViewById(R.id.tv_geshou_name);

        Intent intent = getIntent();
        if (intent.getSerializableExtra("xiangqing") != null) {
            MusicItem xiangqing = (MusicItem) intent.getSerializableExtra("xiangqing");
            if (xiangqing != null) {
                Glide.with(MusicDeat.this).load(xiangqing.getImg()).into(iv_music);
                tv_musci_name.setText("歌曲名称："+xiangqing.getName());
                if (xiangqing.getSinger() != null && xiangqing.getSinger().size() > 0) {
                    tv_geshou_name.setText("歌手："+xiangqing.getSinger().get(0));
                }

            }
        }


    }
}
