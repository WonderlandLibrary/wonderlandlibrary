// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.command.impl;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.client.Minecraft;
import vip.Resolute.command.Command;

public class VClip extends Command
{
    public Minecraft mc;
    
    public VClip() {
        super("VClip", "VClips through blocks", ".vclip <value>", new String[] { "vclip" });
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void onCommand(final String[] args, final String command) {
        if (args.length > 0) {
            final float distance = Float.parseFloat(args[0]);
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + distance, this.mc.thePlayer.posZ, false));
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + distance, this.mc.thePlayer.posZ);
        }
    }
}
