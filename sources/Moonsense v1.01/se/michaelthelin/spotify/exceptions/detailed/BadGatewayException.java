// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.exceptions.detailed;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

public class BadGatewayException extends SpotifyWebApiException
{
    public BadGatewayException() {
    }
    
    public BadGatewayException(final String message) {
        super(message);
    }
    
    public BadGatewayException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
