package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.Gui.Notifications.Notificationsn;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import java.util.ArrayList;
import java.util.Random;

public class ChatUtil {
    public static ArrayList<Notificationsn> notifications = new ArrayList<>();
    public static Random random = new Random();
    public static void drawNotifications() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        double startY = res.getScaledHeight() - 25;
        final double lastY = startY;
        for (int i = 0; i < notifications.size(); i++) {
            Notificationsn not = notifications.get(i);
            if (not.shouldDelete())
                notifications.remove(i);

            not.draw(startY, lastY);
            startY -= not.getHeight() + 1;
            if(notifications.size() >=10) {
                notifications.clear();
                sendClientMessage("Auto Clean All Notifications", Notificationsn.Type.INFO);
            }
        }
    }

    public static void sendClientMessage(String message, Notificationsn.Type type) {
        notifications.add(new Notificationsn(message, type));
    }
    private int updateCounterCreated;
    private IChatComponent lineString;
    private int chatLineID;

    public ChatUtil(int p_i1826_1_, IChatComponent p_i1826_2_, int p_i1826_3_) {
        super();
        this.lineString = p_i1826_2_;
        this.updateCounterCreated = p_i1826_1_;
        this.chatLineID = p_i1826_3_;
    }

    public IChatComponent getChatComponent() {
        return this.lineString;
    }

    public int getUpdatedCounter() {
        return this.updateCounterCreated;
    }

    public int getChatLineID() {
        return this.chatLineID;
    }
}
