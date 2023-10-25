package io.netty.util.internal.logging;

public abstract class InternalLoggerFactory {
  private static volatile InternalLoggerFactory defaultFactory = newDefaultFactory(InternalLoggerFactory.class.getName());
  
  private static InternalLoggerFactory newDefaultFactory(String name) {
    InternalLoggerFactory internalLoggerFactory;
    try {
      internalLoggerFactory = new Slf4JLoggerFactory(true);
      internalLoggerFactory.newInstance(name).debug("Using SLF4J as the default logging framework");
    } catch (Throwable t1) {
      try {
        internalLoggerFactory = new Log4JLoggerFactory();
        internalLoggerFactory.newInstance(name).debug("Using Log4J as the default logging framework");
      } catch (Throwable t2) {
        internalLoggerFactory = new JdkLoggerFactory();
        internalLoggerFactory.newInstance(name).debug("Using java.util.logging as the default logging framework");
      } 
    } 
    return internalLoggerFactory;
  }
  
  public static InternalLoggerFactory getDefaultFactory() {
    return defaultFactory;
  }
  
  public static void setDefaultFactory(InternalLoggerFactory defaultFactory) {
    if (defaultFactory == null)
      throw new NullPointerException("defaultFactory"); 
    InternalLoggerFactory.defaultFactory = defaultFactory;
  }
  
  public static InternalLogger getInstance(Class<?> clazz) {
    return getInstance(clazz.getName());
  }
  
  public static InternalLogger getInstance(String name) {
    return getDefaultFactory().newInstance(name);
  }
  
  protected abstract InternalLogger newInstance(String paramString);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\logging\InternalLoggerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */