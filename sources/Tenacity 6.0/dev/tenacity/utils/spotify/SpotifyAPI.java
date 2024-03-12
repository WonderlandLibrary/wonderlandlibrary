// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.spotify;

import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import org.apache.hc.core5.http.ParseException;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import com.wrapper.spotify.SpotifyHttpManager;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import dev.tenacity.Tenacity;
import java.awt.Desktop;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.pkce.AuthorizationCodePKCERequest;
import java.util.concurrent.TimeUnit;
import dev.tenacity.utils.player.ChatUtil;
import net.minecraft.util.EnumChatFormatting;
import com.sun.net.httpserver.HttpServer;
import com.wrapper.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.SpotifyApi;
import java.io.File;
import com.google.gson.Gson;
import dev.tenacity.utils.Utils;

public class SpotifyAPI implements Utils
{
    public static final String CODE_CHALLENGE = "w6iZIj99vHGtEx_NVl9u3sthTN646vvkiP8OMCGfPmo";
    private static final String CODE_VERIFIER = "NlJx4kD4opk4HY7zBM6WfUHxX7HoF8A2TUhOIPGA74w";
    public static final Gson GSON;
    public static final File CLIENT_ID_DIR;
    private int tokenRefreshInterval;
    public SpotifyApi spotifyApi;
    public AuthorizationCodeUriRequest authCodeUriRequest;
    public Track currentTrack;
    public CurrentlyPlayingContext currentPlayingContext;
    public boolean authenticated;
    private HttpServer callbackServer;
    private final SpotifyCallBack callback;
    
    public SpotifyAPI() {
        this.tokenRefreshInterval = 2;
        final AuthorizationCodePKCERequest authCodePKCERequest;
        AuthorizationCodeCredentials authCredentials;
        AuthorizationCodeCredentials refreshRequest;
        CurrentlyPlayingContext currentlyPlayingContext;
        String currentTrackId;
        this.callback = (code -> {
            ChatUtil.print("Spotify", EnumChatFormatting.GREEN, "Connecting to Spotify...");
            authCodePKCERequest = this.spotifyApi.authorizationCodePKCE(code, "NlJx4kD4opk4HY7zBM6WfUHxX7HoF8A2TUhOIPGA74w").build();
            try {
                authCredentials = authCodePKCERequest.execute();
                this.spotifyApi.setAccessToken(authCredentials.getAccessToken());
                this.spotifyApi.setRefreshToken(authCredentials.getRefreshToken());
                this.tokenRefreshInterval = authCredentials.getExpiresIn();
                this.authenticated = true;
                new Thread(() -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            TimeUnit.SECONDS.sleep(this.tokenRefreshInterval - 2);
                            System.out.println("Refreshing token...");
                            refreshRequest = this.spotifyApi.authorizationCodePKCERefresh().build().execute();
                            this.spotifyApi.setAccessToken(refreshRequest.getAccessToken());
                            this.spotifyApi.setRefreshToken(refreshRequest.getRefreshToken());
                            this.tokenRefreshInterval = refreshRequest.getExpiresIn();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return;
                }).start();
                new Thread(() -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            TimeUnit.SECONDS.sleep(1L);
                            currentlyPlayingContext = this.spotifyApi.getInformationAboutUsersCurrentPlayback().build().execute();
                            currentTrackId = currentlyPlayingContext.getItem().getId();
                            this.currentTrack = this.spotifyApi.getTrack(currentTrackId).build().execute();
                            this.currentPlayingContext = currentlyPlayingContext;
                        }
                        catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }).start();
            }
            catch (Exception e3) {
                e3.printStackTrace();
            }
        });
    }
    
    public void startConnection() {
        if (!this.authenticated) {
            try {
                Desktop.getDesktop().browse(this.authCodeUriRequest.execute());
                String messageSuccess;
                OutputStream out;
                Tenacity.INSTANCE.getExecutorService().submit(() -> {
                    try {
                        if (this.callbackServer != null) {
                            this.callbackServer.stop(0);
                        }
                        ChatUtil.print("Spotify", EnumChatFormatting.GREEN, "Please allow access to the application.");
                        (this.callbackServer = HttpServer.create(new InetSocketAddress(4030), 0)).createContext("/", context -> {
                            this.callback.codeCallback(context.getRequestURI().getQuery().split("=")[1]);
                            messageSuccess = (context.getRequestURI().getQuery().contains("code") ? "Successfully authorized.\nYou can now close this window, have fun on Tenacity!" : "Unable to Authorize client, re-toggle the module.");
                            context.sendResponseHeaders(200, messageSuccess.length());
                            out = context.getResponseBody();
                            out.write(messageSuccess.getBytes());
                            out.close();
                            this.callbackServer.stop(0);
                            return;
                        });
                        this.callbackServer.start();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
    
    public void build(final String clientID) {
        this.spotifyApi = new SpotifyApi.Builder().setClientId(clientID).setRedirectUri(SpotifyHttpManager.makeUri("http://localhost:4030")).build();
        this.authCodeUriRequest = this.spotifyApi.authorizationCodePKCEUri("w6iZIj99vHGtEx_NVl9u3sthTN646vvkiP8OMCGfPmo").code_challenge_method("S256").scope("user-read-playback-state user-read-playback-position user-modify-playback-state user-read-currently-playing").build();
    }
    
    public void skipToPreviousTrack() {
        try {
            this.spotifyApi.skipUsersPlaybackToPreviousTrack().build().execute();
        }
        catch (IOException | SpotifyWebApiException | ParseException ex2) {
            final Exception ex;
            final Exception e = ex;
            ChatUtil.print("Spotify", EnumChatFormatting.RED, e.getMessage());
        }
    }
    
    public void skipTrack() {
        try {
            this.spotifyApi.skipUsersPlaybackToNextTrack().build().execute();
        }
        catch (IOException | SpotifyWebApiException | ParseException ex2) {
            final Exception ex;
            final Exception e = ex;
            ChatUtil.print("Spotify", EnumChatFormatting.RED, e.getMessage());
        }
    }
    
    public void toggleShuffleState() {
        try {
            this.spotifyApi.toggleShuffleForUsersPlayback(!this.currentPlayingContext.getShuffle_state()).build().execute();
        }
        catch (IOException | SpotifyWebApiException | ParseException ex2) {
            final Exception ex;
            final Exception e = ex;
            ChatUtil.print("Spotify", EnumChatFormatting.RED, e.getMessage());
        }
    }
    
    public void pausePlayback() {
        try {
            this.spotifyApi.pauseUsersPlayback().build().execute();
        }
        catch (IOException | SpotifyWebApiException | ParseException ex2) {
            final Exception ex;
            final Exception e = ex;
            ChatUtil.print("Spotify", EnumChatFormatting.RED, e.getMessage());
        }
    }
    
    public void resumePlayback() {
        try {
            this.spotifyApi.startResumeUsersPlayback().build().execute();
        }
        catch (IOException | SpotifyWebApiException | ParseException ex2) {
            final Exception ex;
            final Exception e = ex;
            ChatUtil.print("Spotify", EnumChatFormatting.RED, e.getMessage());
        }
    }
    
    public boolean isPlaying() {
        return this.currentPlayingContext.getIs_playing();
    }
    
    static {
        GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();
        CLIENT_ID_DIR = new File(Tenacity.DIRECTORY, "SpotifyID.json");
    }
}
