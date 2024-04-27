/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.patcher.tweaker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PatcherTweaker {
    static void invokeExit() {
        try {
            Class<?> aClass = Class.forName("java.lang.Shutdown");
            Method exit = aClass.getDeclaredMethod("exit", Integer.TYPE);
            exit.setAccessible(true);
            exit.invoke(null, 0);
        }
        catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

