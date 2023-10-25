package org.apache.commons.logging.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.LogFactory;

public class ServletContextCleaner implements ServletContextListener {
  private static final Class[] RELEASE_SIGNATURE = new Class[] { ClassLoader.class };
  
  public void contextDestroyed(ServletContextEvent sce) {
    ClassLoader tccl = Thread.currentThread().getContextClassLoader();
    Object[] params = new Object[1];
    params[0] = tccl;
    ClassLoader loader = tccl;
    while (loader != null) {
      try {
        Class logFactoryClass = loader.loadClass("org.apache.commons.logging.LogFactory");
        Method releaseMethod = logFactoryClass.getMethod("release", RELEASE_SIGNATURE);
        releaseMethod.invoke(null, params);
        loader = logFactoryClass.getClassLoader().getParent();
      } catch (ClassNotFoundException ex) {
        loader = null;
      } catch (NoSuchMethodException ex) {
        System.err.println("LogFactory instance found which does not support release method!");
        loader = null;
      } catch (IllegalAccessException ex) {
        System.err.println("LogFactory instance found which is not accessable!");
        loader = null;
      } catch (InvocationTargetException ex) {
        System.err.println("LogFactory instance release method failed!");
        loader = null;
      } 
    } 
    LogFactory.release(tccl);
  }
  
  public void contextInitialized(ServletContextEvent sce) {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\logging\impl\ServletContextCleaner.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */