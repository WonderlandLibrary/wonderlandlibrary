package org.apache.logging.log4j.spi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogBuilder;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.internal.DefaultLogBuilder;
import org.apache.logging.log4j.message.DefaultFlowMessageFactory;
import org.apache.logging.log4j.message.EntryMessage;
import org.apache.logging.log4j.message.FlowMessageFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.message.MessageFactory2;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.message.ParameterizedMessageFactory;
import org.apache.logging.log4j.message.ReusableMessageFactory;
import org.apache.logging.log4j.message.SimpleMessage;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Constants;
import org.apache.logging.log4j.util.LambdaUtil;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.util.MessageSupplier;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.StackLocatorUtil;
import org.apache.logging.log4j.util.Strings;
import org.apache.logging.log4j.util.Supplier;

public abstract class AbstractLogger implements ExtendedLogger, LocationAwareLogger, Serializable {
  public static final Marker FLOW_MARKER = MarkerManager.getMarker("FLOW");
  
  public static final Marker ENTRY_MARKER = MarkerManager.getMarker("ENTER").setParents(new Marker[] { FLOW_MARKER });
  
  public static final Marker EXIT_MARKER = MarkerManager.getMarker("EXIT").setParents(new Marker[] { FLOW_MARKER });
  
  public static final Marker EXCEPTION_MARKER = MarkerManager.getMarker("EXCEPTION");
  
  public static final Marker THROWING_MARKER = MarkerManager.getMarker("THROWING").setParents(new Marker[] { EXCEPTION_MARKER });
  
  public static final Marker CATCHING_MARKER = MarkerManager.getMarker("CATCHING").setParents(new Marker[] { EXCEPTION_MARKER });
  
  public static final Class<? extends MessageFactory> DEFAULT_MESSAGE_FACTORY_CLASS = createClassForProperty("log4j2.messageFactory", ReusableMessageFactory.class, ParameterizedMessageFactory.class);
  
  public static final Class<? extends FlowMessageFactory> DEFAULT_FLOW_MESSAGE_FACTORY_CLASS = createFlowClassForProperty("log4j2.flowMessageFactory", DefaultFlowMessageFactory.class);
  
  private static final long serialVersionUID = 2L;
  
  private static final String FQCN = AbstractLogger.class.getName();
  
  private static final String THROWING = "Throwing";
  
  private static final String CATCHING = "Catching";
  
  protected final String name;
  
  private final MessageFactory2 messageFactory;
  
  private final FlowMessageFactory flowMessageFactory;
  
  private static final ThreadLocal<int[]> recursionDepthHolder = (ThreadLocal)new ThreadLocal<>();
  
  protected final transient ThreadLocal<DefaultLogBuilder> logBuilder;
  
  public AbstractLogger() {
    this.name = getClass().getName();
    this.messageFactory = createDefaultMessageFactory();
    this.flowMessageFactory = createDefaultFlowMessageFactory();
    this.logBuilder = new LocalLogBuilder(this);
  }
  
  public AbstractLogger(String name) {
    this(name, (MessageFactory)createDefaultMessageFactory());
  }
  
  public AbstractLogger(String name, MessageFactory messageFactory) {
    this.name = name;
    this.messageFactory = (messageFactory == null) ? createDefaultMessageFactory() : narrow(messageFactory);
    this.flowMessageFactory = createDefaultFlowMessageFactory();
    this.logBuilder = new LocalLogBuilder(this);
  }
  
  public static void checkMessageFactory(ExtendedLogger logger, MessageFactory messageFactory) {
    String name = logger.getName();
    MessageFactory loggerMessageFactory = logger.getMessageFactory();
    if (messageFactory != null && !loggerMessageFactory.equals(messageFactory)) {
      StatusLogger.getLogger().warn("The Logger {} was created with the message factory {} and is now requested with the message factory {}, which may create log events with unexpected formatting.", name, loggerMessageFactory, messageFactory);
    } else if (messageFactory == null && !loggerMessageFactory.getClass().equals(DEFAULT_MESSAGE_FACTORY_CLASS)) {
      StatusLogger.getLogger()
        .warn("The Logger {} was created with the message factory {} and is now requested with a null message factory (defaults to {}), which may create log events with unexpected formatting.", name, loggerMessageFactory, DEFAULT_MESSAGE_FACTORY_CLASS
          
          .getName());
    } 
  }
  
  public void catching(Level level, Throwable throwable) {
    catching(FQCN, level, throwable);
  }
  
  protected void catching(String fqcn, Level level, Throwable throwable) {
    if (isEnabled(level, CATCHING_MARKER, (Object)null, (Throwable)null))
      logMessageSafely(fqcn, level, CATCHING_MARKER, catchingMsg(throwable), throwable); 
  }
  
  public void catching(Throwable throwable) {
    if (isEnabled(Level.ERROR, CATCHING_MARKER, (Object)null, (Throwable)null))
      logMessageSafely(FQCN, Level.ERROR, CATCHING_MARKER, catchingMsg(throwable), throwable); 
  }
  
  protected Message catchingMsg(Throwable throwable) {
    return this.messageFactory.newMessage("Catching");
  }
  
  private static Class<? extends MessageFactory> createClassForProperty(String property, Class<ReusableMessageFactory> reusableParameterizedMessageFactoryClass, Class<ParameterizedMessageFactory> parameterizedMessageFactoryClass) {
    try {
      String fallback = Constants.ENABLE_THREADLOCALS ? reusableParameterizedMessageFactoryClass.getName() : parameterizedMessageFactoryClass.getName();
      String clsName = PropertiesUtil.getProperties().getStringProperty(property, fallback);
      return LoaderUtil.loadClass(clsName).asSubclass(MessageFactory.class);
    } catch (Throwable throwable) {
      return (Class)parameterizedMessageFactoryClass;
    } 
  }
  
  private static Class<? extends FlowMessageFactory> createFlowClassForProperty(String property, Class<DefaultFlowMessageFactory> defaultFlowMessageFactoryClass) {
    try {
      String clsName = PropertiesUtil.getProperties().getStringProperty(property, defaultFlowMessageFactoryClass.getName());
      return LoaderUtil.loadClass(clsName).asSubclass(FlowMessageFactory.class);
    } catch (Throwable throwable) {
      return (Class)defaultFlowMessageFactoryClass;
    } 
  }
  
  private static MessageFactory2 createDefaultMessageFactory() {
    try {
      MessageFactory result = DEFAULT_MESSAGE_FACTORY_CLASS.newInstance();
      return narrow(result);
    } catch (InstantiationException|IllegalAccessException e) {
      throw new IllegalStateException(e);
    } 
  }
  
  private static MessageFactory2 narrow(MessageFactory result) {
    if (result instanceof MessageFactory2)
      return (MessageFactory2)result; 
    return new MessageFactory2Adapter(result);
  }
  
  private static FlowMessageFactory createDefaultFlowMessageFactory() {
    try {
      return DEFAULT_FLOW_MESSAGE_FACTORY_CLASS.newInstance();
    } catch (InstantiationException|IllegalAccessException e) {
      throw new IllegalStateException(e);
    } 
  }
  
  public void debug(Marker marker, CharSequence message) {
    logIfEnabled(FQCN, Level.DEBUG, marker, message, (Throwable)null);
  }
  
  public void debug(Marker marker, CharSequence message, Throwable throwable) {
    logIfEnabled(FQCN, Level.DEBUG, marker, message, throwable);
  }
  
  public void debug(Marker marker, Message message) {
    logIfEnabled(FQCN, Level.DEBUG, marker, message, (message != null) ? message.getThrowable() : null);
  }
  
  public void debug(Marker marker, Message message, Throwable throwable) {
    logIfEnabled(FQCN, Level.DEBUG, marker, message, throwable);
  }
  
  public void debug(Marker marker, Object message) {
    logIfEnabled(FQCN, Level.DEBUG, marker, message, (Throwable)null);
  }
  
  public void debug(Marker marker, Object message, Throwable throwable) {
    logIfEnabled(FQCN, Level.DEBUG, marker, message, throwable);
  }
  
  public void debug(Marker marker, String message) {
    logIfEnabled(FQCN, Level.DEBUG, marker, message, (Throwable)null);
  }
  
  public void debug(Marker marker, String message, Object... params) {
    logIfEnabled(FQCN, Level.DEBUG, marker, message, params);
  }
  
  public void debug(Marker marker, String message, Throwable throwable) {
    logIfEnabled(FQCN, Level.DEBUG, marker, message, throwable);
  }
  
  public void debug(Message message) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, (message != null) ? message.getThrowable() : null);
  }
  
  public void debug(Message message, Throwable throwable) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, throwable);
  }
  
  public void debug(CharSequence message) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, (Throwable)null);
  }
  
  public void debug(CharSequence message, Throwable throwable) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, throwable);
  }
  
  public void debug(Object message) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, (Throwable)null);
  }
  
  public void debug(Object message, Throwable throwable) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, throwable);
  }
  
  public void debug(String message) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, (Throwable)null);
  }
  
  public void debug(String message, Object... params) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, params);
  }
  
  public void debug(String message, Throwable throwable) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, throwable);
  }
  
  public void debug(Supplier<?> messageSupplier) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, messageSupplier, (Throwable)null);
  }
  
  public void debug(Supplier<?> messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, messageSupplier, throwable);
  }
  
  public void debug(Marker marker, Supplier<?> messageSupplier) {
    logIfEnabled(FQCN, Level.DEBUG, marker, messageSupplier, (Throwable)null);
  }
  
  public void debug(Marker marker, String message, Supplier<?>... paramSuppliers) {
    logIfEnabled(FQCN, Level.DEBUG, marker, message, paramSuppliers);
  }
  
  public void debug(Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.DEBUG, marker, messageSupplier, throwable);
  }
  
  public void debug(String message, Supplier<?>... paramSuppliers) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, paramSuppliers);
  }
  
  public void debug(Marker marker, MessageSupplier messageSupplier) {
    logIfEnabled(FQCN, Level.DEBUG, marker, messageSupplier, (Throwable)null);
  }
  
  public void debug(Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.DEBUG, marker, messageSupplier, throwable);
  }
  
  public void debug(MessageSupplier messageSupplier) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, messageSupplier, (Throwable)null);
  }
  
  public void debug(MessageSupplier messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, messageSupplier, throwable);
  }
  
  public void debug(Marker marker, String message, Object p0) {
    logIfEnabled(FQCN, Level.DEBUG, marker, message, p0);
  }
  
  public void debug(Marker marker, String message, Object p0, Object p1) {
    logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1);
  }
  
  public void debug(Marker marker, String message, Object p0, Object p1, Object p2) {
    logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2);
  }
  
  public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
    logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3);
  }
  
  public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4);
  }
  
  public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4, p5);
  }
  
  public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4, p5, p6);
  }
  
  public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }
  
  public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }
  
  public void debug(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    logIfEnabled(FQCN, Level.DEBUG, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }
  
  public void debug(String message, Object p0) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0);
  }
  
  public void debug(String message, Object p0, Object p1) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1);
  }
  
  public void debug(String message, Object p0, Object p1, Object p2) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2);
  }
  
  public void debug(String message, Object p0, Object p1, Object p2, Object p3) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3);
  }
  
  public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3, p4);
  }
  
  public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3, p4, p5);
  }
  
  public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
  }
  
  public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }
  
  public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }
  
  public void debug(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    logIfEnabled(FQCN, Level.DEBUG, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }
  
  protected EntryMessage enter(String fqcn, String format, Supplier<?>... paramSuppliers) {
    EntryMessage entryMsg = null;
    if (isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, (Throwable)null))
      logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)(entryMsg = entryMsg(format, paramSuppliers)), (Throwable)null); 
    return entryMsg;
  }
  
  @Deprecated
  protected EntryMessage enter(String fqcn, String format, MessageSupplier... paramSuppliers) {
    EntryMessage entryMsg = null;
    if (isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, (Throwable)null))
      logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)(entryMsg = entryMsg(format, paramSuppliers)), (Throwable)null); 
    return entryMsg;
  }
  
  protected EntryMessage enter(String fqcn, String format, Object... params) {
    EntryMessage entryMsg = null;
    if (isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, (Throwable)null))
      logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)(entryMsg = entryMsg(format, params)), (Throwable)null); 
    return entryMsg;
  }
  
  @Deprecated
  protected EntryMessage enter(String fqcn, MessageSupplier messageSupplier) {
    EntryMessage message = null;
    if (isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, (Throwable)null))
      logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)(message = this.flowMessageFactory.newEntryMessage(messageSupplier
            .get())), (Throwable)null); 
    return message;
  }
  
  protected EntryMessage enter(String fqcn, Message message) {
    EntryMessage flowMessage = null;
    if (isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, (Throwable)null))
      logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)(flowMessage = this.flowMessageFactory.newEntryMessage(message)), (Throwable)null); 
    return flowMessage;
  }
  
  @Deprecated
  public void entry() {
    entry(FQCN, (Object[])null);
  }
  
  public void entry(Object... params) {
    entry(FQCN, params);
  }
  
  protected void entry(String fqcn, Object... params) {
    if (isEnabled(Level.TRACE, ENTRY_MARKER, (Object)null, (Throwable)null))
      if (params == null) {
        logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)entryMsg((String)null, (Supplier<?>[])null), (Throwable)null);
      } else {
        logMessageSafely(fqcn, Level.TRACE, ENTRY_MARKER, (Message)entryMsg((String)null, params), (Throwable)null);
      }  
  }
  
  protected EntryMessage entryMsg(String format, Object... params) {
    int count = (params == null) ? 0 : params.length;
    if (count == 0) {
      if (Strings.isEmpty(format))
        return this.flowMessageFactory.newEntryMessage(null); 
      return this.flowMessageFactory.newEntryMessage((Message)new SimpleMessage(format));
    } 
    if (format != null)
      return this.flowMessageFactory.newEntryMessage((Message)new ParameterizedMessage(format, params)); 
    StringBuilder sb = new StringBuilder();
    sb.append("params(");
    for (int i = 0; i < count; i++) {
      if (i > 0)
        sb.append(", "); 
      Object parm = params[i];
      sb.append((parm instanceof Message) ? ((Message)parm).getFormattedMessage() : String.valueOf(parm));
    } 
    sb.append(')');
    return this.flowMessageFactory.newEntryMessage((Message)new SimpleMessage(sb));
  }
  
  protected EntryMessage entryMsg(String format, MessageSupplier... paramSuppliers) {
    int count = (paramSuppliers == null) ? 0 : paramSuppliers.length;
    Object[] params = new Object[count];
    for (int i = 0; i < count; i++) {
      params[i] = paramSuppliers[i].get();
      params[i] = (params[i] != null) ? ((Message)params[i]).getFormattedMessage() : null;
    } 
    return entryMsg(format, params);
  }
  
  protected EntryMessage entryMsg(String format, Supplier<?>... paramSuppliers) {
    int count = (paramSuppliers == null) ? 0 : paramSuppliers.length;
    Object[] params = new Object[count];
    for (int i = 0; i < count; i++) {
      params[i] = paramSuppliers[i].get();
      if (params[i] instanceof Message)
        params[i] = ((Message)params[i]).getFormattedMessage(); 
    } 
    return entryMsg(format, params);
  }
  
  public void error(Marker marker, Message message) {
    logIfEnabled(FQCN, Level.ERROR, marker, message, (message != null) ? message.getThrowable() : null);
  }
  
  public void error(Marker marker, Message message, Throwable throwable) {
    logIfEnabled(FQCN, Level.ERROR, marker, message, throwable);
  }
  
  public void error(Marker marker, CharSequence message) {
    logIfEnabled(FQCN, Level.ERROR, marker, message, (Throwable)null);
  }
  
  public void error(Marker marker, CharSequence message, Throwable throwable) {
    logIfEnabled(FQCN, Level.ERROR, marker, message, throwable);
  }
  
  public void error(Marker marker, Object message) {
    logIfEnabled(FQCN, Level.ERROR, marker, message, (Throwable)null);
  }
  
  public void error(Marker marker, Object message, Throwable throwable) {
    logIfEnabled(FQCN, Level.ERROR, marker, message, throwable);
  }
  
  public void error(Marker marker, String message) {
    logIfEnabled(FQCN, Level.ERROR, marker, message, (Throwable)null);
  }
  
  public void error(Marker marker, String message, Object... params) {
    logIfEnabled(FQCN, Level.ERROR, marker, message, params);
  }
  
  public void error(Marker marker, String message, Throwable throwable) {
    logIfEnabled(FQCN, Level.ERROR, marker, message, throwable);
  }
  
  public void error(Message message) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, (message != null) ? message.getThrowable() : null);
  }
  
  public void error(Message message, Throwable throwable) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, throwable);
  }
  
  public void error(CharSequence message) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, (Throwable)null);
  }
  
  public void error(CharSequence message, Throwable throwable) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, throwable);
  }
  
  public void error(Object message) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, (Throwable)null);
  }
  
  public void error(Object message, Throwable throwable) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, throwable);
  }
  
  public void error(String message) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, (Throwable)null);
  }
  
  public void error(String message, Object... params) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, params);
  }
  
  public void error(String message, Throwable throwable) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, throwable);
  }
  
  public void error(Supplier<?> messageSupplier) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, messageSupplier, (Throwable)null);
  }
  
  public void error(Supplier<?> messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, messageSupplier, throwable);
  }
  
  public void error(Marker marker, Supplier<?> messageSupplier) {
    logIfEnabled(FQCN, Level.ERROR, marker, messageSupplier, (Throwable)null);
  }
  
  public void error(Marker marker, String message, Supplier<?>... paramSuppliers) {
    logIfEnabled(FQCN, Level.ERROR, marker, message, paramSuppliers);
  }
  
  public void error(Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.ERROR, marker, messageSupplier, throwable);
  }
  
  public void error(String message, Supplier<?>... paramSuppliers) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, paramSuppliers);
  }
  
  public void error(Marker marker, MessageSupplier messageSupplier) {
    logIfEnabled(FQCN, Level.ERROR, marker, messageSupplier, (Throwable)null);
  }
  
  public void error(Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.ERROR, marker, messageSupplier, throwable);
  }
  
  public void error(MessageSupplier messageSupplier) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, messageSupplier, (Throwable)null);
  }
  
  public void error(MessageSupplier messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, messageSupplier, throwable);
  }
  
  public void error(Marker marker, String message, Object p0) {
    logIfEnabled(FQCN, Level.ERROR, marker, message, p0);
  }
  
  public void error(Marker marker, String message, Object p0, Object p1) {
    logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1);
  }
  
  public void error(Marker marker, String message, Object p0, Object p1, Object p2) {
    logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2);
  }
  
  public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
    logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3);
  }
  
  public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4);
  }
  
  public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4, p5);
  }
  
  public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4, p5, p6);
  }
  
  public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }
  
  public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }
  
  public void error(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    logIfEnabled(FQCN, Level.ERROR, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }
  
  public void error(String message, Object p0) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0);
  }
  
  public void error(String message, Object p0, Object p1) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1);
  }
  
  public void error(String message, Object p0, Object p1, Object p2) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2);
  }
  
  public void error(String message, Object p0, Object p1, Object p2, Object p3) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3);
  }
  
  public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3, p4);
  }
  
  public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3, p4, p5);
  }
  
  public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
  }
  
  public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }
  
  public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }
  
  public void error(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    logIfEnabled(FQCN, Level.ERROR, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }
  
  @Deprecated
  public void exit() {
    exit(FQCN, (Object)null);
  }
  
  @Deprecated
  public <R> R exit(R result) {
    return exit(FQCN, result);
  }
  
  protected <R> R exit(String fqcn, R result) {
    if (isEnabled(Level.TRACE, EXIT_MARKER, (CharSequence)null, (Throwable)null))
      logMessageSafely(fqcn, Level.TRACE, EXIT_MARKER, exitMsg((String)null, result), (Throwable)null); 
    return result;
  }
  
  protected <R> R exit(String fqcn, String format, R result) {
    if (isEnabled(Level.TRACE, EXIT_MARKER, (CharSequence)null, (Throwable)null))
      logMessageSafely(fqcn, Level.TRACE, EXIT_MARKER, exitMsg(format, result), (Throwable)null); 
    return result;
  }
  
  protected Message exitMsg(String format, Object result) {
    if (result == null) {
      if (format == null)
        return this.messageFactory.newMessage("Exit"); 
      return this.messageFactory.newMessage("Exit: " + format);
    } 
    if (format == null)
      return this.messageFactory.newMessage("Exit with(" + result + ')'); 
    return this.messageFactory.newMessage("Exit: " + format, result);
  }
  
  public void fatal(Marker marker, Message message) {
    logIfEnabled(FQCN, Level.FATAL, marker, message, (message != null) ? message.getThrowable() : null);
  }
  
  public void fatal(Marker marker, Message message, Throwable throwable) {
    logIfEnabled(FQCN, Level.FATAL, marker, message, throwable);
  }
  
  public void fatal(Marker marker, CharSequence message) {
    logIfEnabled(FQCN, Level.FATAL, marker, message, (Throwable)null);
  }
  
  public void fatal(Marker marker, CharSequence message, Throwable throwable) {
    logIfEnabled(FQCN, Level.FATAL, marker, message, throwable);
  }
  
  public void fatal(Marker marker, Object message) {
    logIfEnabled(FQCN, Level.FATAL, marker, message, (Throwable)null);
  }
  
  public void fatal(Marker marker, Object message, Throwable throwable) {
    logIfEnabled(FQCN, Level.FATAL, marker, message, throwable);
  }
  
  public void fatal(Marker marker, String message) {
    logIfEnabled(FQCN, Level.FATAL, marker, message, (Throwable)null);
  }
  
  public void fatal(Marker marker, String message, Object... params) {
    logIfEnabled(FQCN, Level.FATAL, marker, message, params);
  }
  
  public void fatal(Marker marker, String message, Throwable throwable) {
    logIfEnabled(FQCN, Level.FATAL, marker, message, throwable);
  }
  
  public void fatal(Message message) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, (message != null) ? message.getThrowable() : null);
  }
  
  public void fatal(Message message, Throwable throwable) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, throwable);
  }
  
  public void fatal(CharSequence message) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, (Throwable)null);
  }
  
  public void fatal(CharSequence message, Throwable throwable) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, throwable);
  }
  
  public void fatal(Object message) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, (Throwable)null);
  }
  
  public void fatal(Object message, Throwable throwable) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, throwable);
  }
  
  public void fatal(String message) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, (Throwable)null);
  }
  
  public void fatal(String message, Object... params) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, params);
  }
  
  public void fatal(String message, Throwable throwable) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, throwable);
  }
  
  public void fatal(Supplier<?> messageSupplier) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, messageSupplier, (Throwable)null);
  }
  
  public void fatal(Supplier<?> messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, messageSupplier, throwable);
  }
  
  public void fatal(Marker marker, Supplier<?> messageSupplier) {
    logIfEnabled(FQCN, Level.FATAL, marker, messageSupplier, (Throwable)null);
  }
  
  public void fatal(Marker marker, String message, Supplier<?>... paramSuppliers) {
    logIfEnabled(FQCN, Level.FATAL, marker, message, paramSuppliers);
  }
  
  public void fatal(Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.FATAL, marker, messageSupplier, throwable);
  }
  
  public void fatal(String message, Supplier<?>... paramSuppliers) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, paramSuppliers);
  }
  
  public void fatal(Marker marker, MessageSupplier messageSupplier) {
    logIfEnabled(FQCN, Level.FATAL, marker, messageSupplier, (Throwable)null);
  }
  
  public void fatal(Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.FATAL, marker, messageSupplier, throwable);
  }
  
  public void fatal(MessageSupplier messageSupplier) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, messageSupplier, (Throwable)null);
  }
  
  public void fatal(MessageSupplier messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, messageSupplier, throwable);
  }
  
  public void fatal(Marker marker, String message, Object p0) {
    logIfEnabled(FQCN, Level.FATAL, marker, message, p0);
  }
  
  public void fatal(Marker marker, String message, Object p0, Object p1) {
    logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1);
  }
  
  public void fatal(Marker marker, String message, Object p0, Object p1, Object p2) {
    logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2);
  }
  
  public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
    logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3);
  }
  
  public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4);
  }
  
  public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4, p5);
  }
  
  public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4, p5, p6);
  }
  
  public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }
  
  public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }
  
  public void fatal(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    logIfEnabled(FQCN, Level.FATAL, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }
  
  public void fatal(String message, Object p0) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0);
  }
  
  public void fatal(String message, Object p0, Object p1) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1);
  }
  
  public void fatal(String message, Object p0, Object p1, Object p2) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2);
  }
  
  public void fatal(String message, Object p0, Object p1, Object p2, Object p3) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3);
  }
  
  public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3, p4);
  }
  
  public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3, p4, p5);
  }
  
  public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
  }
  
  public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }
  
  public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }
  
  public void fatal(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    logIfEnabled(FQCN, Level.FATAL, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }
  
  public <MF extends MessageFactory> MF getMessageFactory() {
    return (MF)this.messageFactory;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void info(Marker marker, Message message) {
    logIfEnabled(FQCN, Level.INFO, marker, message, (message != null) ? message.getThrowable() : null);
  }
  
  public void info(Marker marker, Message message, Throwable throwable) {
    logIfEnabled(FQCN, Level.INFO, marker, message, throwable);
  }
  
  public void info(Marker marker, CharSequence message) {
    logIfEnabled(FQCN, Level.INFO, marker, message, (Throwable)null);
  }
  
  public void info(Marker marker, CharSequence message, Throwable throwable) {
    logIfEnabled(FQCN, Level.INFO, marker, message, throwable);
  }
  
  public void info(Marker marker, Object message) {
    logIfEnabled(FQCN, Level.INFO, marker, message, (Throwable)null);
  }
  
  public void info(Marker marker, Object message, Throwable throwable) {
    logIfEnabled(FQCN, Level.INFO, marker, message, throwable);
  }
  
  public void info(Marker marker, String message) {
    logIfEnabled(FQCN, Level.INFO, marker, message, (Throwable)null);
  }
  
  public void info(Marker marker, String message, Object... params) {
    logIfEnabled(FQCN, Level.INFO, marker, message, params);
  }
  
  public void info(Marker marker, String message, Throwable throwable) {
    logIfEnabled(FQCN, Level.INFO, marker, message, throwable);
  }
  
  public void info(Message message) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, message, (message != null) ? message.getThrowable() : null);
  }
  
  public void info(Message message, Throwable throwable) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, message, throwable);
  }
  
  public void info(CharSequence message) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, message, (Throwable)null);
  }
  
  public void info(CharSequence message, Throwable throwable) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, message, throwable);
  }
  
  public void info(Object message) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, message, (Throwable)null);
  }
  
  public void info(Object message, Throwable throwable) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, message, throwable);
  }
  
  public void info(String message) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, message, (Throwable)null);
  }
  
  public void info(String message, Object... params) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, message, params);
  }
  
  public void info(String message, Throwable throwable) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, message, throwable);
  }
  
  public void info(Supplier<?> messageSupplier) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, messageSupplier, (Throwable)null);
  }
  
  public void info(Supplier<?> messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, messageSupplier, throwable);
  }
  
  public void info(Marker marker, Supplier<?> messageSupplier) {
    logIfEnabled(FQCN, Level.INFO, marker, messageSupplier, (Throwable)null);
  }
  
  public void info(Marker marker, String message, Supplier<?>... paramSuppliers) {
    logIfEnabled(FQCN, Level.INFO, marker, message, paramSuppliers);
  }
  
  public void info(Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.INFO, marker, messageSupplier, throwable);
  }
  
  public void info(String message, Supplier<?>... paramSuppliers) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, message, paramSuppliers);
  }
  
  public void info(Marker marker, MessageSupplier messageSupplier) {
    logIfEnabled(FQCN, Level.INFO, marker, messageSupplier, (Throwable)null);
  }
  
  public void info(Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.INFO, marker, messageSupplier, throwable);
  }
  
  public void info(MessageSupplier messageSupplier) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, messageSupplier, (Throwable)null);
  }
  
  public void info(MessageSupplier messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, messageSupplier, throwable);
  }
  
  public void info(Marker marker, String message, Object p0) {
    logIfEnabled(FQCN, Level.INFO, marker, message, p0);
  }
  
  public void info(Marker marker, String message, Object p0, Object p1) {
    logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1);
  }
  
  public void info(Marker marker, String message, Object p0, Object p1, Object p2) {
    logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2);
  }
  
  public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
    logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3);
  }
  
  public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4);
  }
  
  public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4, p5);
  }
  
  public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4, p5, p6);
  }
  
  public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }
  
  public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }
  
  public void info(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    logIfEnabled(FQCN, Level.INFO, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }
  
  public void info(String message, Object p0) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0);
  }
  
  public void info(String message, Object p0, Object p1) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1);
  }
  
  public void info(String message, Object p0, Object p1, Object p2) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2);
  }
  
  public void info(String message, Object p0, Object p1, Object p2, Object p3) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3);
  }
  
  public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3, p4);
  }
  
  public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3, p4, p5);
  }
  
  public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
  }
  
  public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }
  
  public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }
  
  public void info(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    logIfEnabled(FQCN, Level.INFO, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }
  
  public boolean isDebugEnabled() {
    return isEnabled(Level.DEBUG, (Marker)null, (String)null);
  }
  
  public boolean isDebugEnabled(Marker marker) {
    return isEnabled(Level.DEBUG, marker, (Object)null, (Throwable)null);
  }
  
  public boolean isEnabled(Level level) {
    return isEnabled(level, (Marker)null, (Object)null, (Throwable)null);
  }
  
  public boolean isEnabled(Level level, Marker marker) {
    return isEnabled(level, marker, (Object)null, (Throwable)null);
  }
  
  public boolean isErrorEnabled() {
    return isEnabled(Level.ERROR, (Marker)null, (Object)null, (Throwable)null);
  }
  
  public boolean isErrorEnabled(Marker marker) {
    return isEnabled(Level.ERROR, marker, (Object)null, (Throwable)null);
  }
  
  public boolean isFatalEnabled() {
    return isEnabled(Level.FATAL, (Marker)null, (Object)null, (Throwable)null);
  }
  
  public boolean isFatalEnabled(Marker marker) {
    return isEnabled(Level.FATAL, marker, (Object)null, (Throwable)null);
  }
  
  public boolean isInfoEnabled() {
    return isEnabled(Level.INFO, (Marker)null, (Object)null, (Throwable)null);
  }
  
  public boolean isInfoEnabled(Marker marker) {
    return isEnabled(Level.INFO, marker, (Object)null, (Throwable)null);
  }
  
  public boolean isTraceEnabled() {
    return isEnabled(Level.TRACE, (Marker)null, (Object)null, (Throwable)null);
  }
  
  public boolean isTraceEnabled(Marker marker) {
    return isEnabled(Level.TRACE, marker, (Object)null, (Throwable)null);
  }
  
  public boolean isWarnEnabled() {
    return isEnabled(Level.WARN, (Marker)null, (Object)null, (Throwable)null);
  }
  
  public boolean isWarnEnabled(Marker marker) {
    return isEnabled(Level.WARN, marker, (Object)null, (Throwable)null);
  }
  
  public void log(Level level, Marker marker, Message message) {
    logIfEnabled(FQCN, level, marker, message, (message != null) ? message.getThrowable() : null);
  }
  
  public void log(Level level, Marker marker, Message message, Throwable throwable) {
    logIfEnabled(FQCN, level, marker, message, throwable);
  }
  
  public void log(Level level, Marker marker, CharSequence message) {
    logIfEnabled(FQCN, level, marker, message, (Throwable)null);
  }
  
  public void log(Level level, Marker marker, CharSequence message, Throwable throwable) {
    if (isEnabled(level, marker, message, throwable))
      logMessage(FQCN, level, marker, message, throwable); 
  }
  
  public void log(Level level, Marker marker, Object message) {
    logIfEnabled(FQCN, level, marker, message, (Throwable)null);
  }
  
  public void log(Level level, Marker marker, Object message, Throwable throwable) {
    if (isEnabled(level, marker, message, throwable))
      logMessage(FQCN, level, marker, message, throwable); 
  }
  
  public void log(Level level, Marker marker, String message) {
    logIfEnabled(FQCN, level, marker, message, (Throwable)null);
  }
  
  public void log(Level level, Marker marker, String message, Object... params) {
    logIfEnabled(FQCN, level, marker, message, params);
  }
  
  public void log(Level level, Marker marker, String message, Throwable throwable) {
    logIfEnabled(FQCN, level, marker, message, throwable);
  }
  
  public void log(Level level, Message message) {
    logIfEnabled(FQCN, level, (Marker)null, message, (message != null) ? message.getThrowable() : null);
  }
  
  public void log(Level level, Message message, Throwable throwable) {
    logIfEnabled(FQCN, level, (Marker)null, message, throwable);
  }
  
  public void log(Level level, CharSequence message) {
    logIfEnabled(FQCN, level, (Marker)null, message, (Throwable)null);
  }
  
  public void log(Level level, CharSequence message, Throwable throwable) {
    logIfEnabled(FQCN, level, (Marker)null, message, throwable);
  }
  
  public void log(Level level, Object message) {
    logIfEnabled(FQCN, level, (Marker)null, message, (Throwable)null);
  }
  
  public void log(Level level, Object message, Throwable throwable) {
    logIfEnabled(FQCN, level, (Marker)null, message, throwable);
  }
  
  public void log(Level level, String message) {
    logIfEnabled(FQCN, level, (Marker)null, message, (Throwable)null);
  }
  
  public void log(Level level, String message, Object... params) {
    logIfEnabled(FQCN, level, (Marker)null, message, params);
  }
  
  public void log(Level level, String message, Throwable throwable) {
    logIfEnabled(FQCN, level, (Marker)null, message, throwable);
  }
  
  public void log(Level level, Supplier<?> messageSupplier) {
    logIfEnabled(FQCN, level, (Marker)null, messageSupplier, (Throwable)null);
  }
  
  public void log(Level level, Supplier<?> messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, level, (Marker)null, messageSupplier, throwable);
  }
  
  public void log(Level level, Marker marker, Supplier<?> messageSupplier) {
    logIfEnabled(FQCN, level, marker, messageSupplier, (Throwable)null);
  }
  
  public void log(Level level, Marker marker, String message, Supplier<?>... paramSuppliers) {
    logIfEnabled(FQCN, level, marker, message, paramSuppliers);
  }
  
  public void log(Level level, Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, level, marker, messageSupplier, throwable);
  }
  
  public void log(Level level, String message, Supplier<?>... paramSuppliers) {
    logIfEnabled(FQCN, level, (Marker)null, message, paramSuppliers);
  }
  
  public void log(Level level, Marker marker, MessageSupplier messageSupplier) {
    logIfEnabled(FQCN, level, marker, messageSupplier, (Throwable)null);
  }
  
  public void log(Level level, Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, level, marker, messageSupplier, throwable);
  }
  
  public void log(Level level, MessageSupplier messageSupplier) {
    logIfEnabled(FQCN, level, (Marker)null, messageSupplier, (Throwable)null);
  }
  
  public void log(Level level, MessageSupplier messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, level, (Marker)null, messageSupplier, throwable);
  }
  
  public void log(Level level, Marker marker, String message, Object p0) {
    logIfEnabled(FQCN, level, marker, message, p0);
  }
  
  public void log(Level level, Marker marker, String message, Object p0, Object p1) {
    logIfEnabled(FQCN, level, marker, message, p0, p1);
  }
  
  public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
    logIfEnabled(FQCN, level, marker, message, p0, p1, p2);
  }
  
  public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
    logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3);
  }
  
  public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3, p4);
  }
  
  public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3, p4, p5);
  }
  
  public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3, p4, p5, p6);
  }
  
  public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }
  
  public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }
  
  public void log(Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    logIfEnabled(FQCN, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }
  
  public void log(Level level, String message, Object p0) {
    logIfEnabled(FQCN, level, (Marker)null, message, p0);
  }
  
  public void log(Level level, String message, Object p0, Object p1) {
    logIfEnabled(FQCN, level, (Marker)null, message, p0, p1);
  }
  
  public void log(Level level, String message, Object p0, Object p1, Object p2) {
    logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2);
  }
  
  public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3) {
    logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3);
  }
  
  public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3, p4);
  }
  
  public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3, p4, p5);
  }
  
  public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
  }
  
  public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }
  
  public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }
  
  public void log(Level level, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    logIfEnabled(FQCN, level, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }
  
  public void logIfEnabled(String fqcn, Level level, Marker marker, Message message, Throwable throwable) {
    if (isEnabled(level, marker, message, throwable))
      logMessageSafely(fqcn, level, marker, message, throwable); 
  }
  
  public void logIfEnabled(String fqcn, Level level, Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
    if (isEnabled(level, marker, messageSupplier, throwable))
      logMessage(fqcn, level, marker, messageSupplier, throwable); 
  }
  
  public void logIfEnabled(String fqcn, Level level, Marker marker, Object message, Throwable throwable) {
    if (isEnabled(level, marker, message, throwable))
      logMessage(fqcn, level, marker, message, throwable); 
  }
  
  public void logIfEnabled(String fqcn, Level level, Marker marker, CharSequence message, Throwable throwable) {
    if (isEnabled(level, marker, message, throwable))
      logMessage(fqcn, level, marker, message, throwable); 
  }
  
  public void logIfEnabled(String fqcn, Level level, Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
    if (isEnabled(level, marker, messageSupplier, throwable))
      logMessage(fqcn, level, marker, messageSupplier, throwable); 
  }
  
  public void logIfEnabled(String fqcn, Level level, Marker marker, String message) {
    if (isEnabled(level, marker, message))
      logMessage(fqcn, level, marker, message); 
  }
  
  public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Supplier<?>... paramSuppliers) {
    if (isEnabled(level, marker, message))
      logMessage(fqcn, level, marker, message, paramSuppliers); 
  }
  
  public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object... params) {
    if (isEnabled(level, marker, message, params))
      logMessage(fqcn, level, marker, message, params); 
  }
  
  public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0) {
    if (isEnabled(level, marker, message, p0))
      logMessage(fqcn, level, marker, message, p0); 
  }
  
  public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1) {
    if (isEnabled(level, marker, message, p0, p1))
      logMessage(fqcn, level, marker, message, p0, p1); 
  }
  
  public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
    if (isEnabled(level, marker, message, p0, p1, p2))
      logMessage(fqcn, level, marker, message, p0, p1, p2); 
  }
  
  public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
    if (isEnabled(level, marker, message, p0, p1, p2, p3))
      logMessage(fqcn, level, marker, message, p0, p1, p2, p3); 
  }
  
  public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    if (isEnabled(level, marker, message, p0, p1, p2, p3, p4))
      logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4); 
  }
  
  public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    if (isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5))
      logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4, p5); 
  }
  
  public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    if (isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6))
      logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4, p5, p6); 
  }
  
  public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    if (isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7))
      logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7); 
  }
  
  public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    if (isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8))
      logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8); 
  }
  
  public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    if (isEnabled(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9))
      logMessage(fqcn, level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9); 
  }
  
  public void logIfEnabled(String fqcn, Level level, Marker marker, String message, Throwable throwable) {
    if (isEnabled(level, marker, message, throwable))
      logMessage(fqcn, level, marker, message, throwable); 
  }
  
  protected void logMessage(String fqcn, Level level, Marker marker, CharSequence message, Throwable throwable) {
    logMessageSafely(fqcn, level, marker, this.messageFactory.newMessage(message), throwable);
  }
  
  protected void logMessage(String fqcn, Level level, Marker marker, Object message, Throwable throwable) {
    logMessageSafely(fqcn, level, marker, this.messageFactory.newMessage(message), throwable);
  }
  
  protected void logMessage(String fqcn, Level level, Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
    Message message = LambdaUtil.get(messageSupplier);
    Throwable effectiveThrowable = (throwable == null && message != null) ? message.getThrowable() : throwable;
    logMessageSafely(fqcn, level, marker, message, effectiveThrowable);
  }
  
  protected void logMessage(String fqcn, Level level, Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
    Message message = LambdaUtil.getMessage(messageSupplier, (MessageFactory)this.messageFactory);
    Throwable effectiveThrowable = (throwable == null && message != null) ? message.getThrowable() : throwable;
    logMessageSafely(fqcn, level, marker, message, effectiveThrowable);
  }
  
  protected void logMessage(String fqcn, Level level, Marker marker, String message, Throwable throwable) {
    logMessageSafely(fqcn, level, marker, this.messageFactory.newMessage(message), throwable);
  }
  
  protected void logMessage(String fqcn, Level level, Marker marker, String message) {
    Message msg = this.messageFactory.newMessage(message);
    logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
  }
  
  protected void logMessage(String fqcn, Level level, Marker marker, String message, Object... params) {
    Message msg = this.messageFactory.newMessage(message, params);
    logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
  }
  
  protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0) {
    Message msg = this.messageFactory.newMessage(message, p0);
    logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
  }
  
  protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1) {
    Message msg = this.messageFactory.newMessage(message, p0, p1);
    logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
  }
  
  protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2) {
    Message msg = this.messageFactory.newMessage(message, p0, p1, p2);
    logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
  }
  
  protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
    Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3);
    logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
  }
  
  protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4);
    logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
  }
  
  protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5);
    logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
  }
  
  protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6);
    logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
  }
  
  protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7);
    logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
  }
  
  protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
  }
  
  protected void logMessage(String fqcn, Level level, Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    Message msg = this.messageFactory.newMessage(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
  }
  
  protected void logMessage(String fqcn, Level level, Marker marker, String message, Supplier<?>... paramSuppliers) {
    Message msg = this.messageFactory.newMessage(message, LambdaUtil.getAll((Supplier[])paramSuppliers));
    logMessageSafely(fqcn, level, marker, msg, msg.getThrowable());
  }
  
  public void logMessage(Level level, Marker marker, String fqcn, StackTraceElement location, Message message, Throwable throwable) {
    try {
      incrementRecursionDepth();
      log(level, marker, fqcn, location, message, throwable);
    } catch (Throwable ex) {
      handleLogMessageException(ex, fqcn, message);
    } finally {
      decrementRecursionDepth();
      ReusableMessageFactory.release(message);
    } 
  }
  
  protected void log(Level level, Marker marker, String fqcn, StackTraceElement location, Message message, Throwable throwable) {
    logMessage(fqcn, level, marker, message, throwable);
  }
  
  public void printf(Level level, Marker marker, String format, Object... params) {
    if (isEnabled(level, marker, format, params)) {
      StringFormattedMessage stringFormattedMessage = new StringFormattedMessage(format, params);
      logMessageSafely(FQCN, level, marker, (Message)stringFormattedMessage, stringFormattedMessage.getThrowable());
    } 
  }
  
  public void printf(Level level, String format, Object... params) {
    if (isEnabled(level, (Marker)null, format, params)) {
      StringFormattedMessage stringFormattedMessage = new StringFormattedMessage(format, params);
      logMessageSafely(FQCN, level, (Marker)null, (Message)stringFormattedMessage, stringFormattedMessage.getThrowable());
    } 
  }
  
  @PerformanceSensitive
  private void logMessageSafely(String fqcn, Level level, Marker marker, Message message, Throwable throwable) {
    try {
      logMessageTrackRecursion(fqcn, level, marker, message, throwable);
    } finally {
      ReusableMessageFactory.release(message);
    } 
  }
  
  @PerformanceSensitive
  private void logMessageTrackRecursion(String fqcn, Level level, Marker marker, Message message, Throwable throwable) {
    try {
      incrementRecursionDepth();
      tryLogMessage(fqcn, getLocation(fqcn), level, marker, message, throwable);
    } finally {
      decrementRecursionDepth();
    } 
  }
  
  private static int[] getRecursionDepthHolder() {
    int[] result = recursionDepthHolder.get();
    if (result == null) {
      result = new int[1];
      recursionDepthHolder.set(result);
    } 
    return result;
  }
  
  private static void incrementRecursionDepth() {
    getRecursionDepthHolder()[0] = getRecursionDepthHolder()[0] + 1;
  }
  
  private static void decrementRecursionDepth() {
    int newDepth = getRecursionDepthHolder()[0] = getRecursionDepthHolder()[0] - 1;
    if (newDepth < 0)
      throw new IllegalStateException("Recursion depth became negative: " + newDepth); 
  }
  
  public static int getRecursionDepth() {
    return getRecursionDepthHolder()[0];
  }
  
  @PerformanceSensitive
  private void tryLogMessage(String fqcn, StackTraceElement location, Level level, Marker marker, Message message, Throwable throwable) {
    try {
      log(level, marker, fqcn, location, message, throwable);
    } catch (Throwable t) {
      handleLogMessageException(t, fqcn, message);
    } 
  }
  
  @PerformanceSensitive
  private StackTraceElement getLocation(String fqcn) {
    return requiresLocation() ? StackLocatorUtil.calcLocation(fqcn) : null;
  }
  
  private void handleLogMessageException(Throwable throwable, String fqcn, Message message) {
    if (throwable instanceof LoggingException)
      throw (LoggingException)throwable; 
    StatusLogger.getLogger().warn("{} caught {} logging {}: {}", fqcn, throwable
        
        .getClass().getName(), message
        .getClass().getSimpleName(), message
        .getFormat(), throwable);
  }
  
  public <T extends Throwable> T throwing(T throwable) {
    return throwing(FQCN, Level.ERROR, throwable);
  }
  
  public <T extends Throwable> T throwing(Level level, T throwable) {
    return throwing(FQCN, level, throwable);
  }
  
  protected <T extends Throwable> T throwing(String fqcn, Level level, T throwable) {
    if (isEnabled(level, THROWING_MARKER, (Object)null, (Throwable)null))
      logMessageSafely(fqcn, level, THROWING_MARKER, throwingMsg((Throwable)throwable), (Throwable)throwable); 
    return throwable;
  }
  
  protected Message throwingMsg(Throwable throwable) {
    return this.messageFactory.newMessage("Throwing");
  }
  
  public void trace(Marker marker, Message message) {
    logIfEnabled(FQCN, Level.TRACE, marker, message, (message != null) ? message.getThrowable() : null);
  }
  
  public void trace(Marker marker, Message message, Throwable throwable) {
    logIfEnabled(FQCN, Level.TRACE, marker, message, throwable);
  }
  
  public void trace(Marker marker, CharSequence message) {
    logIfEnabled(FQCN, Level.TRACE, marker, message, (Throwable)null);
  }
  
  public void trace(Marker marker, CharSequence message, Throwable throwable) {
    logIfEnabled(FQCN, Level.TRACE, marker, message, throwable);
  }
  
  public void trace(Marker marker, Object message) {
    logIfEnabled(FQCN, Level.TRACE, marker, message, (Throwable)null);
  }
  
  public void trace(Marker marker, Object message, Throwable throwable) {
    logIfEnabled(FQCN, Level.TRACE, marker, message, throwable);
  }
  
  public void trace(Marker marker, String message) {
    logIfEnabled(FQCN, Level.TRACE, marker, message, (Throwable)null);
  }
  
  public void trace(Marker marker, String message, Object... params) {
    logIfEnabled(FQCN, Level.TRACE, marker, message, params);
  }
  
  public void trace(Marker marker, String message, Throwable throwable) {
    logIfEnabled(FQCN, Level.TRACE, marker, message, throwable);
  }
  
  public void trace(Message message) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, (message != null) ? message.getThrowable() : null);
  }
  
  public void trace(Message message, Throwable throwable) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, throwable);
  }
  
  public void trace(CharSequence message) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, (Throwable)null);
  }
  
  public void trace(CharSequence message, Throwable throwable) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, throwable);
  }
  
  public void trace(Object message) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, (Throwable)null);
  }
  
  public void trace(Object message, Throwable throwable) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, throwable);
  }
  
  public void trace(String message) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, (Throwable)null);
  }
  
  public void trace(String message, Object... params) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, params);
  }
  
  public void trace(String message, Throwable throwable) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, throwable);
  }
  
  public void trace(Supplier<?> messageSupplier) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, messageSupplier, (Throwable)null);
  }
  
  public void trace(Supplier<?> messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, messageSupplier, throwable);
  }
  
  public void trace(Marker marker, Supplier<?> messageSupplier) {
    logIfEnabled(FQCN, Level.TRACE, marker, messageSupplier, (Throwable)null);
  }
  
  public void trace(Marker marker, String message, Supplier<?>... paramSuppliers) {
    logIfEnabled(FQCN, Level.TRACE, marker, message, paramSuppliers);
  }
  
  public void trace(Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.TRACE, marker, messageSupplier, throwable);
  }
  
  public void trace(String message, Supplier<?>... paramSuppliers) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, paramSuppliers);
  }
  
  public void trace(Marker marker, MessageSupplier messageSupplier) {
    logIfEnabled(FQCN, Level.TRACE, marker, messageSupplier, (Throwable)null);
  }
  
  public void trace(Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.TRACE, marker, messageSupplier, throwable);
  }
  
  public void trace(MessageSupplier messageSupplier) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, messageSupplier, (Throwable)null);
  }
  
  public void trace(MessageSupplier messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, messageSupplier, throwable);
  }
  
  public void trace(Marker marker, String message, Object p0) {
    logIfEnabled(FQCN, Level.TRACE, marker, message, p0);
  }
  
  public void trace(Marker marker, String message, Object p0, Object p1) {
    logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1);
  }
  
  public void trace(Marker marker, String message, Object p0, Object p1, Object p2) {
    logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2);
  }
  
  public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
    logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3);
  }
  
  public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4);
  }
  
  public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4, p5);
  }
  
  public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4, p5, p6);
  }
  
  public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }
  
  public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }
  
  public void trace(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    logIfEnabled(FQCN, Level.TRACE, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }
  
  public void trace(String message, Object p0) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0);
  }
  
  public void trace(String message, Object p0, Object p1) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1);
  }
  
  public void trace(String message, Object p0, Object p1, Object p2) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2);
  }
  
  public void trace(String message, Object p0, Object p1, Object p2, Object p3) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3);
  }
  
  public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3, p4);
  }
  
  public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3, p4, p5);
  }
  
  public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
  }
  
  public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }
  
  public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }
  
  public void trace(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    logIfEnabled(FQCN, Level.TRACE, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }
  
  public EntryMessage traceEntry() {
    return enter(FQCN, (String)null, (Object[])null);
  }
  
  public EntryMessage traceEntry(String format, Object... params) {
    return enter(FQCN, format, params);
  }
  
  public EntryMessage traceEntry(Supplier<?>... paramSuppliers) {
    return enter(FQCN, (String)null, paramSuppliers);
  }
  
  public EntryMessage traceEntry(String format, Supplier<?>... paramSuppliers) {
    return enter(FQCN, format, paramSuppliers);
  }
  
  public EntryMessage traceEntry(Message message) {
    return enter(FQCN, message);
  }
  
  public void traceExit() {
    exit(FQCN, (String)null, (Object)null);
  }
  
  public <R> R traceExit(R result) {
    return exit(FQCN, (String)null, result);
  }
  
  public <R> R traceExit(String format, R result) {
    return exit(FQCN, format, result);
  }
  
  public void traceExit(EntryMessage message) {
    if (message != null && isEnabled(Level.TRACE, EXIT_MARKER, (Message)message, (Throwable)null))
      logMessageSafely(FQCN, Level.TRACE, EXIT_MARKER, (Message)this.flowMessageFactory.newExitMessage(message), (Throwable)null); 
  }
  
  public <R> R traceExit(EntryMessage message, R result) {
    if (message != null && isEnabled(Level.TRACE, EXIT_MARKER, (Message)message, (Throwable)null))
      logMessageSafely(FQCN, Level.TRACE, EXIT_MARKER, (Message)this.flowMessageFactory.newExitMessage(result, message), (Throwable)null); 
    return result;
  }
  
  public <R> R traceExit(Message message, R result) {
    if (message != null && isEnabled(Level.TRACE, EXIT_MARKER, message, (Throwable)null))
      logMessageSafely(FQCN, Level.TRACE, EXIT_MARKER, (Message)this.flowMessageFactory.newExitMessage(result, message), (Throwable)null); 
    return result;
  }
  
  public void warn(Marker marker, Message message) {
    logIfEnabled(FQCN, Level.WARN, marker, message, (message != null) ? message.getThrowable() : null);
  }
  
  public void warn(Marker marker, Message message, Throwable throwable) {
    logIfEnabled(FQCN, Level.WARN, marker, message, throwable);
  }
  
  public void warn(Marker marker, CharSequence message) {
    logIfEnabled(FQCN, Level.WARN, marker, message, (Throwable)null);
  }
  
  public void warn(Marker marker, CharSequence message, Throwable throwable) {
    logIfEnabled(FQCN, Level.WARN, marker, message, throwable);
  }
  
  public void warn(Marker marker, Object message) {
    logIfEnabled(FQCN, Level.WARN, marker, message, (Throwable)null);
  }
  
  public void warn(Marker marker, Object message, Throwable throwable) {
    logIfEnabled(FQCN, Level.WARN, marker, message, throwable);
  }
  
  public void warn(Marker marker, String message) {
    logIfEnabled(FQCN, Level.WARN, marker, message, (Throwable)null);
  }
  
  public void warn(Marker marker, String message, Object... params) {
    logIfEnabled(FQCN, Level.WARN, marker, message, params);
  }
  
  public void warn(Marker marker, String message, Throwable throwable) {
    logIfEnabled(FQCN, Level.WARN, marker, message, throwable);
  }
  
  public void warn(Message message) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, message, (message != null) ? message.getThrowable() : null);
  }
  
  public void warn(Message message, Throwable throwable) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, message, throwable);
  }
  
  public void warn(CharSequence message) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, message, (Throwable)null);
  }
  
  public void warn(CharSequence message, Throwable throwable) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, message, throwable);
  }
  
  public void warn(Object message) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, message, (Throwable)null);
  }
  
  public void warn(Object message, Throwable throwable) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, message, throwable);
  }
  
  public void warn(String message) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, message, (Throwable)null);
  }
  
  public void warn(String message, Object... params) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, message, params);
  }
  
  public void warn(String message, Throwable throwable) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, message, throwable);
  }
  
  public void warn(Supplier<?> messageSupplier) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, messageSupplier, (Throwable)null);
  }
  
  public void warn(Supplier<?> messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, messageSupplier, throwable);
  }
  
  public void warn(Marker marker, Supplier<?> messageSupplier) {
    logIfEnabled(FQCN, Level.WARN, marker, messageSupplier, (Throwable)null);
  }
  
  public void warn(Marker marker, String message, Supplier<?>... paramSuppliers) {
    logIfEnabled(FQCN, Level.WARN, marker, message, paramSuppliers);
  }
  
  public void warn(Marker marker, Supplier<?> messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.WARN, marker, messageSupplier, throwable);
  }
  
  public void warn(String message, Supplier<?>... paramSuppliers) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, message, paramSuppliers);
  }
  
  public void warn(Marker marker, MessageSupplier messageSupplier) {
    logIfEnabled(FQCN, Level.WARN, marker, messageSupplier, (Throwable)null);
  }
  
  public void warn(Marker marker, MessageSupplier messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.WARN, marker, messageSupplier, throwable);
  }
  
  public void warn(MessageSupplier messageSupplier) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, messageSupplier, (Throwable)null);
  }
  
  public void warn(MessageSupplier messageSupplier, Throwable throwable) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, messageSupplier, throwable);
  }
  
  public void warn(Marker marker, String message, Object p0) {
    logIfEnabled(FQCN, Level.WARN, marker, message, p0);
  }
  
  public void warn(Marker marker, String message, Object p0, Object p1) {
    logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1);
  }
  
  public void warn(Marker marker, String message, Object p0, Object p1, Object p2) {
    logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2);
  }
  
  public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3) {
    logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3);
  }
  
  public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4);
  }
  
  public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4, p5);
  }
  
  public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4, p5, p6);
  }
  
  public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }
  
  public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }
  
  public void warn(Marker marker, String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    logIfEnabled(FQCN, Level.WARN, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }
  
  public void warn(String message, Object p0) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0);
  }
  
  public void warn(String message, Object p0, Object p1) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1);
  }
  
  public void warn(String message, Object p0, Object p1, Object p2) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2);
  }
  
  public void warn(String message, Object p0, Object p1, Object p2, Object p3) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3);
  }
  
  public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3, p4);
  }
  
  public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3, p4, p5);
  }
  
  public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6);
  }
  
  public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7);
  }
  
  public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
  }
  
  public void warn(String message, Object p0, Object p1, Object p2, Object p3, Object p4, Object p5, Object p6, Object p7, Object p8, Object p9) {
    logIfEnabled(FQCN, Level.WARN, (Marker)null, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
  }
  
  protected boolean requiresLocation() {
    return false;
  }
  
  public LogBuilder atTrace() {
    return atLevel(Level.TRACE);
  }
  
  public LogBuilder atDebug() {
    return atLevel(Level.DEBUG);
  }
  
  public LogBuilder atInfo() {
    return atLevel(Level.INFO);
  }
  
  public LogBuilder atWarn() {
    return atLevel(Level.WARN);
  }
  
  public LogBuilder atError() {
    return atLevel(Level.ERROR);
  }
  
  public LogBuilder atFatal() {
    return atLevel(Level.FATAL);
  }
  
  public LogBuilder always() {
    DefaultLogBuilder builder = this.logBuilder.get();
    if (builder.isInUse())
      return (LogBuilder)new DefaultLogBuilder(this); 
    return builder.reset(Level.OFF);
  }
  
  public LogBuilder atLevel(Level level) {
    if (isEnabled(level))
      return getLogBuilder(level).reset(level); 
    return LogBuilder.NOOP;
  }
  
  private DefaultLogBuilder getLogBuilder(Level level) {
    DefaultLogBuilder builder = this.logBuilder.get();
    return (Constants.ENABLE_THREADLOCALS && !builder.isInUse()) ? builder : new DefaultLogBuilder(this, level);
  }
  
  private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
    s.defaultReadObject();
    try {
      Field f = getClass().getDeclaredField("logBuilder");
      f.setAccessible(true);
      f.set(this, new LocalLogBuilder(this));
    } catch (NoSuchFieldException|IllegalAccessException ex) {
      StatusLogger.getLogger().warn("Unable to initialize LogBuilder");
    } 
  }
  
  private class LocalLogBuilder extends ThreadLocal<DefaultLogBuilder> {
    private AbstractLogger logger;
    
    LocalLogBuilder(AbstractLogger logger) {
      this.logger = logger;
    }
    
    protected DefaultLogBuilder initialValue() {
      return new DefaultLogBuilder(this.logger);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\spi\AbstractLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */