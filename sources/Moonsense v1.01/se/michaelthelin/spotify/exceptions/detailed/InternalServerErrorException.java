// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.exceptions.detailed;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

public class InternalServerErrorException extends SpotifyWebApiException
{
    public InternalServerErrorException() {
    }
    
    public InternalServerErrorException(final String message) {
        super(message);
    }
    
    public InternalServerErrorException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
