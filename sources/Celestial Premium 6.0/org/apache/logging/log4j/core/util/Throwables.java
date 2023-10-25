/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.core.util.Closer;

public final class Throwables {
    private Throwables() {
    }

    public static Throwable getRootCause(Throwable throwable) {
        Throwable cause;
        Throwable slowPointer = throwable;
        boolean advanceSlowPointer = false;
        Throwable parent = throwable;
        while ((cause = parent.getCause()) != null) {
            parent = cause;
            if (parent == slowPointer) {
                throw new IllegalArgumentException("loop in causal chain");
            }
            if (advanceSlowPointer) {
                slowPointer = slowPointer.getCause();
            }
            advanceSlowPointer = !advanceSlowPointer;
        }
        return parent;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static List<String> toStringList(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            throwable.printStackTrace(pw);
        }
        catch (RuntimeException runtimeException) {
            // empty catch block
        }
        pw.flush();
        ArrayList<String> lines = new ArrayList<String>();
        LineNumberReader reader = new LineNumberReader(new StringReader(sw.toString()));
        try {
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        }
        catch (IOException ex) {
            if (ex instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            lines.add(ex.toString());
        }
        finally {
            Closer.closeSilently(reader);
        }
        return lines;
    }

    public static void rethrow(Throwable t) {
        Throwables.rethrow0(t);
    }

    private static <T extends Throwable> void rethrow0(Throwable t) throws T {
        throw t;
    }
}

