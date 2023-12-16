// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.exceptions.detailed;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

public class BadRequestException extends SpotifyWebApiException
{
    public BadRequestException() {
    }
    
    public BadRequestException(final String message) {
        super(message);
    }
    
    public BadRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
