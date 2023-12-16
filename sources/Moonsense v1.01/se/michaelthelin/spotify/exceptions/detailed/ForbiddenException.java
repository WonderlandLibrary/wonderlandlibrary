// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.exceptions.detailed;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

public class ForbiddenException extends SpotifyWebApiException
{
    public ForbiddenException() {
    }
    
    public ForbiddenException(final String message) {
        super(message);
    }
    
    public ForbiddenException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
