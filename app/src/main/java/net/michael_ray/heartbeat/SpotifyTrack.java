package net.michael_ray.heartbeat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by micray01 on 3/28/2017.
 */

public class SpotifyTrack {
    private int track_duration_ms;
    private boolean track_explicit;
    private String track_name;
    private String track_uri;
    private String track_id;
    private int track_popularity;

    private ArrayList<SpotifyArtist> track_artists;
    private SpotifyAlbum track_album;
    private SpotifyTrackDetails track_details;

    public SpotifyTrack(JSONObject track_json) throws Exception {
        track_uri = track_json.getString("uri");
        track_popularity = track_json.getInt("popularity");
        track_name = track_json.getString("name");
        track_id = track_json.getString("id");
        track_explicit = track_json.getBoolean("explicit");
        track_duration_ms = track_json.getInt("duration_ms");
        track_album = new SpotifyAlbum(track_json.getJSONObject("album"));
        JSONArray artists_json = track_json.getJSONArray("artists");
        track_artists = new ArrayList<>();
        for (int i=0; i<artists_json.length(); i++) {
            track_artists.add(new SpotifyArtist(artists_json.getJSONObject(i)));
        }
    }

    public int getTrack_duration_ms() {
        return track_duration_ms;
    }

    public void setTrack_duration_ms(int track_duration_ms) {
        this.track_duration_ms = track_duration_ms;
    }

    public boolean isTrack_explicit() {
        return track_explicit;
    }

    public void setTrack_explicit(boolean track_explicit) {
        this.track_explicit = track_explicit;
    }

    public String getTrack_name() {
        return track_name;
    }

    public void setTrack_name(String track_name) {
        this.track_name = track_name;
    }

    public String getTrack_uri() {
        return track_uri;
    }

    public void setTrack_uri(String track_uri) {
        this.track_uri = track_uri;
    }

    public String getTrack_id() {
        return track_id;
    }

    public void setTrack_id(String track_id) {
        this.track_id = track_id;
    }

    public int getTrack_popularity() {
        return track_popularity;
    }

    public void setTrack_popularity(int track_popularity) {
        this.track_popularity = track_popularity;
    }

    public ArrayList<SpotifyArtist> getTrack_artists() {
        return track_artists;
    }

    public void setTrack_artists(ArrayList<SpotifyArtist> track_artists) {
        this.track_artists = track_artists;
    }

    public SpotifyAlbum getTrack_album() {
        return track_album;
    }

    public void setTrack_album(SpotifyAlbum track_album) {
        this.track_album = track_album;
    }

    public SpotifyTrackDetails getTrack_details() {
        return track_details;
    }

    public void setTrack_details(SpotifyTrackDetails track_details) {
        this.track_details = track_details;
    }
}