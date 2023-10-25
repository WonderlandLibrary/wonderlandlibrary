package org.apache.logging.log4j;

import org.apache.logging.log4j.message.EntryMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.MessageSupplier;
import org.apache.logging.log4j.util.Supplier;

public interface Logger {
  void catching(Level paramLevel, Throwable paramThrowable);
  
  void catching(Throwable paramThrowable);
  
  void debug(Marker paramMarker, Message paramMessage);
  
  void debug(Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  void debug(Marker paramMarker, MessageSupplier paramMessageSupplier);
  
  void debug(Marker paramMarker, MessageSupplier paramMessageSupplier, Throwable paramThrowable);
  
  void debug(Marker paramMarker, CharSequence paramCharSequence);
  
  void debug(Marker paramMarker, CharSequence paramCharSequence, Throwable paramThrowable);
  
  void debug(Marker paramMarker, Object paramObject);
  
  void debug(Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  void debug(Marker paramMarker, String paramString);
  
  void debug(Marker paramMarker, String paramString, Object... paramVarArgs);
  
  void debug(Marker paramMarker, String paramString, Supplier<?>... paramVarArgs);
  
  void debug(Marker paramMarker, String paramString, Throwable paramThrowable);
  
  void debug(Marker paramMarker, Supplier<?> paramSupplier);
  
  void debug(Marker paramMarker, Supplier<?> paramSupplier, Throwable paramThrowable);
  
  void debug(Message paramMessage);
  
  void debug(Message paramMessage, Throwable paramThrowable);
  
  void debug(MessageSupplier paramMessageSupplier);
  
  void debug(MessageSupplier paramMessageSupplier, Throwable paramThrowable);
  
  void debug(CharSequence paramCharSequence);
  
  void debug(CharSequence paramCharSequence, Throwable paramThrowable);
  
  void debug(Object paramObject);
  
  void debug(Object paramObject, Throwable paramThrowable);
  
  void debug(String paramString);
  
  void debug(String paramString, Object... paramVarArgs);
  
  void debug(String paramString, Supplier<?>... paramVarArgs);
  
  void debug(String paramString, Throwable paramThrowable);
  
  void debug(Supplier<?> paramSupplier);
  
  void debug(Supplier<?> paramSupplier, Throwable paramThrowable);
  
  void debug(Marker paramMarker, String paramString, Object paramObject);
  
  void debug(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2);
  
  void debug(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3);
  
  void debug(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4);
  
  void debug(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5);
  
  void debug(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6);
  
  void debug(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7);
  
  void debug(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8);
  
  void debug(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9);
  
  void debug(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10);
  
  void debug(String paramString, Object paramObject);
  
  void debug(String paramString, Object paramObject1, Object paramObject2);
  
  void debug(String paramString, Object paramObject1, Object paramObject2, Object paramObject3);
  
  void debug(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4);
  
  void debug(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5);
  
  void debug(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6);
  
  void debug(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7);
  
  void debug(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8);
  
  void debug(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9);
  
  void debug(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10);
  
  @Deprecated
  void entry();
  
  @Deprecated
  void entry(Object... paramVarArgs);
  
  void error(Marker paramMarker, Message paramMessage);
  
  void error(Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  void error(Marker paramMarker, MessageSupplier paramMessageSupplier);
  
  void error(Marker paramMarker, MessageSupplier paramMessageSupplier, Throwable paramThrowable);
  
  void error(Marker paramMarker, CharSequence paramCharSequence);
  
  void error(Marker paramMarker, CharSequence paramCharSequence, Throwable paramThrowable);
  
  void error(Marker paramMarker, Object paramObject);
  
  void error(Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  void error(Marker paramMarker, String paramString);
  
  void error(Marker paramMarker, String paramString, Object... paramVarArgs);
  
  void error(Marker paramMarker, String paramString, Supplier<?>... paramVarArgs);
  
  void error(Marker paramMarker, String paramString, Throwable paramThrowable);
  
  void error(Marker paramMarker, Supplier<?> paramSupplier);
  
  void error(Marker paramMarker, Supplier<?> paramSupplier, Throwable paramThrowable);
  
  void error(Message paramMessage);
  
  void error(Message paramMessage, Throwable paramThrowable);
  
  void error(MessageSupplier paramMessageSupplier);
  
  void error(MessageSupplier paramMessageSupplier, Throwable paramThrowable);
  
  void error(CharSequence paramCharSequence);
  
  void error(CharSequence paramCharSequence, Throwable paramThrowable);
  
  void error(Object paramObject);
  
  void error(Object paramObject, Throwable paramThrowable);
  
  void error(String paramString);
  
  void error(String paramString, Object... paramVarArgs);
  
  void error(String paramString, Supplier<?>... paramVarArgs);
  
  void error(String paramString, Throwable paramThrowable);
  
  void error(Supplier<?> paramSupplier);
  
  void error(Supplier<?> paramSupplier, Throwable paramThrowable);
  
  void error(Marker paramMarker, String paramString, Object paramObject);
  
  void error(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2);
  
  void error(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3);
  
  void error(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4);
  
  void error(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5);
  
  void error(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6);
  
  void error(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7);
  
  void error(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8);
  
  void error(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9);
  
  void error(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10);
  
  void error(String paramString, Object paramObject);
  
  void error(String paramString, Object paramObject1, Object paramObject2);
  
  void error(String paramString, Object paramObject1, Object paramObject2, Object paramObject3);
  
  void error(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4);
  
  void error(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5);
  
  void error(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6);
  
  void error(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7);
  
  void error(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8);
  
  void error(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9);
  
  void error(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10);
  
  @Deprecated
  void exit();
  
  @Deprecated
  <R> R exit(R paramR);
  
  void fatal(Marker paramMarker, Message paramMessage);
  
  void fatal(Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  void fatal(Marker paramMarker, MessageSupplier paramMessageSupplier);
  
  void fatal(Marker paramMarker, MessageSupplier paramMessageSupplier, Throwable paramThrowable);
  
  void fatal(Marker paramMarker, CharSequence paramCharSequence);
  
  void fatal(Marker paramMarker, CharSequence paramCharSequence, Throwable paramThrowable);
  
  void fatal(Marker paramMarker, Object paramObject);
  
  void fatal(Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  void fatal(Marker paramMarker, String paramString);
  
  void fatal(Marker paramMarker, String paramString, Object... paramVarArgs);
  
  void fatal(Marker paramMarker, String paramString, Supplier<?>... paramVarArgs);
  
  void fatal(Marker paramMarker, String paramString, Throwable paramThrowable);
  
  void fatal(Marker paramMarker, Supplier<?> paramSupplier);
  
  void fatal(Marker paramMarker, Supplier<?> paramSupplier, Throwable paramThrowable);
  
  void fatal(Message paramMessage);
  
  void fatal(Message paramMessage, Throwable paramThrowable);
  
  void fatal(MessageSupplier paramMessageSupplier);
  
  void fatal(MessageSupplier paramMessageSupplier, Throwable paramThrowable);
  
  void fatal(CharSequence paramCharSequence);
  
  void fatal(CharSequence paramCharSequence, Throwable paramThrowable);
  
  void fatal(Object paramObject);
  
  void fatal(Object paramObject, Throwable paramThrowable);
  
  void fatal(String paramString);
  
  void fatal(String paramString, Object... paramVarArgs);
  
  void fatal(String paramString, Supplier<?>... paramVarArgs);
  
  void fatal(String paramString, Throwable paramThrowable);
  
  void fatal(Supplier<?> paramSupplier);
  
  void fatal(Supplier<?> paramSupplier, Throwable paramThrowable);
  
  void fatal(Marker paramMarker, String paramString, Object paramObject);
  
  void fatal(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2);
  
  void fatal(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3);
  
  void fatal(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4);
  
  void fatal(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5);
  
  void fatal(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6);
  
  void fatal(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7);
  
  void fatal(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8);
  
  void fatal(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9);
  
  void fatal(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10);
  
  void fatal(String paramString, Object paramObject);
  
  void fatal(String paramString, Object paramObject1, Object paramObject2);
  
  void fatal(String paramString, Object paramObject1, Object paramObject2, Object paramObject3);
  
  void fatal(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4);
  
  void fatal(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5);
  
  void fatal(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6);
  
  void fatal(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7);
  
  void fatal(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8);
  
  void fatal(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9);
  
  void fatal(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10);
  
  Level getLevel();
  
  <MF extends org.apache.logging.log4j.message.MessageFactory> MF getMessageFactory();
  
  String getName();
  
  void info(Marker paramMarker, Message paramMessage);
  
  void info(Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  void info(Marker paramMarker, MessageSupplier paramMessageSupplier);
  
  void info(Marker paramMarker, MessageSupplier paramMessageSupplier, Throwable paramThrowable);
  
  void info(Marker paramMarker, CharSequence paramCharSequence);
  
  void info(Marker paramMarker, CharSequence paramCharSequence, Throwable paramThrowable);
  
  void info(Marker paramMarker, Object paramObject);
  
  void info(Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  void info(Marker paramMarker, String paramString);
  
  void info(Marker paramMarker, String paramString, Object... paramVarArgs);
  
  void info(Marker paramMarker, String paramString, Supplier<?>... paramVarArgs);
  
  void info(Marker paramMarker, String paramString, Throwable paramThrowable);
  
  void info(Marker paramMarker, Supplier<?> paramSupplier);
  
  void info(Marker paramMarker, Supplier<?> paramSupplier, Throwable paramThrowable);
  
  void info(Message paramMessage);
  
  void info(Message paramMessage, Throwable paramThrowable);
  
  void info(MessageSupplier paramMessageSupplier);
  
  void info(MessageSupplier paramMessageSupplier, Throwable paramThrowable);
  
  void info(CharSequence paramCharSequence);
  
  void info(CharSequence paramCharSequence, Throwable paramThrowable);
  
  void info(Object paramObject);
  
  void info(Object paramObject, Throwable paramThrowable);
  
  void info(String paramString);
  
  void info(String paramString, Object... paramVarArgs);
  
  void info(String paramString, Supplier<?>... paramVarArgs);
  
  void info(String paramString, Throwable paramThrowable);
  
  void info(Supplier<?> paramSupplier);
  
  void info(Supplier<?> paramSupplier, Throwable paramThrowable);
  
  void info(Marker paramMarker, String paramString, Object paramObject);
  
  void info(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2);
  
  void info(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3);
  
  void info(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4);
  
  void info(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5);
  
  void info(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6);
  
  void info(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7);
  
  void info(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8);
  
  void info(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9);
  
  void info(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10);
  
  void info(String paramString, Object paramObject);
  
  void info(String paramString, Object paramObject1, Object paramObject2);
  
  void info(String paramString, Object paramObject1, Object paramObject2, Object paramObject3);
  
  void info(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4);
  
  void info(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5);
  
  void info(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6);
  
  void info(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7);
  
  void info(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8);
  
  void info(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9);
  
  void info(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10);
  
  boolean isDebugEnabled();
  
  boolean isDebugEnabled(Marker paramMarker);
  
  boolean isEnabled(Level paramLevel);
  
  boolean isEnabled(Level paramLevel, Marker paramMarker);
  
  boolean isErrorEnabled();
  
  boolean isErrorEnabled(Marker paramMarker);
  
  boolean isFatalEnabled();
  
  boolean isFatalEnabled(Marker paramMarker);
  
  boolean isInfoEnabled();
  
  boolean isInfoEnabled(Marker paramMarker);
  
  boolean isTraceEnabled();
  
  boolean isTraceEnabled(Marker paramMarker);
  
  boolean isWarnEnabled();
  
  boolean isWarnEnabled(Marker paramMarker);
  
  void log(Level paramLevel, Marker paramMarker, Message paramMessage);
  
  void log(Level paramLevel, Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  void log(Level paramLevel, Marker paramMarker, MessageSupplier paramMessageSupplier);
  
  void log(Level paramLevel, Marker paramMarker, MessageSupplier paramMessageSupplier, Throwable paramThrowable);
  
  void log(Level paramLevel, Marker paramMarker, CharSequence paramCharSequence);
  
  void log(Level paramLevel, Marker paramMarker, CharSequence paramCharSequence, Throwable paramThrowable);
  
  void log(Level paramLevel, Marker paramMarker, Object paramObject);
  
  void log(Level paramLevel, Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  void log(Level paramLevel, Marker paramMarker, String paramString);
  
  void log(Level paramLevel, Marker paramMarker, String paramString, Object... paramVarArgs);
  
  void log(Level paramLevel, Marker paramMarker, String paramString, Supplier<?>... paramVarArgs);
  
  void log(Level paramLevel, Marker paramMarker, String paramString, Throwable paramThrowable);
  
  void log(Level paramLevel, Marker paramMarker, Supplier<?> paramSupplier);
  
  void log(Level paramLevel, Marker paramMarker, Supplier<?> paramSupplier, Throwable paramThrowable);
  
  void log(Level paramLevel, Message paramMessage);
  
  void log(Level paramLevel, Message paramMessage, Throwable paramThrowable);
  
  void log(Level paramLevel, MessageSupplier paramMessageSupplier);
  
  void log(Level paramLevel, MessageSupplier paramMessageSupplier, Throwable paramThrowable);
  
  void log(Level paramLevel, CharSequence paramCharSequence);
  
  void log(Level paramLevel, CharSequence paramCharSequence, Throwable paramThrowable);
  
  void log(Level paramLevel, Object paramObject);
  
  void log(Level paramLevel, Object paramObject, Throwable paramThrowable);
  
  void log(Level paramLevel, String paramString);
  
  void log(Level paramLevel, String paramString, Object... paramVarArgs);
  
  void log(Level paramLevel, String paramString, Supplier<?>... paramVarArgs);
  
  void log(Level paramLevel, String paramString, Throwable paramThrowable);
  
  void log(Level paramLevel, Supplier<?> paramSupplier);
  
  void log(Level paramLevel, Supplier<?> paramSupplier, Throwable paramThrowable);
  
  void log(Level paramLevel, Marker paramMarker, String paramString, Object paramObject);
  
  void log(Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2);
  
  void log(Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3);
  
  void log(Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4);
  
  void log(Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5);
  
  void log(Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6);
  
  void log(Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7);
  
  void log(Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8);
  
  void log(Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9);
  
  void log(Level paramLevel, Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10);
  
  void log(Level paramLevel, String paramString, Object paramObject);
  
  void log(Level paramLevel, String paramString, Object paramObject1, Object paramObject2);
  
  void log(Level paramLevel, String paramString, Object paramObject1, Object paramObject2, Object paramObject3);
  
  void log(Level paramLevel, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4);
  
  void log(Level paramLevel, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5);
  
  void log(Level paramLevel, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6);
  
  void log(Level paramLevel, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7);
  
  void log(Level paramLevel, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8);
  
  void log(Level paramLevel, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9);
  
  void log(Level paramLevel, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10);
  
  void printf(Level paramLevel, Marker paramMarker, String paramString, Object... paramVarArgs);
  
  void printf(Level paramLevel, String paramString, Object... paramVarArgs);
  
  <T extends Throwable> T throwing(Level paramLevel, T paramT);
  
  <T extends Throwable> T throwing(T paramT);
  
  void trace(Marker paramMarker, Message paramMessage);
  
  void trace(Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  void trace(Marker paramMarker, MessageSupplier paramMessageSupplier);
  
  void trace(Marker paramMarker, MessageSupplier paramMessageSupplier, Throwable paramThrowable);
  
  void trace(Marker paramMarker, CharSequence paramCharSequence);
  
  void trace(Marker paramMarker, CharSequence paramCharSequence, Throwable paramThrowable);
  
  void trace(Marker paramMarker, Object paramObject);
  
  void trace(Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  void trace(Marker paramMarker, String paramString);
  
  void trace(Marker paramMarker, String paramString, Object... paramVarArgs);
  
  void trace(Marker paramMarker, String paramString, Supplier<?>... paramVarArgs);
  
  void trace(Marker paramMarker, String paramString, Throwable paramThrowable);
  
  void trace(Marker paramMarker, Supplier<?> paramSupplier);
  
  void trace(Marker paramMarker, Supplier<?> paramSupplier, Throwable paramThrowable);
  
  void trace(Message paramMessage);
  
  void trace(Message paramMessage, Throwable paramThrowable);
  
  void trace(MessageSupplier paramMessageSupplier);
  
  void trace(MessageSupplier paramMessageSupplier, Throwable paramThrowable);
  
  void trace(CharSequence paramCharSequence);
  
  void trace(CharSequence paramCharSequence, Throwable paramThrowable);
  
  void trace(Object paramObject);
  
  void trace(Object paramObject, Throwable paramThrowable);
  
  void trace(String paramString);
  
  void trace(String paramString, Object... paramVarArgs);
  
  void trace(String paramString, Supplier<?>... paramVarArgs);
  
  void trace(String paramString, Throwable paramThrowable);
  
  void trace(Supplier<?> paramSupplier);
  
  void trace(Supplier<?> paramSupplier, Throwable paramThrowable);
  
  void trace(Marker paramMarker, String paramString, Object paramObject);
  
  void trace(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2);
  
  void trace(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3);
  
  void trace(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4);
  
  void trace(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5);
  
  void trace(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6);
  
  void trace(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7);
  
  void trace(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8);
  
  void trace(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9);
  
  void trace(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10);
  
  void trace(String paramString, Object paramObject);
  
  void trace(String paramString, Object paramObject1, Object paramObject2);
  
  void trace(String paramString, Object paramObject1, Object paramObject2, Object paramObject3);
  
  void trace(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4);
  
  void trace(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5);
  
  void trace(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6);
  
  void trace(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7);
  
  void trace(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8);
  
  void trace(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9);
  
  void trace(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10);
  
  EntryMessage traceEntry();
  
  EntryMessage traceEntry(String paramString, Object... paramVarArgs);
  
  EntryMessage traceEntry(Supplier<?>... paramVarArgs);
  
  EntryMessage traceEntry(String paramString, Supplier<?>... paramVarArgs);
  
  EntryMessage traceEntry(Message paramMessage);
  
  void traceExit();
  
  <R> R traceExit(R paramR);
  
  <R> R traceExit(String paramString, R paramR);
  
  void traceExit(EntryMessage paramEntryMessage);
  
  <R> R traceExit(EntryMessage paramEntryMessage, R paramR);
  
  <R> R traceExit(Message paramMessage, R paramR);
  
  void warn(Marker paramMarker, Message paramMessage);
  
  void warn(Marker paramMarker, Message paramMessage, Throwable paramThrowable);
  
  void warn(Marker paramMarker, MessageSupplier paramMessageSupplier);
  
  void warn(Marker paramMarker, MessageSupplier paramMessageSupplier, Throwable paramThrowable);
  
  void warn(Marker paramMarker, CharSequence paramCharSequence);
  
  void warn(Marker paramMarker, CharSequence paramCharSequence, Throwable paramThrowable);
  
  void warn(Marker paramMarker, Object paramObject);
  
  void warn(Marker paramMarker, Object paramObject, Throwable paramThrowable);
  
  void warn(Marker paramMarker, String paramString);
  
  void warn(Marker paramMarker, String paramString, Object... paramVarArgs);
  
  void warn(Marker paramMarker, String paramString, Supplier<?>... paramVarArgs);
  
  void warn(Marker paramMarker, String paramString, Throwable paramThrowable);
  
  void warn(Marker paramMarker, Supplier<?> paramSupplier);
  
  void warn(Marker paramMarker, Supplier<?> paramSupplier, Throwable paramThrowable);
  
  void warn(Message paramMessage);
  
  void warn(Message paramMessage, Throwable paramThrowable);
  
  void warn(MessageSupplier paramMessageSupplier);
  
  void warn(MessageSupplier paramMessageSupplier, Throwable paramThrowable);
  
  void warn(CharSequence paramCharSequence);
  
  void warn(CharSequence paramCharSequence, Throwable paramThrowable);
  
  void warn(Object paramObject);
  
  void warn(Object paramObject, Throwable paramThrowable);
  
  void warn(String paramString);
  
  void warn(String paramString, Object... paramVarArgs);
  
  void warn(String paramString, Supplier<?>... paramVarArgs);
  
  void warn(String paramString, Throwable paramThrowable);
  
  void warn(Supplier<?> paramSupplier);
  
  void warn(Supplier<?> paramSupplier, Throwable paramThrowable);
  
  void warn(Marker paramMarker, String paramString, Object paramObject);
  
  void warn(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2);
  
  void warn(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3);
  
  void warn(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4);
  
  void warn(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5);
  
  void warn(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6);
  
  void warn(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7);
  
  void warn(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8);
  
  void warn(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9);
  
  void warn(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10);
  
  void warn(String paramString, Object paramObject);
  
  void warn(String paramString, Object paramObject1, Object paramObject2);
  
  void warn(String paramString, Object paramObject1, Object paramObject2, Object paramObject3);
  
  void warn(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4);
  
  void warn(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5);
  
  void warn(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6);
  
  void warn(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7);
  
  void warn(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8);
  
  void warn(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9);
  
  void warn(String paramString, Object paramObject1, Object paramObject2, Object paramObject3, Object paramObject4, Object paramObject5, Object paramObject6, Object paramObject7, Object paramObject8, Object paramObject9, Object paramObject10);
  
  default void logMessage(Level level, Marker marker, String fqcn, StackTraceElement location, Message message, Throwable throwable) {}
  
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
  
  default LogBuilder atLevel(Level level) {
    return LogBuilder.NOOP;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\Logger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */