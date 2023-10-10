/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class NativeLibrary {
    private long handle;
    private final String libraryName;
    private final String libraryPath;
    private final Map functions = new HashMap();
    final int callFlags;
    final Map options;
    private static final Map libraries = new HashMap();
    private static final Map searchPaths = Collections.synchronizedMap(new HashMap());
    private static final List librarySearchPath = new LinkedList();
    static /* synthetic */ Class class$com$sun$jna$LastErrorException;

    private static String functionKey(String name, int flags) {
        return name + "|" + flags;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private NativeLibrary(String libraryName, String libraryPath, long handle, Map options) {
        int callingConvention;
        this.libraryName = this.getLibraryName(libraryName);
        this.libraryPath = libraryPath;
        this.handle = handle;
        Object option = options.get("calling-convention");
        this.callFlags = callingConvention = option instanceof Integer ? (Integer)option : 0;
        this.options = options;
        if (Platform.isWindows() && "kernel32".equals(this.libraryName.toLowerCase())) {
            Map map = this.functions;
            synchronized (map) {
                Function f2 = new Function(this, "GetLastError", 1){

                    Object invoke(Object[] args, Class returnType, boolean b2) {
                        return new Integer(Native.getLastError());
                    }
                };
                this.functions.put(NativeLibrary.functionKey("GetLastError", this.callFlags), f2);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static NativeLibrary loadLibrary(String libraryName, Map options) {
        long handle;
        String libraryPath;
        block23: {
            List customPaths;
            LinkedList<String> searchPath = new LinkedList<String>();
            String webstartPath = Native.getWebStartLibraryPath(libraryName);
            if (webstartPath != null) {
                searchPath.add(webstartPath);
            }
            if ((customPaths = (List)searchPaths.get(libraryName)) != null) {
                List list = customPaths;
                synchronized (list) {
                    searchPath.addAll(0, customPaths);
                }
            }
            searchPath.addAll(NativeLibrary.initPaths("jna.library.path"));
            libraryPath = NativeLibrary.findLibraryPath(libraryName, searchPath);
            handle = 0L;
            try {
                handle = Native.open(libraryPath);
            }
            catch (UnsatisfiedLinkError e2) {
                searchPath.addAll(librarySearchPath);
            }
            try {
                if (handle == 0L && (handle = Native.open(libraryPath = NativeLibrary.findLibraryPath(libraryName, searchPath))) == 0L) {
                    throw new UnsatisfiedLinkError("Failed to load library '" + libraryName + "'");
                }
            }
            catch (UnsatisfiedLinkError e3) {
                if (Platform.isLinux()) {
                    libraryPath = NativeLibrary.matchLibrary(libraryName, searchPath);
                    if (libraryPath != null) {
                        try {
                            handle = Native.open(libraryPath);
                        }
                        catch (UnsatisfiedLinkError e2) {
                            e3 = e2;
                        }
                    }
                } else if (Platform.isMac() && !libraryName.endsWith(".dylib")) {
                    libraryPath = "/System/Library/Frameworks/" + libraryName + ".framework/" + libraryName;
                    if (new File(libraryPath).exists()) {
                        try {
                            handle = Native.open(libraryPath);
                        }
                        catch (UnsatisfiedLinkError e2) {
                            e3 = e2;
                        }
                    }
                } else if (Platform.isWindows()) {
                    libraryPath = NativeLibrary.findLibraryPath("lib" + libraryName, searchPath);
                    try {
                        handle = Native.open(libraryPath);
                    }
                    catch (UnsatisfiedLinkError e2) {
                        e3 = e2;
                    }
                }
                if (handle != 0L) break block23;
                throw new UnsatisfiedLinkError("Unable to load library '" + libraryName + "': " + e3.getMessage());
            }
        }
        return new NativeLibrary(libraryName, libraryPath, handle, options);
    }

    private String getLibraryName(String libraryName) {
        String suffix;
        int suffixStart;
        String simplified = libraryName;
        String BASE = "---";
        String template = NativeLibrary.mapLibraryName("---");
        int prefixEnd = template.indexOf("---");
        if (prefixEnd > 0 && simplified.startsWith(template.substring(0, prefixEnd))) {
            simplified = simplified.substring(prefixEnd);
        }
        if ((suffixStart = simplified.indexOf(suffix = template.substring(prefixEnd + "---".length()))) != -1) {
            simplified = simplified.substring(0, suffixStart);
        }
        return simplified;
    }

    public static final NativeLibrary getInstance(String libraryName) {
        return NativeLibrary.getInstance(libraryName, Collections.EMPTY_MAP);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static final NativeLibrary getInstance(String libraryName, Map options) {
        if ((options = new HashMap<String, Integer>(options)).get("calling-convention") == null) {
            options.put("calling-convention", new Integer(0));
        }
        if (Platform.isLinux() && "c".equals(libraryName)) {
            libraryName = null;
        }
        Map map = libraries;
        synchronized (map) {
            NativeLibrary library;
            WeakReference<NativeLibrary> ref = (WeakReference<NativeLibrary>)libraries.get(libraryName + options);
            NativeLibrary nativeLibrary = library = ref != null ? (NativeLibrary)ref.get() : null;
            if (library == null) {
                library = libraryName == null ? new NativeLibrary("<process>", null, Native.open(null), options) : NativeLibrary.loadLibrary(libraryName, options);
                ref = new WeakReference<NativeLibrary>(library);
                libraries.put(library.getName() + options, ref);
                File file = library.getFile();
                if (file != null) {
                    libraries.put(file.getAbsolutePath() + options, ref);
                    libraries.put(file.getName() + options, ref);
                }
            }
            return library;
        }
    }

    public static final synchronized NativeLibrary getProcess() {
        return NativeLibrary.getInstance(null);
    }

    public static final synchronized NativeLibrary getProcess(Map options) {
        return NativeLibrary.getInstance(null, options);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static final void addSearchPath(String libraryName, String path) {
        Map map = searchPaths;
        synchronized (map) {
            List<String> customPaths = (List<String>)searchPaths.get(libraryName);
            if (customPaths == null) {
                customPaths = Collections.synchronizedList(new LinkedList());
                searchPaths.put(libraryName, customPaths);
            }
            customPaths.add(path);
        }
    }

    public Function getFunction(String functionName) {
        return this.getFunction(functionName, this.callFlags);
    }

    Function getFunction(String name, Method method) {
        int flags = this.callFlags;
        Class<?>[] etypes = method.getExceptionTypes();
        for (int i2 = 0; i2 < etypes.length; ++i2) {
            if (!(class$com$sun$jna$LastErrorException == null ? NativeLibrary.class$("com.sun.jna.LastErrorException") : class$com$sun$jna$LastErrorException).isAssignableFrom(etypes[i2])) continue;
            flags |= 4;
        }
        return this.getFunction(name, flags);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Function getFunction(String functionName, int callFlags) {
        if (functionName == null) {
            throw new NullPointerException("Function name may not be null");
        }
        Map map = this.functions;
        synchronized (map) {
            String key = NativeLibrary.functionKey(functionName, callFlags);
            Function function = (Function)this.functions.get(key);
            if (function == null) {
                function = new Function(this, functionName, callFlags);
                this.functions.put(key, function);
            }
            return function;
        }
    }

    public Map getOptions() {
        return this.options;
    }

    public Pointer getGlobalVariableAddress(String symbolName) {
        try {
            return new Pointer(this.getSymbolAddress(symbolName));
        }
        catch (UnsatisfiedLinkError e2) {
            throw new UnsatisfiedLinkError("Error looking up '" + symbolName + "': " + e2.getMessage());
        }
    }

    long getSymbolAddress(String name) {
        if (this.handle == 0L) {
            throw new UnsatisfiedLinkError("Library has been unloaded");
        }
        return Native.findSymbol(this.handle, name);
    }

    public String toString() {
        return "Native Library <" + this.libraryPath + "@" + this.handle + ">";
    }

    public String getName() {
        return this.libraryName;
    }

    public File getFile() {
        if (this.libraryPath == null) {
            return null;
        }
        return new File(this.libraryPath);
    }

    protected void finalize() {
        this.dispose();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    static void disposeAll() {
        HashSet values;
        Map map = libraries;
        synchronized (map) {
            values = new HashSet(libraries.values());
        }
        Iterator i2 = values.iterator();
        while (i2.hasNext()) {
            WeakReference ref = (WeakReference)i2.next();
            NativeLibrary lib = (NativeLibrary)ref.get();
            if (lib == null) continue;
            lib.dispose();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void dispose() {
        Object object = libraries;
        synchronized (object) {
            Iterator i2 = libraries.values().iterator();
            while (i2.hasNext()) {
                WeakReference ref = (WeakReference)i2.next();
                if (ref.get() != this) continue;
                i2.remove();
            }
        }
        object = this;
        synchronized (object) {
            if (this.handle != 0L) {
                Native.close(this.handle);
                this.handle = 0L;
            }
        }
    }

    private static List initPaths(String key) {
        String value = System.getProperty(key, "");
        if ("".equals(value)) {
            return Collections.EMPTY_LIST;
        }
        StringTokenizer st = new StringTokenizer(value, File.pathSeparator);
        ArrayList<String> list = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            String path = st.nextToken();
            if ("".equals(path)) continue;
            list.add(path);
        }
        return list;
    }

    private static String findLibraryPath(String libName, List searchPath) {
        if (new File(libName).isAbsolute()) {
            return libName;
        }
        String name = NativeLibrary.mapLibraryName(libName);
        Iterator it = searchPath.iterator();
        while (it.hasNext()) {
            String path = (String)it.next();
            File file = new File(path, name);
            if (file.exists()) {
                return file.getAbsolutePath();
            }
            if (!Platform.isMac() || !name.endsWith(".dylib") || !(file = new File(path, name.substring(0, name.lastIndexOf(".dylib")) + ".jnilib")).exists()) continue;
            return file.getAbsolutePath();
        }
        return name;
    }

    private static String mapLibraryName(String libName) {
        if (Platform.isMac()) {
            if (libName.startsWith("lib") && (libName.endsWith(".dylib") || libName.endsWith(".jnilib"))) {
                return libName;
            }
            String name = System.mapLibraryName(libName);
            if (name.endsWith(".jnilib")) {
                return name.substring(0, name.lastIndexOf(".jnilib")) + ".dylib";
            }
            return name;
        }
        if (Platform.isLinux() ? NativeLibrary.isVersionedName(libName) || libName.endsWith(".so") : Platform.isWindows() && (libName.endsWith(".drv") || libName.endsWith(".dll"))) {
            return libName;
        }
        return System.mapLibraryName(libName);
    }

    private static boolean isVersionedName(String name) {
        int so;
        if (name.startsWith("lib") && (so = name.lastIndexOf(".so.")) != -1 && so + 4 < name.length()) {
            for (int i2 = so + 4; i2 < name.length(); ++i2) {
                char ch = name.charAt(i2);
                if (Character.isDigit(ch) || ch == '.') continue;
                return false;
            }
            return true;
        }
        return false;
    }

    static String matchLibrary(final String libName, List searchPath) {
        File lib = new File(libName);
        if (lib.isAbsolute()) {
            searchPath = Arrays.asList(lib.getParent());
        }
        FilenameFilter filter = new FilenameFilter(){

            public boolean accept(File dir, String filename) {
                return (filename.startsWith("lib" + libName + ".so") || filename.startsWith(libName + ".so") && libName.startsWith("lib")) && NativeLibrary.isVersionedName(filename);
            }
        };
        LinkedList<File> matches = new LinkedList<File>();
        Iterator<String> it = searchPath.iterator();
        while (it.hasNext()) {
            File[] files = new File(it.next()).listFiles(filter);
            if (files == null || files.length <= 0) continue;
            matches.addAll(Arrays.asList(files));
        }
        double bestVersion = -1.0;
        String bestMatch = null;
        Iterator it2 = matches.iterator();
        while (it2.hasNext()) {
            String path = ((File)it2.next()).getAbsolutePath();
            String ver = path.substring(path.lastIndexOf(".so.") + 4);
            double version = NativeLibrary.parseVersion(ver);
            if (!(version > bestVersion)) continue;
            bestVersion = version;
            bestMatch = path;
        }
        return bestMatch;
    }

    static double parseVersion(String ver) {
        double v2 = 0.0;
        double divisor = 1.0;
        int dot = ver.indexOf(".");
        while (ver != null) {
            String num;
            if (dot != -1) {
                num = ver.substring(0, dot);
                ver = ver.substring(dot + 1);
                dot = ver.indexOf(".");
            } else {
                num = ver;
                ver = null;
            }
            try {
                v2 += (double)Integer.parseInt(num) / divisor;
            }
            catch (NumberFormatException e2) {
                return 0.0;
            }
            divisor *= 100.0;
        }
        return v2;
    }

    static {
        if (Native.POINTER_SIZE == 0) {
            throw new Error("Native library not initialized");
        }
        String webstartPath = Native.getWebStartLibraryPath("jnidispatch");
        if (webstartPath != null) {
            librarySearchPath.add(webstartPath);
        }
        if (System.getProperty("jna.platform.library.path") == null && !Platform.isWindows()) {
            String platformPath = "";
            String sep = "";
            String archPath = "";
            if (Platform.isLinux() || Platform.isSolaris() || Platform.isFreeBSD()) {
                archPath = (Platform.isSolaris() ? "/" : "") + Pointer.SIZE * 8;
            }
            String[] paths = new String[]{"/usr/lib" + archPath, "/lib" + archPath, "/usr/lib", "/lib"};
            if (Platform.isLinux()) {
                String cpu = "";
                String kernel = "linux";
                String libc = "gnu";
                if (Platform.isIntel()) {
                    cpu = Platform.is64Bit() ? "x86_64" : "i386";
                } else if (Platform.isPPC()) {
                    cpu = Platform.is64Bit() ? "powerpc64" : "powerpc";
                } else if (Platform.isARM()) {
                    cpu = "arm";
                    libc = "gnueabi";
                }
                String multiArchPath = cpu + "-" + kernel + "-" + libc;
                paths = new String[]{"/usr/lib/" + multiArchPath, "/lib/" + multiArchPath, "/usr/lib" + archPath, "/lib" + archPath, "/usr/lib", "/lib"};
            }
            for (int i2 = 0; i2 < paths.length; ++i2) {
                File dir = new File(paths[i2]);
                if (!dir.exists() || !dir.isDirectory()) continue;
                platformPath = platformPath + sep + paths[i2];
                sep = File.pathSeparator;
            }
            if (!"".equals(platformPath)) {
                System.setProperty("jna.platform.library.path", platformPath);
            }
        }
        librarySearchPath.addAll(NativeLibrary.initPaths("jna.platform.library.path"));
    }
}

