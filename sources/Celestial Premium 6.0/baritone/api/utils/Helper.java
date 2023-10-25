/*
 * Decompiled with CFR 0.150.
 */
package baritone.api.utils;

import baritone.api.BaritoneAPI;
import baritone.api.utils.gui.BaritoneToast;
import java.util.Arrays;
import java.util.Calendar;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public interface Helper {
    public static final Helper HELPER = new Helper(){};
    public static final Minecraft mc = Minecraft.getMinecraft();

    public static ITextComponent getPrefix() {
        boolean xd;
        Calendar now = Calendar.getInstance();
        boolean bl = xd = now.get(2) == 3 && now.get(5) <= 3;
        TextComponentString baritone = new TextComponentString(xd ? "Baritoe" : ((Boolean)BaritoneAPI.getSettings().shortBaritonePrefix.value != false ? "B" : "Baritone"));
        baritone.getStyle().setColor(TextFormatting.LIGHT_PURPLE);
        TextComponentString prefix = new TextComponentString("");
        prefix.getStyle().setColor(TextFormatting.DARK_PURPLE);
        prefix.appendText("[");
        prefix.appendSibling(baritone);
        prefix.appendText("]");
        return prefix;
    }

    default public void logToast(ITextComponent title, ITextComponent message) {
        mc.addScheduledTask(() -> BaritoneToast.addOrUpdate(mc.getToastGui(), title, message, (Long)BaritoneAPI.getSettings().toastTimer.value));
    }

    default public void logToast(String title, String message) {
        this.logToast(new TextComponentString(title), new TextComponentString(message));
    }

    default public void logToast(String message) {
        this.logToast(Helper.getPrefix(), new TextComponentString(message));
    }

    default public void logDebug(String message) {
        if (!((Boolean)BaritoneAPI.getSettings().chatDebug.value).booleanValue()) {
            return;
        }
        this.logDirect(message, false);
    }

    default public void logDirect(boolean logAsToast, ITextComponent ... components) {
        TextComponentString component = new TextComponentString("");
        if (!logAsToast) {
            component.appendSibling(Helper.getPrefix());
            component.appendSibling(new TextComponentString(" "));
        }
        Arrays.asList(components).forEach(component::appendSibling);
        if (logAsToast) {
            this.logToast(Helper.getPrefix(), component);
        } else {
            mc.addScheduledTask(() -> ((Consumer)BaritoneAPI.getSettings().logger.value).accept(component));
        }
    }

    default public void logDirect(ITextComponent ... components) {
        this.logDirect((Boolean)BaritoneAPI.getSettings().logAsToast.value, components);
    }

    default public void logDirect(String message, TextFormatting color, boolean logAsToast) {
        Stream.of(message.split("\n")).forEach(line -> {
            TextComponentString component = new TextComponentString(line.replace("\t", "    "));
            component.getStyle().setColor(color);
            this.logDirect(logAsToast, component);
        });
    }

    default public void logDirect(String message, TextFormatting color) {
        this.logDirect(message, color, (Boolean)BaritoneAPI.getSettings().logAsToast.value);
    }

    default public void logDirect(String message, boolean logAsToast) {
        this.logDirect(message, TextFormatting.GRAY, logAsToast);
    }

    default public void logDirect(String message) {
        this.logDirect(message, (Boolean)BaritoneAPI.getSettings().logAsToast.value);
    }
}

