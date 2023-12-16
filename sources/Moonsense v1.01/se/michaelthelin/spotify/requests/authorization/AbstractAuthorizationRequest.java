// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.authorization;

import se.michaelthelin.spotify.Base64;
import se.michaelthelin.spotify.requests.AbstractRequest;

public abstract class AbstractAuthorizationRequest<T> extends AbstractRequest<T>
{
    protected AbstractAuthorizationRequest(final Builder<T, ?> builder) {
        super(builder);
    }
    
    public abstract static class Builder<T, BT extends Builder<T, ?>> extends AbstractRequest.Builder<T, BT>
    {
        protected Builder(final String clientId, final String clientSecret) {
            assert clientId != null;
            assert clientSecret != null;
            assert !clientId.equals("");
            assert !clientSecret.equals("");
            this.setHeader("Authorization", invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, Base64.encode(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;, clientId, clientSecret).getBytes())));
        }
    }
}
