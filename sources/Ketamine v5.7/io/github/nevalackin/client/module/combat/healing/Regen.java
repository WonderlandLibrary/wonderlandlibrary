package io.github.nevalackin.client.module.combat.healing;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.player.UpdatePositionEvent;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.client.property.EnumProperty;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.network.play.client.C03PacketPlayer;

public final class Regen extends Module {

    private final EnumProperty<Mode> modeProperty = new EnumProperty<>("Mode", Mode.PACKET);
    private final DoubleProperty packetsProperty = new DoubleProperty("Packets", 5.0, 1.0, 20.0, 1.0);

    public Regen() {
        super("Regen", Category.COMBAT, Category.SubCategory.COMBAT_HEALING);

        this.setSuffix(() -> this.modeProperty.getValue().toString());
        this.register(this.modeProperty, this.packetsProperty);
    }

    @EventLink
    private final Listener<UpdatePositionEvent> onUpdatePosition = event -> {
        if (event.isPre() && event.isOnGround() &&
            this.mc.thePlayer.getHealth() < this.mc.thePlayer.getMaxHealth()) {

            for (int i = 0; i < this.packetsProperty.getValue(); i++) {
                this.mc.thePlayer.sendQueue.sendPacket(new C03PacketPlayer(true));
            }
        }
    };

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    private enum Mode {
        PACKET("Packet");

        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
