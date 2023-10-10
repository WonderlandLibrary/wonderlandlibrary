package today.flux.utility;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import today.flux.Flux;
import today.flux.module.Module;
import today.flux.module.ModuleManager;

public class ChatUtils {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static List<String> ReportedPlayers = new ArrayList<>();

    public static void sendMessageToPlayer(String msg) {
        mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GRAY + "[" + EnumChatFormatting.WHITE + Flux.NAME + EnumChatFormatting.GRAY + "]: " + EnumChatFormatting.GRAY + msg));
    }

    public static void sendErrorToPlayer(String msg) {
        if (mc.thePlayer != null)
            mc.thePlayer.addChatMessage(new ChatComponentText("\247c" + msg));
        else
            System.out.println(msg);
    }

    public static void sendOutputMessage(String msg) {
        if (mc.thePlayer != null)
            mc.thePlayer.addChatMessage(new ChatComponentText("\247b" + msg));
        else
            System.out.println(msg);
    }

    public static void SendMessage(String Message) {
        mc.thePlayer.sendChatMessage(Message);
    }

    public static void ReportPlayer(String PlayerName) {
        if (ReportedPlayers.contains(PlayerName) || mc.thePlayer.getName().equals(PlayerName))
            return;
        ReportedPlayers.add(PlayerName);
        mc.thePlayer.sendChatMessage("/wdr" + " " + PlayerName + " " + "ka speed reach fly antiknockback autoclicker dolphin");
    }

    public static void debug(Object msg) {
        if (!Flux.DEBUG_MODE)
            return;

        if (!ModuleManager.debugMod.isEnabled()) return;

        if (mc.thePlayer == null || mc.theWorld == null)
            return;

        String className = "Unknown";
        StackTraceElement[] mStacks = Thread.currentThread().getStackTrace();
        for (Module module : ModuleManager.getModList()) {
            for (StackTraceElement mStack : mStacks) {
                if (module.getClass().getName().equals(mStack.getClassName()) && !module.getName().equals("Debug")) {
                    className = module.getName();
                    break;
                }
            }
        }


        if (mc.thePlayer != null) {
            mc.thePlayer.addChatMessage(new ChatComponentText("\247d[" + className + "]\247r " + EnumChatFormatting.GRAY + msg));
        } else {
            System.out.println("[DEBUG] " + "\247d[" + className + "]\247r " + EnumChatFormatting.GRAY + msg);
        }
    }

    private static int compare(String str, String target) {
        int[][] d; // 矩阵
        int n = str.length();
        int m = target.length();
        int i; // 遍历str的
        int j; // 遍历target的
        char ch1; // str的
        char ch2; // target的
        int temp; // 记录相同字符,在某个矩阵位置值的增量,不是0就是1

        if (n == 0) {
            return m;
        }

        if (m == 0) {
            return n;
        }

        d = new int[n + 1][m + 1];

        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }

        for (i = 1; i <= n; i++) {
            ch1 = str.charAt(i - 1);
            for (j = 1; j <= m; j++) {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2 || ch1 == ch2 + 32 || ch1 + 32 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }

    private static int min(int one, int two, int three) {
        return (one = Math.min(one, two)) < three ? one : three;
    }

    public static float getSimilarityRatio(String str, String target) {
        int max = Math.max(str.length(), target.length());
        return 1 - (float) compare(str, target) / max;
    }
}