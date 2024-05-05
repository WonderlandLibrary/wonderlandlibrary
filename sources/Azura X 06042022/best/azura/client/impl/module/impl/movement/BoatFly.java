package best.azura.client.impl.module.impl.movement;

import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.impl.events.EventReceivedPacket;
import best.azura.client.impl.events.EventRender3DPost;
import best.azura.client.impl.events.EventUpdate;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.impl.value.NumberValue;
import best.azura.client.util.render.RenderUtil;
import best.azura.eventbus.core.Event;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;

import java.awt.*;

@ModuleInfo(name = "Boat Fly", category = Category.MOVEMENT, description = "Fly using ride-able entities")
public class BoatFly extends Module {
    private boolean riding, goingToBoost;
    private int ticks;
    private final ModeValue mode = new ModeValue("Mode", "Mode of boat fly", "LongJump", "LongJump");
    private final NumberValue<Double> vertical = new NumberValue<>("Vertical", "Vertical speed value", 1.0, 0.0,  9.0);
    private final NumberValue<Double> speed = new NumberValue<>("Speed", "Horizontal speed value", 1.0, 0.0,  9.0);
    @EventHandler
    public final Listener<Event> eventListener = this::handle;
    private void handle(Event event) {
        setSuffix(mode.getObject());
        switch (mode.getObject()) {
            case "LongJump":
                handleLongJumpMode(event);
                break;
            case "VanillaFly":
                break;
        }
    }
    private void handleLongJumpMode(final Event event) {
        if (event instanceof EventRender3DPost && mc.thePlayer.isRiding()) {
            double yaw = mc.thePlayer.getDirection();
            double endX = -Math.sin(yaw) * speed.getObject(), endY = vertical.getObject(), endZ = Math.cos(yaw) * speed.getObject();
            boolean broken = false;
            for (double s = 0; s <= speed.getObject() && !broken; s += 0.1) {
                double x = -Math.sin(yaw) * s;
                double z = Math.cos(yaw) * s;
                for (double y = vertical.getObject(); y >= 0 && !broken; y -= 0.1) {
                    if (!mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().maxY +
                            mc.thePlayer.ridingEntity.height, mc.thePlayer.posZ).add(x, y, z))) {
                        endX = x;
                        endZ = z;
                        endY = y;
                        broken = true;
                    } else {
                        endX += x;
                        endZ += z;
                        endX *= 0.91;
                        endZ *= 0.91;
                    }
                }
            }
            final double x = endX,
                    y = endY,
                    z = endZ;
            RenderUtil.INSTANCE.renderBox(x, y, z, 0.5f, 1.0f, new Color(255, 255, 255), false);
        }
        if (event instanceof EventUpdate) {
            if (mc.thePlayer.isRiding()) {
                riding = true;
                ticks = 0;
                goingToBoost = true;
            }
            if (goingToBoost && !mc.thePlayer.isRiding() && ticks == 0) {
                goingToBoost = false;
                mc.thePlayer.setSpeed(speed.getObject());
                mc.thePlayer.motionY = vertical.getObject();
                ticks++;
            }
        }
        if (event instanceof EventReceivedPacket) {
            EventReceivedPacket e = (EventReceivedPacket) event;
            if (e.getPacket() instanceof S08PacketPlayerPosLook && riding && !mc.thePlayer.isRiding()) {
                riding = false;
            }
        }
    }
}