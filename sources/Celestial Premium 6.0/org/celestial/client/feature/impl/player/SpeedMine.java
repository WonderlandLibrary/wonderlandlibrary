/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.player;

import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventBlockInteract;
import org.celestial.client.event.events.impl.player.EventUpdate;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.settings.impl.ListSetting;

public class SpeedMine
extends Feature {
    public ListSetting mode = new ListSetting("SpeedMine Mode", "Packet", () -> true, "Packet", "Potion");

    public SpeedMine() {
        super("SpeedMine", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0431\u044b\u0441\u0442\u0440\u043e \u043b\u043e\u043c\u0430\u0442\u044c \u0431\u043b\u043e\u043a\u0438", Type.Player);
        this.addSettings(this.mode);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(this.mode.currentMode);
    }

    @EventTarget
    public void onBlockInteract(EventBlockInteract event) {
        switch (this.mode.currentMode) {
            case "Potion": {
                assert (MobEffects.HASTE != null);
                SpeedMine.mc.player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 817, 1));
                break;
            }
            case "Packet": {
                SpeedMine.mc.player.swingArm(EnumHand.MAIN_HAND);
                SpeedMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, event.getPos(), event.getFace()));
                SpeedMine.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, event.getPos(), event.getFace()));
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onDisable() {
        assert (MobEffects.HASTE != null);
        SpeedMine.mc.player.removePotionEffect(MobEffects.HASTE);
        super.onDisable();
    }
}

