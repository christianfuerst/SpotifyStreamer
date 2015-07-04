package com.yourdot.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class TopTracksActivityFragment extends Fragment {

    private TrackAdapter trackAdapter;
    private TextView textView_Header;
    private ImageView imageView_Header;
    private TextView textView;
    private ListView listView;
    private View view;
    private FetchArtistHeaderTask fetchArtistHeaderTask;
    private FetchTracksTask fetchTracksTask;
    private String artist_id;

    public TopTracksActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            artist_id = intent.getStringExtra(Intent.EXTRA_TEXT);
        }

        return inflater.inflate(R.layout.fragment_top_tracks, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imageView_Header = (ImageView) getActivity().findViewById(R.id.imageView_artist_header);
        textView_Header = (TextView) getActivity().findViewById(R.id.textView_artist_header);

        textView = (TextView) getActivity().findViewById(R.id.textView_track_dialog);
        listView = (ListView) getActivity().findViewById(R.id.listView_top_tracks);

        if (fetchArtistHeaderTask != null) {
            fetchArtistHeaderTask.cancel(false);
        }

        fetchArtistHeaderTask = new FetchArtistHeaderTask();
        fetchArtistHeaderTask.execute(artist_id);

        if (fetchTracksTask != null) {
            fetchTracksTask.cancel(false);
        }

        textView.setVisibility(View.GONE);
        fetchTracksTask = new FetchTracksTask();
        fetchTracksTask.execute(artist_id);

    }

    public class FetchTracksTask extends AsyncTask<String, Void, List<Track>> {
        private final String LOG_TAG = FetchTracksTask.class.getSimpleName();

        @Override
        protected List<Track> doInBackground(String... params) {

            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotifyService = spotifyApi.getService();

            Map<String, Object> options = new HashMap<>();
            options.put("country", "DE");

            Tracks tracks = spotifyService.getArtistTopTrack(params[0], options);

            return tracks.tracks;
        }

        @Override
        protected void onPostExecute(List<Track> tracks) {
            super.onPostExecute(tracks);

            trackAdapter = new TrackAdapter(getActivity(), R.layout.item_listview_track, tracks);
            listView.setAdapter(trackAdapter);

            if (tracks.isEmpty()) {
                textView.setText(R.string.track_dialog_no_search_result);
                textView.setVisibility(View.VISIBLE);
            }
        }
    }

    public class FetchArtistHeaderTask extends AsyncTask<String, Void, Artist> {

        @Override
        protected Artist doInBackground(String... params) {

            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotifyService = spotifyApi.getService();

            Artist artist = spotifyService.getArtist(params[0]);

            return artist;
        }

        @Override
        protected void onPostExecute(Artist artist) {
            super.onPostExecute(artist);

            textView_Header.setText(artist.name);

            if (! artist.images.isEmpty()) {
                Picasso.with(getActivity().getApplicationContext())
                        .load(artist.images.get(0).url)
                        .into(imageView_Header);
            }
        }

    }
}
