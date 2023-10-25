/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.params;

@Deprecated
public interface HttpParams {
    public Object getParameter(String var1);

    public HttpParams setParameter(String var1, Object var2);

    public HttpParams copy();

    public boolean removeParameter(String var1);

    public long getLongParameter(String var1, long var2);

    public HttpParams setLongParameter(String var1, long var2);

    public int getIntParameter(String var1, int var2);

    public HttpParams setIntParameter(String var1, int var2);

    public double getDoubleParameter(String var1, double var2);

    public HttpParams setDoubleParameter(String var1, double var2);

    public boolean getBooleanParameter(String var1, boolean var2);

    public HttpParams setBooleanParameter(String var1, boolean var2);

    public boolean isParameterTrue(String var1);

    public boolean isParameterFalse(String var1);
}

