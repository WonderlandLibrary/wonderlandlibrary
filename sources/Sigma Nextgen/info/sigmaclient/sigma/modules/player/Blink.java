package info.sigmaclient.sigma.modules.player;

import com.mojang.authlib.GameProfile;
import info.sigmaclient.sigma.config.values.BooleanValue;
import info.sigmaclient.sigma.config.values.NumberValue;
import info.sigmaclient.sigma.event.net.PacketEvent;
import info.sigmaclient.sigma.event.player.UpdateEvent;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CConfirmTransactionPacket;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.server.management.PlayerInteractionManager;

import java.util.ArrayList;
import java.util.UUID;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Blink extends Module {
    BooleanValue pulse = new BooleanValue("Pulse", false);
    NumberValue pulsePackets = new NumberValue("Pulse Count", 1, 1, 10, NumberValue.NUMBER_TYPE.INT);
    public Blink() {
        super("Blink", Category.Player, "WDF");
     registerValue(pulse);
     registerValue(pulsePackets);
    }

    @Override
    public void onPacketEvent(PacketEvent event) {
        if(event.packet instanceof CPlayerPacket || event.packet instanceof CConfirmTransactionPacket){
            packets.add(event.packet);
            event.cancelable = true;
        }
        super.onPacketEvent(event);
    }
    public ArrayList<IPacket<?>> packets = new ArrayList<>();
    @Override
    public void onEnable() {
        super.onEnable();
    }
    public void clear(){
        packets.forEach(mc.getConnection()::sendPacketNOEvent);
        packets.clear();
    }
    @Override
    public void onUpdateEvent(UpdateEvent event) {
        if(event.isPre()){
            if(pulse.isEnable()){
                if(mc.player.ticksExisted % pulsePackets.getValue().intValue() == 0){
                    clear();
                }
            }
        }
        super.onUpdateEvent(event);
    }

    @Override
    public void onDisable() {
        clear();
        super.onDisable();
    }
}
