// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.combat;

import com.elementars.eclient.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketUseEntity;
import com.elementars.eclient.event.events.EventSendPacket;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class Criticals extends Module
{
    private final Value<String> mode;
    
    public Criticals() {
        super("Criticals", "Automatically crits people", 0, Category.COMBAT, true);
        this.mode = this.register(new Value<String>("Mode", this, "Packet", new String[] { "Jump", "Packet" }));
    }
    
    @EventTarget
    public void sendPacket(final EventSendPacket event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            final CPacketUseEntity packet = (CPacketUseEntity)event.getPacket();
            if (packet.getAction() == CPacketUseEntity.Action.ATTACK && Criticals.mc.player.onGround && !Criticals.mc.gameSettings.keyBindJump.isKeyDown() && packet.getEntityFromWorld((World)Criticals.mc.world) instanceof EntityLivingBase) {
                if (this.mode.getValue().equalsIgnoreCase("Packet")) {
                    Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY + 0.10000000149011612, Criticals.mc.player.posZ, false));
                    Criticals.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Position(Criticals.mc.player.posX, Criticals.mc.player.posY, Criticals.mc.player.posZ, false));
                }
                else {
                    Criticals.mc.player.jump();
                }
            }
        }
    }
}
