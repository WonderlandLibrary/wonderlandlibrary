/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import io.netty.util.CharsetUtil;
import io.netty.util.internal.JavassistTypeParameterMatcherGenerator;
import io.netty.util.internal.MpscLinkedQueue;
import io.netty.util.internal.PlatformDependent0;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.chmv8.ConcurrentHashMapV8;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PlatformDependent {
    private static final boolean IS_WINDOWS;
    private static final int BIT_MODE;
    private static final boolean CAN_ENABLE_TCP_NODELAY_BY_DEFAULT;
    private static final long MAX_DIRECT_MEMORY;
    private static final int JAVA_VERSION;
    private static final boolean DIRECT_BUFFER_PREFERRED;
    private static final boolean IS_ANDROID;
    private static final boolean HAS_UNSAFE;
    private static final File TMPDIR;
    private static final Pattern MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN;
    private static final boolean HAS_JAVASSIST;
    private static final InternalLogger logger;
    private static final int ADDRESS_SIZE;
    private static final long ARRAY_BASE_OFFSET;
    private static final boolean IS_ROOT;
    private static final boolean CAN_USE_CHM_V8;

    private static int bitMode0() {
        int n = SystemPropertyUtil.getInt("io.netty.bitMode", 0);
        if (n > 0) {
            logger.debug("-Dio.netty.bitMode: {}", (Object)n);
            return n;
        }
        n = SystemPropertyUtil.getInt("sun.arch.data.model", 0);
        if (n > 0) {
            logger.debug("-Dio.netty.bitMode: {} (sun.arch.data.model)", (Object)n);
            return n;
        }
        n = SystemPropertyUtil.getInt("com.ibm.vm.bitmode", 0);
        if (n > 0) {
            logger.debug("-Dio.netty.bitMode: {} (com.ibm.vm.bitmode)", (Object)n);
            return n;
        }
        String string = SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim();
        if ("amd64".equals(string) || "x86_64".equals(string)) {
            n = 64;
        } else if ("i386".equals(string) || "i486".equals(string) || "i586".equals(string) || "i686".equals(string)) {
            n = 32;
        }
        if (n > 0) {
            logger.debug("-Dio.netty.bitMode: {} (os.arch: {})", (Object)n, (Object)string);
        }
        String string2 = SystemPropertyUtil.get("java.vm.name", "").toLowerCase(Locale.US);
        Pattern pattern = Pattern.compile("([1-9][0-9]+)-?bit");
        Matcher matcher = pattern.matcher(string2);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 64;
    }

    public static Object getObjectVolatile(Object object, long l) {
        return PlatformDependent0.getObjectVolatile(object, l);
    }

    public static boolean hasUnsafe() {
        return HAS_UNSAFE;
    }

    private PlatformDependent() {
    }

    public static void putLong(long l, long l2) {
        PlatformDependent0.putLong(l, l2);
    }

    public static void freeMemory(long l) {
        PlatformDependent0.freeMemory(l);
    }

    public static void throwException(Throwable throwable) {
        if (PlatformDependent.hasUnsafe()) {
            PlatformDependent0.throwException(throwable);
        } else {
            PlatformDependent.throwException0(throwable);
        }
    }

    public static <U, W> AtomicReferenceFieldUpdater<U, W> newAtomicReferenceFieldUpdater(Class<U> clazz, String string) {
        if (PlatformDependent.hasUnsafe()) {
            try {
                return PlatformDependent0.newAtomicReferenceFieldUpdater(clazz, string);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        return null;
    }

    private static <E extends Throwable> void throwException0(Throwable throwable) throws E {
        throw throwable;
    }

    private static long arrayBaseOffset0() {
        if (!PlatformDependent.hasUnsafe()) {
            return -1L;
        }
        return PlatformDependent0.arrayBaseOffset();
    }

    private static boolean hasUnsafe0() {
        boolean bl = SystemPropertyUtil.getBoolean("io.netty.noUnsafe", false);
        logger.debug("-Dio.netty.noUnsafe: {}", (Object)bl);
        if (PlatformDependent.isAndroid()) {
            logger.debug("sun.misc.Unsafe: unavailable (Android)");
            return false;
        }
        if (bl) {
            logger.debug("sun.misc.Unsafe: unavailable (io.netty.noUnsafe)");
            return false;
        }
        boolean bl2 = SystemPropertyUtil.contains("io.netty.tryUnsafe") ? SystemPropertyUtil.getBoolean("io.netty.tryUnsafe", true) : SystemPropertyUtil.getBoolean("org.jboss.netty.tryUnsafe", true);
        if (!bl2) {
            logger.debug("sun.misc.Unsafe: unavailable (io.netty.tryUnsafe/org.jboss.netty.tryUnsafe)");
            return false;
        }
        try {
            boolean bl3 = PlatformDependent0.hasUnsafe();
            logger.debug("sun.misc.Unsafe: {}", (Object)(bl3 ? "available" : "unavailable"));
            return bl3;
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    public static File tmpdir() {
        return TMPDIR;
    }

    static {
        logger = InternalLoggerFactory.getInstance(PlatformDependent.class);
        MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN = Pattern.compile("\\s*-XX:MaxDirectMemorySize\\s*=\\s*([0-9]+)\\s*([kKmMgG]?)\\s*$");
        IS_ANDROID = PlatformDependent.isAndroid0();
        IS_WINDOWS = PlatformDependent.isWindows0();
        IS_ROOT = PlatformDependent.isRoot0();
        JAVA_VERSION = PlatformDependent.javaVersion0();
        CAN_ENABLE_TCP_NODELAY_BY_DEFAULT = !PlatformDependent.isAndroid();
        HAS_UNSAFE = PlatformDependent.hasUnsafe0();
        CAN_USE_CHM_V8 = HAS_UNSAFE && JAVA_VERSION < 8;
        DIRECT_BUFFER_PREFERRED = HAS_UNSAFE && !SystemPropertyUtil.getBoolean("io.netty.noPreferDirect", false);
        MAX_DIRECT_MEMORY = PlatformDependent.maxDirectMemory0();
        ARRAY_BASE_OFFSET = PlatformDependent.arrayBaseOffset0();
        HAS_JAVASSIST = PlatformDependent.hasJavassist0();
        TMPDIR = PlatformDependent.tmpdir0();
        BIT_MODE = PlatformDependent.bitMode0();
        ADDRESS_SIZE = PlatformDependent.addressSize0();
        if (logger.isDebugEnabled()) {
            logger.debug("-Dio.netty.noPreferDirect: {}", (Object)(!DIRECT_BUFFER_PREFERRED ? 1 : 0));
        }
        if (!PlatformDependent.hasUnsafe() && !PlatformDependent.isAndroid()) {
            logger.info("Your platform does not provide complete low-level API for accessing direct buffers reliably. Unless explicitly requested, heap buffer will always be preferred to avoid potential system unstability.");
        }
    }

    private static File tmpdir0() {
        File file;
        try {
            file = PlatformDependent.toDirectory(SystemPropertyUtil.get("io.netty.tmpdir"));
            if (file != null) {
                logger.debug("-Dio.netty.tmpdir: {}", (Object)file);
                return file;
            }
            file = PlatformDependent.toDirectory(SystemPropertyUtil.get("java.io.tmpdir"));
            if (file != null) {
                logger.debug("-Dio.netty.tmpdir: {} (java.io.tmpdir)", (Object)file);
                return file;
            }
            if (PlatformDependent.isWindows()) {
                file = PlatformDependent.toDirectory(System.getenv("TEMP"));
                if (file != null) {
                    logger.debug("-Dio.netty.tmpdir: {} (%TEMP%)", (Object)file);
                    return file;
                }
                String string = System.getenv("USERPROFILE");
                if (string != null) {
                    file = PlatformDependent.toDirectory(string + "\\AppData\\Local\\Temp");
                    if (file != null) {
                        logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\AppData\\Local\\Temp)", (Object)file);
                        return file;
                    }
                    file = PlatformDependent.toDirectory(string + "\\Local Settings\\Temp");
                    if (file != null) {
                        logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\Local Settings\\Temp)", (Object)file);
                        return file;
                    }
                }
            } else {
                file = PlatformDependent.toDirectory(System.getenv("TMPDIR"));
                if (file != null) {
                    logger.debug("-Dio.netty.tmpdir: {} ($TMPDIR)", (Object)file);
                    return file;
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        file = PlatformDependent.isWindows() ? new File("C:\\Windows\\Temp") : new File("/tmp");
        logger.warn("Failed to get the temporary directory; falling back to: {}", (Object)file);
        return file;
    }

    public static int bitMode() {
        return BIT_MODE;
    }

    public static byte getByte(long l) {
        return PlatformDependent0.getByte(l);
    }

    public static Object getObject(Object object, long l) {
        return PlatformDependent0.getObject(object, l);
    }

    public static short getShort(long l) {
        return PlatformDependent0.getShort(l);
    }

    public static void putOrderedObject(Object object, long l, Object object2) {
        PlatformDependent0.putOrderedObject(object, l, object2);
    }

    public static int getInt(long l) {
        return PlatformDependent0.getInt(l);
    }

    public static long objectFieldOffset(Field field) {
        return PlatformDependent0.objectFieldOffset(field);
    }

    private static boolean hasJavassist0() {
        if (PlatformDependent.isAndroid()) {
            return false;
        }
        boolean bl = SystemPropertyUtil.getBoolean("io.netty.noJavassist", false);
        logger.debug("-Dio.netty.noJavassist: {}", (Object)bl);
        if (bl) {
            logger.debug("Javassist: unavailable (io.netty.noJavassist)");
            return false;
        }
        try {
            JavassistTypeParameterMatcherGenerator.generate(Object.class, PlatformDependent.getClassLoader(PlatformDependent.class));
            logger.debug("Javassist: available");
            return true;
        }
        catch (Throwable throwable) {
            logger.debug("Javassist: unavailable");
            logger.debug("You don't have Javassist in your class path or you don't have enough permission to load dynamically generated classes.  Please check the configuration for better performance.");
            return false;
        }
    }

    public static void freeDirectBuffer(ByteBuffer byteBuffer) {
        if (PlatformDependent.hasUnsafe() && !PlatformDependent.isAndroid()) {
            PlatformDependent0.freeDirectBuffer(byteBuffer);
        }
    }

    public static boolean hasJavassist() {
        return HAS_JAVASSIST;
    }

    public static void putShort(long l, short s) {
        PlatformDependent0.putShort(l, s);
    }

    public static void copyMemory(byte[] byArray, int n, long l, long l2) {
        PlatformDependent0.copyMemory(byArray, ARRAY_BASE_OFFSET + (long)n, null, l, l2);
    }

    public static ClassLoader getSystemClassLoader() {
        return PlatformDependent0.getSystemClassLoader();
    }

    public static long directBufferAddress(ByteBuffer byteBuffer) {
        return PlatformDependent0.directBufferAddress(byteBuffer);
    }

    public static int javaVersion() {
        return JAVA_VERSION;
    }

    public static ClassLoader getClassLoader(Class<?> clazz) {
        return PlatformDependent0.getClassLoader(clazz);
    }

    private static long maxDirectMemory0() {
        GenericDeclaration genericDeclaration;
        Class<?> clazz;
        long l = 0L;
        try {
            clazz = Class.forName("sun.misc.VM", true, PlatformDependent.getSystemClassLoader());
            genericDeclaration = clazz.getDeclaredMethod("maxDirectMemory", new Class[0]);
            l = ((Number)((Method)genericDeclaration).invoke(null, new Object[0])).longValue();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (l > 0L) {
            return l;
        }
        try {
            clazz = Class.forName("java.lang.management.ManagementFactory", true, PlatformDependent.getSystemClassLoader());
            genericDeclaration = Class.forName("java.lang.management.RuntimeMXBean", true, PlatformDependent.getSystemClassLoader());
            Object object = clazz.getDeclaredMethod("getRuntimeMXBean", new Class[0]).invoke(null, new Object[0]);
            List list = (List)((Class)genericDeclaration).getDeclaredMethod("getInputArguments", new Class[0]).invoke(object, new Object[0]);
            for (int i = list.size() - 1; i >= 0; --i) {
                Matcher matcher = MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN.matcher((CharSequence)list.get(i));
                if (!matcher.matches()) continue;
                l = Long.parseLong(matcher.group(1));
                switch (matcher.group(2).charAt(0)) {
                    case 'K': 
                    case 'k': {
                        l *= 1024L;
                        break;
                    }
                    case 'M': 
                    case 'm': {
                        l *= 0x100000L;
                        break;
                    }
                    case 'G': 
                    case 'g': {
                        l *= 0x40000000L;
                    }
                }
                break;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (l <= 0L) {
            l = Runtime.getRuntime().maxMemory();
            logger.debug("maxDirectMemory: {} bytes (maybe)", (Object)l);
        } else {
            logger.debug("maxDirectMemory: {} bytes", (Object)l);
        }
        return l;
    }

    public static boolean isRoot() {
        return IS_ROOT;
    }

    public static boolean isAndroid() {
        return IS_ANDROID;
    }

    public static void copyMemory(long l, long l2, long l3) {
        PlatformDependent0.copyMemory(l, l2, l3);
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap() {
        if (CAN_USE_CHM_V8) {
            return new ConcurrentHashMapV8();
        }
        return new ConcurrentHashMap();
    }

    public static long maxDirectMemory() {
        return MAX_DIRECT_MEMORY;
    }

    private static int javaVersion0() {
        int n;
        if (PlatformDependent.isAndroid()) {
            n = 6;
        } else {
            try {
                Class.forName("java.time.Clock", false, PlatformDependent.getClassLoader(Object.class));
                n = 8;
            }
            catch (Exception exception) {
                try {
                    Class.forName("java.util.concurrent.LinkedTransferQueue", false, PlatformDependent.getClassLoader(BlockingQueue.class));
                    n = 7;
                }
                catch (Exception exception2) {
                    n = 6;
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Java version: {}", (Object)n);
        }
        return n;
    }

    public static long allocateMemory(long l) {
        return PlatformDependent0.allocateMemory(l);
    }

    public static int addressSize() {
        return ADDRESS_SIZE;
    }

    public static ClassLoader getContextClassLoader() {
        return PlatformDependent0.getContextClassLoader();
    }

    public static void copyMemory(long l, byte[] byArray, int n, long l2) {
        PlatformDependent0.copyMemory(null, l, byArray, ARRAY_BASE_OFFSET + (long)n, l2);
    }

    public static boolean isWindows() {
        return IS_WINDOWS;
    }

    private static boolean isAndroid0() {
        boolean bl;
        try {
            Class.forName("android.app.Application", false, PlatformDependent.getSystemClassLoader());
            bl = true;
        }
        catch (Exception exception) {
            bl = false;
        }
        if (bl) {
            logger.debug("Platform: Android");
        }
        return bl;
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int n, float f, int n2) {
        if (CAN_USE_CHM_V8) {
            return new ConcurrentHashMapV8(n, f, n2);
        }
        return new ConcurrentHashMap(n, f, n2);
    }

    public static <T> AtomicLongFieldUpdater<T> newAtomicLongFieldUpdater(Class<?> clazz, String string) {
        if (PlatformDependent.hasUnsafe()) {
            try {
                return PlatformDependent0.newAtomicLongFieldUpdater(clazz, string);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        return null;
    }

    private static File toDirectory(String string) {
        if (string == null) {
            return null;
        }
        File file = new File(string);
        file.mkdirs();
        if (!file.isDirectory()) {
            return null;
        }
        try {
            return file.getAbsoluteFile();
        }
        catch (Exception exception) {
            return file;
        }
    }

    public static boolean directBufferPreferred() {
        return DIRECT_BUFFER_PREFERRED;
    }

    private static boolean isWindows0() {
        boolean bl = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).contains("win");
        if (bl) {
            logger.debug("Platform: Windows");
        }
        return bl;
    }

    public static boolean canEnableTcpNoDelayByDefault() {
        return CAN_ENABLE_TCP_NODELAY_BY_DEFAULT;
    }

    public static void putByte(long l, byte by) {
        PlatformDependent0.putByte(l, by);
    }

    public static int getInt(Object object, long l) {
        return PlatformDependent0.getInt(object, l);
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(Map<? extends K, ? extends V> map) {
        if (CAN_USE_CHM_V8) {
            return new ConcurrentHashMapV8<K, V>(map);
        }
        return new ConcurrentHashMap<K, V>(map);
    }

    public static void putInt(long l, int n) {
        PlatformDependent0.putInt(l, n);
    }

    public static long getLong(long l) {
        return PlatformDependent0.getLong(l);
    }

    public static <T> AtomicIntegerFieldUpdater<T> newAtomicIntegerFieldUpdater(Class<?> clazz, String string) {
        if (PlatformDependent.hasUnsafe()) {
            try {
                return PlatformDependent0.newAtomicIntegerFieldUpdater(clazz, string);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static boolean isRoot0() {
        Object object;
        if (PlatformDependent.isWindows()) {
            return false;
        }
        String[] stringArray = new String[]{"/usr/bin/id", "/bin/id", "id", "/usr/xpg4/bin/id"};
        Pattern pattern = Pattern.compile("^(?:0|[1-9][0-9]*)$");
        for (String string : stringArray) {
            String string2;
            block34: {
                block33: {
                    object = null;
                    BufferedReader bufferedReader = null;
                    string2 = null;
                    try {
                        object = Runtime.getRuntime().exec(new String[]{string, "-u"});
                        bufferedReader = new BufferedReader(new InputStreamReader(((Process)object).getInputStream(), CharsetUtil.US_ASCII));
                        string2 = bufferedReader.readLine();
                        bufferedReader.close();
                        while (true) {
                            try {
                                int n = ((Process)object).waitFor();
                                if (n == 0) break;
                                string2 = null;
                            }
                            catch (InterruptedException interruptedException) {
                                continue;
                            }
                            break;
                        }
                        if (bufferedReader == null) break block33;
                    }
                    catch (Exception exception) {
                        string2 = null;
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            }
                            catch (IOException iOException) {
                                // empty catch block
                            }
                        }
                        if (object != null) {
                            try {
                                ((Process)object).destroy();
                            }
                            catch (Exception exception2) {
                                // empty catch block
                            }
                        }
                        break block34;
                    }
                    try {
                        bufferedReader.close();
                    }
                    catch (IOException iOException) {
                        // empty catch block
                    }
                }
                if (object != null) {
                    try {
                        ((Process)object).destroy();
                    }
                    catch (Exception exception) {}
                }
            }
            if (string2 == null || !pattern.matcher(string2).matches()) continue;
            logger.debug("UID: {}", (Object)string2);
            return "0".equals(string2);
        }
        logger.debug("Could not determine the current UID using /usr/bin/id; attempting to bind at privileged ports.");
        Pattern pattern2 = Pattern.compile(".*(?:denied|not.*permitted).*");
        for (int i = 1023; i > 0; --i) {
            boolean bl;
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket();
                serverSocket.setReuseAddress(true);
                serverSocket.bind(new InetSocketAddress(i));
                if (logger.isDebugEnabled()) {
                    logger.debug("UID: 0 (succeded to bind at port {})", (Object)i);
                }
                bl = true;
                if (serverSocket == null) return bl;
            }
            catch (Exception exception) {
                object = exception.getMessage();
                if (object == null) {
                    object = "";
                }
                if (pattern2.matcher((CharSequence)(object = ((String)object).toLowerCase())).matches()) {
                    if (serverSocket == null) break;
                    try {
                        serverSocket.close();
                    }
                    catch (Exception exception3) {}
                    break;
                }
                if (serverSocket == null) continue;
                try {
                    serverSocket.close();
                }
                catch (Exception exception4) {
                    // empty catch block
                }
                continue;
            }
            try {
                serverSocket.close();
                return bl;
            }
            catch (Exception exception) {
                // empty catch block
            }
            return bl;
        }
        logger.debug("UID: non-root (failed to bind at any privileged ports)");
        return false;
    }

    public static <T> Queue<T> newMpscQueue() {
        return new MpscLinkedQueue();
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int n, float f) {
        if (CAN_USE_CHM_V8) {
            return new ConcurrentHashMapV8(n, f);
        }
        return new ConcurrentHashMap(n, f);
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int n) {
        if (CAN_USE_CHM_V8) {
            return new ConcurrentHashMapV8(n);
        }
        return new ConcurrentHashMap(n);
    }

    private static int addressSize0() {
        if (!PlatformDependent.hasUnsafe()) {
            return -1;
        }
        return PlatformDependent0.addressSize();
    }
}

