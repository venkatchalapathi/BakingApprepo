package com.example.venky.bakingapp.Others;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.venky.bakingapp.Models.Step;
import com.example.venky.bakingapp.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepDetailFragment extends Fragment {
    @BindView(R.id.prev_button)
    ImageView previous;
    @BindView(R.id.next_button)
    ImageView next;
    @BindView(R.id.thumbnail_image)
    ImageView imageView;
    @BindView(R.id.description)
    TextView descri;
    SimpleExoPlayer player;
    @BindView(R.id.video_view)
    PlayerView playerView;
    private long playerStopPosition;
    private boolean stopPlay;
    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;
    private String videourl;
    private List<Step> step_list;

    private int current_position;
    private int total_items=0;

    public StepDetailFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.step_detail,container,false);
        if(getArguments()!=null){

            step_list=(List<Step>) getArguments()
                    .getSerializable(ProjectConstants.STEP_LIST_ACTIVITY_EXTRA_KEY);

            current_position=getArguments()
                    .getInt(ProjectConstants.POSITION_KEY,0);

        }
        ButterKnife.bind(this,v);
        playbackPosition = C.TIME_UNSET;

        imageView.setVisibility(View.GONE);

        if(savedInstanceState!=null)
        {
            playWhenReady = savedInstanceState.getBoolean(ProjectConstants.PLAY_WHEN_READY);
            current_position = savedInstanceState.getInt(ProjectConstants.CURRENT_POS_SAVE_INSTANCE);
            playbackPosition = savedInstanceState.getLong(ProjectConstants.PLAY_BACK_POS_SAVE_INSTANCE,C.TIME_UNSET);

        }
        total_items = step_list.size();

        videourl = step_list.get(current_position).getVideoURL();
        descri.setText(step_list.get(current_position).getDescription());
        hideUnhideExo();

        if(current_position == 0)
        {
            previous.setVisibility(View.INVISIBLE);
        }
        if(current_position == total_items-1)
        {
            next.setVisibility(View.INVISIBLE);
        }
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                releasePlayer();
                previous.setVisibility(View.VISIBLE);
                current_position++;
                descri.setText(step_list.get(current_position).getDescription());
                videourl = step_list.get(current_position).getVideoURL();

                if(current_position==total_items-1)
                {
                    next.setVisibility(View.INVISIBLE);
                }
                hideUnhideExo();
                initializePlayer();
                player.seekTo(0);
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                releasePlayer();
                next.setVisibility(View.VISIBLE);
                current_position--;
                descri.setText(step_list.get(current_position).getDescription());

                if(current_position ==0){
                    previous.setVisibility(View.INVISIBLE);
                }
                videourl=step_list.get(current_position).getVideoURL();

                hideUnhideExo();
                initializePlayer();
                player.seekTo(0);

            }
        });
        return v;
    }
    private void initializePlayer(){

        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),new DefaultTrackSelector(),new DefaultLoadControl());

        playerView.setPlayer(player);
        Uri uri = Uri.parse(videourl);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource,true,false);
        player.setPlayWhenReady(playWhenReady);
        if(playbackPosition!=0&&!stopPlay){
            player.seekTo(playbackPosition);
        }else{
            player.seekTo(playerStopPosition);
        }

    }
    private MediaSource buildMediaSource(Uri uri)
    {
        return new ExtractorMediaSource
                .Factory(new DefaultHttpDataSourceFactory(ProjectConstants.BAKING_APP_AGENT)).createMediaSource(uri);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(Util.SDK_INT > 23)
        {
            initializePlayer();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(Util.SDK_INT <= 23 || player == null)
        {
            initializePlayer();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if(Util.SDK_INT <= 23){
            if(player!=null){
                playerStopPosition = player.getCurrentPosition();
                stopPlay = true;
                releasePlayer();
            }
        }
    }
    private void releasePlayer(){
        if(player != null){
            player.stop();
            player.release();
            player=null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if (player != null) {

                playerStopPosition = player.getCurrentPosition();
                stopPlay = true;
                releasePlayer();
            }

        }
    }

    public void hideUnhideExo(){
        if(TextUtils.isEmpty(videourl)){
            playerView.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(step_list.get(current_position).getThumbnailURL())){
                Glide.with(this).load(step_list.get(current_position).getThumbnailURL()).into(imageView);

            }
        }else{
            playerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt(ProjectConstants.CURRENT_POS_SAVE_INSTANCE,current_position);
        outState.putLong(ProjectConstants.PLAY_BACK_POS_SAVE_INSTANCE,player.getCurrentPosition());
        outState.putBoolean(ProjectConstants.PLAY_WHEN_READY,player.getPlayWhenReady());
    }
}
