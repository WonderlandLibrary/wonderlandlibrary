/*
 * Decompiled with CFR 0.150.
 */
package io.netty.util.internal;

import io.netty.util.CharsetUtil;
import io.netty.util.internal.JavassistTypeParameterMatcherGenerator;
import io.netty.util.internal.PlatformDependent0;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.chmv8.ConcurrentHashMapV8;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PlatformDependent {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(PlatformDependent.class);
    private static final Pattern MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN = Pattern.compile("\\s*-XX:MaxDirectMemorySize\\s*=\\s*([0-9]+)\\s*([kKmMgG]?)\\s*$");
    private static final boolean IS_ANDROID = PlatformDependent.isAndroid0();
    private static final boolean IS_WINDOWS = PlatformDependent.isWindows0();
    private static final boolean IS_ROOT = PlatformDependent.isRoot0();
    private static final int JAVA_VERSION = PlatformDependent.javaVersion0();
    private static final boolean CAN_ENABLE_TCP_NODELAY_BY_DEFAULT = !PlatformDependent.isAndroid();
    private static final boolean HAS_UNSAFE = PlatformDependent.hasUnsafe0();
    private static final boolean CAN_USE_CHM_V8 = HAS_UNSAFE && JAVA_VERSION < 8;
    private static final boolean DIRECT_BUFFER_PREFERRED = HAS_UNSAFE && !SystemPropertyUtil.getBoolean("io.netty.noPreferDirect", false);
    private static final long MAX_DIRECT_MEMORY = PlatformDependent.maxDirectMemory0();
    private static final long ARRAY_BASE_OFFSET = PlatformDependent.arrayBaseOffset0();
    private static final boolean HAS_JAVASSIST = PlatformDependent.hasJavassist0();

    public static boolean isAndroid() {
        return IS_ANDROID;
    }

    public static boolean isWindows() {
        return IS_WINDOWS;
    }

    public static boolean isRoot() {
        return IS_ROOT;
    }

    public static int javaVersion() {
        return JAVA_VERSION;
    }

    public static boolean canEnableTcpNoDelayByDefault() {
        return CAN_ENABLE_TCP_NODELAY_BY_DEFAULT;
    }

    public static boolean hasUnsafe() {
        return HAS_UNSAFE;
    }

    public static boolean directBufferPreferred() {
        return DIRECT_BUFFER_PREFERRED;
    }

    public static long maxDirectMemory() {
        return MAX_DIRECT_MEMORY;
    }

    public static boolean hasJavassist() {
        return HAS_JAVASSIST;
    }

    public static void throwException(Throwable t) {
        if (PlatformDependent.hasUnsafe()) {
            PlatformDependent0.throwException(t);
        } else {
            PlatformDependent.throwException0(t);
        }
    }

    private static <E extends Throwable> void throwException0(Throwable t) throws E {
        throw t;
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap() {
        if (CAN_USE_CHM_V8) {
            return new ConcurrentHashMapV8();
        }
        return new ConcurrentHashMap();
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int initialCapacity) {
        if (CAN_USE_CHM_V8) {
            return new ConcurrentHashMapV8(initialCapacity);
        }
        return new ConcurrentHashMap(initialCapacity);
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int initialCapacity, float loadFactor) {
        if (CAN_USE_CHM_V8) {
            return new ConcurrentHashMapV8(initialCapacity, loadFactor);
        }
        return new ConcurrentHashMap(initialCapacity, loadFactor);
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        if (CAN_USE_CHM_V8) {
            return new ConcurrentHashMapV8(initialCapacity, loadFactor, concurrencyLevel);
        }
        return new ConcurrentHashMap(initialCapacity, loadFactor, concurrencyLevel);
    }

    public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(Map<? extends K, ? extends V> map) {
        if (CAN_USE_CHM_V8) {
            return new ConcurrentHashMapV8<K, V>(map);
        }
        return new ConcurrentHashMap<K, V>(map);
    }

    public static void freeDirectBuffer(ByteBuffer buffer) {
        if (buffer.isDirect()) {
            if (PlatformDependent.hasUnsafe()) {
                PlatformDependent0.freeDirectBufferUnsafe(buffer);
            } else {
                PlatformDependent0.freeDirectBuffer(buffer);
            }
        }
    }

    public static long directBufferAddress(ByteBuffer buffer) {
        return PlatformDependent0.directBufferAddress(buffer);
    }

    public static Object getObject(Object object, long fieldOffset) {
        return PlatformDependent0.getObject(object, fieldOffset);
    }

    public static int getInt(Object object, long fieldOffset) {
        return PlatformDependent0.getInt(object, fieldOffset);
    }

    public static long objectFieldOffset(Field field) {
        return PlatformDependent0.objectFieldOffset(field);
    }

    public static byte getByte(long address) {
        return PlatformDependent0.getByte(address);
    }

    public static short getShort(long address) {
        return PlatformDependent0.getShort(address);
    }

    public static int getInt(long address) {
        return PlatformDependent0.getInt(address);
    }

    public static long getLong(long address) {
        return PlatformDependent0.getLong(address);
    }

    public static void putByte(long address, byte value) {
        PlatformDependent0.putByte(address, value);
    }

    public static void putShort(long address, short value) {
        PlatformDependent0.putShort(address, value);
    }

    public static void putInt(long address, int value) {
        PlatformDependent0.putInt(address, value);
    }

    public static void putLong(long address, long value) {
        PlatformDependent0.putLong(address, value);
    }

    public static void copyMemory(long srcAddr, long dstAddr, long length) {
        PlatformDependent0.copyMemory(srcAddr, dstAddr, length);
    }

    public static void copyMemory(byte[] src, int srcIndex, long dstAddr, long length) {
        PlatformDependent0.copyMemory(src, ARRAY_BASE_OFFSET + (long)srcIndex, null, dstAddr, length);
    }

    public static void copyMemory(long srcAddr, byte[] dst, int dstIndex, long length) {
        PlatformDependent0.copyMemory(null, srcAddr, dst, ARRAY_BASE_OFFSET + (long)dstIndex, length);
    }

    private static boolean isAndroid0() {
        boolean android;
        try {
            Class.forName("android.app.Application", false, ClassLoader.getSystemClassLoader());
            android = true;
        }
        catch (Exception e) {
            android = false;
        }
        if (android) {
            logger.debug("Platform: Android");
        }
        return android;
    }

    private static boolean isWindows0() {
        boolean windows = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).contains("win");
        if (windows) {
            logger.debug("Platform: Windows");
        }
        return windows;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static boolean isRoot0() {
        if (PlatformDependent.isWindows()) {
            return false;
        }
        String[] ID_COMMANDS = new String[]{"/usr/bin/id", "/bin/id", "id", "/usr/xpg4/bin/id"};
        Pattern UID_PATTERN = Pattern.compile("^(?:0|[1-9][0-9]*)$");
        for (String idCmd : ID_COMMANDS) {
            Process p = null;
            BufferedReader in = null;
            String uid = null;
            try {
                p = Runtime.getRuntime().exec(new String[]{idCmd, "-u"});
                in = new BufferedReader(new InputStreamReader(p.getInputStream(), CharsetUtil.US_ASCII));
                uid = in.readLine();
                in.close();
                while (true) {
                    try {
                        int exitCode = p.waitFor();
                        if (exitCode != 0) {
                            uid = null;
                        }
                    }
                    catch (InterruptedException e) {
                        continue;
                    }
                    break;
                }
            }
            catch (Exception e) {
                uid = null;
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    }
                    catch (IOException e) {}
                }
                if (p != null) {
                    try {
                        p.destroy();
                    }
                    catch (Exception e) {}
                }
            }
            if (uid == null || !UID_PATTERN.matcher(uid).matches()) continue;
            logger.debug("UID: {}", (Object)uid);
            return "0".equals(uid);
        }
        logger.debug("Could not determine the current UID using /usr/bin/id; attempting to bind at privileged ports.");
        Pattern PERMISSION_DENIED = Pattern.compile(".*(?:denied|not.*permitted).*");
        for (int i = 1023; i > 0; --i) {
            ServerSocket ss = null;
            try {
                ss = new ServerSocket();
                ss.setReuseAddress(true);
                ss.bind(new InetSocketAddress(i));
                if (logger.isDebugEnabled()) {
                    logger.debug("UID: 0 (succeded to bind at port {})", (Object)i);
                }
                boolean idCmd = true;
                return idCmd;
            }
            catch (Exception e) {
                String message = e.getMessage();
                if (message == null) {
                    message = "";
                }
                if (!PERMISSION_DENIED.matcher(message = message.toLowerCase()).matches()) continue;
                break;
            }
            finally {
                if (ss != null) {
                    try {
                        ss.close();
                    }
                    catch (Exception e) {}
                }
            }
        }
        logger.debug("UID: non-root (failed to bind at any privileged ports)");
        return false;
    }

    private static int javaVersion0() {
        int javaVersion;
        if (PlatformDependent.isAndroid()) {
            javaVersion = 6;
        } else {
            try {
                Class.forName("java.time.Clock", false, Object.class.getClassLoader());
                javaVersion = 8;
            }
            catch (Exception e) {
                try {
                    Class.forName("java.util.concurrent.LinkedTransferQueue", false, BlockingQueue.class.getClassLoader());
                    javaVersion = 7;
                }
                catch (Exception exception) {
                    javaVersion = 6;
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Java version: {}", (Object)javaVersion);
        }
        return javaVersion;
    }

    private static boolean hasUnsafe0() {
        boolean noUnsafe = SystemPropertyUtil.getBoolean("io.netty.noUnsafe", false);
        logger.debug("-Dio.netty.noUnsafe: {}", (Object)noUnsafe);
        if (PlatformDependent.isAndroid()) {
            logger.debug("sun.misc.Unsafe: unavailable (Android)");
            return false;
        }
        if (noUnsafe) {
            logger.debug("sun.misc.Unsafe: unavailable (io.netty.noUnsafe)");
            return false;
        }
        boolean tryUnsafe = SystemPropertyUtil.contains("io.netty.tryUnsafe") ? SystemPropertyUtil.getBoolean("io.netty.tryUnsafe", true) : SystemPropertyUtil.getBoolean("org.jboss.netty.tryUnsafe", true);
        if (!tryUnsafe) {
            logger.debug("sun.misc.Unsafe: unavailable (io.netty.tryUnsafe/org.jboss.netty.tryUnsafe)");
            return false;
        }
        try {
            boolean hasUnsafe = PlatformDependent0.hasUnsafe();
            logger.debug("sun.misc.Unsafe: {}", (Object)(hasUnsafe ? "available" : "unavailable"));
            return hasUnsafe;
        }
        catch (Throwable t) {
            return false;
        }
    }

    private static long arrayBaseOffset0() {
        if (!PlatformDependent.hasUnsafe()) {
            return -1L;
        }
        return PlatformDependent0.arrayBaseOffset();
    }

    private static long maxDirectMemory0() {
        long maxDirectMemory = 0L;
        try {
            Class<?> vmClass = Class.forName("sun.misc.VM", true, ClassLoader.getSystemClassLoader());
            Method m = vmClass.getDeclaredMethod("maxDirectMemory", new Class[0]);
            maxDirectMemory = ((Number)m.invoke(null, new Object[0])).longValue();
        }
        catch (Throwable t) {
            // empty catch block
        }
        if (maxDirectMemory > 0L) {
            return maxDirectMemory;
        }
        try {
            Class<?> mgmtFactoryClass = Class.forName("java.lang.management.ManagementFactory", true, ClassLoader.getSystemClassLoader());
            Class<?> runtimeClass = Class.forName("java.lang.management.RuntimeMXBean", true, ClassLoader.getSystemClassLoader());
            Object runtime = mgmtFactoryClass.getDeclaredMethod("getRuntimeMXBean", new Class[0]).invoke(null, new Object[0]);
            List vmArgs = (List)runtimeClass.getDeclaredMethod("getInputArguments", new Class[0]).invoke(runtime, new Object[0]);
            for (int i = vmArgs.size() - 1; i >= 0; --i) {
                Matcher m = MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN.matcher((CharSequence)vmArgs.get(i));
                if (!m.matches()) continue;
                maxDirectMemory = Long.parseLong(m.group(1));
                switch (m.group(2).charAt(0)) {
                    case 'K': 
                    case 'k': {
                        maxDirectMemory *= 1024L;
                        break;
                    }
                    case 'M': 
                    case 'm': {
                        maxDirectMemory *= 0x100000L;
                        break;
                    }
                    case 'G': 
                    case 'g': {
                        maxDirectMemory *= 0x40000000L;
                    }
                }
                break;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        if (maxDirectMemory <= 0L) {
            maxDirectMemory = Runtime.getRuntime().maxMemory();
            logger.debug("maxDirectMemory: {} bytes (maybe)", (Object)maxDirectMemory);
        } else {
            logger.debug("maxDirectMemory: {} bytes", (Object)maxDirectMemory);
        }
        return maxDirectMemory;
    }

    private static boolean hasJavassist0() {
        if (PlatformDependent.isAndroid()) {
            return false;
        }
        boolean noJavassist = SystemPropertyUtil.getBoolean("io.netty.noJavassist", false);
        logger.debug("-Dio.netty.noJavassist: {}", (Object)noJavassist);
        if (noJavassist) {
            logger.debug("Javassist: unavailable (io.netty.noJavassist)");
            return false;
        }
        try {
            JavassistTypeParameterMatcherGenerator.generate(Object.class, PlatformDependent.class.getClassLoader());
            logger.debug("Javassist: available");
            return true;
        }
        catch (Throwable t) {
            logger.debug("Javassist: unavailable");
            logger.debug("You don't have Javassist in your class path or you don't have enough permission to load dynamically generated classes.  Please check the configuration for better performance.");
            return false;
        }
    }

    private PlatformDependent() {
    }

    static {
        if (logger.isDebugEnabled()) {
            logger.debug("-Dio.netty.noPreferDirect: {}", (Object)(!DIRECT_BUFFER_PREFERRED ? 1 : 0));
        }
        if (!PlatformDependent.hasUnsafe()) {
            logger.info("Your platform does not provide complete low-level API for accessing direct buffers reliably. Unless explicitly requested, heap buffer will always be preferred to avoid potential system unstability.");
        }
    }
}

