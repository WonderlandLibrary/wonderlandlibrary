// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j;

import org.apache.logging.log4j.message.EntryMessage;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.util.Supplier;
import org.apache.logging.log4j.util.MessageSupplier;
import org.apache.logging.log4j.message.Message;

public interface Logger
{
    void catching(final Level level, final Throwable throwable);
    
    void catching(final Throwable throwable);
    
    void debug(final Marker marker, final Message message);
    
    void debug(final Marker marker, final Message message, final Throwable throwable);
    
    void debug(final Marker marker, final MessageSupplier messageSupplier);
    
    void debug(final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable);
    
    void debug(final Marker marker, final CharSequence message);
    
    void debug(final Marker marker, final CharSequence message, final Throwable throwable);
    
    void debug(final Marker marker, final Object message);
    
    void debug(final Marker marker, final Object message, final Throwable throwable);
    
    void debug(final Marker marker, final String message);
    
    void debug(final Marker marker, final String message, final Object... params);
    
    void debug(final Marker marker, final String message, final Supplier<?>... paramSuppliers);
    
    void debug(final Marker marker, final String message, final Throwable throwable);
    
    void debug(final Marker marker, final Supplier<?> messageSupplier);
    
    void debug(final Marker marker, final Supplier<?> messageSupplier, final Throwable throwable);
    
    void debug(final Message message);
    
    void debug(final Message message, final Throwable throwable);
    
    void debug(final MessageSupplier messageSupplier);
    
    void debug(final MessageSupplier messageSupplier, final Throwable throwable);
    
    void debug(final CharSequence message);
    
    void debug(final CharSequence message, final Throwable throwable);
    
    void debug(final Object message);
    
    void debug(final Object message, final Throwable throwable);
    
    void debug(final String message);
    
    void debug(final String message, final Object... params);
    
    void debug(final String message, final Supplier<?>... paramSuppliers);
    
    void debug(final String message, final Throwable throwable);
    
    void debug(final Supplier<?> messageSupplier);
    
    void debug(final Supplier<?> messageSupplier, final Throwable throwable);
    
    void debug(final Marker marker, final String message, final Object p0);
    
    void debug(final Marker marker, final String message, final Object p0, final Object p1);
    
    void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2);
    
    void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3);
    
    void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4);
    
    void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5);
    
    void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6);
    
    void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7);
    
    void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8);
    
    void debug(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9);
    
    void debug(final String message, final Object p0);
    
    void debug(final String message, final Object p0, final Object p1);
    
    void debug(final String message, final Object p0, final Object p1, final Object p2);
    
    void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3);
    
    void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4);
    
    void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5);
    
    void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6);
    
    void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7);
    
    void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8);
    
    void debug(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9);
    
    @Deprecated
    void entry();
    
    @Deprecated
    void entry(final Object... params);
    
    void error(final Marker marker, final Message message);
    
    void error(final Marker marker, final Message message, final Throwable throwable);
    
    void error(final Marker marker, final MessageSupplier messageSupplier);
    
    void error(final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable);
    
    void error(final Marker marker, final CharSequence message);
    
    void error(final Marker marker, final CharSequence message, final Throwable throwable);
    
    void error(final Marker marker, final Object message);
    
    void error(final Marker marker, final Object message, final Throwable throwable);
    
    void error(final Marker marker, final String message);
    
    void error(final Marker marker, final String message, final Object... params);
    
    void error(final Marker marker, final String message, final Supplier<?>... paramSuppliers);
    
    void error(final Marker marker, final String message, final Throwable throwable);
    
    void error(final Marker marker, final Supplier<?> messageSupplier);
    
    void error(final Marker marker, final Supplier<?> messageSupplier, final Throwable throwable);
    
    void error(final Message message);
    
    void error(final Message message, final Throwable throwable);
    
    void error(final MessageSupplier messageSupplier);
    
    void error(final MessageSupplier messageSupplier, final Throwable throwable);
    
    void error(final CharSequence message);
    
    void error(final CharSequence message, final Throwable throwable);
    
    void error(final Object message);
    
    void error(final Object message, final Throwable throwable);
    
    void error(final String message);
    
    void error(final String message, final Object... params);
    
    void error(final String message, final Supplier<?>... paramSuppliers);
    
    void error(final String message, final Throwable throwable);
    
    void error(final Supplier<?> messageSupplier);
    
    void error(final Supplier<?> messageSupplier, final Throwable throwable);
    
    void error(final Marker marker, final String message, final Object p0);
    
    void error(final Marker marker, final String message, final Object p0, final Object p1);
    
    void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2);
    
    void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3);
    
    void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4);
    
    void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5);
    
    void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6);
    
    void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7);
    
    void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8);
    
    void error(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9);
    
    void error(final String message, final Object p0);
    
    void error(final String message, final Object p0, final Object p1);
    
    void error(final String message, final Object p0, final Object p1, final Object p2);
    
    void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3);
    
    void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4);
    
    void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5);
    
    void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6);
    
    void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7);
    
    void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8);
    
    void error(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9);
    
    @Deprecated
    void exit();
    
    @Deprecated
     <R> R exit(final R result);
    
    void fatal(final Marker marker, final Message message);
    
    void fatal(final Marker marker, final Message message, final Throwable throwable);
    
    void fatal(final Marker marker, final MessageSupplier messageSupplier);
    
    void fatal(final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable);
    
    void fatal(final Marker marker, final CharSequence message);
    
    void fatal(final Marker marker, final CharSequence message, final Throwable throwable);
    
    void fatal(final Marker marker, final Object message);
    
    void fatal(final Marker marker, final Object message, final Throwable throwable);
    
    void fatal(final Marker marker, final String message);
    
    void fatal(final Marker marker, final String message, final Object... params);
    
    void fatal(final Marker marker, final String message, final Supplier<?>... paramSuppliers);
    
    void fatal(final Marker marker, final String message, final Throwable throwable);
    
    void fatal(final Marker marker, final Supplier<?> messageSupplier);
    
    void fatal(final Marker marker, final Supplier<?> messageSupplier, final Throwable throwable);
    
    void fatal(final Message message);
    
    void fatal(final Message message, final Throwable throwable);
    
    void fatal(final MessageSupplier messageSupplier);
    
    void fatal(final MessageSupplier messageSupplier, final Throwable throwable);
    
    void fatal(final CharSequence message);
    
    void fatal(final CharSequence message, final Throwable throwable);
    
    void fatal(final Object message);
    
    void fatal(final Object message, final Throwable throwable);
    
    void fatal(final String message);
    
    void fatal(final String message, final Object... params);
    
    void fatal(final String message, final Supplier<?>... paramSuppliers);
    
    void fatal(final String message, final Throwable throwable);
    
    void fatal(final Supplier<?> messageSupplier);
    
    void fatal(final Supplier<?> messageSupplier, final Throwable throwable);
    
    void fatal(final Marker marker, final String message, final Object p0);
    
    void fatal(final Marker marker, final String message, final Object p0, final Object p1);
    
    void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2);
    
    void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3);
    
    void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4);
    
    void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5);
    
    void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6);
    
    void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7);
    
    void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8);
    
    void fatal(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9);
    
    void fatal(final String message, final Object p0);
    
    void fatal(final String message, final Object p0, final Object p1);
    
    void fatal(final String message, final Object p0, final Object p1, final Object p2);
    
    void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3);
    
    void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4);
    
    void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5);
    
    void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6);
    
    void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7);
    
    void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8);
    
    void fatal(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9);
    
    Level getLevel();
    
     <MF extends MessageFactory> MF getMessageFactory();
    
    String getName();
    
    void info(final Marker marker, final Message message);
    
    void info(final Marker marker, final Message message, final Throwable throwable);
    
    void info(final Marker marker, final MessageSupplier messageSupplier);
    
    void info(final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable);
    
    void info(final Marker marker, final CharSequence message);
    
    void info(final Marker marker, final CharSequence message, final Throwable throwable);
    
    void info(final Marker marker, final Object message);
    
    void info(final Marker marker, final Object message, final Throwable throwable);
    
    void info(final Marker marker, final String message);
    
    void info(final Marker marker, final String message, final Object... params);
    
    void info(final Marker marker, final String message, final Supplier<?>... paramSuppliers);
    
    void info(final Marker marker, final String message, final Throwable throwable);
    
    void info(final Marker marker, final Supplier<?> messageSupplier);
    
    void info(final Marker marker, final Supplier<?> messageSupplier, final Throwable throwable);
    
    void info(final Message message);
    
    void info(final Message message, final Throwable throwable);
    
    void info(final MessageSupplier messageSupplier);
    
    void info(final MessageSupplier messageSupplier, final Throwable throwable);
    
    void info(final CharSequence message);
    
    void info(final CharSequence message, final Throwable throwable);
    
    void info(final Object message);
    
    void info(final Object message, final Throwable throwable);
    
    void info(final String message);
    
    void info(final String message, final Object... params);
    
    void info(final String message, final Supplier<?>... paramSuppliers);
    
    void info(final String message, final Throwable throwable);
    
    void info(final Supplier<?> messageSupplier);
    
    void info(final Supplier<?> messageSupplier, final Throwable throwable);
    
    void info(final Marker marker, final String message, final Object p0);
    
    void info(final Marker marker, final String message, final Object p0, final Object p1);
    
    void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2);
    
    void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3);
    
    void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4);
    
    void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5);
    
    void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6);
    
    void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7);
    
    void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8);
    
    void info(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9);
    
    void info(final String message, final Object p0);
    
    void info(final String message, final Object p0, final Object p1);
    
    void info(final String message, final Object p0, final Object p1, final Object p2);
    
    void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3);
    
    void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4);
    
    void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5);
    
    void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6);
    
    void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7);
    
    void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8);
    
    void info(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9);
    
    boolean isDebugEnabled();
    
    boolean isDebugEnabled(final Marker marker);
    
    boolean isEnabled(final Level level);
    
    boolean isEnabled(final Level level, final Marker marker);
    
    boolean isErrorEnabled();
    
    boolean isErrorEnabled(final Marker marker);
    
    boolean isFatalEnabled();
    
    boolean isFatalEnabled(final Marker marker);
    
    boolean isInfoEnabled();
    
    boolean isInfoEnabled(final Marker marker);
    
    boolean isTraceEnabled();
    
    boolean isTraceEnabled(final Marker marker);
    
    boolean isWarnEnabled();
    
    boolean isWarnEnabled(final Marker marker);
    
    void log(final Level level, final Marker marker, final Message message);
    
    void log(final Level level, final Marker marker, final Message message, final Throwable throwable);
    
    void log(final Level level, final Marker marker, final MessageSupplier messageSupplier);
    
    void log(final Level level, final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable);
    
    void log(final Level level, final Marker marker, final CharSequence message);
    
    void log(final Level level, final Marker marker, final CharSequence message, final Throwable throwable);
    
    void log(final Level level, final Marker marker, final Object message);
    
    void log(final Level level, final Marker marker, final Object message, final Throwable throwable);
    
    void log(final Level level, final Marker marker, final String message);
    
    void log(final Level level, final Marker marker, final String message, final Object... params);
    
    void log(final Level level, final Marker marker, final String message, final Supplier<?>... paramSuppliers);
    
    void log(final Level level, final Marker marker, final String message, final Throwable throwable);
    
    void log(final Level level, final Marker marker, final Supplier<?> messageSupplier);
    
    void log(final Level level, final Marker marker, final Supplier<?> messageSupplier, final Throwable throwable);
    
    void log(final Level level, final Message message);
    
    void log(final Level level, final Message message, final Throwable throwable);
    
    void log(final Level level, final MessageSupplier messageSupplier);
    
    void log(final Level level, final MessageSupplier messageSupplier, final Throwable throwable);
    
    void log(final Level level, final CharSequence message);
    
    void log(final Level level, final CharSequence message, final Throwable throwable);
    
    void log(final Level level, final Object message);
    
    void log(final Level level, final Object message, final Throwable throwable);
    
    void log(final Level level, final String message);
    
    void log(final Level level, final String message, final Object... params);
    
    void log(final Level level, final String message, final Supplier<?>... paramSuppliers);
    
    void log(final Level level, final String message, final Throwable throwable);
    
    void log(final Level level, final Supplier<?> messageSupplier);
    
    void log(final Level level, final Supplier<?> messageSupplier, final Throwable throwable);
    
    void log(final Level level, final Marker marker, final String message, final Object p0);
    
    void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1);
    
    void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2);
    
    void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3);
    
    void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4);
    
    void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5);
    
    void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6);
    
    void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7);
    
    void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8);
    
    void log(final Level level, final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9);
    
    void log(final Level level, final String message, final Object p0);
    
    void log(final Level level, final String message, final Object p0, final Object p1);
    
    void log(final Level level, final String message, final Object p0, final Object p1, final Object p2);
    
    void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3);
    
    void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4);
    
    void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5);
    
    void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6);
    
    void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7);
    
    void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8);
    
    void log(final Level level, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9);
    
    void printf(final Level level, final Marker marker, final String format, final Object... params);
    
    void printf(final Level level, final String format, final Object... params);
    
     <T extends Throwable> T throwing(final Level level, final T throwable);
    
     <T extends Throwable> T throwing(final T throwable);
    
    void trace(final Marker marker, final Message message);
    
    void trace(final Marker marker, final Message message, final Throwable throwable);
    
    void trace(final Marker marker, final MessageSupplier messageSupplier);
    
    void trace(final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable);
    
    void trace(final Marker marker, final CharSequence message);
    
    void trace(final Marker marker, final CharSequence message, final Throwable throwable);
    
    void trace(final Marker marker, final Object message);
    
    void trace(final Marker marker, final Object message, final Throwable throwable);
    
    void trace(final Marker marker, final String message);
    
    void trace(final Marker marker, final String message, final Object... params);
    
    void trace(final Marker marker, final String message, final Supplier<?>... paramSuppliers);
    
    void trace(final Marker marker, final String message, final Throwable throwable);
    
    void trace(final Marker marker, final Supplier<?> messageSupplier);
    
    void trace(final Marker marker, final Supplier<?> messageSupplier, final Throwable throwable);
    
    void trace(final Message message);
    
    void trace(final Message message, final Throwable throwable);
    
    void trace(final MessageSupplier messageSupplier);
    
    void trace(final MessageSupplier messageSupplier, final Throwable throwable);
    
    void trace(final CharSequence message);
    
    void trace(final CharSequence message, final Throwable throwable);
    
    void trace(final Object message);
    
    void trace(final Object message, final Throwable throwable);
    
    void trace(final String message);
    
    void trace(final String message, final Object... params);
    
    void trace(final String message, final Supplier<?>... paramSuppliers);
    
    void trace(final String message, final Throwable throwable);
    
    void trace(final Supplier<?> messageSupplier);
    
    void trace(final Supplier<?> messageSupplier, final Throwable throwable);
    
    void trace(final Marker marker, final String message, final Object p0);
    
    void trace(final Marker marker, final String message, final Object p0, final Object p1);
    
    void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2);
    
    void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3);
    
    void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4);
    
    void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5);
    
    void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6);
    
    void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7);
    
    void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8);
    
    void trace(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9);
    
    void trace(final String message, final Object p0);
    
    void trace(final String message, final Object p0, final Object p1);
    
    void trace(final String message, final Object p0, final Object p1, final Object p2);
    
    void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3);
    
    void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4);
    
    void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5);
    
    void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6);
    
    void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7);
    
    void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8);
    
    void trace(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9);
    
    EntryMessage traceEntry();
    
    EntryMessage traceEntry(final String format, final Object... params);
    
    EntryMessage traceEntry(final Supplier<?>... paramSuppliers);
    
    EntryMessage traceEntry(final String format, final Supplier<?>... paramSuppliers);
    
    EntryMessage traceEntry(final Message message);
    
    void traceExit();
    
     <R> R traceExit(final R result);
    
     <R> R traceExit(final String format, final R result);
    
    void traceExit(final EntryMessage message);
    
     <R> R traceExit(final EntryMessage message, final R result);
    
     <R> R traceExit(final Message message, final R result);
    
    void warn(final Marker marker, final Message message);
    
    void warn(final Marker marker, final Message message, final Throwable throwable);
    
    void warn(final Marker marker, final MessageSupplier messageSupplier);
    
    void warn(final Marker marker, final MessageSupplier messageSupplier, final Throwable throwable);
    
    void warn(final Marker marker, final CharSequence message);
    
    void warn(final Marker marker, final CharSequence message, final Throwable throwable);
    
    void warn(final Marker marker, final Object message);
    
    void warn(final Marker marker, final Object message, final Throwable throwable);
    
    void warn(final Marker marker, final String message);
    
    void warn(final Marker marker, final String message, final Object... params);
    
    void warn(final Marker marker, final String message, final Supplier<?>... paramSuppliers);
    
    void warn(final Marker marker, final String message, final Throwable throwable);
    
    void warn(final Marker marker, final Supplier<?> messageSupplier);
    
    void warn(final Marker marker, final Supplier<?> messageSupplier, final Throwable throwable);
    
    void warn(final Message message);
    
    void warn(final Message message, final Throwable throwable);
    
    void warn(final MessageSupplier messageSupplier);
    
    void warn(final MessageSupplier messageSupplier, final Throwable throwable);
    
    void warn(final CharSequence message);
    
    void warn(final CharSequence message, final Throwable throwable);
    
    void warn(final Object message);
    
    void warn(final Object message, final Throwable throwable);
    
    void warn(final String message);
    
    void warn(final String message, final Object... params);
    
    void warn(final String message, final Supplier<?>... paramSuppliers);
    
    void warn(final String message, final Throwable throwable);
    
    void warn(final Supplier<?> messageSupplier);
    
    void warn(final Supplier<?> messageSupplier, final Throwable throwable);
    
    void warn(final Marker marker, final String message, final Object p0);
    
    void warn(final Marker marker, final String message, final Object p0, final Object p1);
    
    void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2);
    
    void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3);
    
    void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4);
    
    void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5);
    
    void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6);
    
    void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7);
    
    void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8);
    
    void warn(final Marker marker, final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9);
    
    void warn(final String message, final Object p0);
    
    void warn(final String message, final Object p0, final Object p1);
    
    void warn(final String message, final Object p0, final Object p1, final Object p2);
    
    void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3);
    
    void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4);
    
    void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5);
    
    void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6);
    
    void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7);
    
    void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8);
    
    void warn(final String message, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9);
    
    default void logMessage(final Level level, final Marker marker, final String fqcn, final StackTraceElement location, final Message message, final Throwable throwable) {
    }
    
    default LogBuilder atTrace() {
        return LogBuilder.NOOP;
    }
    
    default LogBuilder atDebug() {
        return LogBuilder.NOOP;
    }
    
    default LogBuilder atInfo() {
        return LogBuilder.NOOP;
    }
    
    default LogBuilder atWarn() {
        return LogBuilder.NOOP;
    }
    
    default LogBuilder atError() {
        return LogBuilder.NOOP;
    }
    
    default LogBuilder atFatal() {
        return LogBuilder.NOOP;
    }
    
    default LogBuilder always() {
        return LogBuilder.NOOP;
    }
    
    default LogBuilder atLevel(final Level level) {
        return LogBuilder.NOOP;
    }
}
