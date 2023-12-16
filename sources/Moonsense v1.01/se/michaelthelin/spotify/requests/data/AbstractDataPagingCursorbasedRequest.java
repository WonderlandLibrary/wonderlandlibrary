// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify.requests.data;

import se.michaelthelin.spotify.model_objects.specification.PagingCursorbased;

public abstract class AbstractDataPagingCursorbasedRequest<T> extends AbstractDataRequest<T>
{
    protected AbstractDataPagingCursorbasedRequest(final AbstractDataRequest.Builder<T, ?> builder) {
        super(builder);
    }
    
    public abstract static class Builder<T, A, BT extends Builder<T, A, ?>> extends AbstractDataRequest.Builder<PagingCursorbased<T>, BT> implements IPagingCursorbasedRequestBuilder<T, A, BT>
    {
        protected Builder(final String accessToken) {
            super(accessToken);
            assert !accessToken.equals("");
            this.setHeader("Authorization", invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, accessToken));
        }
    }
}
