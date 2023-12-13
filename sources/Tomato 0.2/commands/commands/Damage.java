/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.commands.commands;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.commands.Command;
import cow.milkgod.cheese.events.EventTick;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Damage
extends Command {
    private double damage = 0.5;

    public Damage() {
        super("Damage", null, new String[]{"dmg"}, null, "Damages you.");
    }

    @EventTarget
    public void onChatSend(EventTick event) {
        if (this.getState()) {
            if (this.mc.thePlayer != null) {
                int i2 = 0;
                while ((double)i2 < 80.0 + 40.0 * (this.damage - 0.5)) {
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.049, this.mc.thePlayer.posZ, false));
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
                    ++i2;
                }
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
            }
            this.Toggle();
        }
    }
}

