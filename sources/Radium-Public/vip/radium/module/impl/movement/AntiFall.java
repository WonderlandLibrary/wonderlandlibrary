package vip.radium.module.impl.movement;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.network.play.client.C03PacketPlayer;
import vip.radium.event.impl.player.UpdatePositionEvent;
import vip.radium.module.Module;
import vip.radium.module.ModuleCategory;
import vip.radium.module.ModuleInfo;
import vip.radium.module.ModuleManager;
import vip.radium.property.impl.DoubleProperty;
import vip.radium.property.impl.EnumProperty;
import vip.radium.property.impl.Representation;
import vip.radium.utils.MovementUtils;
import vip.radium.utils.TimerUtil;
import vip.radium.utils.Wrapper;

@ModuleInfo(label = "Anti Fall", category = ModuleCategory.MOVEMENT)
public final class AntiFall extends Module {

    private final TimerUtil timer = new TimerUtil();

    private final EnumProperty<NoVoidMode> noVoidModeProperty = new EnumProperty<>("Mode", NoVoidMode.PACKET);
    private final DoubleProperty distProperty = new DoubleProperty("Distance", 5.0, 3.0, 10.0,
            0.5, Representation.DISTANCE);

    public AntiFall() {
        setSuffixListener(noVoidModeProperty);
    }

    @EventLink
    public final Listener<UpdatePositionEvent> onUpdatePositionEvent = event -> {
        if (event.isPre()) {
            if (!ModuleManager.getInstance(Flight.class).isEnabled() &&
                    Wrapper.getPlayer().fallDistance > distProperty.getValue().floatValue() &&
                    !MovementUtils.isOnGround() &&
                    timer.hasElapsed(500L) &&
                    MovementUtils.isOverVoid()) {
                switch (noVoidModeProperty.getValue()) {
                    case PACKET:
                        Wrapper.sendPacketDirect(
                                new C03PacketPlayer.C06PacketPlayerPosLook(
                                        event.getPosX(),
                                        event.getPosY() + 11.0 + StrictMath.random(),
                                        event.getPosZ(),
                                        event.getYaw(),
                                        event.getPitch(),
                                        false));
                        break;
                    case MOTION:
                        if (Wrapper.getPlayer().motionY < 0.0F)
                            Wrapper.getPlayer().motionY = 2.2F;
                        break;
                }
                Wrapper.getPlayer().fallDistance = 0.0f;
                timer.reset();
            }
        }
    };

    private enum NoVoidMode {
        PACKET, MOTION
    }
}
