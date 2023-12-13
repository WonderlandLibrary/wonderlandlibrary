// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks;

import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Supplier;
import me.gong.mcleaks.util.google.common.cache.CacheLoader;
import me.gong.mcleaks.util.google.common.cache.CacheBuilder;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import me.gong.mcleaks.util.google.gson.Gson;
import java.util.UUID;
import me.gong.mcleaks.util.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutorService;

class MCLeaksAPIImpl implements MCLeaksAPI
{
    private static final String API_PRE = "https://mcleaks.themrgong.xyz/api/v3/";
    private static final String NAME_CHECK = "isnamemcleaks";
    private static final String UUID_CHECK = "isuuidmcleaks";
    private final ExecutorService service;
    private final LoadingCache<String, Boolean> nameCache;
    private final LoadingCache<UUID, Boolean> uuidCache;
    private final MCLeaksChecker<String> nameChecker;
    private final MCLeaksChecker<UUID> uuidChecker;
    private final Gson gson;
    private final String userAgent;
    private final boolean testing;
    
    MCLeaksAPIImpl(final int threadCount, final long expireAfter, final TimeUnit unit, final boolean testing, final String userAgent) {
        this.gson = new Gson();
        this.service = Executors.newFixedThreadPool(threadCount);
        this.nameChecker = new MCLeaksNameChecker();
        this.uuidChecker = new MCLeaksUUIDChecker();
        this.nameCache = this.createCache(expireAfter, unit, this.nameChecker);
        this.uuidCache = this.createCache(expireAfter, unit, this.uuidChecker);
        this.testing = testing;
        this.userAgent = userAgent;
    }
    
    MCLeaksAPIImpl(final int threadCount, final boolean testing, final String userAgent) {
        this.gson = new Gson();
        this.service = Executors.newFixedThreadPool(threadCount);
        this.nameChecker = new MCLeaksNameChecker();
        this.uuidChecker = new MCLeaksUUIDChecker();
        this.nameCache = null;
        this.uuidCache = null;
        this.testing = testing;
        this.userAgent = userAgent;
    }
    
    @Override
    public void checkAccount(final String username, final Consumer<Boolean> callback, final Consumer<Throwable> errorHandler) {
        this.doResultHandling(() -> this.checkAccount(username), callback, errorHandler);
    }
    
    @Override
    public Result checkAccount(final String username) {
        try {
            return new Result(this.checkNameExists(username));
        }
        catch (Exception ex) {
            return new Result(ex.getCause());
        }
    }
    
    @Override
    public Optional<Boolean> getCachedCheck(final String username) {
        return Optional.ofNullable((this.nameCache == null) ? null : this.nameCache.getIfPresent(username));
    }
    
    @Override
    public void checkAccount(final UUID uuid, final Consumer<Boolean> callback, final Consumer<Throwable> errorHandler) {
        this.doResultHandling(() -> this.checkAccount(uuid), callback, errorHandler);
    }
    
    @Override
    public Result checkAccount(final UUID uuid) {
        try {
            return new Result(this.checkUUIDExists(uuid));
        }
        catch (Exception ex) {
            return new Result(ex.getCause());
        }
    }
    
    @Override
    public Optional<Boolean> getCachedCheck(final UUID uuid) {
        return Optional.ofNullable((this.uuidCache == null) ? null : this.uuidCache.getIfPresent(uuid));
    }
    
    @Override
    public void shutdown() {
        this.service.shutdown();
        this.nameCache.cleanUp();
    }
    
    private boolean checkNameExists(final String name) throws Exception {
        if (this.nameCache == null) {
            return this.nameChecker.load(name);
        }
        return this.nameCache.get(name);
    }
    
    private boolean checkUUIDExists(final UUID uuid) throws Exception {
        if (this.uuidCache == null) {
            return this.uuidChecker.load(uuid);
        }
        return this.uuidCache.get(uuid);
    }
    
    private <T> LoadingCache<T, Boolean> createCache(final long expireAfter, final TimeUnit unit, final MCLeaksChecker<T> checker) {
        return CacheBuilder.newBuilder().expireAfterWrite(expireAfter, unit).build((CacheLoader<? super T, Boolean>)checker);
    }
    
    private void doResultHandling(final Supplier<Result> resultSupplier, final Consumer<Boolean> callback, final Consumer<Throwable> errorHandler) {
        final Result result;
        this.service.submit(() -> {
            result = resultSupplier.get();
            if (result.hasError()) {
                errorHandler.accept(result.getError());
            }
            else {
                callback.accept(result.isMCLeaks());
            }
        });
    }
    
    private static class MCLeaksResponse
    {
        private boolean isMcleaks;
    }
    
    private static class MCLeaksError
    {
        private String error;
    }
    
    private class MCLeaksNameChecker extends MCLeaksChecker<String>
    {
        @Override
        protected String getApiType() {
            return "isnamemcleaks";
        }
        
        @Override
        protected String getInputText(final String input) {
            return input;
        }
    }
    
    private class MCLeaksUUIDChecker extends MCLeaksChecker<UUID>
    {
        @Override
        protected String getApiType() {
            return "isuuidmcleaks";
        }
        
        @Override
        protected String getInputText(final UUID input) {
            return input.toString();
        }
    }
    
    private abstract class MCLeaksChecker<T> extends CacheLoader<T, Boolean>
    {
        @Override
        public Boolean load(final T value) throws Exception {
            final URL url = new URL("https://mcleaks.themrgong.xyz/api/v3/" + this.getApiType() + "/" + this.getInputText(value));
            final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", MCLeaksAPIImpl.this.userAgent + (MCLeaksAPIImpl.this.testing ? "-testing" : ""));
            final BufferedReader br = new BufferedReader(new InputStreamReader((conn.getResponseCode() < 400) ? conn.getInputStream() : conn.getErrorStream()));
            final StringBuilder json = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                json.append(output);
            }
            conn.disconnect();
            if (conn.getResponseCode() != 200) {
                MCLeaksError mcLeaksError;
                try {
                    mcLeaksError = MCLeaksAPIImpl.this.gson.fromJson(json.toString(), MCLeaksError.class);
                }
                catch (Exception ex) {
                    throw new RuntimeException("Failed to properly decode error: \"" + json.toString() + "\" with response code \"" + conn.getResponseCode() + "\"", ex);
                }
                throw new RuntimeException("Failed request with response code \"" + conn.getResponseCode() + "\" " + ((mcLeaksError == null) ? "No error message supplied" : ("Error message: " + mcLeaksError.error)));
            }
            try {
                return MCLeaksAPIImpl.this.gson.fromJson(json.toString(), MCLeaksResponse.class).isMcleaks;
            }
            catch (Exception ex2) {
                throw new RuntimeException("Failed to decode response \"" + json.toString() + "\", had OK response code.");
            }
        }
        
        protected abstract String getApiType();
        
        protected abstract String getInputText(final T p0);
    }
}
