package com.example.dell.rhythmmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AudioContentAdapter extends BaseAdapter {

    private Context context;
    private List<AudioContent> songList;
    private View songView;


    public AudioContentAdapter(Context context, List<AudioContent> songList) {
        this.context = context;
        this.songList = songList;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return songList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        songView = inflater.inflate(R.layout.song_row,parent,false);

        AudioContent audioContent = (AudioContent) getItem(i);

        TextView songname =  songView.findViewById(R.id.txtsongname);
        TextView songurl = songView.findViewById(R.id.txtsongurl);
        songname.setText(audioContent.getSongName());
        songurl.setText(audioContent.getSongUrl());


        return songView;
    }

}
