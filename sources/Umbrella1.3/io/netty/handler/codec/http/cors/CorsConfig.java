/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.http.cors;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.util.internal.StringUtil;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class CorsConfig {
    private final String origin;
    private final boolean enabled;
    private final Set<String> exposeHeaders;
    private final boolean allowCredentials;
    private final long maxAge;
    private final Set<HttpMethod> allowedRequestMethods;
    private final Set<String> allowedRequestHeaders;
    private final boolean allowNullOrigin;

    private CorsConfig(Builder builder) {
        this.origin = builder.origin;
        this.enabled = builder.enabled;
        this.exposeHeaders = builder.exposeHeaders;
        this.allowCredentials = builder.allowCredentials;
        this.maxAge = builder.maxAge;
        this.allowedRequestMethods = builder.requestMethods;
        this.allowedRequestHeaders = builder.requestHeaders;
        this.allowNullOrigin = builder.allowNullOrigin;
    }

    public boolean isCorsSupportEnabled() {
        return this.enabled;
    }

    public String origin() {
        return this.origin;
    }

    public boolean isNullOriginAllowed() {
        return this.allowNullOrigin;
    }

    public Set<String> exposedHeaders() {
        return Collections.unmodifiableSet(this.exposeHeaders);
    }

    public boolean isCredentialsAllowed() {
        return this.allowCredentials;
    }

    public long maxAge() {
        return this.maxAge;
    }

    public Set<HttpMethod> allowedRequestMethods() {
        return Collections.unmodifiableSet(this.allowedRequestMethods);
    }

    public Set<String> allowedRequestHeaders() {
        return Collections.unmodifiableSet(this.allowedRequestHeaders);
    }

    public String toString() {
        return StringUtil.simpleClassName(this) + "[enabled=" + this.enabled + ", origin=" + this.origin + ", exposedHeaders=" + this.exposeHeaders + ", isCredentialsAllowed=" + this.allowCredentials + ", maxAge=" + this.maxAge + ", allowedRequestMethods=" + this.allowedRequestMethods + ", allowedRequestHeaders=" + this.allowedRequestHeaders + ']';
    }

    public static Builder anyOrigin() {
        return new Builder("*");
    }

    public static Builder withOrigin(String origin) {
        return new Builder(origin);
    }

    public static class Builder {
        private final String origin;
        private boolean allowNullOrigin;
        private boolean enabled = true;
        private boolean allowCredentials;
        private final Set<String> exposeHeaders = new HashSet<String>();
        private long maxAge;
        private final Set<HttpMethod> requestMethods = new HashSet<HttpMethod>();
        private final Set<String> requestHeaders = new HashSet<String>();

        public Builder(String origin) {
            this.origin = origin;
        }

        public Builder allowNullOrigin() {
            this.allowNullOrigin = true;
            return this;
        }

        public Builder disable() {
            this.enabled = false;
            return this;
        }

        public Builder exposeHeaders(String ... headers) {
            this.exposeHeaders.addAll(Arrays.asList(headers));
            return this;
        }

        public Builder allowCredentials() {
            this.allowCredentials = true;
            return this;
        }

        public Builder maxAge(long max) {
            this.maxAge = max;
            return this;
        }

        public Builder allowedRequestMethods(HttpMethod ... methods) {
            this.requestMethods.addAll(Arrays.asList(methods));
            return this;
        }

        public Builder allowedRequestHeaders(String ... headers) {
            this.requestHeaders.addAll(Arrays.asList(headers));
            return this;
        }

        public CorsConfig build() {
            return new CorsConfig(this);
        }
    }
}

