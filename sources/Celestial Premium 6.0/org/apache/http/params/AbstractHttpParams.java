/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.params;

import java.util.Set;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpParamsNames;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Deprecated
public abstract class AbstractHttpParams
implements HttpParams,
HttpParamsNames {
    protected AbstractHttpParams() {
    }

    @Override
    public long getLongParameter(String name, long defaultValue) {
        Object param = this.getParameter(name);
        if (param == null) {
            return defaultValue;
        }
        return (Long)param;
    }

    @Override
    public HttpParams setLongParameter(String name, long value) {
        this.setParameter(name, value);
        return this;
    }

    @Override
    public int getIntParameter(String name, int defaultValue) {
        Object param = this.getParameter(name);
        if (param == null) {
            return defaultValue;
        }
        return (Integer)param;
    }

    @Override
    public HttpParams setIntParameter(String name, int value) {
        this.setParameter(name, value);
        return this;
    }

    @Override
    public double getDoubleParameter(String name, double defaultValue) {
        Object param = this.getParameter(name);
        if (param == null) {
            return defaultValue;
        }
        return (Double)param;
    }

    @Override
    public HttpParams setDoubleParameter(String name, double value) {
        this.setParameter(name, value);
        return this;
    }

    @Override
    public boolean getBooleanParameter(String name, boolean defaultValue) {
        Object param = this.getParameter(name);
        if (param == null) {
            return defaultValue;
        }
        return (Boolean)param;
    }

    @Override
    public HttpParams setBooleanParameter(String name, boolean value) {
        this.setParameter(name, value ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    @Override
    public boolean isParameterTrue(String name) {
        return this.getBooleanParameter(name, false);
    }

    @Override
    public boolean isParameterFalse(String name) {
        return !this.getBooleanParameter(name, false);
    }

    @Override
    public Set<String> getNames() {
        throw new UnsupportedOperationException();
    }
}

