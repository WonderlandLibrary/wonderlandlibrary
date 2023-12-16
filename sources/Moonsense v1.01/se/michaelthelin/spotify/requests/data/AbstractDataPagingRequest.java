// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data;

import se.michaelthelin.spotify.model_objects.specification.Paging;

public abstract class AbstractDataPagingRequest<T> extends AbstractDataRequest<T>
{
    protected AbstractDataPagingRequest(final AbstractDataRequest.Builder<T, ?> builder) {
        super(builder);
    }
    
    public abstract static class Builder<T, BT extends Builder<T, ?>> extends AbstractDataRequest.Builder<Paging<T>, BT> implements IPagingRequestBuilder<T, BT>
    {
        protected Builder(final String accessToken) {
            super(accessToken);
            assert !accessToken.equals("");
            this.setHeader("Authorization", invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, accessToken));
        }
    }
}
