package com.yourdot.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by fuerst on 04.07.15.
 */
public class TrackAdapter extends ArrayAdapter<Track> {

    Context context;
    int layoutResourceID;
    List<Track> data = null;

    public TrackAdapter(Context context, int layoutResourceID, List<Track> data) {
        super(context, layoutResourceID, data);
        this.layoutResourceID = layoutResourceID;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        TrackHolder trackHolder = null;

        if (view == null) {
            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            view = layoutInflater.inflate(layoutResourceID, parent, false);

            trackHolder = new TrackHolder();
            trackHolder.imageView_album = (ImageView)view.findViewById(R.id.imageView_album);
            trackHolder.textView_track = (TextView)view.findViewById(R.id.textView_track);
            trackHolder.textView_album = (TextView)view.findViewById(R.id.textView_album);

            view.setTag(trackHolder);
        }
        else
        {
            trackHolder = (TrackHolder)view.getTag();
        }

        Track track = data.get(position);

        trackHolder.textView_track.setText(track.name);
        trackHolder.textView_album.setText(track.album.name);

        if (! track.album.images.isEmpty()) {
            Picasso.with(context)
                    .load(track.album.images.get(track.album.images.size() - 1).url)
                    .into(trackHolder.imageView_album);
        } else {
            Picasso.with(context)
                    .load(R.drawable.placeholder)
                    .into(trackHolder.imageView_album);
        }

        return view;
    }

    static class TrackHolder {
        ImageView imageView_album;
        TextView textView_track;
        TextView textView_album;
    }
}
