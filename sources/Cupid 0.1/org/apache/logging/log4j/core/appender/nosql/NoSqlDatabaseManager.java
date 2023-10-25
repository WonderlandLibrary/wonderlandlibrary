package org.apache.logging.log4j.core.appender.nosql;

import java.io.Serializable;
import java.util.Date;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;
import org.apache.logging.log4j.core.util.Closer;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.util.ReadOnlyStringMap;

public final class NoSqlDatabaseManager<W> extends AbstractDatabaseManager {
  private static final NoSQLDatabaseManagerFactory FACTORY = new NoSQLDatabaseManagerFactory();
  
  private final NoSqlProvider<NoSqlConnection<W, ? extends NoSqlObject<W>>> provider;
  
  private NoSqlConnection<W, ? extends NoSqlObject<W>> connection;
  
  private NoSqlDatabaseManager(String name, int bufferSize, NoSqlProvider<NoSqlConnection<W, ? extends NoSqlObject<W>>> provider) {
    super(name, bufferSize);
    this.provider = provider;
  }
  
  protected void startupInternal() {}
  
  protected boolean shutdownInternal() {
    return Closer.closeSilently(this.connection);
  }
  
  protected void connectAndStart() {
    try {
      this.connection = this.provider.getConnection();
    } catch (Exception e) {
      throw new AppenderLoggingException("Failed to get connection from NoSQL connection provider.", e);
    } 
  }
  
  protected void writeInternal(LogEvent event, Serializable serializable) {
    if (!isRunning() || this.connection == null || this.connection.isClosed())
      throw new AppenderLoggingException("Cannot write logging event; NoSQL manager not connected to the database."); 
    NoSqlObject<W> entity = this.connection.createObject();
    if (serializable instanceof MapMessage) {
      setFields((MapMessage<?, ?>)serializable, entity);
    } else {
      setFields(event, entity);
    } 
    this.connection.insertObject(entity);
  }
  
  private void setFields(MapMessage<?, ?> mapMessage, NoSqlObject<W> noSqlObject) {
    mapMessage.forEach((key, value) -> noSqlObject.set(key, value));
  }
  
  private void setFields(LogEvent event, NoSqlObject<W> entity) {
    entity.set("level", event.getLevel());
    entity.set("loggerName", event.getLoggerName());
    entity.set("message", (event.getMessage() == null) ? null : event.getMessage().getFormattedMessage());
    StackTraceElement source = event.getSource();
    if (source == null) {
      entity.set("source", (Object)null);
    } else {
      entity.set("source", convertStackTraceElement(source));
    } 
    Marker marker = event.getMarker();
    if (marker == null) {
      entity.set("marker", (Object)null);
    } else {
      entity.set("marker", buildMarkerEntity(marker));
    } 
    entity.set("threadId", Long.valueOf(event.getThreadId()));
    entity.set("threadName", event.getThreadName());
    entity.set("threadPriority", Integer.valueOf(event.getThreadPriority()));
    entity.set("millis", Long.valueOf(event.getTimeMillis()));
    entity.set("date", new Date(event.getTimeMillis()));
    Throwable thrown = event.getThrown();
    if (thrown == null) {
      entity.set("thrown", (Object)null);
    } else {
      NoSqlObject<W> originalExceptionEntity = this.connection.createObject();
      NoSqlObject<W> exceptionEntity = originalExceptionEntity;
      exceptionEntity.set("type", thrown.getClass().getName());
      exceptionEntity.set("message", thrown.getMessage());
      exceptionEntity.set("stackTrace", convertStackTrace(thrown.getStackTrace()));
      while (thrown.getCause() != null) {
        thrown = thrown.getCause();
        NoSqlObject<W> causingExceptionEntity = this.connection.createObject();
        causingExceptionEntity.set("type", thrown.getClass().getName());
        causingExceptionEntity.set("message", thrown.getMessage());
        causingExceptionEntity.set("stackTrace", convertStackTrace(thrown.getStackTrace()));
        exceptionEntity.set("cause", causingExceptionEntity);
        exceptionEntity = causingExceptionEntity;
      } 
      entity.set("thrown", originalExceptionEntity);
    } 
    ReadOnlyStringMap contextMap = event.getContextData();
    if (contextMap == null) {
      entity.set("contextMap", (Object)null);
    } else {
      NoSqlObject<W> contextMapEntity = this.connection.createObject();
      contextMap.forEach((key, val) -> contextMapEntity.set(key, val));
      entity.set("contextMap", contextMapEntity);
    } 
    ThreadContext.ContextStack contextStack = event.getContextStack();
    if (contextStack == null) {
      entity.set("contextStack", (Object)null);
    } else {
      entity.set("contextStack", contextStack.asList().toArray());
    } 
  }
  
  private NoSqlObject<W> buildMarkerEntity(Marker marker) {
    NoSqlObject<W> entity = this.connection.createObject();
    entity.set("name", marker.getName());
    Marker[] parents = marker.getParents();
    if (parents != null) {
      NoSqlObject[] arrayOfNoSqlObject = new NoSqlObject[parents.length];
      for (int i = 0; i < parents.length; i++)
        arrayOfNoSqlObject[i] = buildMarkerEntity(parents[i]); 
      entity.set("parents", (NoSqlObject<W>[])arrayOfNoSqlObject);
    } 
    return entity;
  }
  
  protected boolean commitAndClose() {
    return true;
  }
  
  private NoSqlObject<W>[] convertStackTrace(StackTraceElement[] stackTrace) {
    NoSqlObject<W>[] stackTraceEntities = this.connection.createList(stackTrace.length);
    for (int i = 0; i < stackTrace.length; i++)
      stackTraceEntities[i] = convertStackTraceElement(stackTrace[i]); 
    return stackTraceEntities;
  }
  
  private NoSqlObject<W> convertStackTraceElement(StackTraceElement element) {
    NoSqlObject<W> elementEntity = this.connection.createObject();
    elementEntity.set("className", element.getClassName());
    elementEntity.set("methodName", element.getMethodName());
    elementEntity.set("fileName", element.getFileName());
    elementEntity.set("lineNumber", Integer.valueOf(element.getLineNumber()));
    return elementEntity;
  }
  
  public static NoSqlDatabaseManager<?> getNoSqlDatabaseManager(String name, int bufferSize, NoSqlProvider<?> provider) {
    return (NoSqlDatabaseManager)AbstractDatabaseManager.getManager(name, new FactoryData(bufferSize, provider), FACTORY);
  }
  
  private static final class FactoryData extends AbstractDatabaseManager.AbstractFactoryData {
    private final NoSqlProvider<?> provider;
    
    protected FactoryData(int bufferSize, NoSqlProvider<?> provider) {
      super(bufferSize, null);
      this.provider = provider;
    }
  }
  
  private static final class NoSQLDatabaseManagerFactory implements ManagerFactory<NoSqlDatabaseManager<?>, FactoryData> {
    private NoSQLDatabaseManagerFactory() {}
    
    public NoSqlDatabaseManager<?> createManager(String name, NoSqlDatabaseManager.FactoryData data) {
      return new NoSqlDatabaseManager(name, data.getBufferSize(), data.provider);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\nosql\NoSqlDatabaseManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */