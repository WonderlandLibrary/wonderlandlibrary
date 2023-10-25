package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.message.MessageFactory;

public interface LoggerContext {
  Object getExternalContext();
  
  default Object getObject(String key) {
    return null;
  }
  
  default Object putObject(String key, Object value) {
    return null;
  }
  
  default Object putObjectIfAbsent(String key, Object value) {
    return null;
  }
  
  default Object removeObject(String key) {
    return null;
  }
  
  default boolean removeObject(String key, Object value) {
    return false;
  }
  
  ExtendedLogger getLogger(String paramString);
  
  default ExtendedLogger getLogger(Class<?> cls) {
    String canonicalName = cls.getCanonicalName();
    return getLogger((canonicalName != null) ? canonicalName : cls.getName());
  }
  
  ExtendedLogger getLogger(String paramString, MessageFactory paramMessageFactory);
  
  default ExtendedLogger getLogger(Class<?> cls, MessageFactory messageFactory) {
    String canonicalName = cls.getCanonicalName();
    return getLogger((canonicalName != null) ? canonicalName : cls.getName(), messageFactory);
  }
  
  boolean hasLogger(String paramString);
  
  boolean hasLogger(String paramString, MessageFactory paramMessageFactory);
  
  boolean hasLogger(String paramString, Class<? extends MessageFactory> paramClass);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\spi\LoggerContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */