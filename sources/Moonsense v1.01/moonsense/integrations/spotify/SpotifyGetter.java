// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.integrations.spotify;

import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;

public class SpotifyGetter
{
    private CurrentlyPlaying currentlyPlaying;
    private AlbumSimplified currentlyPlayingAlbum;
    private long lastRequestMS;
    
    public SpotifyGetter() {
        this.lastRequestMS = System.currentTimeMillis();
        new Thread("Spotify Getter") {
            @Override
            public void run() {
                while (true) {
                    if (SpotifyManager.getSpotifyAPI().getAccessToken() == null) {
                        continue;
                    }
                    if (SpotifyGetter.this.currentlyPlaying != null) {
                        if (SpotifyGetter.this.currentlyPlayingAlbum != null) {
                            if (SpotifyGetter.this.lastRequestMS + SpotifyGetter.this.currentlyPlaying.getItem().getDurationMs() < System.currentTimeMillis()) {
                                try {
                                    SpotifyGetter.access$2(SpotifyGetter.this, SpotifyManager.getCurrentlyPlaying());
                                    SpotifyGetter.access$3(SpotifyGetter.this, System.currentTimeMillis());
                                }
                                catch (Exception ex) {}
                                try {
                                    SpotifyGetter.access$4(SpotifyGetter.this, SpotifyManager.getCurrentlyPlayingAlbum());
                                    SpotifyGetter.access$3(SpotifyGetter.this, System.currentTimeMillis());
                                }
                                catch (Exception ex2) {}
                                continue;
                            }
                            continue;
                        }
                    }
                    try {
                        SpotifyGetter.access$2(SpotifyGetter.this, SpotifyManager.getCurrentlyPlaying());
                        SpotifyGetter.access$3(SpotifyGetter.this, System.currentTimeMillis());
                    }
                    catch (Exception ex3) {}
                    try {
                        SpotifyGetter.access$4(SpotifyGetter.this, SpotifyManager.getCurrentlyPlayingAlbum());
                        SpotifyGetter.access$3(SpotifyGetter.this, System.currentTimeMillis());
                    }
                    catch (Exception ex4) {}
                }
            }
        }.start();
    }
    
    public void update() {
        this.currentlyPlaying = SpotifyManager.getCurrentlyPlaying();
        this.currentlyPlayingAlbum = SpotifyManager.getCurrentlyPlayingAlbum();
        this.lastRequestMS = System.currentTimeMillis();
    }
    
    public CurrentlyPlaying getCurrentlyPlaying() {
        return this.currentlyPlaying;
    }
    
    public AlbumSimplified getCurrentlyPlayingAlbum() {
        return this.currentlyPlayingAlbum;
    }
    
    public long getLastRequestMS() {
        return this.lastRequestMS;
    }
    
    static /* synthetic */ void access$2(final SpotifyGetter spotifyGetter, final CurrentlyPlaying currentlyPlaying) {
        spotifyGetter.currentlyPlaying = currentlyPlaying;
    }
    
    static /* synthetic */ void access$3(final SpotifyGetter spotifyGetter, final long lastRequestMS) {
        spotifyGetter.lastRequestMS = lastRequestMS;
    }
    
    static /* synthetic */ void access$4(final SpotifyGetter spotifyGetter, final AlbumSimplified currentlyPlayingAlbum) {
        spotifyGetter.currentlyPlayingAlbum = currentlyPlayingAlbum;
    }
}
