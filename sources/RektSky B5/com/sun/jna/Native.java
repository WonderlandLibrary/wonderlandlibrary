/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.Callback;
import com.sun.jna.CallbackReference;
import com.sun.jna.CallbackThreadInitializer;
import com.sun.jna.FromNativeContext;
import com.sun.jna.FromNativeConverter;
import com.sun.jna.Function;
import com.sun.jna.FunctionMapper;
import com.sun.jna.IntegerType;
import com.sun.jna.Library;
import com.sun.jna.NativeLibrary;
import com.sun.jna.NativeMapped;
import com.sun.jna.NativeMappedConverter;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;
import com.sun.jna.ToNativeContext;
import com.sun.jna.ToNativeConverter;
import com.sun.jna.TypeMapper;
import com.sun.jna.WString;
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
import java.util.Map;
import java.util.StringTokenizer;
import java.util.WeakHashMap;

public final class Native {
    private static final String VERSION = "3.4.0";
    private static final String VERSION_NATIVE = "3.4.0";
    private static String nativeLibraryPath;
    private static Map typeMappers;
    private static Map alignments;
    private static Map options;
    private static Map libraries;
    private static final Callback.UncaughtExceptionHandler DEFAULT_HANDLER;
    private static Callback.UncaughtExceptionHandler callbackExceptionHandler;
    public static final int POINTER_SIZE;
    public static final int LONG_SIZE;
    public static final int WCHAR_SIZE;
    public static final int SIZE_T_SIZE;
    private static final int TYPE_VOIDP = 0;
    private static final int TYPE_LONG = 1;
    private static final int TYPE_WCHAR_T = 2;
    private static final int TYPE_SIZE_T = 3;
    private static final int THREAD_NOCHANGE = 0;
    private static final int THREAD_DETACH = -1;
    private static final int THREAD_LEAVE_ATTACHED = -2;
    private static final Object finalizer;
    private static final ThreadLocal lastError;
    private static Map registeredClasses;
    private static Map registeredLibraries;
    private static Object unloader;
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
    static /* synthetic */ Class class$java$lang$ClassLoader;
    static /* synthetic */ Class class$com$sun$jna$LastErrorException;
    static /* synthetic */ Class class$java$nio$Buffer;

    private static void dispose() {
        NativeLibrary.disposeAll();
        nativeLibraryPath = null;
    }

    private static boolean deleteNativeLibrary(String path) {
        File flib = new File(path);
        if (flib.delete()) {
            return true;
        }
        Native.markTemporaryFile(flib);
        return false;
    }

    private Native() {
    }

    private static native void initIDs();

    public static synchronized native void setProtected(boolean var0);

    public static synchronized native boolean isProtected();

    public static synchronized native void setPreserveLastError(boolean var0);

    public static synchronized native boolean getPreserveLastError();

    public static long getWindowID(Window w2) throws HeadlessException {
        return AWT.getWindowID(w2);
    }

    public static long getComponentID(Component c2) throws HeadlessException {
        return AWT.getComponentID(c2);
    }

    public static Pointer getWindowPointer(Window w2) throws HeadlessException {
        return new Pointer(AWT.getWindowID(w2));
    }

    public static Pointer getComponentPointer(Component c2) throws HeadlessException {
        return new Pointer(AWT.getComponentID(c2));
    }

    static native long getWindowHandle0(Component var0);

    public static Pointer getDirectBufferPointer(Buffer b2) {
        long peer = Native._getDirectBufferPointer(b2);
        return peer == 0L ? null : new Pointer(peer);
    }

    private static native long _getDirectBufferPointer(Buffer var0);

    public static String toString(byte[] buf) {
        return Native.toString(buf, System.getProperty("jna.encoding"));
    }

    public static String toString(byte[] buf, String encoding) {
        int term;
        String s2 = null;
        if (encoding != null) {
            try {
                s2 = new String(buf, encoding);
            }
            catch (UnsupportedEncodingException e2) {
                // empty catch block
            }
        }
        if (s2 == null) {
            s2 = new String(buf);
        }
        if ((term = s2.indexOf(0)) != -1) {
            s2 = s2.substring(0, term);
        }
        return s2;
    }

    public static String toString(char[] buf) {
        String s2 = new String(buf);
        int term = s2.indexOf(0);
        if (term != -1) {
            s2 = s2.substring(0, term);
        }
        return s2;
    }

    public static Object loadLibrary(Class interfaceClass) {
        return Native.loadLibrary(null, interfaceClass);
    }

    public static Object loadLibrary(Class interfaceClass, Map options) {
        return Native.loadLibrary(null, interfaceClass, options);
    }

    public static Object loadLibrary(String name, Class interfaceClass) {
        return Native.loadLibrary(name, interfaceClass, Collections.EMPTY_MAP);
    }

    public static Object loadLibrary(String name, Class interfaceClass, Map options) {
        Library.Handler handler = new Library.Handler(name, interfaceClass, options);
        ClassLoader loader = interfaceClass.getClassLoader();
        Library proxy = (Library)Proxy.newProxyInstance(loader, new Class[]{interfaceClass}, handler);
        Native.cacheOptions(interfaceClass, options, proxy);
        return proxy;
    }

    private static void loadLibraryInstance(Class cls) {
        if (cls != null && !libraries.containsKey(cls)) {
            try {
                Field[] fields = cls.getFields();
                for (int i2 = 0; i2 < fields.length; ++i2) {
                    Field field = fields[i2];
                    if (field.getType() != cls || !Modifier.isStatic(field.getModifiers())) continue;
                    libraries.put(cls, new WeakReference<Object>(field.get(null)));
                    break;
                }
            }
            catch (Exception e2) {
                throw new IllegalArgumentException("Could not access instance of " + cls + " (" + e2 + ")");
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static Class findEnclosingLibraryClass(Class cls) {
        Class<?> declaring;
        Class fromDeclaring;
        if (cls == null) {
            return null;
        }
        Map map = libraries;
        synchronized (map) {
            if (options.containsKey(cls)) {
                return cls;
            }
        }
        if (Library.class.isAssignableFrom(cls)) {
            return cls;
        }
        if (Callback.class.isAssignableFrom(cls)) {
            cls = CallbackReference.findCallbackClass(cls);
        }
        if ((fromDeclaring = Native.findEnclosingLibraryClass(declaring = cls.getDeclaringClass())) != null) {
            return fromDeclaring;
        }
        return Native.findEnclosingLibraryClass(cls.getSuperclass());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Map getLibraryOptions(Class type) {
        Map map = libraries;
        synchronized (map) {
            Class interfaceClass = Native.findEnclosingLibraryClass(type);
            if (interfaceClass != null) {
                Native.loadLibraryInstance(interfaceClass);
            } else {
                interfaceClass = type;
            }
            if (!options.containsKey(interfaceClass)) {
                try {
                    Field field = interfaceClass.getField("OPTIONS");
                    field.setAccessible(true);
                    options.put(interfaceClass, field.get(null));
                }
                catch (NoSuchFieldException e2) {
                }
                catch (Exception e3) {
                    throw new IllegalArgumentException("OPTIONS must be a public field of type java.util.Map (" + e3 + "): " + interfaceClass);
                }
            }
            return (Map)options.get(interfaceClass);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static TypeMapper getTypeMapper(Class cls) {
        Map map = libraries;
        synchronized (map) {
            Class interfaceClass = Native.findEnclosingLibraryClass(cls);
            if (interfaceClass != null) {
                Native.loadLibraryInstance(interfaceClass);
            } else {
                interfaceClass = cls;
            }
            if (!typeMappers.containsKey(interfaceClass)) {
                try {
                    Field field = interfaceClass.getField("TYPE_MAPPER");
                    field.setAccessible(true);
                    typeMappers.put(interfaceClass, field.get(null));
                }
                catch (NoSuchFieldException e2) {
                    Map options = Native.getLibraryOptions(cls);
                    if (options != null && options.containsKey("type-mapper")) {
                        typeMappers.put(interfaceClass, options.get("type-mapper"));
                    }
                }
                catch (Exception e3) {
                    throw new IllegalArgumentException("TYPE_MAPPER must be a public field of type " + TypeMapper.class.getName() + " (" + e3 + "): " + interfaceClass);
                }
            }
            return (TypeMapper)typeMappers.get(interfaceClass);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int getStructureAlignment(Class cls) {
        Map map = libraries;
        synchronized (map) {
            Integer value;
            Class interfaceClass = Native.findEnclosingLibraryClass(cls);
            if (interfaceClass != null) {
                Native.loadLibraryInstance(interfaceClass);
            } else {
                interfaceClass = cls;
            }
            if (!alignments.containsKey(interfaceClass)) {
                try {
                    Field field = interfaceClass.getField("STRUCTURE_ALIGNMENT");
                    field.setAccessible(true);
                    alignments.put(interfaceClass, field.get(null));
                }
                catch (NoSuchFieldException e2) {
                    Map options = Native.getLibraryOptions(interfaceClass);
                    if (options != null && options.containsKey("structure-alignment")) {
                        alignments.put(interfaceClass, options.get("structure-alignment"));
                    }
                }
                catch (Exception e3) {
                    throw new IllegalArgumentException("STRUCTURE_ALIGNMENT must be a public field of type int (" + e3 + "): " + interfaceClass);
                }
            }
            int n2 = (value = (Integer)alignments.get(interfaceClass)) != null ? value : 0;
            return n2;
        }
    }

    static byte[] getBytes(String s2) {
        try {
            return Native.getBytes(s2, System.getProperty("jna.encoding"));
        }
        catch (UnsupportedEncodingException e2) {
            return s2.getBytes();
        }
    }

    static byte[] getBytes(String s2, String encoding) throws UnsupportedEncodingException {
        if (encoding != null) {
            return s2.getBytes(encoding);
        }
        return s2.getBytes();
    }

    public static byte[] toByteArray(String s2) {
        byte[] bytes = Native.getBytes(s2);
        byte[] buf = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, buf, 0, bytes.length);
        return buf;
    }

    public static byte[] toByteArray(String s2, String encoding) throws UnsupportedEncodingException {
        byte[] bytes = Native.getBytes(s2, encoding);
        byte[] buf = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, buf, 0, bytes.length);
        return buf;
    }

    public static char[] toCharArray(String s2) {
        char[] chars = s2.toCharArray();
        char[] buf = new char[chars.length + 1];
        System.arraycopy(chars, 0, buf, 0, chars.length);
        return buf;
    }

    static String getNativeLibraryResourcePath(int osType, String arch, String name) {
        String osPrefix;
        if ("powerpc".equals(arch = arch.toLowerCase())) {
            arch = "ppc";
        } else if ("powerpc64".equals(arch)) {
            arch = "ppc64";
        }
        switch (osType) {
            case 2: {
                if ("i386".equals(arch)) {
                    arch = "x86";
                }
                osPrefix = "win32-" + arch;
                break;
            }
            case 6: {
                osPrefix = "w32ce-" + arch;
                break;
            }
            case 0: {
                osPrefix = "darwin";
                break;
            }
            case 1: {
                if ("x86".equals(arch)) {
                    arch = "i386";
                } else if ("x86_64".equals(arch)) {
                    arch = "amd64";
                }
                osPrefix = "linux-" + arch;
                break;
            }
            case 3: {
                osPrefix = "sunos-" + arch;
                break;
            }
            default: {
                int space;
                osPrefix = name.toLowerCase();
                if ("x86".equals(arch)) {
                    arch = "i386";
                }
                if ("x86_64".equals(arch)) {
                    arch = "amd64";
                }
                if ((space = osPrefix.indexOf(" ")) != -1) {
                    osPrefix = osPrefix.substring(0, space);
                }
                osPrefix = osPrefix + "-" + arch;
            }
        }
        return "/com/sun/jna/" + osPrefix;
    }

    private static void loadNativeLibrary() {
        block13: {
            Native.removeTemporaryFiles();
            String libName = System.getProperty("jna.boot.library.name", "jnidispatch");
            String bootPath = System.getProperty("jna.boot.library.path");
            if (bootPath != null) {
                StringTokenizer dirs = new StringTokenizer(bootPath, File.pathSeparator);
                while (dirs.hasMoreTokens()) {
                    String ext;
                    String orig;
                    String dir = dirs.nextToken();
                    File file = new File(new File(dir), System.mapLibraryName(libName));
                    String path = file.getAbsolutePath();
                    if (file.exists()) {
                        try {
                            System.load(path);
                            nativeLibraryPath = path;
                            return;
                        }
                        catch (UnsatisfiedLinkError ex) {
                            // empty catch block
                        }
                    }
                    if (!Platform.isMac()) continue;
                    if (path.endsWith("dylib")) {
                        orig = "dylib";
                        ext = "jnilib";
                    } else {
                        orig = "jnilib";
                        ext = "dylib";
                    }
                    if (!new File(path = path.substring(0, path.lastIndexOf(orig)) + ext).exists()) continue;
                    try {
                        System.load(path);
                        nativeLibraryPath = path;
                        return;
                    }
                    catch (UnsatisfiedLinkError ex) {
                        System.err.println("File found at " + path + " but not loadable: " + ex.getMessage());
                    }
                }
            }
            try {
                if (!Boolean.getBoolean("jna.nosys")) {
                    System.loadLibrary(libName);
                    return;
                }
            }
            catch (UnsatisfiedLinkError e2) {
                if (!Boolean.getBoolean("jna.nounpack")) break block13;
                throw e2;
            }
        }
        if (!Boolean.getBoolean("jna.nounpack")) {
            Native.loadNativeLibraryFromJar();
            return;
        }
        throw new UnsatisfiedLinkError("Native jnidispatch library not found");
    }

    private static void loadNativeLibraryFromJar() {
        String libname = System.mapLibraryName("jnidispatch");
        String arch = System.getProperty("os.arch");
        String name = System.getProperty("os.name");
        String resourceName = Native.getNativeLibraryResourcePath(Platform.getOSType(), arch, name) + "/" + libname;
        URL url = Native.class.getResource(resourceName);
        boolean unpacked = false;
        if (url == null && Platform.isMac() && resourceName.endsWith(".dylib")) {
            resourceName = resourceName.substring(0, resourceName.lastIndexOf(".dylib")) + ".jnilib";
            url = Native.class.getResource(resourceName);
        }
        if (url == null) {
            throw new UnsatisfiedLinkError("jnidispatch (" + resourceName + ") not found in resource path");
        }
        File lib = null;
        if (url.getProtocol().toLowerCase().equals("file")) {
            try {
                lib = new File(new URI(url.toString()));
            }
            catch (URISyntaxException e2) {
                lib = new File(url.getPath());
            }
            if (!lib.exists()) {
                throw new Error("File URL " + url + " could not be properly decoded");
            }
        } else {
            InputStream is = Native.class.getResourceAsStream(resourceName);
            if (is == null) {
                throw new Error("Can't obtain jnidispatch InputStream");
            }
            FileOutputStream fos = null;
            try {
                int count;
                File dir = Native.getTempDir();
                lib = File.createTempFile("jna", Platform.isWindows() ? ".dll" : null, dir);
                lib.deleteOnExit();
                fos = new FileOutputStream(lib);
                byte[] buf = new byte[1024];
                while ((count = is.read(buf, 0, buf.length)) > 0) {
                    fos.write(buf, 0, count);
                }
                unpacked = true;
            }
            catch (IOException e3) {
                throw new Error("Failed to create temporary file for jnidispatch library: " + e3);
            }
            finally {
                try {
                    is.close();
                }
                catch (IOException e4) {}
                if (fos != null) {
                    try {
                        fos.close();
                    }
                    catch (IOException e5) {}
                }
            }
        }
        System.load(lib.getAbsolutePath());
        nativeLibraryPath = lib.getAbsolutePath();
        if (unpacked) {
            Native.deleteNativeLibrary(lib.getAbsolutePath());
        }
    }

    private static native int sizeof(int var0);

    private static native String getNativeVersion();

    private static native String getAPIChecksum();

    public static int getLastError() {
        return (Integer)lastError.get();
    }

    public static native void setLastError(int var0);

    static void updateLastError(int e2) {
        lastError.set(new Integer(e2));
    }

    public static Library synchronizedLibrary(final Library library) {
        Class<?> cls = library.getClass();
        if (!Proxy.isProxyClass(cls)) {
            throw new IllegalArgumentException("Library must be a proxy class");
        }
        InvocationHandler ih = Proxy.getInvocationHandler(library);
        if (!(ih instanceof Library.Handler)) {
            throw new IllegalArgumentException("Unrecognized proxy handler: " + ih);
        }
        final Library.Handler handler = (Library.Handler)ih;
        InvocationHandler newHandler = new InvocationHandler(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                NativeLibrary nativeLibrary = handler.getNativeLibrary();
                synchronized (nativeLibrary) {
                    return handler.invoke(library, method, args);
                }
            }
        };
        return (Library)Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), newHandler);
    }

    public static String getWebStartLibraryPath(String libName) {
        if (System.getProperty("javawebstart.version") == null) {
            return null;
        }
        try {
            ClassLoader cl = Native.class.getClassLoader();
            Method m2 = (Method)AccessController.doPrivileged(new PrivilegedAction(){

                public Object run() {
                    try {
                        Method m2 = (class$java$lang$ClassLoader == null ? (class$java$lang$ClassLoader = Native.class$("java.lang.ClassLoader")) : class$java$lang$ClassLoader).getDeclaredMethod("findLibrary", class$java$lang$String == null ? (class$java$lang$String = Native.class$("java.lang.String")) : class$java$lang$String);
                        m2.setAccessible(true);
                        return m2;
                    }
                    catch (Exception e2) {
                        return null;
                    }
                }
            });
            String libpath = (String)m2.invoke(cl, libName);
            if (libpath != null) {
                return new File(libpath).getParent();
            }
            return null;
        }
        catch (Exception e2) {
            return null;
        }
    }

    static void markTemporaryFile(File file) {
        try {
            File marker = new File(file.getParentFile(), file.getName() + ".x");
            marker.createNewFile();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    static File getTempDir() {
        File tmp = new File(System.getProperty("java.io.tmpdir"));
        File jnatmp = new File(tmp, "jna");
        jnatmp.mkdirs();
        return jnatmp.exists() ? jnatmp : tmp;
    }

    static void removeTemporaryFiles() {
        File dir = Native.getTempDir();
        FilenameFilter filter = new FilenameFilter(){

            public boolean accept(File dir, String name) {
                return name.endsWith(".x") && name.indexOf("jna") != -1;
            }
        };
        File[] files = dir.listFiles(filter);
        for (int i2 = 0; files != null && i2 < files.length; ++i2) {
            File marker = files[i2];
            String name = marker.getName();
            name = name.substring(0, name.length() - 2);
            File target = new File(marker.getParentFile(), name);
            if (target.exists() && !target.delete()) continue;
            marker.delete();
        }
    }

    public static int getNativeSize(Class type, Object value) {
        if (type.isArray()) {
            int len = Array.getLength(value);
            if (len > 0) {
                Object o2 = Array.get(value, 0);
                return len * Native.getNativeSize(type.getComponentType(), o2);
            }
            throw new IllegalArgumentException("Arrays of length zero not allowed: " + type);
        }
        if (Structure.class.isAssignableFrom(type) && !Structure.ByReference.class.isAssignableFrom(type)) {
            if (value == null) {
                value = Structure.newInstance(type);
            }
            return ((Structure)value).size();
        }
        try {
            return Native.getNativeSize(type);
        }
        catch (IllegalArgumentException e2) {
            throw new IllegalArgumentException("The type \"" + type.getName() + "\" is not supported: " + e2.getMessage());
        }
    }

    public static int getNativeSize(Class cls) {
        if (NativeMapped.class.isAssignableFrom(cls)) {
            cls = NativeMappedConverter.getInstance(cls).nativeType();
        }
        if (cls == Boolean.TYPE || cls == Boolean.class) {
            return 4;
        }
        if (cls == Byte.TYPE || cls == Byte.class) {
            return 1;
        }
        if (cls == Short.TYPE || cls == Short.class) {
            return 2;
        }
        if (cls == Character.TYPE || cls == Character.class) {
            return WCHAR_SIZE;
        }
        if (cls == Integer.TYPE || cls == Integer.class) {
            return 4;
        }
        if (cls == Long.TYPE || cls == Long.class) {
            return 8;
        }
        if (cls == Float.TYPE || cls == Float.class) {
            return 4;
        }
        if (cls == Double.TYPE || cls == Double.class) {
            return 8;
        }
        if (Structure.class.isAssignableFrom(cls)) {
            if (Structure.ByValue.class.isAssignableFrom(cls)) {
                return Structure.newInstance(cls).size();
            }
            return POINTER_SIZE;
        }
        if (Pointer.class.isAssignableFrom(cls) || Platform.HAS_BUFFERS && Buffers.isBuffer(cls) || Callback.class.isAssignableFrom(cls) || String.class == cls || WString.class == cls) {
            return POINTER_SIZE;
        }
        throw new IllegalArgumentException("Native size for type \"" + cls.getName() + "\" is unknown");
    }

    public static boolean isSupportedNativeType(Class cls) {
        if (Structure.class.isAssignableFrom(cls)) {
            return true;
        }
        try {
            return Native.getNativeSize(cls) != 0;
        }
        catch (IllegalArgumentException e2) {
            return false;
        }
    }

    public static void setCallbackExceptionHandler(Callback.UncaughtExceptionHandler eh) {
        callbackExceptionHandler = eh == null ? DEFAULT_HANDLER : eh;
    }

    public static Callback.UncaughtExceptionHandler getCallbackExceptionHandler() {
        return callbackExceptionHandler;
    }

    public static void register(String libName) {
        Native.register(Native.getNativeClass(Native.getCallingClass()), NativeLibrary.getInstance(libName));
    }

    public static void register(NativeLibrary lib) {
        Native.register(Native.getNativeClass(Native.getCallingClass()), lib);
    }

    static Class getNativeClass(Class cls) {
        Method[] methods = cls.getDeclaredMethods();
        for (int i2 = 0; i2 < methods.length; ++i2) {
            if ((methods[i2].getModifiers() & 0x100) == 0) continue;
            return cls;
        }
        int idx = cls.getName().lastIndexOf("$");
        if (idx != -1) {
            String name = cls.getName().substring(0, idx);
            try {
                return Native.getNativeClass(Class.forName(name, true, cls.getClassLoader()));
            }
            catch (ClassNotFoundException e2) {
                // empty catch block
            }
        }
        throw new IllegalArgumentException("Can't determine class with native methods from the current context (" + cls + ")");
    }

    static Class getCallingClass() {
        Class[] context = new SecurityManager(){

            public Class[] getClassContext() {
                return super.getClassContext();
            }
        }.getClassContext();
        if (context.length < 4) {
            throw new IllegalStateException("This method must be called from the static initializer of a class");
        }
        return context[3];
    }

    public static void setCallbackThreadInitializer(Callback cb, CallbackThreadInitializer initializer) {
        CallbackReference.setCallbackThreadInitializer(cb, initializer);
    }

    public static void unregister() {
        Native.unregister(Native.getNativeClass(Native.getCallingClass()));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void unregister(Class cls) {
        Map map = registeredClasses;
        synchronized (map) {
            if (registeredClasses.containsKey(cls)) {
                Native.unregister(cls, (long[])registeredClasses.get(cls));
                registeredClasses.remove(cls);
                registeredLibraries.remove(cls);
            }
        }
    }

    private static native void unregister(Class var0, long[] var1);

    private static String getSignature(Class cls) {
        if (cls.isArray()) {
            return "[" + Native.getSignature(cls.getComponentType());
        }
        if (cls.isPrimitive()) {
            if (cls == Void.TYPE) {
                return "V";
            }
            if (cls == Boolean.TYPE) {
                return "Z";
            }
            if (cls == Byte.TYPE) {
                return "B";
            }
            if (cls == Short.TYPE) {
                return "S";
            }
            if (cls == Character.TYPE) {
                return "C";
            }
            if (cls == Integer.TYPE) {
                return "I";
            }
            if (cls == Long.TYPE) {
                return "J";
            }
            if (cls == Float.TYPE) {
                return "F";
            }
            if (cls == Double.TYPE) {
                return "D";
            }
        }
        return "L" + Native.replace(".", "/", cls.getName()) + ";";
    }

    static String replace(String s1, String s2, String str) {
        StringBuffer buf = new StringBuffer();
        while (true) {
            int idx;
            if ((idx = str.indexOf(s1)) == -1) break;
            buf.append(str.substring(0, idx));
            buf.append(s2);
            str = str.substring(idx + s1.length());
        }
        buf.append(str);
        return buf.toString();
    }

    private static int getConversion(Class type, TypeMapper mapper) {
        if (type == Boolean.class) {
            type = Boolean.TYPE;
        } else if (type == Byte.class) {
            type = Byte.TYPE;
        } else if (type == Short.class) {
            type = Short.TYPE;
        } else if (type == Character.class) {
            type = Character.TYPE;
        } else if (type == Integer.class) {
            type = Integer.TYPE;
        } else if (type == Long.class) {
            type = Long.TYPE;
        } else if (type == Float.class) {
            type = Float.TYPE;
        } else if (type == Double.class) {
            type = Double.TYPE;
        } else if (type == Void.class) {
            type = Void.TYPE;
        }
        if (mapper != null && (mapper.getFromNativeConverter(type) != null || mapper.getToNativeConverter(type) != null)) {
            return 21;
        }
        if (Pointer.class.isAssignableFrom(type)) {
            return 1;
        }
        if (String.class == type) {
            return 2;
        }
        if (WString.class.isAssignableFrom(type)) {
            return 18;
        }
        if (Platform.HAS_BUFFERS && Buffers.isBuffer(type)) {
            return 5;
        }
        if (Structure.class.isAssignableFrom(type)) {
            if (Structure.ByValue.class.isAssignableFrom(type)) {
                return 4;
            }
            return 3;
        }
        if (type.isArray()) {
            switch (type.getName().charAt(1)) {
                case 'Z': {
                    return 13;
                }
                case 'B': {
                    return 6;
                }
                case 'S': {
                    return 7;
                }
                case 'C': {
                    return 8;
                }
                case 'I': {
                    return 9;
                }
                case 'J': {
                    return 10;
                }
                case 'F': {
                    return 11;
                }
                case 'D': {
                    return 12;
                }
            }
        }
        if (type.isPrimitive()) {
            return type == Boolean.TYPE ? 14 : 0;
        }
        if (Callback.class.isAssignableFrom(type)) {
            return 15;
        }
        if (IntegerType.class.isAssignableFrom(type)) {
            return 19;
        }
        if (PointerType.class.isAssignableFrom(type)) {
            return 20;
        }
        if (NativeMapped.class.isAssignableFrom(type)) {
            return 17;
        }
        return -1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void register(Class cls, NativeLibrary lib) {
        Method[] methods = cls.getDeclaredMethods();
        ArrayList<Method> mlist = new ArrayList<Method>();
        TypeMapper mapper = (TypeMapper)lib.getOptions().get("type-mapper");
        for (int i2 = 0; i2 < methods.length; ++i2) {
            if ((methods[i2].getModifiers() & 0x100) == 0) continue;
            mlist.add(methods[i2]);
        }
        long[] handles = new long[mlist.size()];
        for (int i3 = 0; i3 < handles.length; ++i3) {
            long rtype;
            long closure_rtype;
            Method method = (Method)mlist.get(i3);
            String sig = "(";
            Class<?> rclass = method.getReturnType();
            Class<?>[] ptypes = method.getParameterTypes();
            long[] atypes = new long[ptypes.length];
            long[] closure_atypes = new long[ptypes.length];
            int[] cvt = new int[ptypes.length];
            ToNativeConverter[] toNative = new ToNativeConverter[ptypes.length];
            FromNativeConverter fromNative = null;
            int rcvt = Native.getConversion(rclass, mapper);
            boolean throwLastError = false;
            switch (rcvt) {
                case -1: {
                    throw new IllegalArgumentException(rclass + " is not a supported return type (in method " + method.getName() + " in " + cls + ")");
                }
                case 21: {
                    fromNative = mapper.getFromNativeConverter(rclass);
                    closure_rtype = Structure.FFIType.get(rclass).peer;
                    rtype = Structure.FFIType.get((Object)fromNative.nativeType()).peer;
                    break;
                }
                case 17: 
                case 19: 
                case 20: {
                    closure_rtype = Structure.FFIType.get((Object)(Native.class$com$sun$jna$Pointer == null ? Native.class$((String)"com.sun.jna.Pointer") : Native.class$com$sun$jna$Pointer)).peer;
                    rtype = Structure.FFIType.get((Object)NativeMappedConverter.getInstance(rclass).nativeType()).peer;
                    break;
                }
                case 3: {
                    closure_rtype = rtype = Structure.FFIType.get((Object)(Native.class$com$sun$jna$Pointer == null ? Native.class$((String)"com.sun.jna.Pointer") : Native.class$com$sun$jna$Pointer)).peer;
                    break;
                }
                case 4: {
                    closure_rtype = Structure.FFIType.get((Object)(Native.class$com$sun$jna$Pointer == null ? Native.class$((String)"com.sun.jna.Pointer") : Native.class$com$sun$jna$Pointer)).peer;
                    rtype = Structure.FFIType.get(rclass).peer;
                    break;
                }
                default: {
                    closure_rtype = rtype = Structure.FFIType.get(rclass).peer;
                }
            }
            block19: for (int t2 = 0; t2 < ptypes.length; ++t2) {
                Class type = ptypes[t2];
                sig = sig + Native.getSignature(type);
                cvt[t2] = Native.getConversion(type, mapper);
                if (cvt[t2] == -1) {
                    throw new IllegalArgumentException(type + " is not a supported argument type (in method " + method.getName() + " in " + cls + ")");
                }
                if (cvt[t2] == 17 || cvt[t2] == 19) {
                    type = NativeMappedConverter.getInstance(type).nativeType();
                } else if (cvt[t2] == 21) {
                    toNative[t2] = mapper.getToNativeConverter(type);
                }
                switch (cvt[t2]) {
                    case 4: 
                    case 17: 
                    case 19: 
                    case 20: {
                        atypes[t2] = Structure.FFIType.get((Object)type).peer;
                        closure_atypes[t2] = Structure.FFIType.get((Object)(Native.class$com$sun$jna$Pointer == null ? Native.class$((String)"com.sun.jna.Pointer") : Native.class$com$sun$jna$Pointer)).peer;
                        continue block19;
                    }
                    case 21: {
                        closure_atypes[t2] = type.isPrimitive() ? Structure.FFIType.get((Object)type).peer : Structure.FFIType.get((Object)(Native.class$com$sun$jna$Pointer == null ? Native.class$((String)"com.sun.jna.Pointer") : Native.class$com$sun$jna$Pointer)).peer;
                        atypes[t2] = Structure.FFIType.get((Object)toNative[t2].nativeType()).peer;
                        continue block19;
                    }
                    case 0: {
                        closure_atypes[t2] = atypes[t2] = Structure.FFIType.get((Object)type).peer;
                        continue block19;
                    }
                    default: {
                        closure_atypes[t2] = atypes[t2] = Structure.FFIType.get((Object)(Native.class$com$sun$jna$Pointer == null ? Native.class$((String)"com.sun.jna.Pointer") : Native.class$com$sun$jna$Pointer)).peer;
                    }
                }
            }
            sig = sig + ")";
            sig = sig + Native.getSignature(rclass);
            Class<?>[] etypes = method.getExceptionTypes();
            for (int e2 = 0; e2 < etypes.length; ++e2) {
                if (!(class$com$sun$jna$LastErrorException == null ? Native.class$("com.sun.jna.LastErrorException") : class$com$sun$jna$LastErrorException).isAssignableFrom(etypes[e2])) continue;
                throwLastError = true;
                break;
            }
            String name = method.getName();
            FunctionMapper fmapper = (FunctionMapper)lib.getOptions().get("function-mapper");
            if (fmapper != null) {
                name = fmapper.getFunctionName(lib, method);
            }
            Function f2 = lib.getFunction(name, method);
            try {
                handles[i3] = Native.registerMethod(cls, method.getName(), sig, cvt, closure_atypes, atypes, rcvt, closure_rtype, rtype, rclass, f2.peer, f2.getCallingConvention(), throwLastError, toNative, fromNative);
                continue;
            }
            catch (NoSuchMethodError e3) {
                throw new UnsatisfiedLinkError("No method " + method.getName() + " with signature " + sig + " in " + cls);
            }
        }
        Map map = registeredClasses;
        synchronized (map) {
            registeredClasses.put(cls, handles);
            registeredLibraries.put(cls, lib);
        }
        Native.cacheOptions(cls, lib.getOptions(), null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void cacheOptions(Class cls, Map libOptions, Object proxy) {
        Map map = libraries;
        synchronized (map) {
            if (!libOptions.isEmpty()) {
                options.put(cls, libOptions);
            }
            if (libOptions.containsKey("type-mapper")) {
                typeMappers.put(cls, libOptions.get("type-mapper"));
            }
            if (libOptions.containsKey("structure-alignment")) {
                alignments.put(cls, libOptions.get("structure-alignment"));
            }
            if (proxy != null) {
                libraries.put(cls, new WeakReference<Object>(proxy));
            }
            if (!cls.isInterface() && Library.class.isAssignableFrom(cls)) {
                Class<?>[] ifaces = cls.getInterfaces();
                for (int i2 = 0; i2 < ifaces.length; ++i2) {
                    if (!(class$com$sun$jna$Library == null ? Native.class$("com.sun.jna.Library") : class$com$sun$jna$Library).isAssignableFrom(ifaces[i2])) continue;
                    Native.cacheOptions(ifaces[i2], libOptions, proxy);
                    break;
                }
            }
        }
    }

    private static native long registerMethod(Class var0, String var1, String var2, int[] var3, long[] var4, long[] var5, int var6, long var7, long var9, Class var11, long var12, int var14, boolean var15, ToNativeConverter[] var16, FromNativeConverter var17);

    private static NativeMapped fromNative(Class cls, Object value) {
        return (NativeMapped)NativeMappedConverter.getInstance(cls).fromNative(value, new FromNativeContext(cls));
    }

    private static Class nativeType(Class cls) {
        return NativeMappedConverter.getInstance(cls).nativeType();
    }

    private static Object toNative(ToNativeConverter cvt, Object o2) {
        return cvt.toNative(o2, new ToNativeContext());
    }

    private static Object fromNative(FromNativeConverter cvt, Object o2, Class cls) {
        return cvt.fromNative(o2, new FromNativeContext(cls));
    }

    public static native long ffi_prep_cif(int var0, int var1, long var2, long var4);

    public static native void ffi_call(long var0, long var2, long var4, long var6);

    public static native long ffi_prep_closure(long var0, ffi_callback var2);

    public static native void ffi_free_closure(long var0);

    static native int initialize_ffi_type(long var0);

    public static void main(String[] args) {
        String version;
        String title;
        String DEFAULT_TITLE = "Java Native Access (JNA)";
        String DEFAULT_VERSION = "3.4.0";
        String DEFAULT_BUILD = "3.4.0 (package information missing)";
        Package pkg = Native.class.getPackage();
        String string = title = pkg != null ? pkg.getSpecificationTitle() : "Java Native Access (JNA)";
        if (title == null) {
            title = "Java Native Access (JNA)";
        }
        String string2 = version = pkg != null ? pkg.getSpecificationVersion() : "3.4.0";
        if (version == null) {
            version = "3.4.0";
        }
        title = title + " API Version " + version;
        System.out.println(title);
        String string3 = version = pkg != null ? pkg.getImplementationVersion() : "3.4.0 (package information missing)";
        if (version == null) {
            version = "3.4.0 (package information missing)";
        }
        System.out.println("Version: " + version);
        System.out.println(" Native: " + Native.getNativeVersion() + " (" + Native.getAPIChecksum() + ")");
        System.exit(0);
    }

    static synchronized native void freeNativeCallback(long var0);

    static synchronized native long createNativeCallback(Callback var0, Method var1, Class[] var2, Class var3, int var4, boolean var5);

    static native int invokeInt(long var0, int var2, Object[] var3);

    static native long invokeLong(long var0, int var2, Object[] var3);

    static native void invokeVoid(long var0, int var2, Object[] var3);

    static native float invokeFloat(long var0, int var2, Object[] var3);

    static native double invokeDouble(long var0, int var2, Object[] var3);

    static native long invokePointer(long var0, int var2, Object[] var3);

    private static native void invokeStructure(long var0, int var2, Object[] var3, long var4, long var6);

    static Structure invokeStructure(long fp, int callFlags, Object[] args, Structure s2) {
        Native.invokeStructure(fp, callFlags, args, s2.getPointer().peer, s2.getTypeInfo().peer);
        return s2;
    }

    static native Object invokeObject(long var0, int var2, Object[] var3);

    static native long open(String var0);

    static native void close(long var0);

    static native long findSymbol(long var0, String var2);

    static native long indexOf(long var0, byte var2);

    static native void read(long var0, byte[] var2, int var3, int var4);

    static native void read(long var0, short[] var2, int var3, int var4);

    static native void read(long var0, char[] var2, int var3, int var4);

    static native void read(long var0, int[] var2, int var3, int var4);

    static native void read(long var0, long[] var2, int var3, int var4);

    static native void read(long var0, float[] var2, int var3, int var4);

    static native void read(long var0, double[] var2, int var3, int var4);

    static native void write(long var0, byte[] var2, int var3, int var4);

    static native void write(long var0, short[] var2, int var3, int var4);

    static native void write(long var0, char[] var2, int var3, int var4);

    static native void write(long var0, int[] var2, int var3, int var4);

    static native void write(long var0, long[] var2, int var3, int var4);

    static native void write(long var0, float[] var2, int var3, int var4);

    static native void write(long var0, double[] var2, int var3, int var4);

    static native byte getByte(long var0);

    static native char getChar(long var0);

    static native short getShort(long var0);

    static native int getInt(long var0);

    static native long getLong(long var0);

    static native float getFloat(long var0);

    static native double getDouble(long var0);

    static Pointer getPointer(long addr) {
        long peer = Native._getPointer(addr);
        return peer == 0L ? null : new Pointer(peer);
    }

    private static native long _getPointer(long var0);

    static native String getString(long var0, boolean var2);

    static native void setMemory(long var0, long var2, byte var4);

    static native void setByte(long var0, byte var2);

    static native void setShort(long var0, short var2);

    static native void setChar(long var0, char var2);

    static native void setInt(long var0, int var2);

    static native void setLong(long var0, long var2);

    static native void setFloat(long var0, float var2);

    static native void setDouble(long var0, double var2);

    static native void setPointer(long var0, long var2);

    static native void setString(long var0, String var2, boolean var3);

    public static native long malloc(long var0);

    public static native void free(long var0);

    public static native ByteBuffer getDirectByteBuffer(long var0, long var2);

    public static void detach(boolean detach) {
        Native.setLastError(detach ? -1 : -2);
    }

    static {
        String version;
        nativeLibraryPath = null;
        typeMappers = new WeakHashMap();
        alignments = new WeakHashMap();
        options = new WeakHashMap();
        libraries = new WeakHashMap();
        callbackExceptionHandler = DEFAULT_HANDLER = new Callback.UncaughtExceptionHandler(){

            public void uncaughtException(Callback c2, Throwable e2) {
                System.err.println("JNA: Callback " + c2 + " threw the following exception:");
                e2.printStackTrace();
            }
        };
        Native.loadNativeLibrary();
        POINTER_SIZE = Native.sizeof(0);
        LONG_SIZE = Native.sizeof(1);
        WCHAR_SIZE = Native.sizeof(2);
        SIZE_T_SIZE = Native.sizeof(3);
        Native.initIDs();
        if (Boolean.getBoolean("jna.protected")) {
            Native.setProtected(true);
        }
        if (!"3.4.0".equals(version = Native.getNativeVersion())) {
            String LS = System.getProperty("line.separator");
            throw new Error(LS + LS + "There is an incompatible JNA native library installed on this system." + LS + "To resolve this issue you may do one of the following:" + LS + " - remove or uninstall the offending library" + LS + " - set the system property jna.nosys=true" + LS + " - set jna.boot.library.path to include the path to the version of the " + LS + "   jnidispatch library included with the JNA jar file you are using" + LS);
        }
        Native.setPreserveLastError("true".equalsIgnoreCase(System.getProperty("jna.preserve_last_error", "true")));
        finalizer = new Object(){

            protected void finalize() {
                Native.dispose();
            }
        };
        lastError = new ThreadLocal(){

            protected synchronized Object initialValue() {
                return new Integer(0);
            }
        };
        registeredClasses = new HashMap();
        registeredLibraries = new HashMap();
        unloader = new Object(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            protected void finalize() {
                Map map = registeredClasses;
                synchronized (map) {
                    Iterator i2 = registeredClasses.entrySet().iterator();
                    while (i2.hasNext()) {
                        Map.Entry e2 = i2.next();
                        Native.unregister((Class)e2.getKey(), (long[])e2.getValue());
                        i2.remove();
                    }
                }
            }
        };
    }

    private static class AWT {
        private AWT() {
        }

        static long getWindowID(Window w2) throws HeadlessException {
            return AWT.getComponentID(w2);
        }

        static long getComponentID(Object o2) throws HeadlessException {
            if (GraphicsEnvironment.isHeadless()) {
                throw new HeadlessException("No native windows when headless");
            }
            Component c2 = (Component)o2;
            if (c2.isLightweight()) {
                throw new IllegalArgumentException("Component must be heavyweight");
            }
            if (!c2.isDisplayable()) {
                throw new IllegalStateException("Component must be displayable");
            }
            if (Platform.isX11() && System.getProperty("java.version").startsWith("1.4") && !c2.isVisible()) {
                throw new IllegalStateException("Component must be visible");
            }
            return Native.getWindowHandle0(c2);
        }
    }

    private static class Buffers {
        private Buffers() {
        }

        static boolean isBuffer(Class cls) {
            return (class$java$nio$Buffer == null ? (class$java$nio$Buffer = Native.class$("java.nio.Buffer")) : class$java$nio$Buffer).isAssignableFrom(cls);
        }
    }

    public static interface ffi_callback {
        public void invoke(long var1, long var3, long var5);
    }
}

