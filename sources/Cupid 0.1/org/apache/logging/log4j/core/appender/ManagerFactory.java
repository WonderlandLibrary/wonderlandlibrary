package org.apache.logging.log4j.core.appender;

public interface ManagerFactory<M, T> {
  M createManager(String paramString, T paramT);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\appender\ManagerFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */