/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.MessageSupplier;
import org.apache.logging.log4j.util.Supplier;

public interface ExtendedLogger
extends Logger {
    public boolean isEnabled(Level var1, Marker var2, Message var3, Throwable var4);

    public boolean isEnabled(Level var1, Marker var2, CharSequence var3, Throwable var4);

    public boolean isEnabled(Level var1, Marker var2, Object var3, Throwable var4);

    public boolean isEnabled(Level var1, Marker var2, String var3, Throwable var4);

    public boolean isEnabled(Level var1, Marker var2, String var3);

    public boolean isEnabled(Level var1, Marker var2, String var3, Object ... var4);

    public boolean isEnabled(Level var1, Marker var2, String var3, Object var4);

    public boolean isEnabled(Level var1, Marker var2, String var3, Object var4, Object var5);

    public boolean isEnabled(Level var1, Marker var2, String var3, Object var4, Object var5, Object var6);

    public boolean isEnabled(Level var1, Marker var2, String var3, Object var4, Object var5, Object var6, Object var7);

    public boolean isEnabled(Level var1, Marker var2, String var3, Object var4, Object var5, Object var6, Object var7, Object var8);

    public boolean isEnabled(Level var1, Marker var2, String var3, Object var4, Object var5, Object var6, Object var7, Object var8, Object var9);

    public boolean isEnabled(Level var1, Marker var2, String var3, Object var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10);

    public boolean isEnabled(Level var1, Marker var2, String var3, Object var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10, Object var11);

    public boolean isEnabled(Level var1, Marker var2, String var3, Object var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10, Object var11, Object var12);

    public boolean isEnabled(Level var1, Marker var2, String var3, Object var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10, Object var11, Object var12, Object var13);

    public void logIfEnabled(String var1, Level var2, Marker var3, Message var4, Throwable var5);

    public void logIfEnabled(String var1, Level var2, Marker var3, CharSequence var4, Throwable var5);

    public void logIfEnabled(String var1, Level var2, Marker var3, Object var4, Throwable var5);

    public void logIfEnabled(String var1, Level var2, Marker var3, String var4, Throwable var5);

    public void logIfEnabled(String var1, Level var2, Marker var3, String var4);

    public void logIfEnabled(String var1, Level var2, Marker var3, String var4, Object ... var5);

    public void logIfEnabled(String var1, Level var2, Marker var3, String var4, Object var5);

    public void logIfEnabled(String var1, Level var2, Marker var3, String var4, Object var5, Object var6);

    public void logIfEnabled(String var1, Level var2, Marker var3, String var4, Object var5, Object var6, Object var7);

    public void logIfEnabled(String var1, Level var2, Marker var3, String var4, Object var5, Object var6, Object var7, Object var8);

    public void logIfEnabled(String var1, Level var2, Marker var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9);

    public void logIfEnabled(String var1, Level var2, Marker var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10);

    public void logIfEnabled(String var1, Level var2, Marker var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10, Object var11);

    public void logIfEnabled(String var1, Level var2, Marker var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10, Object var11, Object var12);

    public void logIfEnabled(String var1, Level var2, Marker var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10, Object var11, Object var12, Object var13);

    public void logIfEnabled(String var1, Level var2, Marker var3, String var4, Object var5, Object var6, Object var7, Object var8, Object var9, Object var10, Object var11, Object var12, Object var13, Object var14);

    public void logMessage(String var1, Level var2, Marker var3, Message var4, Throwable var5);

    public void logIfEnabled(String var1, Level var2, Marker var3, MessageSupplier var4, Throwable var5);

    public void logIfEnabled(String var1, Level var2, Marker var3, String var4, Supplier<?> ... var5);

    public void logIfEnabled(String var1, Level var2, Marker var3, Supplier<?> var4, Throwable var5);
}

