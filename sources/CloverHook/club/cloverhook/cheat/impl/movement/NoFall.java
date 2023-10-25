package club.cloverhook.cheat.impl.movement;

import club.cloverhook.cheat.Cheat;
import club.cloverhook.cheat.CheatCategory;
import club.cloverhook.event.minecraft.PlayerUpdateEvent;
import club.cloverhook.event.minecraft.SendPacketEvent;
import club.cloverhook.utils.property.impl.BooleanProperty;
import club.cloverhook.utils.property.impl.StringsProperty;
import me.hippo.systems.lwjeb.annotation.Collect;

import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall extends Cheat {

    public NoFall() {
        super("NoFall", "Negates fall damage on hypickle.", CheatCategory.MOVEMENT);
    }

    boolean canmeme;

    @Collect
    public void onSendPacket(SendPacketEvent sendPacketEvent) {
        if (sendPacketEvent.getPacket() instanceof C02PacketUseEntity) {
            if (canmeme) {
                sendPacketEvent.setCancelled(true);
            }
        }
    }

    @Collect
    public void onPlayerUpdate(PlayerUpdateEvent playerUpdateEvent) {
        if (this.getState()) {
            if (mc.thePlayer.onGround) {
                canmeme = false;
                mc.thePlayer.fallDistance = 0;
            }
            if (mc.thePlayer.fallDistance >= 2.95) {
                playerUpdateEvent.setOnGround(canmeme = true);
            } else {
                canmeme = false;
            }
        }
    }

}
