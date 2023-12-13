package me.earth.phobos.features.modules.player;

import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class Yaw
        extends Module {
    public Setting<Boolean> lockYaw = this.register(new Setting<Boolean>("LockYaw", false));
    public Setting<Boolean> byDirection = this.register(new Setting<Boolean>("ByDirection", false));
    public Setting<Direction> direction = this.register(new Setting<Object>("Direction", Direction.NORTH, v -> this.byDirection.getValue()));
    public Setting<Integer> yaw = this.register(new Setting<Object>("Yaw", Integer.valueOf(0), Integer.valueOf(-180), Integer.valueOf(180), v -> this.byDirection.getValue() == false));
    public Setting<Boolean> lockPitch = this.register(new Setting<Boolean>("LockPitch", false));
    public Setting<Integer> pitch = this.register(new Setting<Integer>("Pitch", 0, -180, 180));

    public Yaw() {
        super("Yaw", "Locks your yaw", Module.Category.PLAYER, true, false, false);
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayer(UpdateWalkingPlayerEvent event) {
        if (this.lockYaw.getValue().booleanValue()) {
            if (this.byDirection.getValue().booleanValue()) {
                switch (this.direction.getValue()) {
                    case NORTH: {
                        this.setYaw(180);
                        break;
                    }
                    case NE: {
                        this.setYaw(225);
                        break;
                    }
                    case EAST: {
                        this.setYaw(270);
                        break;
                    }
                    case SE: {
                        this.setYaw(315);
                        break;
                    }
                    case SOUTH: {
                        this.setYaw(0);
                        break;
                    }
                    case SW: {
                        this.setYaw(45);
                        break;
                    }
                    case WEST: {
                        this.setYaw(90);
                        break;
                    }
                    case NW: {
                        this.setYaw(135);
                    }
                }
            } else {
                this.setYaw(this.yaw.getValue());
            }
        }
        if (this.lockPitch.getValue().booleanValue()) {
            if (Yaw.mc.player.isRiding()) {
                Objects.requireNonNull(Yaw.mc.player.getRidingEntity()).rotationPitch = this.pitch.getValue().intValue();
            }
            Yaw.mc.player.rotationPitch = this.pitch.getValue().intValue();
        }
    }

    private void setYaw(int yaw) {
        if (Yaw.mc.player.isRiding()) {
            Objects.requireNonNull(Yaw.mc.player.getRidingEntity()).rotationYaw = yaw;
        }
        Yaw.mc.player.rotationYaw = yaw;
    }

    public enum Direction {
        NORTH,
        NE,
        EAST,
        SE,
        SOUTH,
        SW,
        WEST,
        NW

    }
}

