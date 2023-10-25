package com.sun.jna;

import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Window;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.WeakHashMap;

public final class Native {
  private static final String VERSION = "3.4.0";
  
  private static final String VERSION_NATIVE = "3.4.0";
  
  private static String nativeLibraryPath = null;
  
  private static Map typeMappers = new WeakHashMap();
  
  private static Map alignments = new WeakHashMap();
  
  private static Map options = new WeakHashMap();
  
  private static Map libraries = new WeakHashMap();
  
  private static final Callback.UncaughtExceptionHandler DEFAULT_HANDLER = new Callback.UncaughtExceptionHandler() {
      public void uncaughtException(Callback c, Throwable e) {
        System.err.println("JNA: Callback " + c + " threw the following exception:");
        e.printStackTrace();
      }
    };
  
  private static Callback.UncaughtExceptionHandler callbackExceptionHandler = DEFAULT_HANDLER;
  
  static {
    loadNativeLibrary();
  }
  
  public static final int POINTER_SIZE = sizeof(0);
  
  public static final int LONG_SIZE = sizeof(1);
  
  public static final int WCHAR_SIZE = sizeof(2);
  
  public static final int SIZE_T_SIZE = sizeof(3);
  
  private static final int TYPE_VOIDP = 0;
  
  private static final int TYPE_LONG = 1;
  
  private static final int TYPE_WCHAR_T = 2;
  
  private static final int TYPE_SIZE_T = 3;
  
  private static final int THREAD_NOCHANGE = 0;
  
  private static final int THREAD_DETACH = -1;
  
  private static final int THREAD_LEAVE_ATTACHED = -2;
  
  static {
    initIDs();
    if (Boolean.getBoolean("jna.protected"))
      setProtected(true); 
    String version = getNativeVersion();
    if (!"3.4.0".equals(version)) {
      String LS = System.getProperty("line.separator");
      throw new Error(LS + LS + "There is an incompatible JNA native library installed on this system." + LS + "To resolve this issue you may do one of the following:" + LS + " - remove or uninstall the offending library" + LS + " - set the system property jna.nosys=true" + LS + " - set jna.boot.library.path to include the path to the version of the " + LS + "   jnidispatch library included with the JNA jar file you are using" + LS);
    } 
    setPreserveLastError("true".equalsIgnoreCase(System.getProperty("jna.preserve_last_error", "true")));
  }
  
  private static final Object finalizer = new Object() {
      protected void finalize() {
        Native.dispose();
      }
    };
  
  private static void dispose() {
    NativeLibrary.disposeAll();
    nativeLibraryPath = null;
  }
  
  private static boolean deleteNativeLibrary(String path) {
    File flib = new File(path);
    if (flib.delete())
      return true; 
    markTemporaryFile(flib);
    return false;
  }
  
  public static long getWindowID(Window w) throws HeadlessException {
    return AWT.getWindowID(w);
  }
  
  public static long getComponentID(Component c) throws HeadlessException {
    return AWT.getComponentID(c);
  }
  
  public static Pointer getWindowPointer(Window w) throws HeadlessException {
    return new Pointer(AWT.getWindowID(w));
  }
  
  public static Pointer getComponentPointer(Component c) throws HeadlessException {
    return new Pointer(AWT.getComponentID(c));
  }
  
  public static Pointer getDirectBufferPointer(Buffer b) {
    long peer = _getDirectBufferPointer(b);
    return (peer == 0L) ? null : new Pointer(peer);
  }
  
  public static String toString(byte[] buf) {
    return toString(buf, System.getProperty("jna.encoding"));
  }
  
  public static String toString(byte[] buf, String encoding) {
    String s = null;
    if (encoding != null)
      try {
        s = new String(buf, encoding);
      } catch (UnsupportedEncodingException e) {} 
    if (s == null)
      s = new String(buf); 
    int term = s.indexOf(false);
    if (term != -1)
      s = s.substring(0, term); 
    return s;
  }
  
  public static String toString(char[] buf) {
    String s = new String(buf);
    int term = s.indexOf(false);
    if (term != -1)
      s = s.substring(0, term); 
    return s;
  }
  
  public static Object loadLibrary(Class interfaceClass) {
    return loadLibrary((String)null, interfaceClass);
  }
  
  public static Object loadLibrary(Class interfaceClass, Map options) {
    return loadLibrary(null, interfaceClass, options);
  }
  
  public static Object loadLibrary(String name, Class interfaceClass) {
    return loadLibrary(name, interfaceClass, Collections.EMPTY_MAP);
  }
  
  public static Object loadLibrary(String name, Class interfaceClass, Map options) {
    Library.Handler handler = new Library.Handler(name, interfaceClass, options);
    ClassLoader loader = interfaceClass.getClassLoader();
    Library proxy = (Library)Proxy.newProxyInstance(loader, new Class[] { interfaceClass }, handler);
    cacheOptions(interfaceClass, options, proxy);
    return proxy;
  }
  
  private static void loadLibraryInstance(Class cls) {
    if (cls != null && !libraries.containsKey(cls))
      try {
        Field[] fields = cls.getFields();
        for (int i = 0; i < fields.length; i++) {
          Field field = fields[i];
          if (field.getType() == cls && Modifier.isStatic(field.getModifiers())) {
            libraries.put(cls, new WeakReference(field.get(null)));
            break;
          } 
        } 
      } catch (Exception e) {
        throw new IllegalArgumentException("Could not access instance of " + cls + " (" + e + ")");
      }  
  }
  
  static Class findEnclosingLibraryClass(Class cls) {
    if (cls == null)
      return null; 
    synchronized (libraries) {
      if (options.containsKey(cls))
        return cls; 
    } 
    if (Library.class.isAssignableFrom(cls))
      return cls; 
    if (Callback.class.isAssignableFrom(cls))
      cls = CallbackReference.findCallbackClass(cls); 
    Class declaring = cls.getDeclaringClass();
    Class fromDeclaring = findEnclosingLibraryClass(declaring);
    if (fromDeclaring != null)
      return fromDeclaring; 
    return findEnclosingLibraryClass(cls.getSuperclass());
  }
  
  public static Map getLibraryOptions(Class type) {
    synchronized (libraries) {
      Class interfaceClass = findEnclosingLibraryClass(type);
      if (interfaceClass != null) {
        loadLibraryInstance(interfaceClass);
      } else {
        interfaceClass = type;
      } 
      if (!options.containsKey(interfaceClass))
        try {
          Field field = interfaceClass.getField("OPTIONS");
          field.setAccessible(true);
          options.put(interfaceClass, field.get(null));
        } catch (NoSuchFieldException e) {
        
        } catch (Exception e) {
          throw new IllegalArgumentException("OPTIONS must be a public field of type java.util.Map (" + e + "): " + interfaceClass);
        }  
      return (Map)options.get(interfaceClass);
    } 
  }
  
  public static TypeMapper getTypeMapper(Class cls) {
    synchronized (libraries) {
      Class interfaceClass = findEnclosingLibraryClass(cls);
      if (interfaceClass != null) {
        loadLibraryInstance(interfaceClass);
      } else {
        interfaceClass = cls;
      } 
      if (!typeMappers.containsKey(interfaceClass))
        try {
          Field field = interfaceClass.getField("TYPE_MAPPER");
          field.setAccessible(true);
          typeMappers.put(interfaceClass, field.get(null));
        } catch (NoSuchFieldException e) {
          Map options = getLibraryOptions(cls);
          if (options != null && options.containsKey("type-mapper"))
            typeMappers.put(interfaceClass, options.get("type-mapper")); 
        } catch (Exception e) {
          throw new IllegalArgumentException("TYPE_MAPPER must be a public field of type " + TypeMapper.class.getName() + " (" + e + "): " + interfaceClass);
        }  
      return (TypeMapper)typeMappers.get(interfaceClass);
    } 
  }
  
  public static int getStructureAlignment(Class cls) {
    synchronized (libraries) {
      Class interfaceClass = findEnclosingLibraryClass(cls);
      if (interfaceClass != null) {
        loadLibraryInstance(interfaceClass);
      } else {
        interfaceClass = cls;
      } 
      if (!alignments.containsKey(interfaceClass))
        try {
          Field field = interfaceClass.getField("STRUCTURE_ALIGNMENT");
          field.setAccessible(true);
          alignments.put(interfaceClass, field.get(null));
        } catch (NoSuchFieldException e) {
          Map options = getLibraryOptions(interfaceClass);
          if (options != null && options.containsKey("structure-alignment"))
            alignments.put(interfaceClass, options.get("structure-alignment")); 
        } catch (Exception e) {
          throw new IllegalArgumentException("STRUCTURE_ALIGNMENT must be a public field of type int (" + e + "): " + interfaceClass);
        }  
      Integer value = (Integer)alignments.get(interfaceClass);
      return (value != null) ? value.intValue() : 0;
    } 
  }
  
  static byte[] getBytes(String s) {
    try {
      return getBytes(s, System.getProperty("jna.encoding"));
    } catch (UnsupportedEncodingException e) {
      return s.getBytes();
    } 
  }
  
  static byte[] getBytes(String s, String encoding) throws UnsupportedEncodingException {
    if (encoding != null)
      return s.getBytes(encoding); 
    return s.getBytes();
  }
  
  public static byte[] toByteArray(String s) {
    byte[] bytes = getBytes(s);
    byte[] buf = new byte[bytes.length + 1];
    System.arraycopy(bytes, 0, buf, 0, bytes.length);
    return buf;
  }
  
  public static byte[] toByteArray(String s, String encoding) throws UnsupportedEncodingException {
    byte[] bytes = getBytes(s, encoding);
    byte[] buf = new byte[bytes.length + 1];
    System.arraycopy(bytes, 0, buf, 0, bytes.length);
    return buf;
  }
  
  public static char[] toCharArray(String s) {
    char[] chars = s.toCharArray();
    char[] buf = new char[chars.length + 1];
    System.arraycopy(chars, 0, buf, 0, chars.length);
    return buf;
  }
  
  static String getNativeLibraryResourcePath(int osType, String arch, String name) {
    arch = arch.toLowerCase();
    if ("powerpc".equals(arch)) {
      arch = "ppc";
    } else if ("powerpc64".equals(arch)) {
      arch = "ppc64";
    } 
    switch (osType) {
      case 2:
        if ("i386".equals(arch))
          arch = "x86"; 
        osPrefix = "win32-" + arch;
        return "/com/sun/jna/" + osPrefix;
      case 6:
        osPrefix = "w32ce-" + arch;
        return "/com/sun/jna/" + osPrefix;
      case 0:
        osPrefix = "darwin";
        return "/com/sun/jna/" + osPrefix;
      case 1:
        if ("x86".equals(arch)) {
          arch = "i386";
        } else if ("x86_64".equals(arch)) {
          arch = "amd64";
        } 
        osPrefix = "linux-" + arch;
        return "/com/sun/jna/" + osPrefix;
      case 3:
        osPrefix = "sunos-" + arch;
        return "/com/sun/jna/" + osPrefix;
    } 
    String osPrefix = name.toLowerCase();
    if ("x86".equals(arch))
      arch = "i386"; 
    if ("x86_64".equals(arch))
      arch = "amd64"; 
    int space = osPrefix.indexOf(" ");
    if (space != -1)
      osPrefix = osPrefix.substring(0, space); 
    osPrefix = osPrefix + "-" + arch;
    return "/com/sun/jna/" + osPrefix;
  }
  
  private static void loadNativeLibrary() {
    removeTemporaryFiles();
    String libName = System.getProperty("jna.boot.library.name", "jnidispatch");
    String bootPath = System.getProperty("jna.boot.library.path");
    if (bootPath != null) {
      StringTokenizer dirs = new StringTokenizer(bootPath, File.pathSeparator);
      while (dirs.hasMoreTokens()) {
        String dir = dirs.nextToken();
        File file = new File(new File(dir), System.mapLibraryName(libName));
        String path = file.getAbsolutePath();
        if (file.exists())
          try {
            System.load(path);
            nativeLibraryPath = path;
            return;
          } catch (UnsatisfiedLinkError ex) {} 
        if (Platform.isMac()) {
          String orig, ext;
          if (path.endsWith("dylib")) {
            orig = "dylib";
            ext = "jnilib";
          } else {
            orig = "jnilib";
            ext = "dylib";
          } 
          path = path.substring(0, path.lastIndexOf(orig)) + ext;
          if ((new File(path)).exists())
            try {
              System.load(path);
              nativeLibraryPath = path;
              return;
            } catch (UnsatisfiedLinkError ex) {
              System.err.println("File found at " + path + " but not loadable: " + ex.getMessage());
            }  
        } 
      } 
    } 
    try {
      if (!Boolean.getBoolean("jna.nosys")) {
        System.loadLibrary(libName);
        return;
      } 
    } catch (UnsatisfiedLinkError e) {
      if (Boolean.getBoolean("jna.nounpack"))
        throw e; 
    } 
    if (!Boolean.getBoolean("jna.nounpack")) {
      loadNativeLibraryFromJar();
      return;
    } 
    throw new UnsatisfiedLinkError("Native jnidispatch library not found");
  }
  
  private static void loadNativeLibraryFromJar() {
    String libname = System.mapLibraryName("jnidispatch");
    String arch = System.getProperty("os.arch");
    String name = System.getProperty("os.name");
    String resourceName = getNativeLibraryResourcePath(Platform.getOSType(), arch, name) + "/" + libname;
    URL url = Native.class.getResource(resourceName);
    boolean unpacked = false;
    if (url == null && Platform.isMac() && resourceName.endsWith(".dylib")) {
      resourceName = resourceName.substring(0, resourceName.lastIndexOf(".dylib")) + ".jnilib";
      url = Native.class.getResource(resourceName);
    } 
    if (url == null)
      throw new UnsatisfiedLinkError("jnidispatch (" + resourceName + ") not found in resource path"); 
    File lib = null;
    if (url.getProtocol().toLowerCase().equals("file")) {
      try {
        lib = new File(new URI(url.toString()));
      } catch (URISyntaxException e) {
        lib = new File(url.getPath());
      } 
      if (!lib.exists())
        throw new Error("File URL " + url + " could not be properly decoded"); 
    } else {
      InputStream is = Native.class.getResourceAsStream(resourceName);
      if (is == null)
        throw new Error("Can't obtain jnidispatch InputStream"); 
      FileOutputStream fos = null;
      try {
        File dir = getTempDir();
        lib = File.createTempFile("jna", Platform.isWindows() ? ".dll" : null, dir);
        lib.deleteOnExit();
        fos = new FileOutputStream(lib);
        byte[] buf = new byte[1024];
        int count;
        while ((count = is.read(buf, 0, buf.length)) > 0)
          fos.write(buf, 0, count); 
        unpacked = true;
      } catch (IOException e) {
        throw new Error("Failed to create temporary file for jnidispatch library: " + e);
      } finally {
        try {
          is.close();
        } catch (IOException e) {}
        if (fos != null)
          try {
            fos.close();
          } catch (IOException e) {} 
      } 
    } 
    System.load(lib.getAbsolutePath());
    nativeLibraryPath = lib.getAbsolutePath();
    if (unpacked)
      deleteNativeLibrary(lib.getAbsolutePath()); 
  }
  
  private static final ThreadLocal lastError = new ThreadLocal() {
      protected synchronized Object initialValue() {
        return new Integer(0);
      }
    };
  
  public static int getLastError() {
    return ((Integer)lastError.get()).intValue();
  }
  
  static void updateLastError(int e) {
    lastError.set(new Integer(e));
  }
  
  public static Library synchronizedLibrary(final Library library) {
    Class cls = library.getClass();
    if (!Proxy.isProxyClass(cls))
      throw new IllegalArgumentException("Library must be a proxy class"); 
    InvocationHandler ih = Proxy.getInvocationHandler(library);
    if (!(ih instanceof Library.Handler))
      throw new IllegalArgumentException("Unrecognized proxy handler: " + ih); 
    final Library.Handler handler = (Library.Handler)ih;
    InvocationHandler newHandler = new InvocationHandler() {
        private final Library.Handler val$handler;
        
        private final Library val$library;
        
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
          synchronized (handler.getNativeLibrary()) {
            return handler.invoke(library, method, args);
          } 
        }
      };
    return (Library)Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), newHandler);
  }
  
  public static String getWebStartLibraryPath(String libName) {
    if (System.getProperty("javawebstart.version") == null)
      return null; 
    try {
      ClassLoader cl = Native.class.getClassLoader();
      Method m = AccessController.<Method>doPrivileged(new PrivilegedAction() {
            public Object run() {
              try {
                Method m = ((Native.class$java$lang$ClassLoader == null) ? (Native.class$java$lang$ClassLoader = Native.class$("java.lang.ClassLoader")) : Native.class$java$lang$ClassLoader).getDeclaredMethod("findLibrary", new Class[] { (Native.class$java$lang$String == null) ? (Native.class$java$lang$String = Native.class$("java.lang.String")) : Native.class$java$lang$String });
                m.setAccessible(true);
                return m;
              } catch (Exception e) {
                return null;
              } 
            }
          });
      String libpath = (String)m.invoke(cl, new Object[] { libName });
      if (libpath != null)
        return (new File(libpath)).getParent(); 
      return null;
    } catch (Exception e) {
      return null;
    } 
  }
  
  static void markTemporaryFile(File file) {
    try {
      File marker = new File(file.getParentFile(), file.getName() + ".x");
      marker.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  static File getTempDir() {
    File tmp = new File(System.getProperty("java.io.tmpdir"));
    File jnatmp = new File(tmp, "jna");
    jnatmp.mkdirs();
    return jnatmp.exists() ? jnatmp : tmp;
  }
  
  static void removeTemporaryFiles() {
    File dir = getTempDir();
    FilenameFilter filter = new FilenameFilter() {
        public boolean accept(File dir, String name) {
          return (name.endsWith(".x") && name.indexOf("jna") != -1);
        }
      };
    File[] files = dir.listFiles(filter);
    for (int i = 0; files != null && i < files.length; i++) {
      File marker = files[i];
      String name = marker.getName();
      name = name.substring(0, name.length() - 2);
      File target = new File(marker.getParentFile(), name);
      if (!target.exists() || target.delete())
        marker.delete(); 
    } 
  }
  
  public static int getNativeSize(Class type, Object value) {
    if (type.isArray()) {
      int len = Array.getLength(value);
      if (len > 0) {
        Object o = Array.get(value, 0);
        return len * getNativeSize(type.getComponentType(), o);
      } 
      throw new IllegalArgumentException("Arrays of length zero not allowed: " + type);
    } 
    if (Structure.class.isAssignableFrom(type) && !Structure.ByReference.class.isAssignableFrom(type)) {
      if (value == null)
        value = Structure.newInstance(type); 
      return ((Structure)value).size();
    } 
    try {
      return getNativeSize(type);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("The type \"" + type.getName() + "\" is not supported: " + e.getMessage());
    } 
  }
  
  public static int getNativeSize(Class cls) {
    if (NativeMapped.class.isAssignableFrom(cls))
      cls = NativeMappedConverter.getInstance(cls).nativeType(); 
    if (cls == boolean.class || cls == Boolean.class)
      return 4; 
    if (cls == byte.class || cls == Byte.class)
      return 1; 
    if (cls == short.class || cls == Short.class)
      return 2; 
    if (cls == char.class || cls == Character.class)
      return WCHAR_SIZE; 
    if (cls == int.class || cls == Integer.class)
      return 4; 
    if (cls == long.class || cls == Long.class)
      return 8; 
    if (cls == float.class || cls == Float.class)
      return 4; 
    if (cls == double.class || cls == Double.class)
      return 8; 
    if (Structure.class.isAssignableFrom(cls)) {
      if (Structure.ByValue.class.isAssignableFrom(cls))
        return Structure.newInstance(cls).size(); 
      return POINTER_SIZE;
    } 
    if (Pointer.class.isAssignableFrom(cls) || (Platform.HAS_BUFFERS && Buffers.isBuffer(cls)) || Callback.class.isAssignableFrom(cls) || String.class == cls || WString.class == cls)
      return POINTER_SIZE; 
    throw new IllegalArgumentException("Native size for type \"" + cls.getName() + "\" is unknown");
  }
  
  public static boolean isSupportedNativeType(Class cls) {
    if (Structure.class.isAssignableFrom(cls))
      return true; 
    try {
      return (getNativeSize(cls) != 0);
    } catch (IllegalArgumentException e) {
      return false;
    } 
  }
  
  public static void setCallbackExceptionHandler(Callback.UncaughtExceptionHandler eh) {
    callbackExceptionHandler = (eh == null) ? DEFAULT_HANDLER : eh;
  }
  
  public static Callback.UncaughtExceptionHandler getCallbackExceptionHandler() {
    return callbackExceptionHandler;
  }
  
  public static void register(String libName) {
    register(getNativeClass(getCallingClass()), NativeLibrary.getInstance(libName));
  }
  
  public static void register(NativeLibrary lib) {
    register(getNativeClass(getCallingClass()), lib);
  }
  
  static Class getNativeClass(Class cls) {
    Method[] methods = cls.getDeclaredMethods();
    for (int i = 0; i < methods.length; i++) {
      if ((methods[i].getModifiers() & 0x100) != 0)
        return cls; 
    } 
    int idx = cls.getName().lastIndexOf("$");
    if (idx != -1) {
      String name = cls.getName().substring(0, idx);
      try {
        return getNativeClass(Class.forName(name, true, cls.getClassLoader()));
      } catch (ClassNotFoundException e) {}
    } 
    throw new IllegalArgumentException("Can't determine class with native methods from the current context (" + cls + ")");
  }
  
  static Class getCallingClass() {
    Class[] context = (new SecurityManager() {
        public Class[] getClassContext() {
          return super.getClassContext();
        }
      }).getClassContext();
    if (context.length < 4)
      throw new IllegalStateException("This method must be called from the static initializer of a class"); 
    return context[3];
  }
  
  public static void setCallbackThreadInitializer(Callback cb, CallbackThreadInitializer initializer) {
    CallbackReference.setCallbackThreadInitializer(cb, initializer);
  }
  
  private static Map registeredClasses = new HashMap();
  
  private static Map registeredLibraries = new HashMap();
  
  private static Object unloader = new Object() {
      protected void finalize() {
        synchronized (Native.registeredClasses) {
          for (Iterator i = Native.registeredClasses.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry e = i.next();
            Native.unregister((Class)e.getKey(), (long[])e.getValue());
            i.remove();
          } 
        } 
      }
    };
  
  static final int CB_HAS_INITIALIZER = 1;
  
  private static final int CVT_UNSUPPORTED = -1;
  
  private static final int CVT_DEFAULT = 0;
  
  private static final int CVT_POINTER = 1;
  
  private static final int CVT_STRING = 2;
  
  private static final int CVT_STRUCTURE = 3;
  
  private static final int CVT_STRUCTURE_BYVAL = 4;
  
  private static final int CVT_BUFFER = 5;
  
  private static final int CVT_ARRAY_BYTE = 6;
  
  private static final int CVT_ARRAY_SHORT = 7;
  
  private static final int CVT_ARRAY_CHAR = 8;
  
  private static final int CVT_ARRAY_INT = 9;
  
  private static final int CVT_ARRAY_LONG = 10;
  
  private static final int CVT_ARRAY_FLOAT = 11;
  
  private static final int CVT_ARRAY_DOUBLE = 12;
  
  private static final int CVT_ARRAY_BOOLEAN = 13;
  
  private static final int CVT_BOOLEAN = 14;
  
  private static final int CVT_CALLBACK = 15;
  
  private static final int CVT_FLOAT = 16;
  
  private static final int CVT_NATIVE_MAPPED = 17;
  
  private static final int CVT_WSTRING = 18;
  
  private static final int CVT_INTEGER_TYPE = 19;
  
  private static final int CVT_POINTER_TYPE = 20;
  
  private static final int CVT_TYPE_MAPPER = 21;
  
  static Class class$java$lang$ClassLoader;
  
  static Class class$java$nio$Buffer;
  
  public static void unregister() {
    unregister(getNativeClass(getCallingClass()));
  }
  
  public static void unregister(Class cls) {
    synchronized (registeredClasses) {
      if (registeredClasses.containsKey(cls)) {
        unregister(cls, (long[])registeredClasses.get(cls));
        registeredClasses.remove(cls);
        registeredLibraries.remove(cls);
      } 
    } 
  }
  
  private static String getSignature(Class cls) {
    if (cls.isArray())
      return "[" + getSignature(cls.getComponentType()); 
    if (cls.isPrimitive()) {
      if (cls == void.class)
        return "V"; 
      if (cls == boolean.class)
        return "Z"; 
      if (cls == byte.class)
        return "B"; 
      if (cls == short.class)
        return "S"; 
      if (cls == char.class)
        return "C"; 
      if (cls == int.class)
        return "I"; 
      if (cls == long.class)
        return "J"; 
      if (cls == float.class)
        return "F"; 
      if (cls == double.class)
        return "D"; 
    } 
    return "L" + replace(".", "/", cls.getName()) + ";";
  }
  
  static String replace(String s1, String s2, String str) {
    StringBuffer buf = new StringBuffer();
    while (true) {
      int idx = str.indexOf(s1);
      if (idx == -1) {
        buf.append(str);
        break;
      } 
      buf.append(str.substring(0, idx));
      buf.append(s2);
      str = str.substring(idx + s1.length());
    } 
    return buf.toString();
  }
  
  private static int getConversion(Class type, TypeMapper mapper) {
    Class clazz1;
    Class clazz;
    if (type == Boolean.class) {
      clazz1 = boolean.class;
    } else {
      Class clazz2;
      if (clazz1 == Byte.class) {
        clazz2 = byte.class;
      } else {
        Class clazz3;
        if (clazz2 == Short.class) {
          clazz3 = short.class;
        } else {
          Class clazz4;
          if (clazz3 == Character.class) {
            clazz4 = char.class;
          } else {
            Class clazz5;
            if (clazz4 == Integer.class) {
              clazz5 = int.class;
            } else {
              Class clazz6;
              if (clazz5 == Long.class) {
                clazz6 = long.class;
              } else {
                Class clazz7;
                if (clazz6 == Float.class) {
                  clazz7 = float.class;
                } else {
                  Class clazz8;
                  if (clazz7 == Double.class) {
                    clazz8 = double.class;
                  } else if (clazz8 == Void.class) {
                    clazz = void.class;
                  } 
                } 
              } 
            } 
          } 
        } 
      } 
    } 
    if (mapper != null && (mapper.getFromNativeConverter(clazz) != null || mapper.getToNativeConverter(clazz) != null))
      return 21; 
    if (Pointer.class.isAssignableFrom(clazz))
      return 1; 
    if (String.class == clazz)
      return 2; 
    if (WString.class.isAssignableFrom(clazz))
      return 18; 
    if (Platform.HAS_BUFFERS && Buffers.isBuffer(clazz))
      return 5; 
    if (Structure.class.isAssignableFrom(clazz)) {
      if (Structure.ByValue.class.isAssignableFrom(clazz))
        return 4; 
      return 3;
    } 
    if (clazz.isArray())
      switch (clazz.getName().charAt(1)) {
        case 'Z':
          return 13;
        case 'B':
          return 6;
        case 'S':
          return 7;
        case 'C':
          return 8;
        case 'I':
          return 9;
        case 'J':
          return 10;
        case 'F':
          return 11;
        case 'D':
          return 12;
      }  
    if (clazz.isPrimitive())
      return (clazz == boolean.class) ? 14 : 0; 
    if (Callback.class.isAssignableFrom(clazz))
      return 15; 
    if (IntegerType.class.isAssignableFrom(clazz))
      return 19; 
    if (PointerType.class.isAssignableFrom(clazz))
      return 20; 
    if (NativeMapped.class.isAssignableFrom(clazz))
      return 17; 
    return -1;
  }
  
  public static void register(Class cls, NativeLibrary lib) {
    Method[] methods = cls.getDeclaredMethods();
    List mlist = new ArrayList();
    TypeMapper mapper = (TypeMapper)lib.getOptions().get("type-mapper");
    for (int i = 0; i < methods.length; i++) {
      if ((methods[i].getModifiers() & 0x100) != 0)
        mlist.add(methods[i]); 
    } 
    long[] handles = new long[mlist.size()];
    for (int j = 0; j < handles.length; j++) {
      long rtype, closure_rtype;
      Method method = mlist.get(j);
      String sig = "(";
      Class rclass = method.getReturnType();
      Class[] ptypes = method.getParameterTypes();
      long[] atypes = new long[ptypes.length];
      long[] closure_atypes = new long[ptypes.length];
      int[] cvt = new int[ptypes.length];
      ToNativeConverter[] toNative = new ToNativeConverter[ptypes.length];
      FromNativeConverter fromNative = null;
      int rcvt = getConversion(rclass, mapper);
      boolean throwLastError = false;
      switch (rcvt) {
        case -1:
          throw new IllegalArgumentException(rclass + " is not a supported return type (in method " + method.getName() + " in " + cls + ")");
        case 21:
          fromNative = mapper.getFromNativeConverter(rclass);
          closure_rtype = (Structure.FFIType.get(rclass)).peer;
          rtype = (Structure.FFIType.get(fromNative.nativeType())).peer;
          break;
        case 17:
        case 19:
        case 20:
          closure_rtype = (Structure.FFIType.get(Pointer.class)).peer;
          rtype = (Structure.FFIType.get(NativeMappedConverter.getInstance(rclass).nativeType())).peer;
          break;
        case 3:
          closure_rtype = rtype = (Structure.FFIType.get(Pointer.class)).peer;
          break;
        case 4:
          closure_rtype = (Structure.FFIType.get(Pointer.class)).peer;
          rtype = (Structure.FFIType.get(rclass)).peer;
          break;
        default:
          closure_rtype = rtype = (Structure.FFIType.get(rclass)).peer;
          break;
      } 
      for (int t = 0; t < ptypes.length; t++) {
        Class type = ptypes[t];
        sig = sig + getSignature(type);
        cvt[t] = getConversion(type, mapper);
        if (cvt[t] == -1)
          throw new IllegalArgumentException(type + " is not a supported argument type (in method " + method.getName() + " in " + cls + ")"); 
        if (cvt[t] == 17 || cvt[t] == 19) {
          type = NativeMappedConverter.getInstance(type).nativeType();
        } else if (cvt[t] == 21) {
          toNative[t] = mapper.getToNativeConverter(type);
        } 
        switch (cvt[t]) {
          case 4:
          case 17:
          case 19:
          case 20:
            atypes[t] = (Structure.FFIType.get(type)).peer;
            closure_atypes[t] = (Structure.FFIType.get(Pointer.class)).peer;
            break;
          case 21:
            if (type.isPrimitive()) {
              closure_atypes[t] = (Structure.FFIType.get(type)).peer;
            } else {
              closure_atypes[t] = (Structure.FFIType.get(Pointer.class)).peer;
            } 
            atypes[t] = (Structure.FFIType.get(toNative[t].nativeType())).peer;
            break;
          case 0:
            atypes[t] = (Structure.FFIType.get(type)).peer;
            closure_atypes[t] = (Structure.FFIType.get(type)).peer;
          default:
            atypes[t] = (Structure.FFIType.get(Pointer.class)).peer;
            closure_atypes[t] = (Structure.FFIType.get(Pointer.class)).peer;
            break;
        } 
      } 
      sig = sig + ")";
      sig = sig + getSignature(rclass);
      Class[] etypes = method.getExceptionTypes();
      for (int e = 0; e < etypes.length; e++) {
        if (LastErrorException.class.isAssignableFrom(etypes[e])) {
          throwLastError = true;
          break;
        } 
      } 
      String name = method.getName();
      FunctionMapper fmapper = (FunctionMapper)lib.getOptions().get("function-mapper");
      if (fmapper != null)
        name = fmapper.getFunctionName(lib, method); 
      Function f = lib.getFunction(name, method);
      try {
        handles[j] = registerMethod(cls, method.getName(), sig, cvt, closure_atypes, atypes, rcvt, closure_rtype, rtype, rclass, f.peer, f.getCallingConvention(), throwLastError, toNative, fromNative);
      } catch (NoSuchMethodError noSuchMethodError) {
        throw new UnsatisfiedLinkError("No method " + method.getName() + " with signature " + sig + " in " + cls);
      } 
    } 
    synchronized (registeredClasses) {
      registeredClasses.put(cls, handles);
      registeredLibraries.put(cls, lib);
    } 
    cacheOptions(cls, lib.getOptions(), null);
  }
  
  private static void cacheOptions(Class cls, Map libOptions, Object proxy) {
    synchronized (libraries) {
      if (!libOptions.isEmpty())
        options.put(cls, libOptions); 
      if (libOptions.containsKey("type-mapper"))
        typeMappers.put(cls, libOptions.get("type-mapper")); 
      if (libOptions.containsKey("structure-alignment"))
        alignments.put(cls, libOptions.get("structure-alignment")); 
      if (proxy != null)
        libraries.put(cls, new WeakReference(proxy)); 
      if (!cls.isInterface() && Library.class.isAssignableFrom(cls)) {
        Class[] ifaces = cls.getInterfaces();
        for (int i = 0; i < ifaces.length; i++) {
          if (Library.class.isAssignableFrom(ifaces[i])) {
            cacheOptions(ifaces[i], libOptions, proxy);
            break;
          } 
        } 
      } 
    } 
  }
  
  private static NativeMapped fromNative(Class cls, Object value) {
    return (NativeMapped)NativeMappedConverter.getInstance(cls).fromNative(value, new FromNativeContext(cls));
  }
  
  private static Class nativeType(Class cls) {
    return NativeMappedConverter.getInstance(cls).nativeType();
  }
  
  private static Object toNative(ToNativeConverter cvt, Object o) {
    return cvt.toNative(o, new ToNativeContext());
  }
  
  private static Object fromNative(FromNativeConverter cvt, Object o, Class cls) {
    return cvt.fromNative(o, new FromNativeContext(cls));
  }
  
  public static void main(String[] args) {
    String DEFAULT_TITLE = "Java Native Access (JNA)";
    String DEFAULT_VERSION = "3.4.0";
    String DEFAULT_BUILD = "3.4.0 (package information missing)";
    Package pkg = Native.class.getPackage();
    String title = (pkg != null) ? pkg.getSpecificationTitle() : "Java Native Access (JNA)";
    if (title == null)
      title = "Java Native Access (JNA)"; 
    String version = (pkg != null) ? pkg.getSpecificationVersion() : "3.4.0";
    if (version == null)
      version = "3.4.0"; 
    title = title + " API Version " + version;
    System.out.println(title);
    version = (pkg != null) ? pkg.getImplementationVersion() : "3.4.0 (package information missing)";
    if (version == null)
      version = "3.4.0 (package information missing)"; 
    System.out.println("Version: " + version);
    System.out.println(" Native: " + getNativeVersion() + " (" + getAPIChecksum() + ")");
    System.exit(0);
  }
  
  static Structure invokeStructure(long fp, int callFlags, Object[] args, Structure s) {
    invokeStructure(fp, callFlags, args, (s.getPointer()).peer, (s.getTypeInfo()).peer);
    return s;
  }
  
  static Pointer getPointer(long addr) {
    long peer = _getPointer(addr);
    return (peer == 0L) ? null : new Pointer(peer);
  }
  
  public static void detach(boolean detach) {
    setLastError(detach ? -1 : -2);
  }
  
  private static native void initIDs();
  
  public static synchronized native void setProtected(boolean paramBoolean);
  
  public static synchronized native boolean isProtected();
  
  public static synchronized native void setPreserveLastError(boolean paramBoolean);
  
  public static synchronized native boolean getPreserveLastError();
  
  static native long getWindowHandle0(Component paramComponent);
  
  private static native long _getDirectBufferPointer(Buffer paramBuffer);
  
  private static native int sizeof(int paramInt);
  
  private static native String getNativeVersion();
  
  private static native String getAPIChecksum();
  
  public static native void setLastError(int paramInt);
  
  private static native void unregister(Class paramClass, long[] paramArrayOflong);
  
  private static native long registerMethod(Class paramClass1, String paramString1, String paramString2, int[] paramArrayOfint, long[] paramArrayOflong1, long[] paramArrayOflong2, int paramInt1, long paramLong1, long paramLong2, Class paramClass2, long paramLong3, int paramInt2, boolean paramBoolean, ToNativeConverter[] paramArrayOfToNativeConverter, FromNativeConverter paramFromNativeConverter);
  
  public static native long ffi_prep_cif(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
  
  public static native void ffi_call(long paramLong1, long paramLong2, long paramLong3, long paramLong4);
  
  public static native long ffi_prep_closure(long paramLong, ffi_callback paramffi_callback);
  
  public static native void ffi_free_closure(long paramLong);
  
  static native int initialize_ffi_type(long paramLong);
  
  static synchronized native void freeNativeCallback(long paramLong);
  
  static synchronized native long createNativeCallback(Callback paramCallback, Method paramMethod, Class[] paramArrayOfClass, Class paramClass, int paramInt, boolean paramBoolean);
  
  static native int invokeInt(long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  static native long invokeLong(long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  static native void invokeVoid(long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  static native float invokeFloat(long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  static native double invokeDouble(long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  static native long invokePointer(long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  private static native void invokeStructure(long paramLong1, int paramInt, Object[] paramArrayOfObject, long paramLong2, long paramLong3);
  
  static native Object invokeObject(long paramLong, int paramInt, Object[] paramArrayOfObject);
  
  static native long open(String paramString);
  
  static native void close(long paramLong);
  
  static native long findSymbol(long paramLong, String paramString);
  
  static native long indexOf(long paramLong, byte paramByte);
  
  static native void read(long paramLong, byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  static native void read(long paramLong, short[] paramArrayOfshort, int paramInt1, int paramInt2);
  
  static native void read(long paramLong, char[] paramArrayOfchar, int paramInt1, int paramInt2);
  
  static native void read(long paramLong, int[] paramArrayOfint, int paramInt1, int paramInt2);
  
  static native void read(long paramLong, long[] paramArrayOflong, int paramInt1, int paramInt2);
  
  static native void read(long paramLong, float[] paramArrayOffloat, int paramInt1, int paramInt2);
  
  static native void read(long paramLong, double[] paramArrayOfdouble, int paramInt1, int paramInt2);
  
  static native void write(long paramLong, byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  static native void write(long paramLong, short[] paramArrayOfshort, int paramInt1, int paramInt2);
  
  static native void write(long paramLong, char[] paramArrayOfchar, int paramInt1, int paramInt2);
  
  static native void write(long paramLong, int[] paramArrayOfint, int paramInt1, int paramInt2);
  
  static native void write(long paramLong, long[] paramArrayOflong, int paramInt1, int paramInt2);
  
  static native void write(long paramLong, float[] paramArrayOffloat, int paramInt1, int paramInt2);
  
  static native void write(long paramLong, double[] paramArrayOfdouble, int paramInt1, int paramInt2);
  
  static native byte getByte(long paramLong);
  
  static native char getChar(long paramLong);
  
  static native short getShort(long paramLong);
  
  static native int getInt(long paramLong);
  
  static native long getLong(long paramLong);
  
  static native float getFloat(long paramLong);
  
  static native double getDouble(long paramLong);
  
  private static native long _getPointer(long paramLong);
  
  static native String getString(long paramLong, boolean paramBoolean);
  
  static native void setMemory(long paramLong1, long paramLong2, byte paramByte);
  
  static native void setByte(long paramLong, byte paramByte);
  
  static native void setShort(long paramLong, short paramShort);
  
  static native void setChar(long paramLong, char paramChar);
  
  static native void setInt(long paramLong, int paramInt);
  
  static native void setLong(long paramLong1, long paramLong2);
  
  static native void setFloat(long paramLong, float paramFloat);
  
  static native void setDouble(long paramLong, double paramDouble);
  
  static native void setPointer(long paramLong1, long paramLong2);
  
  static native void setString(long paramLong, String paramString, boolean paramBoolean);
  
  public static native long malloc(long paramLong);
  
  public static native void free(long paramLong);
  
  public static native ByteBuffer getDirectByteBuffer(long paramLong1, long paramLong2);
  
  private static class Buffers {
    static boolean isBuffer(Class cls) {
      return ((Native.class$java$nio$Buffer == null) ? (Native.class$java$nio$Buffer = Native.class$("java.nio.Buffer")) : Native.class$java$nio$Buffer).isAssignableFrom(cls);
    }
  }
  
  private static class AWT {
    static long getWindowID(Window w) throws HeadlessException {
      return getComponentID(w);
    }
    
    static long getComponentID(Object o) throws HeadlessException {
      if (GraphicsEnvironment.isHeadless())
        throw new HeadlessException("No native windows when headless"); 
      Component c = (Component)o;
      if (c.isLightweight())
        throw new IllegalArgumentException("Component must be heavyweight"); 
      if (!c.isDisplayable())
        throw new IllegalStateException("Component must be displayable"); 
      if (Platform.isX11() && System.getProperty("java.version").startsWith("1.4"))
        if (!c.isVisible())
          throw new IllegalStateException("Component must be visible");  
      return Native.getWindowHandle0(c);
    }
  }
  
  public static interface ffi_callback {
    void invoke(long param1Long1, long param1Long2, long param1Long3);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\Native.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */