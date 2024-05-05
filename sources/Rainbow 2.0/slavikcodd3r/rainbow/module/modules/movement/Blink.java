package slavikcodd3r.rainbow.module.modules.movement;

import java.util.Iterator;

import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import slavikcodd3r.rainbow.event.EventTarget;
import slavikcodd3r.rainbow.event.events.PacketSendEvent;
import slavikcodd3r.rainbow.event.events.UpdateEvent;
import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.module.Module.Mod;
import slavikcodd3r.rainbow.option.Option;
import slavikcodd3r.rainbow.utils.ClientUtils;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import java.util.ArrayList;

import net.minecraft.network.Packet;
import java.util.List;
import net.minecraft.client.entity.EntityOtherPlayerMP;
@Mod (displayName = "Blink")
public class Blink extends Module
{
    private EntityOtherPlayerMP blinkEntity;
    private List<Packet> packetList;
    @Option.Op(name = "Block Place")
    private boolean blockPlace;
    @Option.Op(name = "Attack")
    private boolean attack;
    @Option.Op(name = "All")
    private boolean all;
    
    public Blink() {
        this.packetList = new ArrayList<Packet>();
        this.blockPlace = true;
        this.attack = true;
    }
    
    @Override
    public void enable() {
        if (ClientUtils.player() == null) {
            return;
        }
        super.enable();
    }
    
    @EventTarget
    private void onPacketSend(final PacketSendEvent event) {
        if (this.all || event.getPacket() instanceof C0BPacketEntityAction || event.getPacket() instanceof C03PacketPlayer || (this.attack && (event.getPacket() instanceof C02PacketUseEntity || event.getPacket() instanceof C0APacketAnimation)) || (this.blockPlace && event.getPacket() instanceof C08PacketPlayerBlockPlacement)) {
            this.packetList.add(event.getPacket());
            event.setCancelled(true);
        }
    }
    
    @Override
    public void disable() {
        super.disable();
        for (final Packet packet : this.packetList) {
            ClientUtils.packet(packet);
        }
        this.packetList.clear();
    }
}
