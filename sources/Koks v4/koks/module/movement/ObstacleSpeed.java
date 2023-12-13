package koks.module.movement;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.utils.MovementUtil;
import koks.event.UpdateEvent;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

@Module.Info(name = "ObstacleSpeed", description = "You are fast under blocks", category = Module.Category.MOVEMENT)
public class ObstacleSpeed extends Module {

    @Value(name = "Jump")
    boolean jump = true;

    @Value(name = "Spoof Jump")
    boolean spoofJump = false;

    @Value(name = "Boost")
    boolean boost = false;

    @Value(name = "KeepBoost")
    boolean keepBoost = false;

    @Value(name = "Speed", minimum = 0.1, maximum = 1)
    double speed = 0.2;

    boolean wasObstacle;

    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        return switch (name) {
            case "Speed", "KeepBoost" -> boost;
            case "Spoof Jump" -> jump;
            default -> super.isVisible(value, name);
        };
    }

    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof UpdateEvent) {
            final MovementUtil movementUtil = MovementUtil.getInstance();
            if (isMoving()) {
                if ((getWorld().getBlockState(new BlockPos(getX(), getY() + 2.5, getZ())).getBlock() != Blocks.air)) {
                    wasObstacle = true;
                    if (getPlayer().onGround) {
                        if (boost) {
                            movementUtil.setSpeed(speed);
                        }
                        if (jump)
                            if (spoofJump)
                                sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY() + 0.42, getZ(), false));
                            else
                                getPlayer().jump();
                    }
                } else {
                    if (wasObstacle && keepBoost) {
                        if (boost) {
                            movementUtil.setSpeed(speed);
                        }
                    } else {
                        wasObstacle = false;
                    }
                }
            } else {
                wasObstacle = false;
            }
        }
    }

    @Override
    public void onEnable() {
        wasObstacle = false;
    }

    @Override
    public void onDisable() {

    }
}
