/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.cors;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.internal.StringUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

public final class CorsConfig {
    private final Map<CharSequence, Callable<?>> preflightHeaders;
    private final boolean shortCurcuit;
    private final Set<String> allowedRequestHeaders;
    private final boolean allowCredentials;
    private final Set<HttpMethod> allowedRequestMethods;
    private final boolean allowNullOrigin;
    private final boolean anyOrigin;
    private final boolean enabled;
    private final long maxAge;
    private final Set<String> origins;
    private final Set<String> exposeHeaders;

    public Set<HttpMethod> allowedRequestMethods() {
        return Collections.unmodifiableSet(this.allowedRequestMethods);
    }

    public static Builder withOrigins(String ... stringArray) {
        return new Builder(stringArray);
    }

    public boolean isCorsSupportEnabled() {
        return this.enabled;
    }

    public Set<String> origins() {
        return this.origins;
    }

    public Set<String> allowedRequestHeaders() {
        return Collections.unmodifiableSet(this.allowedRequestHeaders);
    }

    public Set<String> exposedHeaders() {
        return Collections.unmodifiableSet(this.exposeHeaders);
    }

    public static Builder withAnyOrigin() {
        return new Builder();
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "[enabled=" + this.enabled + ", origins=" + this.origins + ", anyOrigin=" + this.anyOrigin + ", exposedHeaders=" + this.exposeHeaders + ", isCredentialsAllowed=" + this.allowCredentials + ", maxAge=" + this.maxAge + ", allowedRequestMethods=" + this.allowedRequestMethods + ", allowedRequestHeaders=" + this.allowedRequestHeaders + ", preflightHeaders=" + this.preflightHeaders + ']';
    }

    public boolean isNullOriginAllowed() {
        return this.allowNullOrigin;
    }

    private CorsConfig(Builder builder) {
        this.origins = new LinkedHashSet<String>(builder.origins);
        this.anyOrigin = builder.anyOrigin;
        this.enabled = builder.enabled;
        this.exposeHeaders = builder.exposeHeaders;
        this.allowCredentials = builder.allowCredentials;
        this.maxAge = builder.maxAge;
        this.allowedRequestMethods = builder.requestMethods;
        this.allowedRequestHeaders = builder.requestHeaders;
        this.allowNullOrigin = builder.allowNullOrigin;
        this.preflightHeaders = builder.preflightHeaders;
        this.shortCurcuit = builder.shortCurcuit;
    }

    public long maxAge() {
        return this.maxAge;
    }

    public static Builder withOrigin(String string) {
        if ("*".equals(string)) {
            return new Builder();
        }
        return new Builder(string);
    }

    public boolean isCredentialsAllowed() {
        return this.allowCredentials;
    }

    public boolean isAnyOriginSupported() {
        return this.anyOrigin;
    }

    public HttpHeaders preflightResponseHeaders() {
        if (this.preflightHeaders.isEmpty()) {
            return HttpHeaders.EMPTY_HEADERS;
        }
        DefaultHttpHeaders defaultHttpHeaders = new DefaultHttpHeaders();
        for (Map.Entry<CharSequence, Callable<?>> entry : this.preflightHeaders.entrySet()) {
            Object obj = CorsConfig.getValue(entry.getValue());
            if (obj instanceof Iterable) {
                ((HttpHeaders)defaultHttpHeaders).add(entry.getKey(), (Iterable)obj);
                continue;
            }
            ((HttpHeaders)defaultHttpHeaders).add(entry.getKey(), obj);
        }
        return defaultHttpHeaders;
    }

    public String origin() {
        return this.origins.isEmpty() ? "*" : this.origins.iterator().next();
    }

    private static <T> T getValue(Callable<T> callable) {
        try {
            return callable.call();
        }
        catch (Exception exception) {
            throw new IllegalStateException("Could not generate value for callable [" + callable + ']', exception);
        }
    }

    public boolean isShortCurcuit() {
        return this.shortCurcuit;
    }

    private static final class ConstantValueGenerator
    implements Callable<Object> {
        private final Object value;

        @Override
        public Object call() {
            return this.value;
        }

        private ConstantValueGenerator(Object object) {
            if (object == null) {
                throw new IllegalArgumentException("value must not be null");
            }
            this.value = object;
        }
    }

    public static class Builder {
        private final Map<CharSequence, Callable<?>> preflightHeaders;
        private long maxAge;
        private boolean allowCredentials;
        private boolean allowNullOrigin;
        private final Set<HttpMethod> requestMethods;
        private final Set<String> requestHeaders;
        private boolean noPreflightHeaders;
        private final boolean anyOrigin;
        private final Set<String> origins;
        private final Set<String> exposeHeaders = new HashSet<String>();
        private boolean shortCurcuit;
        private boolean enabled = true;

        public Builder allowedRequestHeaders(String ... stringArray) {
            this.requestHeaders.addAll(Arrays.asList(stringArray));
            return this;
        }

        public Builder disable() {
            this.enabled = false;
            return this;
        }

        public <T> Builder preflightResponseHeader(String string, Callable<T> callable) {
            this.preflightHeaders.put(string, callable);
            return this;
        }

        public <T> Builder preflightResponseHeader(CharSequence charSequence, Iterable<T> iterable) {
            this.preflightHeaders.put(charSequence, new ConstantValueGenerator(iterable));
            return this;
        }

        public Builder noPreflightResponseHeaders() {
            this.noPreflightHeaders = true;
            return this;
        }

        public Builder() {
            this.requestMethods = new HashSet<HttpMethod>();
            this.requestHeaders = new HashSet<String>();
            this.preflightHeaders = new HashMap();
            this.anyOrigin = true;
            this.origins = Collections.emptySet();
        }

        public Builder preflightResponseHeader(CharSequence charSequence, Object ... objectArray) {
            if (objectArray.length == 1) {
                this.preflightHeaders.put(charSequence, new ConstantValueGenerator(objectArray[0]));
            } else {
                this.preflightResponseHeader(charSequence, Arrays.asList(objectArray));
            }
            return this;
        }

        public Builder shortCurcuit() {
            this.shortCurcuit = true;
            return this;
        }

        public Builder allowCredentials() {
            this.allowCredentials = true;
            return this;
        }

        public Builder exposeHeaders(String ... stringArray) {
            this.exposeHeaders.addAll(Arrays.asList(stringArray));
            return this;
        }

        public Builder maxAge(long l) {
            this.maxAge = l;
            return this;
        }

        public Builder allowNullOrigin() {
            this.allowNullOrigin = true;
            return this;
        }

        public Builder allowedRequestMethods(HttpMethod ... httpMethodArray) {
            this.requestMethods.addAll(Arrays.asList(httpMethodArray));
            return this;
        }

        public CorsConfig build() {
            if (this.preflightHeaders.isEmpty() && !this.noPreflightHeaders) {
                this.preflightHeaders.put("Date", new DateValueGenerator());
                this.preflightHeaders.put("Content-Length", new ConstantValueGenerator("0"));
            }
            return new CorsConfig(this);
        }

        public Builder(String ... stringArray) {
            this.requestMethods = new HashSet<HttpMethod>();
            this.requestHeaders = new HashSet<String>();
            this.preflightHeaders = new HashMap();
            this.origins = new LinkedHashSet<String>(Arrays.asList(stringArray));
            this.anyOrigin = false;
        }
    }

    public static final class DateValueGenerator
    implements Callable<Date> {
        @Override
        public Date call() throws Exception {
            return new Date();
        }
    }
}

