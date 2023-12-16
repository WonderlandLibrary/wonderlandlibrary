// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify;

import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import org.apache.hc.core5.http.Header;
import java.net.URI;

public interface IHttpManager
{
    String get(final URI p0, final Header[] p1) throws IOException, SpotifyWebApiException, ParseException;
    
    String post(final URI p0, final Header[] p1, final HttpEntity p2) throws IOException, SpotifyWebApiException, ParseException;
    
    String put(final URI p0, final Header[] p1, final HttpEntity p2) throws IOException, SpotifyWebApiException, ParseException;
    
    String delete(final URI p0, final Header[] p1, final HttpEntity p2) throws IOException, SpotifyWebApiException, ParseException;
}
