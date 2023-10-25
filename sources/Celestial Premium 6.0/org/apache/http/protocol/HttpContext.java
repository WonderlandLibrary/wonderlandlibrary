/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.protocol;

public interface HttpContext {
    public static final String RESERVED_PREFIX = "http.";

    public Object getAttribute(String var1);

    public void setAttribute(String var1, Object var2);

    public Object removeAttribute(String var1);
}

