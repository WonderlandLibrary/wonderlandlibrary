/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.utils;

import cow.milkgod.cheese.Cheese;
import java.io.PrintStream;
import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class SessionUtils {
    private static Minecraft mc = Minecraft.getMinecraft();

    public static Session getSession() {
        try {
            Field session = mc.getClass().getDeclaredField("session");
            session.setAccessible(true);
            return (Session)session.get(mc);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static void setSession(Session session) {
        try {
            Field f2 = mc.getClass().getDeclaredField("session");
            f2.setAccessible(true);
            f2.set(mc, session);
        }
        catch (Exception e) {
            Cheese.getInstance();
            System.out.println("[" + Cheese.getClient_Name() + "]: WARNING! Could not set session.");
        }
    }
}

