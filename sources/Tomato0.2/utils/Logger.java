/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.utils;

import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.properties.Property;
import java.io.PrintStream;
import java.util.Arrays;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class Logger {
    public static void logChat(String message) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            Cheese.getInstance();
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("\u00a77[\u00a7c" + Cheese.getClient_Name() + "\u00a77] " + message));
        } else {
            System.out.println(message);
        }
    }

    public static void logToggleMessage(String context, boolean enabled) {
        Object[] arrobject = new Object[5];
        arrobject[0] = "\u00a7e";
        arrobject[1] = context;
        arrobject[2] = "\u00a77";
        arrobject[3] = enabled ? "\u00a7aenabled" : "\u00a7cdisabled";
        arrobject[4] = "\u00a77";
        Logger.logChat(String.format("%s%s%s has been %s%s.", arrobject));
    }

    public static void logToggleMessage(String context, boolean enabled, String moduleLabel) {
        Object[] arrobject = new Object[5];
        arrobject[0] = "\u00a7e";
        arrobject[1] = context;
        arrobject[2] = "\u00a77";
        arrobject[3] = String.valueOf(enabled ? "\u00a7aenabled" : "\u00a7cdisabled") + "\u00a77";
        arrobject[4] = moduleLabel;
        Logger.logChat(String.format("%s%s%s has been %s for %s.", arrobject));
    }

    public static void logSetMessage(String context, String property) {
        Logger.logChat(String.format("%s%s%s set to %s%s%s.", "\u00a7e", context, "\u00a77", "\u00a7e", property, "\u00a77"));
    }

    public static void logSetMessage(String Module2, String context, Property property) {
        Object[] arrobject = new Object[7];
        arrobject[0] = Module2;
        arrobject[1] = "\u00a7e";
        arrobject[2] = context;
        arrobject[3] = "\u00a77";
        arrobject[4] = "\u00a7e";
        arrobject[5] = property.value instanceof Boolean ? (((Boolean)property.value).booleanValue() ? "\u00a7aenabled" : "\u00a7cdisabled") : String.valueOf(property.value);
        arrobject[6] = "\u00a77";
        Logger.logChat(String.format("%s's %s%s \u00a77set to %s%s%s.", arrobject));
    }

    public static String LogExecutionFail(String context, String[] executors) {
        Logger.logChat(String.format("Invalid %s %s.", context, Arrays.toString(executors)));
        return String.format("Invalid %s %s.", context, Arrays.toString(executors));
    }

    public static String LogExecutionFail(String context) {
        Logger.logChat(String.format("Invalid %s", context));
        return String.format("Invalid %s.", context);
    }
}

