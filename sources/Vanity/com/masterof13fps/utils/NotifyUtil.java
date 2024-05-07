package com.masterof13fps.utils;

import com.masterof13fps.Client;
import com.masterof13fps.Methods;
import com.masterof13fps.Wrapper;
import com.masterof13fps.manager.notificationmanager.Notification;
import com.masterof13fps.manager.notificationmanager.NotificationManager;
import com.masterof13fps.manager.notificationmanager.NotificationType;
import net.minecraft.util.ChatComponentText;

public class NotifyUtil implements Methods {

    public void debug(String msg) {
        System.out.println(getColors().getAnsiPurple() + "[" + getColors().getAnsiCyan() + getTime() + getColors().getAnsiPurple() + "] " + getColors().getAnsiBlue() + Client.main().getClientName() + getColors().getAnsiPurple() + " >> " + getColors().getAnsiRed() + msg + getColors().getAnsiReset());
    }

    public void chat(String msg) {
        if (Wrapper.mc.thePlayer != null) {
            Wrapper.mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§c" + Client.main().getClientName() + " §7>> §a" + msg));
        }
    }

    public void notification(String title, String message, NotificationType type, long seconds){
        NotificationManager.show(new Notification(type, title, message, seconds));
    }

}