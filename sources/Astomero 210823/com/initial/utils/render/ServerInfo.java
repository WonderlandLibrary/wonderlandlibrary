package com.initial.utils.render;

import net.minecraft.client.*;
import com.initial.modules.impl.visual.*;
import com.initial.modules.*;
import com.initial.*;
import com.initial.events.*;
import net.minecraft.util.*;
import net.minecraft.network.play.server.*;
import com.initial.events.impl.*;

public class ServerInfo
{
    private static long prevTime;
    private static long lastServerPacket;
    private static float[] ticks;
    private static int currentTick;
    
    @EventTarget
    public void onRender(final float partialTicks) {
        if ((ServerInfo.lastServerPacket != -1L && Math.abs(System.currentTimeMillis() - ServerInfo.lastServerPacket) > 3500L && !Minecraft.getMinecraft().isSingleplayer()) || ModuleManager.getModule(HUD.class).isEnabled()) {
            final long seconds = Math.abs(System.currentTimeMillis() - ServerInfo.lastServerPacket) / 1000L;
            Astomero.addDebugMessage("Server not responding " + seconds + "s");
        }
    }
    
    public static double getTps() {
        int tickCount = 0;
        float tickRate = 0.0f;
        for (int i = 0; i < ServerInfo.ticks.length; ++i) {
            final float tick = ServerInfo.ticks[i];
            if (tick > 0.0f) {
                tickRate += tick;
                ++tickCount;
            }
        }
        return MathHelper.clamp_double(tickRate / tickCount, 0.0, 20.0);
    }
    
    @EventTarget
    public void onReceive(final EventReceivePacket e) {
        if (e.getPacket() instanceof S03PacketTimeUpdate) {
            if (ServerInfo.prevTime != -1L) {
                ServerInfo.ticks[ServerInfo.currentTick % ServerInfo.ticks.length] = MathHelper.clamp_float(20.0f / ((System.currentTimeMillis() - ServerInfo.prevTime) / 1000.0f), 0.0f, 20.0f);
                ++ServerInfo.currentTick;
            }
            ServerInfo.prevTime = System.currentTimeMillis();
        }
    }
    
    @EventTarget
    public void onSend(final EventSendPacket e) {
        ServerInfo.lastServerPacket = System.currentTimeMillis();
    }
    
    static {
        ServerInfo.ticks = new float[20];
    }
}
