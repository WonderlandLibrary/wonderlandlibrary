package best.azura.client.impl.module.impl.movement;

import best.azura.client.impl.events.EventMotion;
import best.azura.client.impl.value.ModeValue;
import best.azura.client.util.math.MathUtil;
import best.azura.eventbus.handler.EventHandler;
import best.azura.eventbus.handler.Listener;
import best.azura.client.api.module.Category;
import best.azura.client.api.module.Module;
import best.azura.client.api.module.ModuleInfo;
import best.azura.client.util.player.MovementUtil;
import net.minecraft.util.BlockPos;

@ModuleInfo(name = "Phase", category = Category.MOVEMENT, description = "Clip through walls")
public class Phase extends Module {

    private final ModeValue modeValue = new ModeValue("Mode", "Mode for phasing", "Old Watchdog",  "Old Watchdog", "Mercury");

    @EventHandler
    public final Listener<EventMotion> eventMotionListener = e -> {
        switch (modeValue.getObject()) {
            case "Old Watchdog":
                if (e.isPre()) {

                    if (mc.thePlayer.isSneaking()) {
                        MovementUtil.vClip((float)-4);
                        this.setEnabled(false);
                    }
                    boolean isInWall = false;
                    if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.isMoving()) {
                        double endX = 0;
                        double endZ = 0;
                        for (double d = 0; d <= 1; d += 0.02) {
                            double x = -Math.sin(mc.thePlayer.getDirection()) * d;
                            double z = Math.cos(mc.thePlayer.getDirection()) * d;
                            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(x, 0, z)).isEmpty()) {
                                endX = x;
                                endZ = z;
                                mc.thePlayer.setEntityBoundingBox(mc.thePlayer.getEntityBoundingBox().offset(x * 0.001, 0, z * 0.001));
                            }
                        }
                        mc.thePlayer.setEntityBoundingBox(mc.thePlayer.getEntityBoundingBox().offset(endX * 0.01, 0, endZ * 0.01));
                    }
                    if (mc.thePlayer.isMoving()) {
                        for (double d = 0; d <= 0.1; d += 0.02) {
                            double x = -Math.sin(mc.thePlayer.getDirection()) * d;
                            double z = Math.cos(mc.thePlayer.getDirection()) * d;
                            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(x, 0, z)).isEmpty()) {
                                isInWall = true;
                            }
                        }
                        if (isInWall) {
                            if (mc.thePlayer.onGround) {
                                double x = -Math.sin(mc.thePlayer.getDirection()) * 0.2873;
                                double z = Math.cos(mc.thePlayer.getDirection()) * 0.2873;
                                mc.thePlayer.setEntityBoundingBox(mc.thePlayer.getEntityBoundingBox().offset(x, 0, z));
                            }
                        }
                    }
                }
                break;
            case "Mercury":
                if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.isMoving()) {
                    if (e.isPre() && mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)))
                        e.y -= 1;
                    if (e.isPost()) {
                        if (!mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ)) &&
                                !mc.theWorld.isAirBlock(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.height, mc.thePlayer.posZ))) {
                            MovementUtil.vClip(1);
                        }
                        double endX = 0;
                        double endZ = 0;
                        for (double d = 0; d <= 1; d += 0.02) {
                            double x = -Math.sin(mc.thePlayer.getDirection()) * d;
                            double z = Math.cos(mc.thePlayer.getDirection()) * d;
                            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(x, 0, z)).isEmpty()) {
                                endX = x;
                                endZ = z;
                                mc.thePlayer.setEntityBoundingBox(mc.thePlayer.getEntityBoundingBox().offset(x * 0.001, 0, z * 0.001));
                            }
                        }
                    }
                }
                break;
        }
    };

}