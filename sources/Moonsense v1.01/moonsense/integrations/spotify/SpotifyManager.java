// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.integrations.spotify;

import java.io.OutputStream;
import moonsense.utils.Multithreading;
import com.sun.net.httpserver.HttpExchange;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import java.io.File;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import se.michaelthelin.spotify.model_objects.specification.Playlist;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlaying;
import java.util.concurrent.CompletableFuture;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import java.util.concurrent.ScheduledExecutorService;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.Executor;
import com.sun.net.httpserver.HttpHandler;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.net.URI;
import java.awt.Desktop;
import java.io.IOException;
import se.michaelthelin.spotify.SpotifyHttpManager;
import moonsense.MoonsenseClient;
import java.util.HashMap;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import se.michaelthelin.spotify.model_objects.specification.User;
import com.sun.net.httpserver.HttpServer;
import se.michaelthelin.spotify.SpotifyApi;

public class SpotifyManager
{
    private static SpotifyApi spotifyAPI;
    private static boolean logged;
    private static HttpServer server;
    public static User currentUser;
    public static final Map<String, ResourceLocation> albumImages;
    public static final Map<String, ResourceLocation> userImages;
    
    static {
        SpotifyManager.logged = false;
        albumImages = new HashMap<String, ResourceLocation>();
        userImages = new HashMap<String, ResourceLocation>();
    }
    
    public static void init() {
        final SpotifyApi.Builder builder = new SpotifyApi.Builder();
        MoonsenseClient.INSTANCE.getSpotifyData().getClass();
        final SpotifyApi.Builder setClientId = builder.setClientId("660a969342384d94a3a94ec5c5137f00");
        MoonsenseClient.INSTANCE.getSpotifyData().getClass();
        final SpotifyApi.Builder setClientSecret = setClientId.setClientSecret("ff6878aa55e648fea4d7509f24754e3f");
        MoonsenseClient.INSTANCE.getSpotifyData().getClass();
        SpotifyManager.spotifyAPI = setClientSecret.setRedirectUri(SpotifyHttpManager.makeUri("http://localhost:9103/callback/")).build();
    }
    
    public static void connectSpotify() {
        attemptToOpenAuthURL();
    }
    
    private static void attemptToOpenAuthURL() {
        try {
            initializeHandler();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        final Desktop desktop = Desktop.getDesktop();
        final StringBuilder sb = new StringBuilder("https://accounts.spotify.com/authorize?client_id=");
        MoonsenseClient.INSTANCE.getSpotifyData().getClass();
        final String URL = sb.append("660a969342384d94a3a94ec5c5137f00").append("&response_type=code&redirect_uri=http%3A%2F%2Flocalhost:9103%2Fcallback%2F&scope=user-read-playback-state%20user-read-currently-playing%20user-modify-playback-state&state=34fFs29kd09").toString();
        try {
            desktop.browse(new URI(URL));
        }
        catch (URISyntaxException e2) {
            e2.printStackTrace();
        }
        catch (Exception e3) {
            e3.printStackTrace();
        }
    }
    
    private static void initializeHandler() throws IOException {
        try {
            SpotifyManager.server = HttpServer.create(new InetSocketAddress(9103), 0);
        }
        catch (IOException e) {
            throw e;
        }
        SpotifyManager.server.createContext("/callback", new SpotifyCallbackHandler(null));
        SpotifyManager.server.setExecutor(null);
        SpotifyManager.server.start();
    }
    
    private static void handleRequest(final String code) {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        final Runnable logout = new Runnable() {
            @Override
            public void run() {
                if (SpotifyManager.getCurrentlyPlaying() != null && SpotifyManager.getCurrentlyPlaying().getIs_playing()) {
                    SpotifyManager.pauseCurrentTrack();
                }
                SpotifyManager.access$3(false);
                SpotifyManager.currentUser = null;
                MoonsenseClient.INSTANCE.setSpotifyLoggedIn(false);
            }
        };
        final ScheduledFuture<?> logoutHandler = scheduler.scheduleAtFixedRate(logout, 1L, 1L, TimeUnit.HOURS);
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                logoutHandler.cancel(true);
            }
        }, 1L, TimeUnit.HOURS);
        try {
            final SpotifyApi.Builder builder = new SpotifyApi.Builder();
            MoonsenseClient.INSTANCE.getSpotifyData().getClass();
            final SpotifyApi.Builder setClientId = builder.setClientId("660a969342384d94a3a94ec5c5137f00");
            MoonsenseClient.INSTANCE.getSpotifyData().getClass();
            final SpotifyApi.Builder setClientSecret = setClientId.setClientSecret("ff6878aa55e648fea4d7509f24754e3f");
            MoonsenseClient.INSTANCE.getSpotifyData().getClass();
            final SpotifyApi spotifyAPI = setClientSecret.setRedirectUri(SpotifyHttpManager.makeUri("http://localhost:9103/callback/")).build();
            final AuthorizationCodeRequest authorizationCodeRequest = spotifyAPI.authorizationCode(code).build();
            final CompletableFuture<AuthorizationCodeCredentials> authorizationCodeCredentialsFuture = authorizationCodeRequest.executeAsync();
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeCredentialsFuture.join();
            SpotifyManager.spotifyAPI.setAccessToken(authorizationCodeCredentials.getAccessToken());
            SpotifyManager.spotifyAPI.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
            if (SpotifyManager.spotifyAPI.getRefreshToken() != null) {
                SpotifyManager.logged = true;
                SpotifyManager.currentUser = getCurrentUser();
                MoonsenseClient.INSTANCE.setSpotifyLoggedIn(true);
                SpotifyManager.spotifyAPI.getUsersCurrentlyPlayingTrack().build().execute();
            }
            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
            final CurrentlyPlaying cp = getCurrentlyPlaying();
            if (getCurrentlyPlaying() != null && !getCurrentlyPlaying().getIs_playing()) {
                startResumeCurrentTrack();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static SpotifyApi getSpotifyAPI() {
        return SpotifyManager.spotifyAPI;
    }
    
    public static boolean isLoggedIn() {
        return getCurrentUser() != null && SpotifyManager.logged;
    }
    
    public static void setLogged(final boolean logged) {
        SpotifyManager.logged = logged;
    }
    
    public static User getCurrentUser() {
        try {
            return SpotifyManager.spotifyAPI.getCurrentUsersProfile().build().execute();
        }
        catch (ParseException | SpotifyWebApiException | IOException ex2) {
            final Exception ex;
            final Exception e = ex;
            return null;
        }
    }
    
    public static CurrentlyPlaying getCurrentlyPlaying() {
        try {
            return SpotifyManager.spotifyAPI.getUsersCurrentlyPlayingTrack().build().execute();
        }
        catch (ParseException | SpotifyWebApiException | IOException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
            return null;
        }
    }
    
    public static AlbumSimplified getCurrentlyPlayingAlbum() {
        try {
            final Track t = SpotifyManager.spotifyAPI.getTrack(getCurrentlyPlaying().getItem().getId()).build().execute();
            if (t == null) {
                return null;
            }
            if (t.getAlbum() != null) {
                return t.getAlbum();
            }
            return null;
        }
        catch (ParseException | SpotifyWebApiException | IOException | NullPointerException ex2) {
            final Exception ex;
            final Exception e = ex;
            if (!(e instanceof NullPointerException)) {
                e.printStackTrace();
            }
            return null;
        }
    }
    
    public static Track getCurrentTrack() {
        try {
            return SpotifyManager.spotifyAPI.getTrack(getCurrentlyPlaying().getItem().getId()).build().execute();
        }
        catch (ParseException | SpotifyWebApiException | IOException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
            return null;
        }
    }
    
    public static CurrentlyPlayingContext getCurrentTrackInfo() {
        try {
            return SpotifyManager.spotifyAPI.getInformationAboutUsersCurrentPlayback().build().execute();
        }
        catch (ParseException | SpotifyWebApiException | IOException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
            return null;
        }
    }
    
    public static void pauseCurrentTrack() {
        try {
            SpotifyManager.spotifyAPI.pauseUsersPlayback().build().execute();
        }
        catch (ParseException | SpotifyWebApiException | IOException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
        }
    }
    
    public static void startResumeCurrentTrack() {
        try {
            SpotifyManager.spotifyAPI.startResumeUsersPlayback().build().execute();
        }
        catch (ParseException | SpotifyWebApiException | IOException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
        }
    }
    
    public static void skipToPreviousTrack() {
        try {
            SpotifyManager.spotifyAPI.skipUsersPlaybackToPreviousTrack().build().execute();
        }
        catch (ParseException | SpotifyWebApiException | IOException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
        }
    }
    
    public static void seekTo(final int ms) {
        try {
            SpotifyManager.spotifyAPI.seekToPositionInCurrentlyPlayingTrack(ms).build().execute();
        }
        catch (ParseException | SpotifyWebApiException | IOException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
        }
    }
    
    public static void skipToNextTrack() {
        try {
            SpotifyManager.spotifyAPI.skipUsersPlaybackToNextTrack().build().execute();
        }
        catch (ParseException | SpotifyWebApiException | IOException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
        }
    }
    
    public static Playlist getPlaylist(final String id) {
        try {
            return SpotifyManager.spotifyAPI.getPlaylist(id).build().execute();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void addItemToPlaybackQueue(final String id) {
        try {
            SpotifyManager.spotifyAPI.addItemToUsersPlaybackQueue(id).build().execute();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static ResourceLocation getAlbumImageLocation(final String url) {
        final ResourceLocation albumImage = SpotifyManager.albumImages.getOrDefault(url, new ResourceLocation("streamlined/icons/spotify/album_images/" + url + ".png"));
        if (!SpotifyManager.albumImages.containsKey(url)) {
            final ThreadDownloadImageData imageData = new ThreadDownloadImageData(null, url, new ResourceLocation("streamlined/icons/spotify.png"), null);
            Minecraft.getMinecraft().getTextureManager().loadTexture(albumImage, imageData);
            SpotifyManager.albumImages.put(url, albumImage);
        }
        return albumImage;
    }
    
    public static ResourceLocation getUserImageLocation(final String url) {
        final ResourceLocation userImage = SpotifyManager.userImages.getOrDefault(url, new ResourceLocation("streamlined/icons/spotify/user_images/" + url + ".png"));
        if (!SpotifyManager.userImages.containsKey(url)) {
            final ThreadDownloadImageData imageData = new ThreadDownloadImageData(null, url, new ResourceLocation("streamlined/icons/account.png"), null);
            Minecraft.getMinecraft().getTextureManager().loadTexture(userImage, imageData);
            SpotifyManager.userImages.put(url, userImage);
        }
        return userImage;
    }
    
    static /* synthetic */ void access$3(final boolean logged) {
        SpotifyManager.logged = logged;
    }
    
    private static class SpotifyCallbackHandler implements HttpHandler
    {
        @Override
        public void handle(final HttpExchange t) throws IOException {
            Multithreading.runAsync(() -> handleRequest(t.getRequestURI().toString().replace("/callback/?code=", "").substring(0, t.getRequestURI().toString().replace("/callback/?code=", "").length() - 18)));
            final String response = "Success! You may close this window and go back to Moonsense Client.";
            t.sendResponseHeaders(200, "Success! You may close this window and go back to Moonsense Client.".length());
            final OutputStream os = t.getResponseBody();
            os.write("Success! You may close this window and go back to Moonsense Client.".getBytes());
            os.close();
            SpotifyManager.server.stop(0);
        }
    }
}
