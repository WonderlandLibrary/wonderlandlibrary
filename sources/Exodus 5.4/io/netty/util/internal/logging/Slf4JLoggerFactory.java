/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.slf4j.LoggerFactory
 *  org.slf4j.helpers.NOPLoggerFactory
 */
package io.netty.util.internal.logging;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLogger;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.NOPLoggerFactory;

public class Slf4JLoggerFactory
extends InternalLoggerFactory {
    @Override
    public InternalLogger newInstance(String string) {
        return new Slf4JLogger(LoggerFactory.getLogger((String)string));
    }

    public Slf4JLoggerFactory() {
    }

    Slf4JLoggerFactory(boolean bl) {
        assert (bl);
        final StringBuffer stringBuffer = new StringBuffer();
        PrintStream printStream = System.err;
        try {
            System.setErr(new PrintStream(new OutputStream(){

                @Override
                public void write(int n) {
                    stringBuffer.append((char)n);
                }
            }, true, "US-ASCII"));
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new Error(unsupportedEncodingException);
        }
        if (LoggerFactory.getILoggerFactory() instanceof NOPLoggerFactory) {
            throw new NoClassDefFoundError(stringBuffer.toString());
        }
        printStream.print(stringBuffer);
        printStream.flush();
        System.setErr(printStream);
    }
}

