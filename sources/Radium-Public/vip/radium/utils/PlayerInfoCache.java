package vip.radium.utils;

import io.github.nevalackin.homoBus.annotations.EventLink;
import io.github.nevalackin.homoBus.Listener;
import net.minecraft.client.entity.EntityPlayerSP;
import vip.radium.RadiumClient;
import vip.radium.event.EventBusPriorities;
import vip.radium.event.impl.player.MoveEntityEvent;
import vip.radium.event.impl.player.UpdatePositionEvent;

public final class PlayerInfoCache {

    private static double lastDist;
    private static double prevLastDist;
    private static double baseMoveSpeed;

    static {
        RadiumClient.getInstance().getEventBus().subscribe(new PlayerUpdatePositionSubscriber());
    }

    public static double getPrevLastDist() {
        return prevLastDist;
    }

    public static double getLastDist() {
        return lastDist;
    }

    public static double getBaseMoveSpeed() {
        return baseMoveSpeed;
    }

    public static double getFriction(double moveSpeed) {
        return MovementUtils.calculateFriction(moveSpeed, lastDist, baseMoveSpeed);
    }

    private static class PlayerUpdatePositionSubscriber {
        @EventLink(EventBusPriorities.HIGHEST)
        private final Listener<MoveEntityEvent> onMoveEntity = event -> {
            baseMoveSpeed = MovementUtils.getBaseMoveSpeed();
        };

        @EventLink(EventBusPriorities.HIGHEST)
        private final Listener<UpdatePositionEvent> onUpdatePositionEvent = event -> {
            if (event.isPre()) {
                EntityPlayerSP player = Wrapper.getPlayer();
                double xDif = player.posX - player.lastTickPosX;
                double zDif = player.posZ - player.lastTickPosZ;
                prevLastDist = lastDist;
                lastDist = StrictMath.sqrt(xDif * xDif + zDif * zDif);
            }
        };
    }

}
