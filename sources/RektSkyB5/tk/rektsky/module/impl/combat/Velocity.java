/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.PacketReceiveEvent;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.specials.flys.OldFly;

public class Velocity
extends Module {
    public static boolean shouldEnable = true;
    boolean velHurt = false;
    long atkTimer = 0L;
    long velTimer = this.getTime();

    public Velocity() {
        super("Velocity", "Lower knockback : )", Category.COMBAT);
    }

    public long getTime() {
        return Minecraft.getSystemTime();
    }

    @Subscribe
    public void onPacketRec(PacketReceiveEvent event) {
        Packet<?> packet;
        if (event != null && Minecraft.getSystemTime() - OldFly.disable > 5000L && shouldEnable && (packet = event.getPacket()) instanceof S12PacketEntityVelocity) {
            ((S12PacketEntityVelocity)event.getPacket()).motionX = 0;
            ((S12PacketEntityVelocity)event.getPacket()).motionZ = 0;
            event.setCanceled(true);
        }
    }

    public void onPacketSend(PacketSentEvent event) {
        if (event.getPacket() instanceof C02PacketUseEntity && ((C02PacketUseEntity)event.getPacket()).getAction() == C02PacketUseEntity.Action.ATTACK) {
            this.atkTimer = this.getTime();
        }
    }
}

