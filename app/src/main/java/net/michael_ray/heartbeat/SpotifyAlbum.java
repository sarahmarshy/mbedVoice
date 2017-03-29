package net.michael_ray.heartbeat;

import org.json.JSONObject;

public class SpotifyAlbum {

    private String album_id;
    private String album_name;
    private String album_uri;
    private String album_art;

    public SpotifyAlbum(JSONObject album_json) throws Exception {
        album_id = album_json.getString("id");
        album_name = album_json.getString("name");
        album_uri = album_json.getString("uri");
        album_art = album_json.getJSONArray("images").getJSONObject(0).getString("url");
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getAlbum_uri() {
        return album_uri;
    }

    public void setAlbum_uri(String album_uri) {
        this.album_uri = album_uri;
    }

    public String getAlbum_art() {
        return album_art;
    }

    public void setAlbum_art(String album_art) {
        this.album_art = album_art;
    }
}