/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.params;

import java.util.HashSet;
import java.util.Set;
import org.apache.http.params.AbstractHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpParamsNames;
import org.apache.http.util.Args;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Deprecated
public final class DefaultedHttpParams
extends AbstractHttpParams {
    private final HttpParams local;
    private final HttpParams defaults;

    public DefaultedHttpParams(HttpParams local, HttpParams defaults) {
        this.local = Args.notNull(local, "Local HTTP parameters");
        this.defaults = defaults;
    }

    @Override
    public HttpParams copy() {
        HttpParams clone = this.local.copy();
        return new DefaultedHttpParams(clone, this.defaults);
    }

    @Override
    public Object getParameter(String name) {
        Object obj = this.local.getParameter(name);
        if (obj == null && this.defaults != null) {
            obj = this.defaults.getParameter(name);
        }
        return obj;
    }

    @Override
    public boolean removeParameter(String name) {
        return this.local.removeParameter(name);
    }

    @Override
    public HttpParams setParameter(String name, Object value) {
        return this.local.setParameter(name, value);
    }

    public HttpParams getDefaults() {
        return this.defaults;
    }

    @Override
    public Set<String> getNames() {
        HashSet<String> combined = new HashSet<String>(this.getNames(this.defaults));
        combined.addAll(this.getNames(this.local));
        return combined;
    }

    public Set<String> getDefaultNames() {
        return new HashSet<String>(this.getNames(this.defaults));
    }

    public Set<String> getLocalNames() {
        return new HashSet<String>(this.getNames(this.local));
    }

    private Set<String> getNames(HttpParams params) {
        if (params instanceof HttpParamsNames) {
            return ((HttpParamsNames)((Object)params)).getNames();
        }
        throw new UnsupportedOperationException("HttpParams instance does not implement HttpParamsNames");
    }
}

