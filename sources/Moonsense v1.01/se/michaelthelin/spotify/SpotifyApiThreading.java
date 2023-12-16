// 
// Decompiled by Procyon v0.5.36
// 

package se.michaelthelin.spotify;

import java.util.concurrent.Executors;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class SpotifyApiThreading
{
    public static final ExecutorService THREADPOOL;
    
    public static <T> CompletableFuture<T> executeAsync(final Callable<T> callable) {
        final CompletableFuture<T> future = new CompletableFuture<T>();
        final CompletableFuture<T> completableFuture;
        SpotifyApiThreading.THREADPOOL.execute(() -> {
            try {
                completableFuture.complete(callable.call());
            }
            catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
            return;
        });
        return future;
    }
    
    static {
        THREADPOOL = Executors.newCachedThreadPool();
    }
}
