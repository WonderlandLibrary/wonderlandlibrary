package rip.helium.cheat.impl.movement;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import org.apache.commons.lang3.RandomUtils;
import rip.helium.Helium;
import rip.helium.cheat.Cheat;
import rip.helium.cheat.CheatCategory;
import rip.helium.event.minecraft.PlayerMoveEvent;
import rip.helium.event.minecraft.PlayerUpdateEvent;
import rip.helium.event.minecraft.ProcessPacketEvent;
import rip.helium.event.minecraft.SendPacketEvent;
import rip.helium.utils.Stopwatch;
import rip.helium.utils.property.impl.StringsProperty;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

//Anyone that wants to fucking clean up this code and or fix these or fix else if else if else if else if else if else if (thanks kansio) feel free to do so - shotbowxd <3
// ok - Kansio <3

public class Disabler extends Cheat {

    private final StringsProperty mode = new StringsProperty("Mode", "How the priority target will be selected.", null, false, true, new String[]{"Ghostly"/*"Muncher", "Kohi", "RinaOrc", "Mineplex", "Faithful", "Verus", "PingSpoof", "OmegaCraft", "Watchdog", "Poopful"*/}, new Boolean[]{true /*false, false, false, false, false, false, false, false, false, false*/});

    public Disabler() {
        super("Disabler", "Fuck that little faggot!", CheatCategory.MOVEMENT);
        registerProperties(mode);
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
    }

    @Collect
    public void playerMoveEvent(PlayerMoveEvent event) {
        switch (mode.getSelectedStrings().get(0)) {
            case "Ghostly": {
                mc.thePlayer.sendQueue.addToSendQueue(new C00PacketKeepAlive(0));
                if (mc.thePlayer.ticksExisted % 3 == 0) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C0CPacketInput());
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                            mc.thePlayer.posY, mc.thePlayer.posZ, true));
                }
                break;
            }
        }
    }

    @Collect
    public void packetEvent(SendPacketEvent event) {
        switch (mode.getSelectedStrings().get(0)) {
            case "Ghostly": {
                if (event.getPacket() instanceof S00PacketKeepAlive) {
                    event.setCancelled(true);
                }
                if (event.getPacket() instanceof C0FPacketConfirmTransaction) {
                    event.setCancelled(true);
                }
                if (event.getPacket() instanceof C03PacketPlayer) {
                    mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0CPacketInput());
                }
                if (event.getPacket() instanceof C0CPacketInput) {
                    C0CPacketInput packet = (C0CPacketInput) event.getPacket();
                    packet.forwardSpeed = Float.MAX_VALUE;
                    packet.strafeSpeed = Float.MAX_VALUE;
                    packet.jumping = (mc.thePlayer.ticksExisted % 2 == 0);
                    packet.sneaking = (mc.thePlayer.ticksExisted % 2 != 0);
                }
                break;
            }
        }
    }
}
