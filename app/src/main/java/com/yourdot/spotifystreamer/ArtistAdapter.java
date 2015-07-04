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

import kaaes.spotify.webapi.android.models.Artist;

/**
 * Created by fuerst on 03.07.15.
 */
public class ArtistAdapter extends ArrayAdapter<Artist> {

    Context context;
    int layoutResourceID;
    List<Artist> data = null;

    public ArtistAdapter(Context context, int layoutResourceID, List<Artist> data) {
        super(context, layoutResourceID, data);
        this.layoutResourceID = layoutResourceID;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ArtistHolder artistHolder = null;

        if (view == null) {
            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            view = layoutInflater.inflate(layoutResourceID, parent, false);

            artistHolder = new ArtistHolder();
            artistHolder.imageView_artist = (ImageView)view.findViewById(R.id.imageView_artist);
            artistHolder.textView_artist = (TextView)view.findViewById(R.id.textView_artist);

            view.setTag(artistHolder);
        }
        else
        {
            artistHolder = (ArtistHolder)view.getTag();
        }

        Artist artist = data.get(position);

        artistHolder.textView_artist.setText(artist.name);

        if (! artist.images.isEmpty()) {
            Picasso.with(context)
                    .load(artist.images.get(artist.images.size() - 1).url)
                    .into(artistHolder.imageView_artist);
        } else {
            Picasso.with(context)
                    .load(R.drawable.artist_placeholder)
                    .into(artistHolder.imageView_artist);
        }

        return view;
    }

    static class ArtistHolder {
        ImageView imageView_artist;
        TextView textView_artist;
    }
}

