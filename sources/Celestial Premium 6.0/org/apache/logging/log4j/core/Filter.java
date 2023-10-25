/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.EnglishEnums;

public interface Filter {
    public Result getOnMismatch();

    public Result getOnMatch();

    public Result filter(Logger var1, Level var2, Marker var3, String var4, Object ... var5);

    public Result filter(Logger var1, Level var2, Marker var3, Object var4, Throwable var5);

    public Result filter(Logger var1, Level var2, Marker var3, Message var4, Throwable var5);

    public Result filter(LogEvent var1);

    public static enum Result {
        ACCEPT,
        NEUTRAL,
        DENY;


        public static Result toResult(String name) {
            return Result.toResult(name, null);
        }

        public static Result toResult(String name, Result defaultResult) {
            return EnglishEnums.valueOf(Result.class, name, defaultResult);
        }
    }
}

