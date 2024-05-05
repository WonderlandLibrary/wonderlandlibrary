package net.caffeinemc.phosphor.api.util;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;
import sun.misc.Unsafe;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadLocalRandom;

import static org.objectweb.asm.Opcodes.*;

public class MathUtils {
    public static int getRandomInt(int from, int to) {
        if (from >= to) return from;
        return ThreadLocalRandom.current().nextInt(from, to + 1);
    }

    public static double getRandomDouble(double from, double to) {
        if (from >= to) return from;
        return ThreadLocalRandom.current().nextDouble(from, to);
    }

    public static double getAverage(double int1, double int2) {
        return (int1 + int2) / 2;
    }

    public static boolean getRandomBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    public static final Unsafe unsafe;
    private static final String[] naughtyFlags = {
            "-XBootclasspath",
            "-javaagent",
            "-Xdebug",
            "-agentlib",
            "-Xrunjdwp",
            "-Xnoagent",
            "-verbose",
            "-DproxySet",
            "-DproxyHost",
            "-DproxyPort",
            "-Djavax.net.ssl.trustStore",
            "-Djavax.net.ssl.trustStorePassword"
    };
    private static Method findNative;
    private static ClassLoader classLoader;
    private static boolean ENABLE;

    /* UnsafeProvider */
    static {
        Unsafe ref;
        try {
            Class<?> clazz = Class.forName("sun.misc.Unsafe");
            Field theUnsafe = clazz.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            ref = (Unsafe) theUnsafe.get(null);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException e) {
            ref = null;
        }

        unsafe = ref;
    }

    /* CookieFuckery */
    public static void check() {
        if (!ENABLE) return;
        try {
            RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
            String jvmArgs = runtimeBean.getInputArguments().toString();
            for (String arg : naughtyFlags) {
                if (jvmArgs.contains(arg)) {
                    try {
                        unsafe.putAddress(0, 0);
                    } catch (Exception e) {
                    }
                    Error error = new Error();
                    error.setStackTrace(new StackTraceElement[]{});
                    throw error;
                }
            }
            try {
                byte[] bytes = createDummyClass("java/lang/instrument/Instrumentation");
            } catch (Throwable e) {
                try {
                    unsafe.putAddress(0, 0);
                } catch (Exception e1) {
                }
                Error error = new Error();
                error.setStackTrace(new StackTraceElement[]{});
                throw error;
            }
            if (isClassLoaded("sun.instrument.InstrumentationImpl")) {
                try {
                    unsafe.putAddress(0, 0);
                } catch (Exception e) {
                }
                Error error = new Error();
                error.setStackTrace(new StackTraceElement[]{});
                throw error;
            }
            byte[] bytes = createDummyClass("net/caffeinemc/phosphor/Main");
            System.setProperty("sun.jvm.hotspot.tools.jcore.filter", "net.caffeinemc.phosphor.Main");
            disassembleStruct();
        } catch (Throwable e) {
            try {
                unsafe.putAddress(0, 0);
            } catch (Exception e1) {
            }
            Error error = new Error();
            error.setStackTrace(new StackTraceElement[]{});
            throw error;
        }
    }

    private static boolean isClassLoaded(@SuppressWarnings("SameParameterValue") String clazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method m = ClassLoader.class.getDeclaredMethod("findLoadedClass", String.class);
        m.setAccessible(true);
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        ClassLoader scl = ClassLoader.getSystemClassLoader();
        return m.invoke(cl, clazz) != null || m.invoke(scl, clazz) != null;
    }


    /* DummyClassProvider */
    private static byte[] createDummyClass(String name) {
        ClassNode classNode = new ClassNode();
        classNode.name = name.replace('.', '/');
        classNode.access = ACC_PUBLIC;
        classNode.version = V1_8;
        classNode.superName = "java/lang/Object";

        List<MethodNode> methods = new ArrayList<>();
        MethodNode methodNode = new MethodNode(ACC_PUBLIC + ACC_STATIC, "<clinit>", "()V", null, null);

        InsnList insn = new InsnList();
        insn.add(new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
        insn.add(new LdcInsnNode("Nice try"));
        insn.add(new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));
        insn.add(new TypeInsnNode(NEW, "java/lang/Throwable"));
        insn.add(new InsnNode(DUP));
        insn.add(new LdcInsnNode("owned"));
        insn.add(new MethodInsnNode(INVOKESPECIAL, "java/lang/Throwable", "<init>", "(Ljava/lang/String;)V", false));
        insn.add(new InsnNode(ATHROW));

        methodNode.instructions = insn;

        methods.add(methodNode);
        classNode.methods = methods;

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    /* StructDissasembler */
    private static void resolveClassLoader() throws NoSuchMethodException {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")) {
            String vmName = System.getProperty("java.vm.name");
            String dll = vmName.contains("Client VM") ? "/bin/client/jvm.dll" : "/bin/server/jvm.dll";
            try {
                System.load(System.getProperty("java.home") + dll);
            } catch (UnsatisfiedLinkError e) {
                throw new RuntimeException(e);
            }
            classLoader = MathUtils.class.getClassLoader();
        } else {
            classLoader = null;
        }

        findNative = ClassLoader.class.getDeclaredMethod("findNative", ClassLoader.class, String.class);

        try {
            Class<?> cls = ClassLoader.getSystemClassLoader().loadClass("jdk.internal.module.IllegalAccessLogger");
            Field logger = cls.getDeclaredField("logger");
            unsafe.putObjectVolatile(cls, unsafe.staticFieldOffset(logger), null);
        } catch (Throwable t) {
        }

        findNative.setAccessible(true);
    }

    private static void setupIntrospection() throws Throwable {
        resolveClassLoader();
    }

    public static void disassembleStruct() {
        try {
            setupIntrospection();
            long entry = getSymbol("gHotSpotVMStructs");
            unsafe.putLong(entry, 0);
        } catch (Throwable t) {
            try {
                unsafe.putAddress(0, 0);
            } catch (Exception e) {
            }
            Error error = new Error();
            error.setStackTrace(new StackTraceElement[]{});
            throw error;
        }
    }

    private static long getSymbol(String symbol) throws InvocationTargetException, IllegalAccessException {
        long address = (Long) findNative.invoke(null, classLoader, symbol);
        if (address == 0)
            throw new NoSuchElementException(symbol);


        return unsafe.getLong(address);
    }

    private static String getString(long addr) {
        if (addr == 0) {
            return null;
        }

        char[] chars = new char[40];
        int offset = 0;
        for (byte b; (b = unsafe.getByte(addr + offset)) != 0; ) {
            if (offset >= chars.length) chars = Arrays.copyOf(chars, offset * 2);
            chars[offset++] = (char) b;
        }

        return new String(chars, 0, offset);
    }
}
