// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.exceptions.detailed;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

public class UnauthorizedException extends SpotifyWebApiException
{
    public UnauthorizedException() {
    }
    
    public UnauthorizedException(final String message) {
        super(message);
    }
    
    public UnauthorizedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
