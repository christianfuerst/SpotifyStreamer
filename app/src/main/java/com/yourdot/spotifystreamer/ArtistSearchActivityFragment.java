package com.yourdot.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistSearchActivityFragment extends Fragment {

    private ArtistAdapter artistAdapter;
    private EditText editText;
    private TextView textView;
    private ListView listView;
    private View view;
    private FetchArtistsTask fetchArtistsTask;

    public ArtistSearchActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_artist_search, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        editText = (EditText) getActivity().findViewById(R.id.editText_artist_search);
        textView = (TextView) getActivity().findViewById(R.id.textView_artist_dialog);
        listView = (ListView) getActivity().findViewById(R.id.listView_artist);

        textView.setText(R.string.artist_dialog_empty_search_string);

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {

                    if (fetchArtistsTask != null) {
                        fetchArtistsTask.cancel(false);
                    }

                    textView.setVisibility(View.GONE);
                    fetchArtistsTask = new FetchArtistsTask();
                    fetchArtistsTask.execute(s.toString());
                } else {

                    if (fetchArtistsTask != null) {
                        fetchArtistsTask.cancel(false);
                    }

                    textView.setText(R.string.artist_dialog_empty_search_string);
                    textView.setVisibility(View.VISIBLE);
                    artistAdapter.clear();
                    artistAdapter.notifyDataSetChanged();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String artist_id = artistAdapter.getItem(position).id ;

                Intent sendArtistToTopTracksActivity = new Intent(getActivity(), TopTracksActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, artist_id);

                startActivity(sendArtistToTopTracksActivity);

            }
        });

    }

    public class FetchArtistsTask extends AsyncTask<String, Void, List<Artist>> {
        private final String LOG_TAG = FetchArtistsTask.class.getSimpleName();

        @Override
        protected List<Artist> doInBackground(String... params) {

            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotifyService = spotifyApi.getService();
            ArtistsPager artistsPager = spotifyService.searchArtists(params[0]);

            return artistsPager.artists.items;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            super.onPostExecute(artists);

            artistAdapter = new ArtistAdapter(getActivity(), R.layout.item_listview_artist, artists);
            listView.setAdapter(artistAdapter);

            if (artists.isEmpty()) {
                textView.setText(R.string.artist_dialog_no_search_result);
                textView.setVisibility(View.VISIBLE);
            }
        }
    }

}
