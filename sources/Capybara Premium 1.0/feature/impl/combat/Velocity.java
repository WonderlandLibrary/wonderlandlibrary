package fun.expensive.client.feature.impl.combat;

import fun.rich.client.event.EventTarget;
import fun.rich.client.event.events.impl.packet.EventReceivePacket;
import fun.rich.client.feature.Feature;
import fun.rich.client.feature.impl.FeatureCategory;
import fun.rich.client.ui.settings.impl.BooleanSetting;
import fun.rich.client.ui.settings.impl.ListSetting;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;

public class Velocity extends Feature {
    public static BooleanSetting cancelOtherDamage;
    public static ListSetting velocityMode;


    public Velocity() {
        super("Velocity", "Вы не будете откидываться", FeatureCategory.Combat);
        velocityMode = new ListSetting("Velocity Mode", "Packet", () -> true, "Packet", "Matrix");
        cancelOtherDamage = new BooleanSetting("Cancel Other Damage", true, () -> true);
        addSettings(velocityMode, cancelOtherDamage);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        String mode = velocityMode.getOptions();
        setSuffix("" + mode);
            if (cancelOtherDamage.getBoolValue() && mc.player.hurtTime > 0 && event.getPacket() instanceof SPacketEntityVelocity && !mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE) && (mc.player.isPotionActive(MobEffects.POISON) || mc.player.isPotionActive(MobEffects.WITHER) || mc.player.isBurning())) {
                event.setCancelled(true);
        }
        if (mode.equalsIgnoreCase("Packet")) {
            SPacketEntityVelocity velocity;
            if ((event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof SPacketExplosion) && (velocity = (SPacketEntityVelocity)event.getPacket()).getEntityID() == mc.player.getEntityId()) {
                event.setCancelled(true);
            }
        } else if (mode.equals("Matrix")) {
            if (mc.player.hurtTime > 8) {
                mc.player.onGround = true;
            }
        }
    }
}