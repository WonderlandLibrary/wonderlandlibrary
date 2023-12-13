// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.command.impl;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.client.Minecraft;
import exhibition.management.command.Command;

public class Damage extends Command
{
    static Minecraft mc;
    
    public Damage(final String[] names, final String description) {
        super(names, description);
    }
    
    @Override
    public void fire(final String[] args) {
        damagePlayer();
    }
    
    public static void damagePlayer() {
        for (int index = 0; index < 70; ++index) {
            Damage.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Damage.mc.thePlayer.posX, Damage.mc.thePlayer.posY + 0.06, Damage.mc.thePlayer.posZ, false));
            Damage.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Damage.mc.thePlayer.posX, Damage.mc.thePlayer.posY, Damage.mc.thePlayer.posZ, false));
        }
        Damage.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Damage.mc.thePlayer.posX, Damage.mc.thePlayer.posY + 0.1, Damage.mc.thePlayer.posZ, false));
    }
    
    @Override
    public String getUsage() {
        return null;
    }
    
    static {
        Damage.mc = Minecraft.getMinecraft();
    }
}
