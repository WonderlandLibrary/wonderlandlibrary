package ravenweave.client.module.modules.player;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.weavemc.loader.api.event.ShutdownEvent;
import net.weavemc.loader.api.event.StartGameEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.WorldEvent;
import ravenweave.client.event.ext.EventDirection;
import ravenweave.client.event.impl.PacketEvent;
import ravenweave.client.module.Module;
import ravenweave.client.module.setting.impl.DescriptionSetting;
import ravenweave.client.module.setting.impl.TickSetting;

import java.util.ArrayList;

public class Blink extends Module {
    public static TickSetting inbound, outbound, spawnFake;

    private final ArrayList<? extends Packet> outboundPackets = new ArrayList<>();
    private final ArrayList<? extends Packet> inboundPackets = new ArrayList<>();
    private static EntityOtherPlayerMP fakePlayer;

    public Blink() {
        super("Blink", ModuleCategory.player);
        this.registerSetting(new DescriptionSetting("Chokes packets until disabled."));
        this.registerSetting(inbound = new TickSetting("Block Inbound", true));
        this.registerSetting(outbound = new TickSetting("Block Outbound", true));
        this.registerSetting(spawnFake = new TickSetting("Spawn fake player", true));
    }
    
    @SubscribeEvent
    public void onPacket(PacketEvent e) {
        if (e.getDirection() == EventDirection.INCOMING) {
            if (!inbound.isToggled()) return;
            inboundPackets.add(e.getPacket());
        } else {
            if (!outbound.isToggled()) return;
            if (!e.getPacket().getClass().getCanonicalName().startsWith("net.minecraft.network.play.client")) return;
            outboundPackets.add(e.getPacket());
        }
        e.setCancelled(true);
    }
    
    @Override
    public void onEnable() {
        outboundPackets.clear();
        inboundPackets.clear();
        if (spawnFake.isToggled()) {
            if (mc.thePlayer != null) {
                fakePlayer = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getGameProfile());
                fakePlayer.setRotationYawHead(mc.thePlayer.rotationYawHead);
                fakePlayer.copyLocationAndAnglesFrom(mc.thePlayer);
                mc.theWorld.addEntityToWorld(fakePlayer.getEntityId(), fakePlayer);
            }
        }
    }

    @Override
    public void onDisable() {
        for (Packet packet : outboundPackets) {
            mc.getNetHandler().addToSendQueue(packet);
        }

        outboundPackets.clear();
        inboundPackets.clear();
        if (fakePlayer != null) {
            mc.theWorld.removeEntityFromWorld(fakePlayer.getEntityId());
            fakePlayer = null;
        }
    }

    @SubscribeEvent
    public void onShutdown(ShutdownEvent e) {
        this.disable();
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent e) {
        this.disable();
    }

    @SubscribeEvent
    public void onStart(StartGameEvent e) {
        this.disable();
    }

}
