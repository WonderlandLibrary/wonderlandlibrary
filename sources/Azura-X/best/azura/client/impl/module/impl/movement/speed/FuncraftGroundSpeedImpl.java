package best.azura.client.impl.module.impl.movement.speed;

import best.azura.client.impl.Client;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.impl.events.EventJump;
import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.events.EventMove;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.api.module.ModeImpl;
import best.azura.client.impl.module.impl.movement.Speed;
import best.azura.client.api.ui.notification.Notification;
import best.azura.client.api.ui.notification.Type;
import best.azura.client.util.player.MovementUtil;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class FuncraftGroundSpeedImpl implements ModeImpl<Speed> {
    private double speed;
    private int ticks;

    @Override
    public Speed getParent() {
        return (Speed) Client.INSTANCE.getModuleManager().getModule(Speed.class);
    }

    @Override
    public String getName() {
        return "Funcraft Ground";
    }

    @Override
    public void onEnable() {
        Client.INSTANCE.getEventBus().register(this);
        speed = 0;
        ticks = 0;
    }

    @Override
    public void onDisable() {
        Client.INSTANCE.getEventBus().unregister(this);
        speed = 0;
        mc.timer.timerSpeed = 1.0f;
        ticks = 0;
    }

    @EventHandler
    public final Listener<EventMove> moveListener = e -> {
        if (!mc.thePlayer.isMoving() || mc.thePlayer.isCollidedHorizontally) speed = 0;
        MovementUtil.setSpeed(mc.thePlayer.isMoving() ? Math.max(MovementUtil.getBaseSpeed(), speed) : 0, e);
    };

    @EventHandler
    public final Listener<EventReceivedPacket> eventReceivedPacketListener = e -> {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            Client.INSTANCE.getNotificationManager().addToQueue(new Notification("Disabled", "Disabled due to lag back.", 1000, Type.INFO));
            getParent().setEnabled(false);
        }
    };

    @EventHandler
    public final Listener<EventJump> eventJumpListener = e -> {
        //e.setSpeed(0);
    };

    @EventHandler
    public final Listener<EventMotion> motionListener = e -> {
        if (!e.isPre()) return;
        if (mc.thePlayer.isMoving()) {
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
            } else {
                mc.thePlayer.motionY = -5;
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.lastTickPosY, mc.thePlayer.posZ);
            }
            speed = mc.thePlayer.getSpeed();
        }
        if (!mc.thePlayer.isMoving()) speed = 0;
    };
}