package best.azura.client.impl.module.impl.movement.clicktp;

import best.azura.client.impl.Client;
import best.azura.client.impl.events.EventMotion;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Teleport;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.util.math.CustomVec3;
import best.azura.client.util.pathing.Path;
import best.azura.client.util.pathing.PathUtil;
import best.azura.client.util.player.RaytraceUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MovingObjectPosition;

public class RedeDarkClickTPImpl implements ModeImpl<Teleport> {

    @Override
    public Teleport getParent() {
        return (Teleport) Client.INSTANCE.getModuleManager().getModule(Teleport.class);
    }

    @Override
    public String getName() {
        return "Rede Dark";
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onEvent(final Event event) {
        if (event instanceof EventMotion) {
            final EventMotion e = (EventMotion) event;
            if (e.isPre()) {
                final MovingObjectPosition position = RaytraceUtil.rayTrace(200, e.yaw, e.pitch);
                if (mc.gameSettings.keyBindAttack.pressed && position.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && !mc.theWorld.isAirBlock(position.getBlockPos())) {
                    mc.gameSettings.keyBindAttack.pressed = false;
                    if (!mc.thePlayer.onGround) {
                        Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Click TP", "Please stand on ground when selecting...", 4000, Type.WARNING));
                        return;
                    }
                    final CustomVec3 target = new CustomVec3(position.getBlockPos());
                    target.setY(mc.thePlayer.posY);
                    Path path = PathUtil.findPath(new CustomVec3(mc.thePlayer), target, 0.15);
                    for (CustomVec3 v : path.getAdditionalVectors())
                        mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(e.x = v.getX(), e.y = v.getY(), e.z = v.getZ(), true));
                    mc.thePlayer.setPosition(target.getX(), target.getY(), target.getZ());
                }
            }
        }
    }
}