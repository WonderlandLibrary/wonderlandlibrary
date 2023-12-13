package vip.radium.module.impl.combat;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.MovingObjectPosition;
import vip.radium.event.impl.packet.PacketSendEvent;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.EnumProperty;
import vip.radium.property.impl.Representation;
import vip.radium.utils.MovementUtils;
import vip.radium.utils.ServerUtils;
import vip.radium.utils.TimerUtil;
import vip.radium.utils.Wrapper;

@ModuleInfo(label = "Criticals", category = ModuleCategory.COMBAT)
public final class Criticals extends Module {

    private final TimerUtil timer = new TimerUtil();
    private final EnumProperty<CriticalsMode> criticalsModeProperty = new EnumProperty<>("Mode", CriticalsMode.WATCHDOG);
    private final DoubleProperty delayProperty = new DoubleProperty("Delay", 490, 0, 1000,
            10, Representation.MILLISECONDS);
    private int groundTicks;
    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent = event -> {
        if (event.isPre())
            groundTicks = MovementUtils.isOnGround() ? groundTicks + 1 : 0;
    };

    public Criticals() {
        setSuffixListener(criticalsModeProperty);
    }

    @Override
    public void onEnable() {
        timer.reset();
        groundTicks = 0;
    }

    private boolean hasTarget() {
        if (KillAura.getInstance().getTarget() != null)
            return true;
        final MovingObjectPosition target = Wrapper.getMinecraft().objectMouseOver;
        return target != null && target.entityHit != null;
    }

    @EventLink
    public final Listener<PacketSendEvent> onPacketSendEvent = event -> {
        switch (criticalsModeProperty.getValue()) {
            case WATCHDOG:
            case NCP:
                if (event.getPacket() instanceof C0APacketAnimation && hasTarget() && ServerUtils.isOnHypixel()) {
                    if (timer.hasElapsed(delayProperty.getValue().longValue())) {
                        if (groundTicks > 1) {
                            for (double offset : criticalsModeProperty.getValue().offsets) {
                                Wrapper.sendPacketDirect(
                                        new C03PacketPlayer.C04PacketPlayerPosition(
                                                Wrapper.getPlayer().posX,
                                                Wrapper.getPlayer().posY + offset + (StrictMath.random() * 0.0003F),
                                                Wrapper.getPlayer().posZ,
                                                false));
                            }
                            timer.reset();
                        }
                    }
                }
                break;
        }
    };

    private enum CriticalsMode {
        WATCHDOG(new double[]{0.056f, 0.016f, 0.003f}),
        NCP(new double[]{0.06252f, 0.0f});

        private final double[] offsets;

        CriticalsMode(double[] offsets) {
            this.offsets = offsets;
        }
    }

}
