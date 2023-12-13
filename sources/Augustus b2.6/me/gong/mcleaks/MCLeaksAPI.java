// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks;

import java.util.concurrent.TimeUnit;
import java.util.UUID;
import java.util.Optional;
import java.util.function.Consumer;

public interface MCLeaksAPI
{
    void checkAccount(final String p0, final Consumer<Boolean> p1, final Consumer<Throwable> p2);
    
    Result checkAccount(final String p0);
    
    Optional<Boolean> getCachedCheck(final String p0);
    
    void checkAccount(final UUID p0, final Consumer<Boolean> p1, final Consumer<Throwable> p2);
    
    Result checkAccount(final UUID p0);
    
    Optional<Boolean> getCachedCheck(final UUID p0);
    
    void shutdown();
    
    default Builder builder() {
        return new Builder();
    }
    
    public static class Result
    {
        private boolean isMCLeaks;
        private Throwable error;
        
        Result(final boolean isMCLeaks) {
            this.isMCLeaks = isMCLeaks;
        }
        
        Result(final Throwable error) {
            this.error = error;
        }
        
        public boolean isMCLeaks() {
            return this.isMCLeaks;
        }
        
        public Throwable getError() {
            return this.error;
        }
        
        public boolean hasError() {
            return this.error != null;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final Result result = (Result)o;
            return this.isMCLeaks == result.isMCLeaks && ((this.error != null) ? this.error.equals(result.error) : (result.error == null));
        }
        
        @Override
        public int hashCode() {
            int result = this.isMCLeaks ? 1 : 0;
            result = 31 * result + ((this.error != null) ? this.error.hashCode() : 0);
            return result;
        }
        
        @Override
        public String toString() {
            return "Result{isMCLeaks=" + this.isMCLeaks + ", error=" + this.error + '}';
        }
    }
    
    public static class Builder
    {
        private int threadCount;
        private long expireAfter;
        private TimeUnit unit;
        private boolean testing;
        private boolean noCache;
        private String userAgent;
        
        public Builder() {
            this.threadCount = 3;
            this.expireAfter = 5L;
            this.unit = TimeUnit.MINUTES;
            this.userAgent = "MCLeaksApiClient";
        }
        
        public Builder threadCount(final int threadCount) {
            this.threadCount = threadCount;
            return this;
        }
        
        public Builder expireAfter(final long expireAfter, final TimeUnit unit) {
            this.expireAfter = expireAfter;
            this.unit = unit;
            return this;
        }
        
        public Builder testing() {
            this.testing = true;
            return this;
        }
        
        public Builder userAgent(final String userAgent) {
            this.userAgent = userAgent;
            return this;
        }
        
        public Builder nocache() {
            this.noCache = true;
            return this;
        }
        
        public MCLeaksAPI build() {
            if (this.noCache) {
                return new MCLeaksAPIImpl(this.threadCount, this.testing, this.userAgent);
            }
            return new MCLeaksAPIImpl(this.threadCount, this.expireAfter, this.unit, this.testing, this.userAgent);
        }
    }
}
