package io.netty.util.internal.logging;

public abstract class InternalLoggerFactory {
   private static volatile InternalLoggerFactory defaultFactory;

   public static InternalLoggerFactory getDefaultFactory() {
      return defaultFactory;
   }

   public static void setDefaultFactory(InternalLoggerFactory var0) {
      if (var0 == null) {
         throw new NullPointerException("defaultFactory");
      } else {
         defaultFactory = var0;
      }
   }

   public static InternalLogger getInstance(Class var0) {
      return getInstance(var0.getName());
   }

   public static InternalLogger getInstance(String var0) {
      return getDefaultFactory().newInstance(var0);
   }

   protected abstract InternalLogger newInstance(String var1);

   static {
      String var0 = InternalLoggerFactory.class.getName();

      Object var1;
      try {
         var1 = new Slf4JLoggerFactory(true);
         ((InternalLoggerFactory)var1).newInstance(var0).debug("Using SLF4J as the default logging framework");
         defaultFactory = (InternalLoggerFactory)var1;
      } catch (Throwable var5) {
         try {
            var1 = new Log4JLoggerFactory();
            ((InternalLoggerFactory)var1).newInstance(var0).debug("Using Log4J as the default logging framework");
         } catch (Throwable var4) {
            var1 = new JdkLoggerFactory();
            ((InternalLoggerFactory)var1).newInstance(var0).debug("Using java.util.logging as the default logging framework");
         }
      }

      defaultFactory = (InternalLoggerFactory)var1;
   }
}
