package wtf.diablo.utils.logging;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import wtf.diablo.Diablo;
import wtf.diablo.utils.render.ColorUtil;

public class LoggingUtil {
    public static void log(String message) {
        System.out.println("[" + Diablo.name + "] " + message + ColorUtil.RESET);
    }

    public static void infoLog(String message) {
        System.out.println(ColorUtil.BLUE_BRIGHT + "[INFO] " + message + ColorUtil.RESET);
    }

    public static void warningLog(String message) {
        System.out.println(ColorUtil.YELLOW + "[WARNING] " + message + ColorUtil.RESET);
    }

    public static void errorLog(String message) {
        System.out.println(ColorUtil.RED_BRIGHT + "[ERROR] " + message + ColorUtil.RESET);
    }

    public static void successLog(String message) {
        System.out.println(ColorUtil.GREEN_BRIGHT + "[SUCCESS] " + message + ColorUtil.RESET);
    }


}
