package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Locale;

public final class NativeLibraryLoader {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(NativeLibraryLoader.class);
  
  private static final String NATIVE_RESOURCE_HOME = "META-INF/native/";
  
  private static final String OSNAME = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
  
  private static final File WORKDIR;
  
  static {
    String workdir = SystemPropertyUtil.get("io.netty.native.workdir");
    if (workdir != null) {
      File f = new File(workdir);
      f.mkdirs();
      try {
        f = f.getAbsoluteFile();
      } catch (Exception ignored) {}
      WORKDIR = f;
      logger.debug("-Dio.netty.netty.workdir: " + WORKDIR);
    } else {
      WORKDIR = tmpdir();
      logger.debug("-Dio.netty.netty.workdir: " + WORKDIR + " (io.netty.tmpdir)");
    } 
  }
  
  private static File tmpdir() {
    File f;
    try {
      f = toDirectory(SystemPropertyUtil.get("io.netty.tmpdir"));
      if (f != null) {
        logger.debug("-Dio.netty.tmpdir: " + f);
        return f;
      } 
      f = toDirectory(SystemPropertyUtil.get("java.io.tmpdir"));
      if (f != null) {
        logger.debug("-Dio.netty.tmpdir: " + f + " (java.io.tmpdir)");
        return f;
      } 
      if (isWindows()) {
        f = toDirectory(System.getenv("TEMP"));
        if (f != null) {
          logger.debug("-Dio.netty.tmpdir: " + f + " (%TEMP%)");
          return f;
        } 
        String userprofile = System.getenv("USERPROFILE");
        if (userprofile != null) {
          f = toDirectory(userprofile + "\\AppData\\Local\\Temp");
          if (f != null) {
            logger.debug("-Dio.netty.tmpdir: " + f + " (%USERPROFILE%\\AppData\\Local\\Temp)");
            return f;
          } 
          f = toDirectory(userprofile + "\\Local Settings\\Temp");
          if (f != null) {
            logger.debug("-Dio.netty.tmpdir: " + f + " (%USERPROFILE%\\Local Settings\\Temp)");
            return f;
          } 
        } 
      } else {
        f = toDirectory(System.getenv("TMPDIR"));
        if (f != null) {
          logger.debug("-Dio.netty.tmpdir: " + f + " ($TMPDIR)");
          return f;
        } 
      } 
    } catch (Exception ignored) {}
    if (isWindows()) {
      f = new File("C:\\Windows\\Temp");
    } else {
      f = new File("/tmp");
    } 
    logger.warn("Failed to get the temporary directory; falling back to: " + f);
    return f;
  }
  
  private static File toDirectory(String path) {
    if (path == null)
      return null; 
    File f = new File(path);
    f.mkdirs();
    if (!f.isDirectory())
      return null; 
    try {
      return f.getAbsoluteFile();
    } catch (Exception ignored) {
      return f;
    } 
  }
  
  private static boolean isWindows() {
    return OSNAME.startsWith("windows");
  }
  
  private static boolean isOSX() {
    return (OSNAME.startsWith("macosx") || OSNAME.startsWith("osx"));
  }
  
  public static void load(String name, ClassLoader loader) {
    String libname = System.mapLibraryName(name);
    String path = "META-INF/native/" + libname;
    URL url = loader.getResource(path);
    if (url == null && isOSX())
      if (path.endsWith(".jnilib")) {
        url = loader.getResource("META-INF/native/lib" + name + ".dynlib");
      } else {
        url = loader.getResource("META-INF/native/lib" + name + ".jnilib");
      }  
    if (url == null) {
      System.loadLibrary(name);
      return;
    } 
    int index = libname.lastIndexOf('.');
    String prefix = libname.substring(0, index);
    String suffix = libname.substring(index, libname.length());
    InputStream in = null;
    OutputStream out = null;
    File tmpFile = null;
    boolean loaded = false;
    try {
      tmpFile = File.createTempFile(prefix, suffix, WORKDIR);
      in = url.openStream();
      out = new FileOutputStream(tmpFile);
      byte[] buffer = new byte[8192];
      int length;
      while ((length = in.read(buffer)) > 0)
        out.write(buffer, 0, length); 
      out.flush();
      out.close();
      out = null;
      System.load(tmpFile.getPath());
      loaded = true;
    } catch (Exception e) {
      throw (UnsatisfiedLinkError)(new UnsatisfiedLinkError("could not load a native library: " + name)).initCause(e);
    } finally {
      if (in != null)
        try {
          in.close();
        } catch (IOException ignore) {} 
      if (out != null)
        try {
          out.close();
        } catch (IOException ignore) {} 
      if (tmpFile != null)
        if (loaded) {
          tmpFile.deleteOnExit();
        } else if (!tmpFile.delete()) {
          tmpFile.deleteOnExit();
        }  
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\NativeLibraryLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */