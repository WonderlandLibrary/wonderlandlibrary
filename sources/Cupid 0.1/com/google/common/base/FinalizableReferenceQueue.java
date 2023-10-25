package com.google.common.base;

import com.google.common.annotations.VisibleForTesting;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FinalizableReferenceQueue implements Closeable {
  private static final Logger logger = Logger.getLogger(FinalizableReferenceQueue.class.getName());
  
  private static final String FINALIZER_CLASS_NAME = "com.google.common.base.internal.Finalizer";
  
  private static final Method startFinalizer;
  
  static {
    Class<?> finalizer = loadFinalizer(new FinalizerLoader[] { new SystemLoader(), new DecoupledLoader(), new DirectLoader() });
    startFinalizer = getStartFinalizer(finalizer);
  }
  
  final ReferenceQueue<Object> queue = new ReferenceQueue();
  
  final PhantomReference<Object> frqRef = new PhantomReference(this, this.queue);
  
  final boolean threadStarted;
  
  public FinalizableReferenceQueue() {
    boolean threadStarted = false;
    try {
      startFinalizer.invoke(null, new Object[] { FinalizableReference.class, this.queue, this.frqRef });
      threadStarted = true;
    } catch (IllegalAccessException impossible) {
      throw new AssertionError(impossible);
    } catch (Throwable t) {
      logger.log(Level.INFO, "Failed to start reference finalizer thread. Reference cleanup will only occur when new references are created.", t);
    } 
    this.threadStarted = threadStarted;
  }
  
  public void close() {
    this.frqRef.enqueue();
    cleanUp();
  }
  
  void cleanUp() {
    if (this.threadStarted)
      return; 
    Reference<?> reference;
    while ((reference = this.queue.poll()) != null) {
      reference.clear();
      try {
        ((FinalizableReference)reference).finalizeReferent();
      } catch (Throwable t) {
        logger.log(Level.SEVERE, "Error cleaning up after reference.", t);
      } 
    } 
  }
  
  private static Class<?> loadFinalizer(FinalizerLoader... loaders) {
    for (FinalizerLoader loader : loaders) {
      Class<?> finalizer = loader.loadFinalizer();
      if (finalizer != null)
        return finalizer; 
    } 
    throw new AssertionError();
  }
  
  static interface FinalizerLoader {
    Class<?> loadFinalizer();
  }
  
  static class SystemLoader implements FinalizerLoader {
    @VisibleForTesting
    static boolean disabled;
    
    public Class<?> loadFinalizer() {
      ClassLoader systemLoader;
      if (disabled)
        return null; 
      try {
        systemLoader = ClassLoader.getSystemClassLoader();
      } catch (SecurityException e) {
        FinalizableReferenceQueue.logger.info("Not allowed to access system class loader.");
        return null;
      } 
      if (systemLoader != null)
        try {
          return systemLoader.loadClass("com.google.common.base.internal.Finalizer");
        } catch (ClassNotFoundException e) {
          return null;
        }  
      return null;
    }
  }
  
  static class DecoupledLoader implements FinalizerLoader {
    private static final String LOADING_ERROR = "Could not load Finalizer in its own class loader.Loading Finalizer in the current class loader instead. As a result, you will not be ableto garbage collect this class loader. To support reclaiming this class loader, eitherresolve the underlying issue, or move Google Collections to your system class path.";
    
    public Class<?> loadFinalizer() {
      try {
        ClassLoader finalizerLoader = newLoader(getBaseUrl());
        return finalizerLoader.loadClass("com.google.common.base.internal.Finalizer");
      } catch (Exception e) {
        FinalizableReferenceQueue.logger.log(Level.WARNING, "Could not load Finalizer in its own class loader.Loading Finalizer in the current class loader instead. As a result, you will not be ableto garbage collect this class loader. To support reclaiming this class loader, eitherresolve the underlying issue, or move Google Collections to your system class path.", e);
        return null;
      } 
    }
    
    URL getBaseUrl() throws IOException {
      String finalizerPath = "com.google.common.base.internal.Finalizer".replace('.', '/') + ".class";
      URL finalizerUrl = getClass().getClassLoader().getResource(finalizerPath);
      if (finalizerUrl == null)
        throw new FileNotFoundException(finalizerPath); 
      String urlString = finalizerUrl.toString();
      if (!urlString.endsWith(finalizerPath))
        throw new IOException("Unsupported path style: " + urlString); 
      urlString = urlString.substring(0, urlString.length() - finalizerPath.length());
      return new URL(finalizerUrl, urlString);
    }
    
    URLClassLoader newLoader(URL base) {
      return new URLClassLoader(new URL[] { base }, null);
    }
  }
  
  static class DirectLoader implements FinalizerLoader {
    public Class<?> loadFinalizer() {
      try {
        return Class.forName("com.google.common.base.internal.Finalizer");
      } catch (ClassNotFoundException e) {
        throw new AssertionError(e);
      } 
    }
  }
  
  static Method getStartFinalizer(Class<?> finalizer) {
    try {
      return finalizer.getMethod("startFinalizer", new Class[] { Class.class, ReferenceQueue.class, PhantomReference.class });
    } catch (NoSuchMethodException e) {
      throw new AssertionError(e);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\base\FinalizableReferenceQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */