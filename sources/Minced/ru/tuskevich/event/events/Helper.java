// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events;

import net.minecraft.network.Packet;
import net.minecraft.client.gui.ScaledResolution;
import ru.tuskevich.util.math.TimerHelper;
import java.util.Random;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.Minecraft;

public interface Helper
{
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static final Gui gui = new Gui();
    public static final Random random = new Random();
    public static final TimerHelper timerHelper = new TimerHelper();
    public static final ScaledResolution sr = new ScaledResolution(Helper.mc);
    
    default void sendPacket(final Packet<?> packet) {
        Minecraft.player.connection.sendPacket(packet);
    }
}
