// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.exceptions;

import org.apache.hc.core5.http.HttpException;

public class SpotifyWebApiException extends HttpException
{
    public SpotifyWebApiException() {
    }
    
    public SpotifyWebApiException(final String message) {
        super(message);
    }
    
    public SpotifyWebApiException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
