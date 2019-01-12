package com.example.dell.rhythmmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RequestContentAdapter extends BaseAdapter {
    private Context context;
    private List<RequestContent> rlist;
    private View rView;

    public RequestContentAdapter(Context context, List<RequestContent> rlist) {
        this.context = context;
        this.rlist = rlist;
    }

    @Override
    public int getCount() {
        return rlist.size();
    }

    @Override
    public Object getItem(int position) {
        return rlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rView = inflater.inflate(R.layout.request_list,parent,false);

        RequestContent requestContent = (RequestContent) getItem(i);

        TextView songname = rView.findViewById(R.id.tsongname);
        TextView singername = rView.findViewById(R.id.tsingername);

        songname.setText(requestContent.getSong());
        singername.setText(requestContent.getSinger());


        return rView;

    }
}
