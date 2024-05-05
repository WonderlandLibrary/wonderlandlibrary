/*
 * Decompiled with CFR 0.152.
 */
package me.kiras.aimwhere.libraries.slick.util;

import java.io.PrintStream;
import java.util.Date;
import me.kiras.aimwhere.libraries.slick.util.LogSystem;

public class DefaultLogSystem
implements LogSystem {
    public static PrintStream out = System.out;

    @Override
    public void error(String message, Throwable e) {
        this.error(message);
        this.error(e);
    }

    @Override
    public void error(Throwable e) {
        out.println(new Date() + " ERROR:" + e.getMessage());
        e.printStackTrace(out);
    }

    @Override
    public void error(String message) {
        out.println(new Date() + " ERROR:" + message);
    }

    @Override
    public void warn(String message) {
        out.println(new Date() + " WARN:" + message);
    }

    @Override
    public void info(String message) {
        out.println(new Date() + " INFO:" + message);
    }

    @Override
    public void debug(String message) {
        out.println(new Date() + " DEBUG:" + message);
    }

    @Override
    public void warn(String message, Throwable e) {
        this.warn(message);
        e.printStackTrace(out);
    }
}

