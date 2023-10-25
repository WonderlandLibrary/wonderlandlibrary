/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.client.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.utils.Idn;

@Immutable
public class JdkIdn
implements Idn {
    private final Method toUnicode;

    public JdkIdn() throws ClassNotFoundException {
        Class<?> clazz = Class.forName("java.net.IDN");
        try {
            this.toUnicode = clazz.getMethod("toUnicode", String.class);
        }
        catch (SecurityException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        catch (NoSuchMethodException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public String toUnicode(String punycode) {
        try {
            return (String)this.toUnicode.invoke(null, punycode);
        }
        catch (IllegalAccessException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        catch (InvocationTargetException e) {
            Throwable t = e.getCause();
            throw new RuntimeException(t.getMessage(), t);
        }
    }
}

