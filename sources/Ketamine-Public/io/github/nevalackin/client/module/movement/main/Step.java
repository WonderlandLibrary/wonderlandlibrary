package io.github.nevalackin.client.module.movement.main;

import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.event.game.GetTimerSpeedEvent;
import io.github.nevalackin.client.event.player.StepEvent;
import io.github.nevalackin.client.event.player.UpdatePositionEvent;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.EnumProperty;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.network.play.client.C03PacketPlayer;

public final class Step extends Module {

    private final EnumProperty<Mode> modeProperty = new EnumProperty<>("Mode", Mode.NCP);
    private final BooleanProperty lessPacketsProperty = new BooleanProperty("Less Packets", true,
                                                                            () -> this.modeProperty.getValue() == Mode.NCP);
    private final double[][] offsets = {
        {0.42f, 0.7532f},
        {0.42f + 0.005, 0.821, 0.709, 0.599, 1.022, 1.372, 1.652}
    };

    private double stepTimer;
    private boolean lessPackets;
    private int cancelledPackets;

    public Step() {
        super("Step", Category.MOVEMENT, Category.SubCategory.MOVEMENT_MAIN);

        this.setSuffix(() -> this.modeProperty.getValue().toString());

        this.register(this.modeProperty, this.lessPacketsProperty);
    }

    @EventLink
    private final Listener<StepEvent> onStep = event -> {
        if (!this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isInLava() &&
            this.mc.thePlayer.onGround && this.mc.thePlayer.isCollidedVertically &&
            !this.mc.thePlayer.isOnLadder()) {

            if (event.isPre()) {
                event.setStepHeight(1.f);
            } else if (this.modeProperty.getValue() == Mode.NCP) {
                final double steppedHeight = event.getHeightStepped();

                if (steppedHeight > this.mc.thePlayer.stepHeight) {
                    final double[] offsets = this.offsets[steppedHeight > 1.0 ? 1 : 0];

                    for (final double offset : offsets) {
                        this.mc.thePlayer.sendQueue.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
                            this.mc.thePlayer.posX,
                            this.mc.thePlayer.posY + offset * Math.min(steppedHeight, 1.0),
                            this.mc.thePlayer.posZ,
                            false
                        ));
                    }
                    this.stepTimer = 1.0 / (offsets.length + 1);
                    this.lessPackets = this.lessPacketsProperty.getValue();
                }
            }
        }
    };

    @EventLink
    private final Listener<GetTimerSpeedEvent> onGetTimer = event -> {
        if (this.lessPackets) {
            event.setTimerSpeed(this.stepTimer);
        }
    };

    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePosition = event -> {
        if (this.cancelledPackets > 1) {
            this.lessPackets = false;
            this.cancelledPackets = 0;
        }

        if (this.lessPackets) {
            this.cancelledPackets++;
        }
    };

    @Override
    public void onEnable() {
        this.cancelledPackets = 0;
        this.lessPackets = false;
    }

    @Override
    public void onDisable() {

    }

    private enum Mode {
        VANILLA("Vanilla"),
        NCP("NCP");

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
