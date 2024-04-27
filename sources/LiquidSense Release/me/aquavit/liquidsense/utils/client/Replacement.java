package me.aquavit.liquidsense.utils.client;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.utils.entity.EntityUtils;
import me.aquavit.liquidsense.utils.mc.MinecraftInstance;
import me.aquavit.liquidsense.utils.misc.ServerUtils;
import me.aquavit.liquidsense.utils.module.CPSCounter;
import me.aquavit.liquidsense.ui.client.hud.element.elements.Text;
import net.minecraft.client.Minecraft;

public class Replacement extends MinecraftInstance {
    public static String getReplacement(String str) {
        if (mc.thePlayer != null) {

            switch (str.toLowerCase()) {
                case "x":
                    return Text.DECIMAL_FORMAT.format(mc.thePlayer.posX);
                case "y":
                    return Text.Y_FORMAT.format(mc.thePlayer.posY);
                case "z":
                    return Text.DECIMAL_FORMAT.format(mc.thePlayer.posZ);
                case "xdp":
                    return String.valueOf(mc.thePlayer.posX);
                case "ydp":
                    return String.valueOf(mc.thePlayer.posY);
                case "zdp":
                    return String.valueOf(mc.thePlayer.posZ);
                case "velocity":
                    return Text.DECIMAL_FORMAT.format(Math.sqrt(
                            mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ) * 20);
                case "timer":
                    return Text.DECIMAL_FORMAT.format(mc.timer.timerSpeed);
                case "ping":
                    return String.valueOf(EntityUtils.getPing(mc.thePlayer));
                case "food":
                    return String.valueOf(mc.thePlayer.getFoodStats().getFoodLevel());
                case "health":
                    return Text.DECIMAL_FORMAT.format(mc.thePlayer.getHealth());
                case "maxheatlh":
                    return Text.DECIMAL_FORMAT.format(mc.thePlayer.getMaxHealth());
            }
        }

        switch (str.toLowerCase()){
            case "username":
                return mc.getSession().getUsername();
            case "clientname":
                return LiquidSense.CLIENT_NAME;
            case "clientversion":
                return "b"+ LiquidSense.CLIENT_VERSION;
            case "clientcreator":
                return LiquidSense.CLIENT_CREATOR;
            case "fps":
                return String.valueOf(Minecraft.getDebugFPS());
            case "date":
                return Text.DECIMAL_FORMAT.format(System.currentTimeMillis());
            case "time":
                return Text.HOUR_FORMAT.format(System.currentTimeMillis());
            case "serverip":
                return ServerUtils.getRemoteIp();
            case "cps":
            case "lcps":
                return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.LEFT));
            case "mcps":
                return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.MIDDLE));
            case "rcps":
                return String.valueOf(CPSCounter.getCPS(CPSCounter.MouseButton.RIGHT));
            default:
                return null;

        }
    }

    public static String multiReplace(String str) {
        int lastPercent = -1;
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < ((CharSequence)str).length(); ++i) {
            if (str.charAt(i) == '%') {
                if (lastPercent != -1) {
                    if (lastPercent + 1 != i) {
                        String replacement = getReplacement(str.substring(lastPercent + 1, i));
                        if (replacement != null) {
                            result.append(replacement);
                            lastPercent = -1;
                            continue;
                        }
                    }

                    result.append(str, lastPercent, i);
                }
                lastPercent = i;
            } else if (lastPercent == -1) result.append(str.charAt(i));
        }
        if (lastPercent != -1) result.append(str, lastPercent, str.length());

        return result.toString();
    }
}
