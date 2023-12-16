// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify;

import se.michaelthelin.spotify.requests.AbstractRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetUsersProfileRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetSeveralTracksRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetAudioFeaturesForSeveralTracksRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetAudioFeaturesForTrackRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetAudioAnalysisForTrackRequest;
import se.michaelthelin.spotify.requests.data.shows.GetShowsEpisodesRequest;
import se.michaelthelin.spotify.requests.data.shows.GetSeveralShowsRequest;
import se.michaelthelin.spotify.requests.data.shows.GetShowRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchShowsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchEpisodesRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.special.SearchAlbumsSpecialRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchAlbumsRequest;
import se.michaelthelin.spotify.requests.data.search.SearchItemRequest;
import se.michaelthelin.spotify.requests.data.playlists.UploadCustomPlaylistCoverImageRequest;
import se.michaelthelin.spotify.requests.data.playlists.ReplacePlaylistsItemsRequest;
import se.michaelthelin.spotify.requests.data.playlists.ReorderPlaylistsItemsRequest;
import se.michaelthelin.spotify.requests.data.playlists.RemoveItemsFromPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistsItemsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistCoverImageRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetPlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.playlists.CreatePlaylistRequest;
import se.michaelthelin.spotify.requests.data.playlists.ChangePlaylistsDetailsRequest;
import se.michaelthelin.spotify.requests.data.playlists.AddItemsToPlaylistRequest;
import se.michaelthelin.spotify.requests.data.player.AddItemToUsersPlaybackQueueRequest;
import se.michaelthelin.spotify.requests.data.player.TransferUsersPlaybackRequest;
import se.michaelthelin.spotify.requests.data.player.ToggleShuffleForUsersPlaybackRequest;
import se.michaelthelin.spotify.requests.data.player.StartResumeUsersPlaybackRequest;
import se.michaelthelin.spotify.requests.data.player.SkipUsersPlaybackToPreviousTrackRequest;
import se.michaelthelin.spotify.requests.data.player.SkipUsersPlaybackToNextTrackRequest;
import se.michaelthelin.spotify.requests.data.player.SetVolumeForUsersPlaybackRequest;
import se.michaelthelin.spotify.requests.data.player.SetRepeatModeOnUsersPlaybackRequest;
import se.michaelthelin.spotify.requests.data.player.SeekToPositionInCurrentlyPlayingTrackRequest;
import se.michaelthelin.spotify.requests.data.player.PauseUsersPlaybackRequest;
import se.michaelthelin.spotify.requests.data.player.GetUsersCurrentlyPlayingTrackRequest;
import se.michaelthelin.spotify.requests.data.player.GetUsersAvailableDevicesRequest;
import se.michaelthelin.spotify.requests.data.player.GetCurrentUsersRecentlyPlayedTracksRequest;
import se.michaelthelin.spotify.requests.data.player.GetInformationAboutUsersCurrentPlaybackRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import se.michaelthelin.spotify.requests.data.personalization.GetUsersTopArtistsAndTracksRequest;
import se.michaelthelin.spotify.requests.data.personalization.interfaces.IArtistTrackModelObject;
import se.michaelthelin.spotify.requests.data.library.SaveTracksForUserRequest;
import se.michaelthelin.spotify.requests.data.library.SaveShowsForCurrentUserRequest;
import se.michaelthelin.spotify.requests.data.library.SaveAlbumsForCurrentUserRequest;
import se.michaelthelin.spotify.requests.data.library.RemoveUsersSavedTracksRequest;
import se.michaelthelin.spotify.requests.data.library.RemoveUsersSavedShowsRequest;
import se.michaelthelin.spotify.requests.data.library.RemoveAlbumsForCurrentUserRequest;
import se.michaelthelin.spotify.requests.data.library.GetUsersSavedTracksRequest;
import se.michaelthelin.spotify.requests.data.library.GetUsersSavedShowsRequest;
import se.michaelthelin.spotify.requests.data.library.GetCurrentUsersSavedAlbumsRequest;
import se.michaelthelin.spotify.requests.data.library.CheckUsersSavedTracksRequest;
import se.michaelthelin.spotify.requests.data.library.CheckUsersSavedShowsRequest;
import se.michaelthelin.spotify.requests.data.library.CheckUsersSavedAlbumsRequest;
import se.michaelthelin.spotify.requests.data.follow.legacy.UnfollowPlaylistRequest;
import se.michaelthelin.spotify.requests.data.follow.UnfollowArtistsOrUsersRequest;
import se.michaelthelin.spotify.requests.data.follow.GetUsersFollowedArtistsRequest;
import se.michaelthelin.spotify.requests.data.follow.legacy.FollowPlaylistRequest;
import com.google.gson.JsonArray;
import se.michaelthelin.spotify.requests.data.follow.FollowArtistsOrUsersRequest;
import se.michaelthelin.spotify.requests.data.follow.CheckUsersFollowPlaylistRequest;
import se.michaelthelin.spotify.requests.data.follow.CheckCurrentUserFollowsArtistsOrUsersRequest;
import se.michaelthelin.spotify.enums.ModelObjectType;
import se.michaelthelin.spotify.requests.data.episodes.GetSeveralEpisodesRequest;
import se.michaelthelin.spotify.requests.data.episodes.GetEpisodeRequest;
import se.michaelthelin.spotify.requests.data.browse.miscellaneous.GetAvailableGenreSeedsRequest;
import se.michaelthelin.spotify.requests.data.browse.GetRecommendationsRequest;
import se.michaelthelin.spotify.requests.data.browse.GetListOfNewReleasesRequest;
import se.michaelthelin.spotify.requests.data.browse.GetListOfFeaturedPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.browse.GetListOfCategoriesRequest;
import se.michaelthelin.spotify.requests.data.browse.GetCategorysPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.browse.GetCategoryRequest;
import se.michaelthelin.spotify.requests.data.artists.GetSeveralArtistsRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsRelatedArtistsRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsTopTracksRequest;
import com.neovisionaries.i18n.CountryCode;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsAlbumsRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;
import se.michaelthelin.spotify.requests.data.albums.GetSeveralAlbumsRequest;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumsTracksRequest;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumRequest;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.pkce.AuthorizationCodePKCERequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.pkce.AuthorizationCodePKCERefreshRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import java.util.TimeZone;
import java.text.ParseException;
import java.util.Date;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

public class SpotifyApi
{
    public static final String DEFAULT_AUTHENTICATION_HOST = "accounts.spotify.com";
    public static final int DEFAULT_AUTHENTICATION_PORT = 443;
    public static final String DEFAULT_AUTHENTICATION_SCHEME = "https";
    public static final String DEFAULT_HOST = "api.spotify.com";
    public static final IHttpManager DEFAULT_HTTP_MANAGER;
    public static final int DEFAULT_PORT = 443;
    public static final String DEFAULT_SCHEME = "https";
    public static final Logger LOGGER;
    private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT;
    private final IHttpManager httpManager;
    private final String scheme;
    private final String host;
    private final Integer port;
    private final String proxyUrl;
    private final Integer proxyPort;
    private final Integer proxyUsername;
    private final Integer proxyPassword;
    private final String clientId;
    private final String clientSecret;
    private final URI redirectUri;
    private String accessToken;
    private String refreshToken;
    
    private SpotifyApi(final Builder builder) {
        assert builder.httpManager != null;
        this.httpManager = builder.httpManager;
        this.scheme = builder.scheme;
        this.host = builder.host;
        this.port = builder.port;
        this.proxyUrl = builder.proxyUrl;
        this.proxyPort = builder.proxyPort;
        this.proxyUsername = builder.proxyUsername;
        this.proxyPassword = builder.proxyPassword;
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.redirectUri = builder.redirectUri;
        this.accessToken = builder.accessToken;
        this.refreshToken = builder.refreshToken;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static String concat(final String[] parts, final char character) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final String part : parts) {
            stringBuilder.append(part).append(character);
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
    
    public static Date parseDefaultDate(final String date) throws ParseException {
        return SpotifyApi.SIMPLE_DATE_FORMAT.get().parse(date);
    }
    
    public static String formatDefaultDate(final Date date) {
        return SpotifyApi.SIMPLE_DATE_FORMAT.get().format(date);
    }
    
    public static SimpleDateFormat makeSimpleDateFormat(final String pattern, final String id) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(id));
        return simpleDateFormat;
    }
    
    public IHttpManager getHttpManager() {
        return this.httpManager;
    }
    
    public String getScheme() {
        return this.scheme;
    }
    
    public String getHost() {
        return this.host;
    }
    
    public Integer getPort() {
        return this.port;
    }
    
    public String getProxyUrl() {
        return this.proxyUrl;
    }
    
    public Integer getProxyPort() {
        return this.proxyPort;
    }
    
    public Integer getProxyUsername() {
        return this.proxyUsername;
    }
    
    public Integer getProxyPassword() {
        return this.proxyPassword;
    }
    
    public String getClientId() {
        return this.clientId;
    }
    
    public String getClientSecret() {
        return this.clientSecret;
    }
    
    public URI getRedirectURI() {
        return this.redirectUri;
    }
    
    public String getAccessToken() {
        return this.accessToken;
    }
    
    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }
    
    public String getRefreshToken() {
        return this.refreshToken;
    }
    
    public void setRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public AuthorizationCodeRefreshRequest.Builder authorizationCodeRefresh(final String client_id, final String client_secret, final String refresh_token) {
        return ((AbstractRequest.Builder<T, AuthorizationCodeRefreshRequest.Builder>)new AuthorizationCodeRefreshRequest.Builder(client_id, client_secret)).setDefaults(this.httpManager, this.scheme, this.host, this.port).grant_type("refresh_token").refresh_token(refresh_token);
    }
    
    public AuthorizationCodeRefreshRequest.Builder authorizationCodeRefresh() {
        return ((AbstractRequest.Builder<T, AuthorizationCodeRefreshRequest.Builder>)new AuthorizationCodeRefreshRequest.Builder(this.clientId, this.clientSecret)).setDefaults(this.httpManager, this.scheme, this.host, this.port).grant_type("refresh_token").refresh_token(this.refreshToken);
    }
    
    public AuthorizationCodePKCERefreshRequest.Builder authorizationCodePKCERefresh(final String client_id, final String refresh_token) {
        return ((AbstractRequest.Builder<T, AuthorizationCodePKCERefreshRequest.Builder>)new AuthorizationCodePKCERefreshRequest.Builder()).setDefaults(this.httpManager, this.scheme, this.host, this.port).client_id(client_id).grant_type("refresh_token").refresh_token(refresh_token);
    }
    
    public AuthorizationCodePKCERefreshRequest.Builder authorizationCodePKCERefresh() {
        return ((AbstractRequest.Builder<T, AuthorizationCodePKCERefreshRequest.Builder>)new AuthorizationCodePKCERefreshRequest.Builder()).setDefaults(this.httpManager, this.scheme, this.host, this.port).client_id(this.clientId).grant_type("refresh_token").refresh_token(this.refreshToken);
    }
    
    public AuthorizationCodeRequest.Builder authorizationCode(final String client_id, final String client_secret, final String code, final URI redirect_uri) {
        return ((AbstractRequest.Builder<T, AuthorizationCodeRequest.Builder>)new AuthorizationCodeRequest.Builder(client_id, client_secret)).setDefaults(this.httpManager, this.scheme, this.host, this.port).grant_type("authorization_code").code(code).redirect_uri(redirect_uri);
    }
    
    public AuthorizationCodeRequest.Builder authorizationCode(final String code) {
        return ((AbstractRequest.Builder<T, AuthorizationCodeRequest.Builder>)new AuthorizationCodeRequest.Builder(this.clientId, this.clientSecret)).setDefaults(this.httpManager, this.scheme, this.host, this.port).grant_type("authorization_code").code(code).redirect_uri(this.redirectUri);
    }
    
    public AuthorizationCodePKCERequest.Builder authorizationCodePKCE(final String client_id, final String code, final String code_verifier, final URI redirect_uri) {
        return ((AbstractRequest.Builder<T, AuthorizationCodePKCERequest.Builder>)new AuthorizationCodePKCERequest.Builder()).setDefaults(this.httpManager, this.scheme, this.host, this.port).client_id(client_id).code_verifier(code_verifier).grant_type("authorization_code").code(code).redirect_uri(redirect_uri);
    }
    
    public AuthorizationCodePKCERequest.Builder authorizationCodePKCE(final String code, final String code_verifier) {
        return ((AbstractRequest.Builder<T, AuthorizationCodePKCERequest.Builder>)new AuthorizationCodePKCERequest.Builder()).setDefaults(this.httpManager, this.scheme, this.host, this.port).client_id(this.clientId).code_verifier(code_verifier).grant_type("authorization_code").code(code).redirect_uri(this.redirectUri);
    }
    
    public AuthorizationCodeUriRequest.Builder authorizationCodeUri(final String client_id, final URI redirect_uri) {
        return ((AbstractRequest.Builder<T, AuthorizationCodeUriRequest.Builder>)new AuthorizationCodeUriRequest.Builder()).setDefaults(this.httpManager, this.scheme, this.host, this.port).client_id(client_id).response_type("code").redirect_uri(redirect_uri);
    }
    
    public AuthorizationCodeUriRequest.Builder authorizationCodeUri() {
        return ((AbstractRequest.Builder<T, AuthorizationCodeUriRequest.Builder>)new AuthorizationCodeUriRequest.Builder()).setDefaults(this.httpManager, this.scheme, this.host, this.port).client_id(this.clientId).response_type("code").redirect_uri(this.redirectUri);
    }
    
    public AuthorizationCodeUriRequest.Builder authorizationCodePKCEUri(final String client_id, final String code_challenge, final URI redirect_uri) {
        return ((AbstractRequest.Builder<T, AuthorizationCodeUriRequest.Builder>)new AuthorizationCodeUriRequest.Builder()).setDefaults(this.httpManager, this.scheme, this.host, this.port).client_id(client_id).response_type("code").code_challenge_method("S256").code_challenge(code_challenge).redirect_uri(redirect_uri);
    }
    
    public AuthorizationCodeUriRequest.Builder authorizationCodePKCEUri(final String code_challenge) {
        return ((AbstractRequest.Builder<T, AuthorizationCodeUriRequest.Builder>)new AuthorizationCodeUriRequest.Builder()).setDefaults(this.httpManager, this.scheme, this.host, this.port).client_id(this.clientId).response_type("code").code_challenge_method("S256").code_challenge(code_challenge).redirect_uri(this.redirectUri);
    }
    
    public ClientCredentialsRequest.Builder clientCredentials() {
        return ((AbstractRequest.Builder<T, ClientCredentialsRequest.Builder>)new ClientCredentialsRequest.Builder(this.clientId, this.clientSecret)).setDefaults(this.httpManager, this.scheme, this.host, this.port).grant_type("client_credentials");
    }
    
    public GetAlbumRequest.Builder getAlbum(final String id) {
        return ((AbstractRequest.Builder<T, GetAlbumRequest.Builder>)new GetAlbumRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).id(id);
    }
    
    public GetAlbumsTracksRequest.Builder getAlbumsTracks(final String id) {
        return ((AbstractRequest.Builder<T, GetAlbumsTracksRequest.Builder>)new GetAlbumsTracksRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).id(id);
    }
    
    public GetSeveralAlbumsRequest.Builder getSeveralAlbums(final String... ids) {
        return ((AbstractRequest.Builder<T, GetSeveralAlbumsRequest.Builder>)new GetSeveralAlbumsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(concat(ids, ','));
    }
    
    public GetArtistRequest.Builder getArtist(final String id) {
        return ((AbstractRequest.Builder<T, GetArtistRequest.Builder>)new GetArtistRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).id(id);
    }
    
    public GetArtistsAlbumsRequest.Builder getArtistsAlbums(final String id) {
        return ((AbstractRequest.Builder<T, GetArtistsAlbumsRequest.Builder>)new GetArtistsAlbumsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).id(id);
    }
    
    public GetArtistsTopTracksRequest.Builder getArtistsTopTracks(final String id, final CountryCode country) {
        return ((AbstractRequest.Builder<T, GetArtistsTopTracksRequest.Builder>)new GetArtistsTopTracksRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).id(id).country(country);
    }
    
    public GetArtistsRelatedArtistsRequest.Builder getArtistsRelatedArtists(final String id) {
        return ((AbstractRequest.Builder<T, GetArtistsRelatedArtistsRequest.Builder>)new GetArtistsRelatedArtistsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).id(id);
    }
    
    public GetSeveralArtistsRequest.Builder getSeveralArtists(final String... ids) {
        return ((AbstractRequest.Builder<T, GetSeveralArtistsRequest.Builder>)new GetSeveralArtistsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(concat(ids, ','));
    }
    
    public GetCategoryRequest.Builder getCategory(final String category_id) {
        return ((AbstractRequest.Builder<T, GetCategoryRequest.Builder>)new GetCategoryRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).category_id(category_id);
    }
    
    public GetCategorysPlaylistsRequest.Builder getCategorysPlaylists(final String category_id) {
        return ((AbstractRequest.Builder<T, GetCategorysPlaylistsRequest.Builder>)new GetCategorysPlaylistsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).category_id(category_id);
    }
    
    public GetListOfCategoriesRequest.Builder getListOfCategories() {
        return ((AbstractRequest.Builder<T, GetListOfCategoriesRequest.Builder>)new GetListOfCategoriesRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port);
    }
    
    public GetListOfFeaturedPlaylistsRequest.Builder getListOfFeaturedPlaylists() {
        return ((AbstractRequest.Builder<T, GetListOfFeaturedPlaylistsRequest.Builder>)new GetListOfFeaturedPlaylistsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port);
    }
    
    public GetListOfNewReleasesRequest.Builder getListOfNewReleases() {
        return ((AbstractRequest.Builder<T, GetListOfNewReleasesRequest.Builder>)new GetListOfNewReleasesRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port);
    }
    
    public GetRecommendationsRequest.Builder getRecommendations() {
        return ((AbstractRequest.Builder<T, GetRecommendationsRequest.Builder>)new GetRecommendationsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port);
    }
    
    public GetAvailableGenreSeedsRequest.Builder getAvailableGenreSeeds() {
        return ((AbstractRequest.Builder<T, GetAvailableGenreSeedsRequest.Builder>)new GetAvailableGenreSeedsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port);
    }
    
    public GetEpisodeRequest.Builder getEpisode(final String id) {
        return ((AbstractRequest.Builder<T, GetEpisodeRequest.Builder>)new GetEpisodeRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).id(id);
    }
    
    public GetSeveralEpisodesRequest.Builder getSeveralEpisodes(final String... ids) {
        return ((AbstractRequest.Builder<T, GetSeveralEpisodesRequest.Builder>)new GetSeveralEpisodesRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(concat(ids, ','));
    }
    
    public CheckCurrentUserFollowsArtistsOrUsersRequest.Builder checkCurrentUserFollowsArtistsOrUsers(final ModelObjectType type, final String[] ids) {
        return ((AbstractRequest.Builder<T, CheckCurrentUserFollowsArtistsOrUsersRequest.Builder>)new CheckCurrentUserFollowsArtistsOrUsersRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).type(type).ids(concat(ids, ','));
    }
    
    public CheckUsersFollowPlaylistRequest.Builder checkUsersFollowPlaylist(final String owner_id, final String playlist_id, final String[] ids) {
        return ((AbstractRequest.Builder<T, CheckUsersFollowPlaylistRequest.Builder>)new CheckUsersFollowPlaylistRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).owner_id(owner_id).playlist_id(playlist_id).ids(concat(ids, ','));
    }
    
    public FollowArtistsOrUsersRequest.Builder followArtistsOrUsers(final ModelObjectType type, final String[] ids) {
        return ((AbstractRequest.Builder<T, FollowArtistsOrUsersRequest.Builder>)new FollowArtistsOrUsersRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).type(type).ids(concat(ids, ','));
    }
    
    public FollowArtistsOrUsersRequest.Builder followArtistsOrUsers(final ModelObjectType type, final JsonArray ids) {
        return ((AbstractRequest.Builder<T, FollowArtistsOrUsersRequest.Builder>)new FollowArtistsOrUsersRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).type(type).ids(ids);
    }
    
    public FollowPlaylistRequest.Builder followPlaylist(final String owner_id, final String playlist_id, final boolean public_) {
        return ((AbstractRequest.Builder<T, FollowPlaylistRequest.Builder>)new FollowPlaylistRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).owner_id(owner_id).playlist_id(playlist_id).public_(public_);
    }
    
    public se.michaelthelin.spotify.requests.data.follow.FollowPlaylistRequest.Builder followPlaylist(final String playlist_id, final boolean public_) {
        return ((AbstractRequest.Builder<T, se.michaelthelin.spotify.requests.data.follow.FollowPlaylistRequest.Builder>)new se.michaelthelin.spotify.requests.data.follow.FollowPlaylistRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).playlist_id(playlist_id).public_(public_);
    }
    
    public GetUsersFollowedArtistsRequest.Builder getUsersFollowedArtists(final ModelObjectType type) {
        return ((AbstractRequest.Builder<T, GetUsersFollowedArtistsRequest.Builder>)new GetUsersFollowedArtistsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).type(type);
    }
    
    public UnfollowArtistsOrUsersRequest.Builder unfollowArtistsOrUsers(final ModelObjectType type, final String[] ids) {
        return ((AbstractRequest.Builder<T, UnfollowArtistsOrUsersRequest.Builder>)new UnfollowArtistsOrUsersRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).type(type).ids(concat(ids, ','));
    }
    
    public UnfollowArtistsOrUsersRequest.Builder unfollowArtistsOrUsers(final ModelObjectType type, final JsonArray ids) {
        return ((AbstractRequest.Builder<T, UnfollowArtistsOrUsersRequest.Builder>)new UnfollowArtistsOrUsersRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).type(type).ids(ids);
    }
    
    public UnfollowPlaylistRequest.Builder unfollowPlaylist(final String owner_id, final String playlist_id) {
        return ((AbstractRequest.Builder<T, UnfollowPlaylistRequest.Builder>)new UnfollowPlaylistRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).owner_id(owner_id).playlist_id(playlist_id);
    }
    
    public se.michaelthelin.spotify.requests.data.follow.UnfollowPlaylistRequest.Builder unfollowPlaylist(final String playlist_id) {
        return ((AbstractRequest.Builder<T, se.michaelthelin.spotify.requests.data.follow.UnfollowPlaylistRequest.Builder>)new se.michaelthelin.spotify.requests.data.follow.UnfollowPlaylistRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).playlist_id(playlist_id);
    }
    
    public CheckUsersSavedAlbumsRequest.Builder checkUsersSavedAlbums(final String... ids) {
        return ((AbstractRequest.Builder<T, CheckUsersSavedAlbumsRequest.Builder>)new CheckUsersSavedAlbumsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(concat(ids, ','));
    }
    
    public CheckUsersSavedShowsRequest.Builder checkUsersSavedShows(final String... ids) {
        return ((AbstractRequest.Builder<T, CheckUsersSavedShowsRequest.Builder>)new CheckUsersSavedShowsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(concat(ids, ','));
    }
    
    public CheckUsersSavedTracksRequest.Builder checkUsersSavedTracks(final String... ids) {
        return ((AbstractRequest.Builder<T, CheckUsersSavedTracksRequest.Builder>)new CheckUsersSavedTracksRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(concat(ids, ','));
    }
    
    public GetCurrentUsersSavedAlbumsRequest.Builder getCurrentUsersSavedAlbums() {
        return ((AbstractRequest.Builder<T, GetCurrentUsersSavedAlbumsRequest.Builder>)new GetCurrentUsersSavedAlbumsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port);
    }
    
    public GetUsersSavedShowsRequest.Builder getUsersSavedShows() {
        return ((AbstractRequest.Builder<T, GetUsersSavedShowsRequest.Builder>)new GetUsersSavedShowsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port);
    }
    
    public GetUsersSavedTracksRequest.Builder getUsersSavedTracks() {
        return ((AbstractRequest.Builder<T, GetUsersSavedTracksRequest.Builder>)new GetUsersSavedTracksRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port);
    }
    
    public RemoveAlbumsForCurrentUserRequest.Builder removeAlbumsForCurrentUser(final String... ids) {
        return ((AbstractRequest.Builder<T, RemoveAlbumsForCurrentUserRequest.Builder>)new RemoveAlbumsForCurrentUserRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(concat(ids, ','));
    }
    
    public RemoveAlbumsForCurrentUserRequest.Builder removeAlbumsForCurrentUser(final JsonArray ids) {
        return ((AbstractRequest.Builder<T, RemoveAlbumsForCurrentUserRequest.Builder>)new RemoveAlbumsForCurrentUserRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(ids);
    }
    
    public RemoveUsersSavedShowsRequest.Builder removeUsersSavedShows(final String... ids) {
        return ((AbstractRequest.Builder<T, RemoveUsersSavedShowsRequest.Builder>)new RemoveUsersSavedShowsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(concat(ids, ','));
    }
    
    public RemoveUsersSavedShowsRequest.Builder removeUsersSavedShows(final JsonArray ids) {
        return ((AbstractRequest.Builder<T, RemoveUsersSavedShowsRequest.Builder>)new RemoveUsersSavedShowsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(ids);
    }
    
    public RemoveUsersSavedTracksRequest.Builder removeUsersSavedTracks(final String... ids) {
        return ((AbstractRequest.Builder<T, RemoveUsersSavedTracksRequest.Builder>)new RemoveUsersSavedTracksRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(concat(ids, ','));
    }
    
    public RemoveUsersSavedTracksRequest.Builder removeUsersSavedTracks(final JsonArray ids) {
        return ((AbstractRequest.Builder<T, RemoveUsersSavedTracksRequest.Builder>)new RemoveUsersSavedTracksRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(ids);
    }
    
    public SaveAlbumsForCurrentUserRequest.Builder saveAlbumsForCurrentUser(final String... ids) {
        return ((AbstractRequest.Builder<T, SaveAlbumsForCurrentUserRequest.Builder>)new SaveAlbumsForCurrentUserRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(concat(ids, ','));
    }
    
    public SaveAlbumsForCurrentUserRequest.Builder saveAlbumsForCurrentUser(final JsonArray ids) {
        return ((AbstractRequest.Builder<T, SaveAlbumsForCurrentUserRequest.Builder>)new SaveAlbumsForCurrentUserRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(ids);
    }
    
    public SaveShowsForCurrentUserRequest.Builder saveShowsForCurrentUser(final String... ids) {
        return ((AbstractRequest.Builder<T, SaveShowsForCurrentUserRequest.Builder>)new SaveShowsForCurrentUserRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(concat(ids, ','));
    }
    
    public SaveShowsForCurrentUserRequest.Builder saveShowsForCurrentUser(final JsonArray ids) {
        return ((AbstractRequest.Builder<T, SaveShowsForCurrentUserRequest.Builder>)new SaveShowsForCurrentUserRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(ids);
    }
    
    public SaveTracksForUserRequest.Builder saveTracksForUser(final String... ids) {
        return ((AbstractRequest.Builder<T, SaveTracksForUserRequest.Builder>)new SaveTracksForUserRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(concat(ids, ','));
    }
    
    public SaveTracksForUserRequest.Builder saveTracksForUser(final JsonArray ids) {
        return ((AbstractRequest.Builder<T, SaveTracksForUserRequest.Builder>)new SaveTracksForUserRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(ids);
    }
    
    public <T extends IArtistTrackModelObject> GetUsersTopArtistsAndTracksRequest.Builder<T> getUsersTopArtistsAndTracks(final ModelObjectType type) {
        return (GetUsersTopArtistsAndTracksRequest.Builder<T>)new GetUsersTopArtistsAndTracksRequest.Builder<Object>(this.accessToken).setDefaults(this.httpManager, this.scheme, this.host, this.port).type(type);
    }
    
    public GetUsersTopArtistsRequest.Builder getUsersTopArtists() {
        return ((AbstractRequest.Builder<T, GetUsersTopArtistsRequest.Builder>)new GetUsersTopArtistsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port);
    }
    
    public GetUsersTopTracksRequest.Builder getUsersTopTracks() {
        return ((AbstractRequest.Builder<T, GetUsersTopTracksRequest.Builder>)new GetUsersTopTracksRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port);
    }
    
    public GetInformationAboutUsersCurrentPlaybackRequest.Builder getInformationAboutUsersCurrentPlayback() {
        return ((AbstractRequest.Builder<T, GetInformationAboutUsersCurrentPlaybackRequest.Builder>)new GetInformationAboutUsersCurrentPlaybackRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port);
    }
    
    public GetCurrentUsersRecentlyPlayedTracksRequest.Builder getCurrentUsersRecentlyPlayedTracks() {
        return ((AbstractRequest.Builder<T, GetCurrentUsersRecentlyPlayedTracksRequest.Builder>)new GetCurrentUsersRecentlyPlayedTracksRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port);
    }
    
    public GetUsersAvailableDevicesRequest.Builder getUsersAvailableDevices() {
        return ((AbstractRequest.Builder<T, GetUsersAvailableDevicesRequest.Builder>)new GetUsersAvailableDevicesRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port);
    }
    
    public GetUsersCurrentlyPlayingTrackRequest.Builder getUsersCurrentlyPlayingTrack() {
        return ((AbstractRequest.Builder<T, GetUsersCurrentlyPlayingTrackRequest.Builder>)new GetUsersCurrentlyPlayingTrackRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port);
    }
    
    public PauseUsersPlaybackRequest.Builder pauseUsersPlayback() {
        return ((AbstractRequest.Builder<T, PauseUsersPlaybackRequest.Builder>)new PauseUsersPlaybackRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port);
    }
    
    public SeekToPositionInCurrentlyPlayingTrackRequest.Builder seekToPositionInCurrentlyPlayingTrack(final int position_ms) {
        return ((AbstractRequest.Builder<T, SeekToPositionInCurrentlyPlayingTrackRequest.Builder>)new SeekToPositionInCurrentlyPlayingTrackRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).position_ms(position_ms);
    }
    
    public SetRepeatModeOnUsersPlaybackRequest.Builder setRepeatModeOnUsersPlayback(final String state) {
        return ((AbstractRequest.Builder<T, SetRepeatModeOnUsersPlaybackRequest.Builder>)new SetRepeatModeOnUsersPlaybackRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).state(state);
    }
    
    public SetVolumeForUsersPlaybackRequest.Builder setVolumeForUsersPlayback(final int volume_percent) {
        return ((AbstractRequest.Builder<T, SetVolumeForUsersPlaybackRequest.Builder>)new SetVolumeForUsersPlaybackRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).volume_percent(volume_percent);
    }
    
    public SkipUsersPlaybackToNextTrackRequest.Builder skipUsersPlaybackToNextTrack() {
        return ((AbstractRequest.Builder<T, SkipUsersPlaybackToNextTrackRequest.Builder>)new SkipUsersPlaybackToNextTrackRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port);
    }
    
    public SkipUsersPlaybackToPreviousTrackRequest.Builder skipUsersPlaybackToPreviousTrack() {
        return ((AbstractRequest.Builder<T, SkipUsersPlaybackToPreviousTrackRequest.Builder>)new SkipUsersPlaybackToPreviousTrackRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port);
    }
    
    public StartResumeUsersPlaybackRequest.Builder startResumeUsersPlayback() {
        return ((AbstractRequest.Builder<T, StartResumeUsersPlaybackRequest.Builder>)new StartResumeUsersPlaybackRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port);
    }
    
    public ToggleShuffleForUsersPlaybackRequest.Builder toggleShuffleForUsersPlayback(final boolean state) {
        return ((AbstractRequest.Builder<T, ToggleShuffleForUsersPlaybackRequest.Builder>)new ToggleShuffleForUsersPlaybackRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).state(state);
    }
    
    public TransferUsersPlaybackRequest.Builder transferUsersPlayback(final JsonArray device_ids) {
        return ((AbstractRequest.Builder<T, TransferUsersPlaybackRequest.Builder>)new TransferUsersPlaybackRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).device_ids(device_ids);
    }
    
    public AddItemToUsersPlaybackQueueRequest.Builder addItemToUsersPlaybackQueue(final String uri) {
        return ((AbstractRequest.Builder<T, AddItemToUsersPlaybackQueueRequest.Builder>)new AddItemToUsersPlaybackQueueRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).uri(uri);
    }
    
    public AddItemsToPlaylistRequest.Builder addItemsToPlaylist(final String playlist_id, final String[] uris) {
        return ((AbstractRequest.Builder<T, AddItemsToPlaylistRequest.Builder>)new AddItemsToPlaylistRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).playlist_id(playlist_id).uris(concat(uris, ','));
    }
    
    public AddItemsToPlaylistRequest.Builder addItemsToPlaylist(final String playlist_id, final JsonArray uris) {
        return ((AbstractRequest.Builder<T, AddItemsToPlaylistRequest.Builder>)new AddItemsToPlaylistRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).playlist_id(playlist_id).uris(uris);
    }
    
    public ChangePlaylistsDetailsRequest.Builder changePlaylistsDetails(final String playlist_id) {
        return ((AbstractRequest.Builder<T, ChangePlaylistsDetailsRequest.Builder>)new ChangePlaylistsDetailsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).playlist_id(playlist_id);
    }
    
    public CreatePlaylistRequest.Builder createPlaylist(final String user_id, final String name) {
        return ((AbstractRequest.Builder<T, CreatePlaylistRequest.Builder>)new CreatePlaylistRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).user_id(user_id).name(name);
    }
    
    public GetListOfCurrentUsersPlaylistsRequest.Builder getListOfCurrentUsersPlaylists() {
        return ((AbstractRequest.Builder<T, GetListOfCurrentUsersPlaylistsRequest.Builder>)new GetListOfCurrentUsersPlaylistsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port);
    }
    
    public GetListOfUsersPlaylistsRequest.Builder getListOfUsersPlaylists(final String user_id) {
        return ((AbstractRequest.Builder<T, GetListOfUsersPlaylistsRequest.Builder>)new GetListOfUsersPlaylistsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).user_id(user_id);
    }
    
    public GetPlaylistRequest.Builder getPlaylist(final String playlist_id) {
        return ((AbstractRequest.Builder<T, GetPlaylistRequest.Builder>)new GetPlaylistRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).playlist_id(playlist_id);
    }
    
    public GetPlaylistCoverImageRequest.Builder getPlaylistCoverImage(final String playlist_id) {
        return ((AbstractRequest.Builder<T, GetPlaylistCoverImageRequest.Builder>)new GetPlaylistCoverImageRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).playlist_id(playlist_id);
    }
    
    public GetPlaylistsItemsRequest.Builder getPlaylistsItems(final String playlist_id) {
        return ((AbstractRequest.Builder<T, GetPlaylistsItemsRequest.Builder>)new GetPlaylistsItemsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).playlist_id(playlist_id);
    }
    
    public RemoveItemsFromPlaylistRequest.Builder removeItemsFromPlaylist(final String playlist_id, final JsonArray tracks) {
        return ((AbstractRequest.Builder<T, RemoveItemsFromPlaylistRequest.Builder>)new RemoveItemsFromPlaylistRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).playlist_id(playlist_id).tracks(tracks);
    }
    
    public ReorderPlaylistsItemsRequest.Builder reorderPlaylistsItems(final String playlist_id, final int range_start, final int insert_before) {
        return ((AbstractRequest.Builder<T, ReorderPlaylistsItemsRequest.Builder>)new ReorderPlaylistsItemsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).playlist_id(playlist_id).range_start(range_start).insert_before(insert_before);
    }
    
    public ReplacePlaylistsItemsRequest.Builder replacePlaylistsItems(final String playlist_id, final String[] uris) {
        return ((AbstractRequest.Builder<T, ReplacePlaylistsItemsRequest.Builder>)new ReplacePlaylistsItemsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).playlist_id(playlist_id).uris(concat(uris, ','));
    }
    
    public ReplacePlaylistsItemsRequest.Builder replacePlaylistsItems(final String playlist_id, final JsonArray uris) {
        return ((AbstractRequest.Builder<T, ReplacePlaylistsItemsRequest.Builder>)new ReplacePlaylistsItemsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).playlist_id(playlist_id).uris(uris);
    }
    
    public UploadCustomPlaylistCoverImageRequest.Builder uploadCustomPlaylistCoverImage(final String playlist_id) {
        return ((AbstractRequest.Builder<T, UploadCustomPlaylistCoverImageRequest.Builder>)new UploadCustomPlaylistCoverImageRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).playlist_id(playlist_id);
    }
    
    public SearchItemRequest.Builder searchItem(final String q, final String type) {
        return ((AbstractRequest.Builder<T, SearchItemRequest.Builder>)new SearchItemRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).q(q).type(type);
    }
    
    public SearchAlbumsRequest.Builder searchAlbums(final String q) {
        return ((AbstractRequest.Builder<T, SearchAlbumsRequest.Builder>)new SearchAlbumsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).q(q);
    }
    
    public SearchAlbumsSpecialRequest.Builder searchAlbumsSpecial(final String q) {
        return ((AbstractRequest.Builder<T, SearchAlbumsSpecialRequest.Builder>)new SearchAlbumsSpecialRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).q(q);
    }
    
    public SearchArtistsRequest.Builder searchArtists(final String q) {
        return ((AbstractRequest.Builder<T, SearchArtistsRequest.Builder>)new SearchArtistsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).q(q);
    }
    
    public SearchEpisodesRequest.Builder searchEpisodes(final String q) {
        return ((AbstractRequest.Builder<T, SearchEpisodesRequest.Builder>)new SearchEpisodesRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).q(q);
    }
    
    public SearchPlaylistsRequest.Builder searchPlaylists(final String q) {
        return ((AbstractRequest.Builder<T, SearchPlaylistsRequest.Builder>)new SearchPlaylistsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).q(q);
    }
    
    public SearchShowsRequest.Builder searchShows(final String q) {
        return ((AbstractRequest.Builder<T, SearchShowsRequest.Builder>)new SearchShowsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).q(q);
    }
    
    public SearchTracksRequest.Builder searchTracks(final String q) {
        return ((AbstractRequest.Builder<T, SearchTracksRequest.Builder>)new SearchTracksRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).q(q);
    }
    
    public GetShowRequest.Builder getShow(final String id) {
        return ((AbstractRequest.Builder<T, GetShowRequest.Builder>)new GetShowRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).id(id);
    }
    
    public GetSeveralShowsRequest.Builder getSeveralShows(final String... ids) {
        return ((AbstractRequest.Builder<T, GetSeveralShowsRequest.Builder>)new GetSeveralShowsRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(concat(ids, ','));
    }
    
    public GetShowsEpisodesRequest.Builder getShowEpisodes(final String id) {
        return ((AbstractRequest.Builder<T, GetShowsEpisodesRequest.Builder>)new GetShowsEpisodesRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).id(id);
    }
    
    public GetAudioAnalysisForTrackRequest.Builder getAudioAnalysisForTrack(final String id) {
        return ((AbstractRequest.Builder<T, GetAudioAnalysisForTrackRequest.Builder>)new GetAudioAnalysisForTrackRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).id(id);
    }
    
    public GetAudioFeaturesForTrackRequest.Builder getAudioFeaturesForTrack(final String id) {
        return ((AbstractRequest.Builder<T, GetAudioFeaturesForTrackRequest.Builder>)new GetAudioFeaturesForTrackRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).id(id);
    }
    
    public GetAudioFeaturesForSeveralTracksRequest.Builder getAudioFeaturesForSeveralTracks(final String... ids) {
        return ((AbstractRequest.Builder<T, GetAudioFeaturesForSeveralTracksRequest.Builder>)new GetAudioFeaturesForSeveralTracksRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(concat(ids, ','));
    }
    
    public GetSeveralTracksRequest.Builder getSeveralTracks(final String... ids) {
        return ((AbstractRequest.Builder<T, GetSeveralTracksRequest.Builder>)new GetSeveralTracksRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).ids(concat(ids, ','));
    }
    
    public GetTrackRequest.Builder getTrack(final String id) {
        return ((AbstractRequest.Builder<T, GetTrackRequest.Builder>)new GetTrackRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).id(id);
    }
    
    public GetCurrentUsersProfileRequest.Builder getCurrentUsersProfile() {
        return ((AbstractRequest.Builder<T, GetCurrentUsersProfileRequest.Builder>)new GetCurrentUsersProfileRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port);
    }
    
    public GetUsersProfileRequest.Builder getUsersProfile(final String user_id) {
        return ((AbstractRequest.Builder<T, GetUsersProfileRequest.Builder>)new GetUsersProfileRequest.Builder(this.accessToken)).setDefaults(this.httpManager, this.scheme, this.host, this.port).user_id(user_id);
    }
    
    static {
        DEFAULT_HTTP_MANAGER = new SpotifyHttpManager.Builder().build();
        LOGGER = Logger.getLogger(SpotifyApi.class.getName());
        SIMPLE_DATE_FORMAT = ThreadLocal.withInitial(() -> makeSimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", "GMT"));
    }
    
    public static class Builder
    {
        private IHttpManager httpManager;
        private String scheme;
        private String host;
        private Integer port;
        private String proxyUrl;
        private Integer proxyPort;
        private Integer proxyUsername;
        private Integer proxyPassword;
        private String clientId;
        private String clientSecret;
        private URI redirectUri;
        private String accessToken;
        private String refreshToken;
        
        public Builder() {
            this.httpManager = SpotifyApi.DEFAULT_HTTP_MANAGER;
            this.scheme = "https";
            this.host = "api.spotify.com";
            this.port = 443;
        }
        
        public Builder setHttpManager(final IHttpManager httpManager) {
            this.httpManager = httpManager;
            return this;
        }
        
        public Builder setScheme(final String scheme) {
            this.scheme = scheme;
            return this;
        }
        
        public Builder setHost(final String host) {
            this.host = host;
            return this;
        }
        
        public Builder setPort(final Integer port) {
            this.port = port;
            return this;
        }
        
        public Builder setProxyUrl(final String proxyUrl) {
            this.proxyUrl = proxyUrl;
            return this;
        }
        
        public Builder setProxyPort(final Integer proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }
        
        public Builder setProxyUsername(final Integer proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }
        
        public Builder setProxyPassword(final Integer proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }
        
        public Builder setClientId(final String clientId) {
            this.clientId = clientId;
            return this;
        }
        
        public Builder setClientSecret(final String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }
        
        public Builder setRedirectUri(final URI redirectUri) {
            this.redirectUri = redirectUri;
            return this;
        }
        
        public Builder setAccessToken(final String accessToken) {
            this.accessToken = accessToken;
            return this;
        }
        
        public Builder setRefreshToken(final String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }
        
        public SpotifyApi build() {
            return new SpotifyApi(this, null);
        }
    }
}
