// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.exceptions.detailed;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

public class TooManyRequestsException extends SpotifyWebApiException
{
    private int retryAfter;
    
    public TooManyRequestsException() {
    }
    
    public TooManyRequestsException(final String message, final int retryAfter) {
        super(message);
        this.setRetryAfter(retryAfter);
    }
    
    public TooManyRequestsException(final String message) {
        super(message);
    }
    
    public TooManyRequestsException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public int getRetryAfter() {
        return this.retryAfter;
    }
    
    public void setRetryAfter(final int retryAfter) {
        this.retryAfter = retryAfter;
    }
}
