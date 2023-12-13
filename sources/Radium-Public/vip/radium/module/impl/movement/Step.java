package vip.radium.module.impl.movement;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.network.play.client.C03PacketPlayer;
import vip.radium.event.impl.packet.PacketSendEvent;
import vip.radium.event.impl.player.StepEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.property.Property;
import vip.radium.utils.MovementUtils;
import vip.radium.utils.Wrapper;

@ModuleInfo(label = "Step", category = ModuleCategory.MOVEMENT)
public final class Step extends Module {

    public static boolean cancelStep;
    private final Property<Boolean> cancelExtraPackets = new Property<>("Less Packets", true);
    private final double[] offsets = {0.42f, 0.7532f};
    private float timerWhenStepping;
    private boolean cancelMorePackets;
    private byte cancelledPackets;

    @Override
    public void onDisable() {
        Wrapper.getTimer().timerSpeed = 1.0f;
    }

    @EventLink
    public final Listener<StepEvent> onStepEvent = e -> {
        if (!MovementUtils.isInLiquid() && MovementUtils.isOnGround() && !MovementUtils.isOnStairs()) {
            if (e.isPre()) {
                e.setStepHeight(cancelStep ? 0.0f : 1.0F);
            } else {
                double steppedHeight = e.getHeightStepped();
                for (double offset : offsets) {
                    Wrapper.sendPacketDirect(new C03PacketPlayer.C04PacketPlayerPosition(
                        Wrapper.getPlayer().posX,
                        Wrapper.getPlayer().posY + (offset * steppedHeight),
                        Wrapper.getPlayer().posZ,
                        false));
                }
                timerWhenStepping = 1.0f / (offsets.length + 1);
                cancelMorePackets = true;
            }
        }
    };

    @EventLink
    public final Listener<PacketSendEvent> onPacketSendEvent = e -> {
        if (cancelExtraPackets.getValue() && e.getPacket() instanceof C03PacketPlayer) {
            if (cancelledPackets > 0) {
                cancelMorePackets = false;
                cancelledPackets = 0;
                Wrapper.getTimer().timerSpeed = 1.0f;
            }

            if (cancelMorePackets) {
                Wrapper.getTimer().timerSpeed = timerWhenStepping;
                cancelledPackets++;
            }
        }
    };
}
