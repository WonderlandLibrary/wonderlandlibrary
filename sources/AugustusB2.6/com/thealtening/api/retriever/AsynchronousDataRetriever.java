// 
// Decompiled by Procyon v0.5.36
// 

package com.thealtening.api.retriever;

import com.thealtening.api.TheAlteningException;
import java.util.function.Function;
import java.util.List;
import com.thealtening.api.response.Account;
import com.thealtening.api.response.License;
import java.util.concurrent.CompletableFuture;

public class AsynchronousDataRetriever extends BasicDataRetriever
{
    public AsynchronousDataRetriever(final String apiKey) {
        super(apiKey);
    }
    
    public CompletableFuture<License> getLicenseDataAsync() {
        return this.completeTask(BasicDataRetriever::getLicense);
    }
    
    public CompletableFuture<Account> getAccountDataAsync() {
        return this.completeTask(BasicDataRetriever::getAccount);
    }
    
    public CompletableFuture<Boolean> isPrivateAsync(final String token) {
        return this.completeTask(dr -> dr.isPrivate(token));
    }
    
    public CompletableFuture<Boolean> isFavoriteAsync(final String token) {
        return this.completeTask(dr -> dr.isFavorite(token));
    }
    
    public CompletableFuture<List<Account>> getPrivatedAccountsAsync() {
        return this.completeTask(BasicDataRetriever::getPrivatedAccounts);
    }
    
    public CompletableFuture<List<Account>> getFavoritedAccountsAsync() {
        return this.completeTask(BasicDataRetriever::getFavoriteAccounts);
    }
    
    private <T> CompletableFuture<T> completeTask(final Function<BasicDataRetriever, T> function) {
        final CompletableFuture<T> returnValue = new CompletableFuture<T>();
        try {
            returnValue.complete(function.apply(this));
        }
        catch (TheAlteningException exception) {
            returnValue.completeExceptionally(exception);
        }
        return returnValue;
    }
}
