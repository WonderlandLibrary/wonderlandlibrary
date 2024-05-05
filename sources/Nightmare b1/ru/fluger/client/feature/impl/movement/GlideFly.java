// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.movement;

import ru.fluger.client.helpers.movement.MovementHelper;
import ru.fluger.client.helpers.world.InventoryHelper;
import ru.fluger.client.event.events.impl.player.EventPreMotion;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.packet.EventSendPacket;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.feature.Feature;

public class GlideFly extends Feature
{
    public final NumberSetting glideSpeed;
    public final NumberSetting motionY;
    
    public GlideFly() {
        super("GlideFly", "", Type.Movement);
        this.glideSpeed = new NumberSetting("GlideFly Speed", 0.2f, 0.06f, 0.3f, 0.01f, () -> true);
        this.motionY = new NumberSetting("Motion Y", 0.39f, 0.1f, 1.0f, 0.01f, () -> true);
        this.addSettings(this.glideSpeed, this.motionY);
    }
    
    @EventTarget
    public void on(final EventSendPacket event) {
        if (event.getPacket() instanceof lk) {
            final lk cPacketPlayer = (lk)event.getPacket();
            cPacketPlayer.f = false;
        }
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotion eventPreMotion) {
        InventoryHelper.swapElytraToChestplate();
        if (GlideFly.mc.h.T % 6 == 0) {
            InventoryHelper.disabler(InventoryHelper.getSlotWithElytra());
        }
        MovementHelper.setMotion(MovementHelper.getSpeed() * 1.0f + this.glideSpeed.getCurrentValue());
        GlideFly.mc.h.t = this.motionY.getCurrentValue();
    }
}
