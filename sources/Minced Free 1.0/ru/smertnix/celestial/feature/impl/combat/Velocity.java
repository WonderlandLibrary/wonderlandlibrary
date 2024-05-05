package ru.smertnix.celestial.feature.impl.combat;

import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.packet.EventReceivePacket;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;

public class Velocity extends Feature {


    public Velocity() {
        super("Velocity", "Не даёт откидываться игроку", FeatureCategory.Combat);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
            if (mc.player.hurtTime > 0 && event.getPacket() instanceof SPacketEntityVelocity && !mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE) && (mc.player.isPotionActive(MobEffects.POISON) || mc.player.isPotionActive(MobEffects.WITHER) || mc.player.isBurning())) {
                event.setCancelled(true);
        }
            SPacketEntityVelocity velocity;
            if ((event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof SPacketExplosion) && (velocity = (SPacketEntityVelocity)event.getPacket()).getEntityID() == mc.player.getEntityId()) {
                event.setCancelled(true);
            }
    }
}