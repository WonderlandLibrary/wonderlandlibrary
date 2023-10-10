/*
 * Decompiled with CFR 0.152.
 */
package org.newdawn.slick.util;

import java.io.PrintStream;
import java.util.Date;
import org.newdawn.slick.util.LogSystem;

public class DefaultLogSystem
implements LogSystem {
    public static PrintStream out = System.out;

    public void error(String message, Throwable e2) {
        this.error(message);
        this.error(e2);
    }

    public void error(Throwable e2) {
        out.println(new Date() + " ERROR:" + e2.getMessage());
        e2.printStackTrace(out);
    }

    public void error(String message) {
        out.println(new Date() + " ERROR:" + message);
    }

    public void warn(String message) {
        out.println(new Date() + " WARN:" + message);
    }

    public void info(String message) {
        out.println(new Date() + " INFO:" + message);
    }

    public void debug(String message) {
        out.println(new Date() + " DEBUG:" + message);
    }

    public void warn(String message, Throwable e2) {
        this.warn(message);
        e2.printStackTrace(out);
    }
}

