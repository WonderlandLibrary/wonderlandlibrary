// 
// Decompiled by Procyon v0.5.36
// 

package com.thealtening.api;

import com.thealtening.api.retriever.AsynchronousDataRetriever;
import com.thealtening.api.retriever.BasicDataRetriever;

public final class TheAltening
{
    public static BasicDataRetriever newBasicRetriever(final String apiKey) {
        return new BasicDataRetriever(apiKey);
    }
    
    public static AsynchronousDataRetriever newAsyncRetriever(final String apiKey) {
        return new AsynchronousDataRetriever(apiKey);
    }
}
