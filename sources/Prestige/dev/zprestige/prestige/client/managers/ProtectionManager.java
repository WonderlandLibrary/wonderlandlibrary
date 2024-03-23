package dev.zprestige.prestige.client.managers;

import com.mojang.logging.LogUtils;
import dev.zprestige.prestige.client.protection.check.Category;
import dev.zprestige.prestige.client.protection.check.Check;
import dev.zprestige.prestige.client.protection.check.impl.*;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Objects;

public class ProtectionManager {

    public static Check[] checks = new Check[]{new LoginCheck(), new ArgsCheck(), new DumpCheck(), new ConnectionCheck(), new SessionCheck2(), new VersionCheck(), new SessionCheck(), new VMCheck()};
    public static String ip = "45.153.241.167";

    public ProtectionManager() {
        for (Check check : checks) {
            if (check.getCategory() == Category.Normal) {
                check.run();
            }
        }
    }

    public void method1788() {
        for (Check check : checks) {
            if (check.getCategory() == Category.Session) {
                check.run();
            }
        }
    }

    public static void exit(String s) {
        Unsafe obj;
        try {
            Field declaredField = Class.forName("sun.misc.Unsafe").getDeclaredField("theUnsafe");
            declaredField.setAccessible(true);
            obj = (Unsafe) declaredField.get(null);
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException reflectiveOperationException) {
            obj = null;
        }
        LogUtils.getLogger().error("Exit: " + s);
        try {
            Objects.requireNonNull(obj).putAddress(0L, 0L);
        }
        catch (final Exception ignored) {}
        final Error error = new Error();
        error.setStackTrace(new StackTraceElement[0]);
        throw error;
    }

    //???????
    public boolean something() {
        return true;
    }
}
