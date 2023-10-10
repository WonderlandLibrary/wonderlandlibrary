package tech.atani.client.feature.module.impl.misc;

import cn.muyang.nativeobfuscator.Native;
import net.minecraft.network.Packet;
import tech.atani.client.listener.event.minecraft.network.PacketEvent;
import tech.atani.client.listener.event.minecraft.player.movement.UpdateEvent;
import tech.atani.client.listener.radbus.Listen;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.data.ModuleData;
import tech.atani.client.feature.module.data.enums.Category;
import tech.atani.client.utility.interfaces.Methods;
import tech.atani.client.utility.math.time.TimeHelper;
import tech.atani.client.feature.value.impl.CheckBoxValue;
import tech.atani.client.feature.value.impl.SliderValue;

import java.util.ArrayDeque;

@Native
@ModuleData(name = "Blink", description = "Blocks your packets for a time being", category = Category.MISCELLANEOUS, alwaysRegistered = true)
public class Blink extends Module {
    private final CheckBoxValue incoming = new CheckBoxValue("Incoming", "Queue incoming packets?", this, false),
            pulse = new CheckBoxValue("Pulse", "Disable and enable the module every x amount of ms?", this, false);
    private final SliderValue<Long> pulseDelay = new SliderValue<Long>("Pulse Delay", "What'll be the delay between pulsing?", this, 150L, 50L, 5000L, 1);

    private final ArrayDeque<Packet<?>> outPacketDeque = new ArrayDeque<>();
    private final TimeHelper fakeLagTimer = new TimeHelper();

    boolean active = false;

    @Listen
    public void onUpdate(UpdateEvent updateEvent) {
        if(active && !this.isEnabled()) {
            while (!outPacketDeque.isEmpty()) {
                sendPacketUnlogged(outPacketDeque.poll());
            }
            active = false;
        }
    }

    @Listen
    public void onPacket(PacketEvent event) {
    	if(Methods.mc.thePlayer == null || Methods.mc.theWorld == null)
    		this.setEnabled(false);
        if (active && (event.getType() == PacketEvent.Type.OUTGOING || incoming.getValue())) {
            outPacketDeque.add(event.getPacket());
            if (pulse.getValue() && fakeLagTimer.hasReached(pulseDelay.getValue())) {
                while (!outPacketDeque.isEmpty()) {
                    sendPacketUnlogged(outPacketDeque.poll());
                }
                fakeLagTimer.reset();
            }
            event.setCancelled(true);
        }
    }

    @Override
    public void onEnable() {
        active = true;
    }

    @Override
    public void onDisable() {
    }

}