// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data;

import se.michaelthelin.spotify.requests.AbstractRequest;

public abstract class AbstractDataRequest<T> extends AbstractRequest<T>
{
    protected AbstractDataRequest(final Builder<T, ?> builder) {
        super(builder);
    }
    
    public abstract static class Builder<T, BT extends Builder<T, ?>> extends AbstractRequest.Builder<T, BT>
    {
        protected Builder(final String accessToken) {
            assert accessToken != null;
            assert !accessToken.equals("");
            this.setHeader("Authorization", invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, accessToken));
        }
    }
}
