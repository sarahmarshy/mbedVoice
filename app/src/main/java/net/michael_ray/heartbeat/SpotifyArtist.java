package net.michael_ray.heartbeat;

import org.json.JSONObject;

/**
 * Created by micray01 on 3/28/2017.
 */

public class SpotifyArtist {
    private String artist_id;
    private String artist_name;
    private String artist_uri;

    public SpotifyArtist(JSONObject artist_json) throws Exception {
        artist_id = artist_json.getString("id");
        artist_name = artist_json.getString("name");
        artist_uri = artist_json.getString("uri");
    }

    public String getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getArtist_uri() {
        return artist_uri;
    }

    public void setArtist_uri(String artist_uri) {
        this.artist_uri = artist_uri;
    }
}
