/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C00PacketKeepAlive
 *  net.minecraft.network.play.client.C16PacketClientStatus
 */
package net.ccbluex.liquidbounce.features.module.modules.movement;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C16PacketClientStatus;

@ModuleInfo(name="PingSpoof", description="Spoofs your ping to a given value.", category=ModuleCategory.FUN)
public class PingSpoof
extends Module {
    private final IntegerValue maxDelayValue = new IntegerValue("MaxDelay", 1000, 0, 5000){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int minDelayValue2 = (Integer)PingSpoof.this.minDelayValue.get();
            if (minDelayValue2 > newValue) {
                this.set(minDelayValue2);
            }
        }
    };
    private final IntegerValue minDelayValue = new IntegerValue("MinDelay", 500, 0, 5000){

        @Override
        protected void onChanged(Integer oldValue, Integer newValue) {
            int maxDelayValue2 = (Integer)PingSpoof.this.maxDelayValue.get();
            if (maxDelayValue2 < newValue) {
                this.set(maxDelayValue2);
            }
        }
    };
    private final HashMap<Packet<?>, Long> packetsMap = new HashMap();

    @Override
    public void onDisable() {
        this.packetsMap.clear();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public void onPacket(PacketEvent event) {
        Packet<?> packet = event.getPacket();
        if (!(!(packet instanceof C00PacketKeepAlive) && !(packet instanceof C16PacketClientStatus) || PingSpoof.mc.field_71439_g.field_70128_L || PingSpoof.mc.field_71439_g.func_110143_aJ() <= 0.0f || this.packetsMap.containsKey(packet))) {
            event.cancelEvent();
            HashMap<Packet<?>, Long> hashMap = this.packetsMap;
            synchronized (hashMap) {
                this.packetsMap.put(packet, System.currentTimeMillis() + TimeUtils.randomDelay((Integer)this.minDelayValue.get(), (Integer)this.maxDelayValue.get()));
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget(ignoreCondition=true)
    public void onUpdate(UpdateEvent event) {
        try {
            HashMap<Packet<?>, Long> hashMap = this.packetsMap;
            synchronized (hashMap) {
                Iterator<Map.Entry<Packet<?>, Long>> iterator2 = this.packetsMap.entrySet().iterator();
                while (iterator2.hasNext()) {
                    Map.Entry<Packet<?>, Long> entry = iterator2.next();
                    if (entry.getValue() >= System.currentTimeMillis()) continue;
                    mc.func_147114_u().func_147297_a(entry.getKey());
                    iterator2.remove();
                }
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

