// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.exceptions.detailed;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

public class NotFoundException extends SpotifyWebApiException
{
    public NotFoundException() {
    }
    
    public NotFoundException(final String message) {
        super(message);
    }
    
    public NotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
