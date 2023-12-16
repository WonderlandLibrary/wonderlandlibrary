// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.exceptions.detailed;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

public class ServiceUnavailableException extends SpotifyWebApiException
{
    public ServiceUnavailableException() {
    }
    
    public ServiceUnavailableException(final String message) {
        super(message);
    }
    
    public ServiceUnavailableException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
