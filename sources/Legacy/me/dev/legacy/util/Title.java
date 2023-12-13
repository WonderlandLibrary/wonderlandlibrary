package me.dev.legacy.util;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

public class Title {
    int ticks = 0;
    int bruh = 0;
    int breakTimer = 0;
    String bruh1 = "Legacy | v1.1.2 ";
    boolean qwerty = false;
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        ++ticks;
        if (ticks % 17 == 0)
        {
            Display.setTitle((bruh1.substring(0, bruh1.length()-bruh)));
            if ((bruh == bruh1.length() && breakTimer != 0) || (bruh == 0 && breakTimer != 0)) {
                breakTimer++;
                return;
            } else breakTimer = 0;
            if (bruh == bruh1.length()) qwerty = true;
            if (qwerty) --bruh;
            else ++bruh;
            if (bruh == 0) qwerty = false;
        }
    }
}