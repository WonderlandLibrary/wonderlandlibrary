package io.github.nevalackin.client.module.combat.rage;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.packet.ReceivePacketEvent;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public final class Velocity extends Module {

    private final DoubleProperty horizontalPercentageProperty = new DoubleProperty("Horizontal", 0.0, 0.0, 100.0, 1.0);
    private final DoubleProperty verticalPercentageProperty = new DoubleProperty("Vertical", 0.0, 0.0, 100.0, 1.0);

    public Velocity() {
        super("Velocity", Category.COMBAT, Category.SubCategory.COMBAT_RAGE);

        this.setSuffix(() -> String.format("%s%% %s%%", this.horizontalPercentageProperty.getValue().intValue(), this.verticalPercentageProperty.getValue().intValue()));

        this.register(this.horizontalPercentageProperty, this.verticalPercentageProperty);
    }

    @EventLink
    private final Listener<ReceivePacketEvent> onReceivePacket = event -> {
        final Packet<?> packet = event.getPacket();

        if (packet instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity velocity = (S12PacketEntityVelocity) packet;

            if (velocity.getEntityID() != this.mc.thePlayer.getEntityId()) return;

            final float verticalPercentage = verticalPercentageProperty.getValue().floatValue() / 100.0F;
            final float horizontalPercentage = horizontalPercentageProperty.getValue().floatValue() / 100.0F;

            if (verticalPercentage == 0 && horizontalPercentage == 0) {
                event.setCancelled();
                return;
            }

            velocity.setMotionX((int) Math.ceil(velocity.getMotionX() * horizontalPercentage));
            velocity.setMotionY((int) Math.ceil(velocity.getMotionY() * verticalPercentage));
            velocity.setMotionZ((int) Math.ceil(velocity.getMotionZ() * horizontalPercentage));
        } else if (packet instanceof S27PacketExplosion) {
            S27PacketExplosion explosion = (S27PacketExplosion) packet;

            final float verticalPercentage = verticalPercentageProperty.getValue().floatValue() / 100.0F;
            final float horizontalPercentage = horizontalPercentageProperty.getValue().floatValue() / 100.0F;

            if (verticalPercentage == 0 && horizontalPercentage == 0) {
                event.setCancelled();
                return;
            }

            explosion.setMotionY(explosion.getMotionY() * verticalPercentage);
            explosion.setMotionX(explosion.getMotionX() * horizontalPercentage);
            explosion.setMotionZ(explosion.getMotionZ() * horizontalPercentage);
        }
    };

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
