package dev.tenacity.event.impl.player;

import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.network.PacketSendEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.setting.impl.ModeSetting;
import dev.tenacity.setting.impl.NumberSetting;
import dev.tenacity.util.network.PacketUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("unused")
public final class Blink extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Fake Lag", "Fake Lag");
    private final NumberSetting packetCap = new NumberSetting("Packet Cap", 1, 1, 1000, 1);
    private final List<Packet<?>> packetsList = new CopyOnWriteArrayList<>();
    private final IEventListener<PacketSendEvent> packetSendEventEventListener = event -> {
        if(event.getPacket() instanceof C03PacketPlayer){
            if(event.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition){
                packetsList.add(event.getPacket());
                event.cancel();
            }
            if(event.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook){
                packetsList.add(event.getPacket());
                event.cancel();
            }
            if(event.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook){
                packetsList.add(event.getPacket());
                event.cancel();
            }
        }
    };

    private final IEventListener<MotionEvent> motionEventEventListener = event -> {
        if(packetsList.size() >= (int) packetCap.getCurrentValue() && !packetsList.isEmpty()){
            for(Packet<?> packet : packetsList){
                PacketUtils.sendPacketNoEvent(packet);
                packetsList.remove(packet);
            }
        }
    };

    public Blink() {
        super("Blink", "holds movement packets", ModuleCategory.PLAYER);
        initializeSettings(mode, packetCap);
    }
}
