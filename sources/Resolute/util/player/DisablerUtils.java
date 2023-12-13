// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class DisablerUtils extends Thread
{
    private Packet packet;
    private long delay;
    
    public DisablerUtils(final Packet packet, final long delay) {
        this.packet = packet;
        this.delay = delay;
    }
    
    public static void damage() {
        Minecraft.getMinecraft().getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY + 4.1001, Minecraft.getMinecraft().thePlayer.posZ, false));
        Minecraft.getMinecraft().getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ, false));
        Minecraft.getMinecraft().getNetHandler().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.getMinecraft().thePlayer.posX, Minecraft.getMinecraft().thePlayer.posY, Minecraft.getMinecraft().thePlayer.posZ, true));
        Minecraft.getMinecraft().thePlayer.jump();
    }
    
    @Override
    public void run() {
        try {
            Thread.sleep(this.delay);
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(this.packet);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
