package net.michael_ray.heartbeat;

import org.json.JSONObject;

/**
 * Created by micray01 on 3/28/2017.
 */

public class SpotifyTrackDetails {
    private double danceability;
    private double energy;
    private int key;
    private double loudness;
    private double speechiness;
    private double acousticness;
    private double instrumentalness;
    private double liveness;
    private double tempo;

    public SpotifyTrackDetails(JSONObject details_json) throws Exception {
        danceability = details_json.getDouble("danceability");
        energy = details_json.getDouble("energy");
        key = details_json.getInt("key");
        loudness = details_json.getDouble("loudness");
        speechiness = details_json.getDouble("speechiness");
        acousticness = details_json.getDouble("acousticness");
        instrumentalness = details_json.getDouble("instrumentalness");
        liveness = details_json.getDouble("liveness");
        tempo = details_json.getDouble("tempo");
    }

    public double getDanceability() {
        return danceability;
    }

    public void setDanceability(double danceability) {
        this.danceability = danceability;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public double getLoudness() {
        return loudness;
    }

    public void setLoudness(double loudness) {
        this.loudness = loudness;
    }

    public double getSpeechiness() {
        return speechiness;
    }

    public void setSpeechiness(double speechiness) {
        this.speechiness = speechiness;
    }

    public double getAcousticness() {
        return acousticness;
    }

    public void setAcousticness(double acousticness) {
        this.acousticness = acousticness;
    }

    public double getInstrumentalness() {
        return instrumentalness;
    }

    public void setInstrumentalness(double instrumentalness) {
        this.instrumentalness = instrumentalness;
    }

    public double getLiveness() {
        return liveness;
    }

    public void setLiveness(double liveness) {
        this.liveness = liveness;
    }

    public double getTempo() {
        return tempo;
    }

    public void setTempo(double tempo) {
        this.tempo = tempo;
    }
}
