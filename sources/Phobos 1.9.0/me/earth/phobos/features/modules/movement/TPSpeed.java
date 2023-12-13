package me.earth.phobos.features.modules.movement;

import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.MathUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

public class TPSpeed
        extends Module {
    private final Setting<Mode> mode = this.register(new Setting<Mode>("Mode", Mode.NORMAL));
    private final Setting<Double> speed = this.register(new Setting<Double>("Speed", 0.25, 0.1, 10.0));
    private final Setting<Double> fallSpeed = this.register(new Setting<Object>("FallSpeed", Double.valueOf(0.25), Double.valueOf(0.1), Double.valueOf(10.0), v -> this.mode.getValue() == Mode.STEP));
    private final Setting<Boolean> turnOff = this.register(new Setting<Boolean>("Off", false));
    private final Setting<Integer> tpLimit = this.register(new Setting<Object>("Limit", 2, 1, 10, v -> this.turnOff.getValue(), "Turn it off."));
    private int tps = 0;
    private final double[] selectedPositions = new double[]{0.42, 0.75, 1.0};

    public TPSpeed() {
        super("TpSpeed", "Teleports you.", Module.Category.MOVEMENT, true, false, false);
    }

    private static boolean collidesHorizontally(AxisAlignedBB bb) {
        if (TPSpeed.mc.world.collidesWithAnyBlock(bb)) {
            Vec3d center = bb.getCenter();
            BlockPos blockpos = new BlockPos(center.x, bb.minY, center.z);
            return TPSpeed.mc.world.isBlockFullCube(blockpos.west()) || TPSpeed.mc.world.isBlockFullCube(blockpos.east()) || TPSpeed.mc.world.isBlockFullCube(blockpos.north()) || TPSpeed.mc.world.isBlockFullCube(blockpos.south()) || TPSpeed.mc.world.isBlockFullCube(blockpos);
        }
        return false;
    }

    @Override
    public void onEnable() {
        this.tps = 0;
    }

    @SubscribeEvent
    public void onUpdatePlayerWalking(UpdateWalkingPlayerEvent event) {
        if (event.getStage() != 0) {
            return;
        }
        if (this.mode.getValue() == Mode.NORMAL) {
            if (this.turnOff.getValue().booleanValue() && this.tps >= this.tpLimit.getValue()) {
                this.disable();
                return;
            }
            if (TPSpeed.mc.player.moveForward != 0.0f || TPSpeed.mc.player.moveStrafing != 0.0f && TPSpeed.mc.player.onGround) {
                for (double x = 0.0625; x < this.speed.getValue(); x += 0.262) {
                    double[] dir = MathUtil.directionSpeed(x);
                    TPSpeed.mc.player.connection.sendPacket(new CPacketPlayer.Position(TPSpeed.mc.player.posX + dir[0], TPSpeed.mc.player.posY, TPSpeed.mc.player.posZ + dir[1], TPSpeed.mc.player.onGround));
                }
                TPSpeed.mc.player.connection.sendPacket(new CPacketPlayer.Position(TPSpeed.mc.player.posX + TPSpeed.mc.player.motionX, 0.0, TPSpeed.mc.player.posZ + TPSpeed.mc.player.motionZ, TPSpeed.mc.player.onGround));
                ++this.tps;
            }
        } else if ((TPSpeed.mc.player.moveForward != 0.0f || TPSpeed.mc.player.moveStrafing != 0.0f) && TPSpeed.mc.player.onGround) {
            double pawnY = 0.0;
            double[] lastStep = MathUtil.directionSpeed(0.262);
            for (double x = 0.0625; x < this.speed.getValue(); x += 0.262) {
                double[] dir = MathUtil.directionSpeed(x);
                AxisAlignedBB bb = Objects.requireNonNull(TPSpeed.mc.player.getEntityBoundingBox()).offset(dir[0], pawnY, dir[1]);
                while (TPSpeed.collidesHorizontally(bb)) {
                    for (double position : this.selectedPositions) {
                        TPSpeed.mc.player.connection.sendPacket(new CPacketPlayer.Position(TPSpeed.mc.player.posX + dir[0] - lastStep[0], TPSpeed.mc.player.posY + pawnY + position, TPSpeed.mc.player.posZ + dir[1] - lastStep[1], true));
                    }
                    bb = Objects.requireNonNull(TPSpeed.mc.player.getEntityBoundingBox()).offset(dir[0], pawnY += 1.0, dir[1]);
                }
                if (!TPSpeed.mc.world.checkBlockCollision(bb.grow(0.0125, 0.0, 0.0125).offset(0.0, -1.0, 0.0))) {
                    for (double i = 0.0; i <= 1.0; i += this.fallSpeed.getValue().doubleValue()) {
                        TPSpeed.mc.player.connection.sendPacket(new CPacketPlayer.Position(TPSpeed.mc.player.posX + dir[0], TPSpeed.mc.player.posY + pawnY - i, TPSpeed.mc.player.posZ + dir[1], true));
                    }
                    pawnY -= 1.0;
                }
                TPSpeed.mc.player.connection.sendPacket(new CPacketPlayer.Position(TPSpeed.mc.player.posX + dir[0], TPSpeed.mc.player.posY + pawnY, TPSpeed.mc.player.posZ + dir[1], TPSpeed.mc.player.onGround));
            }
            TPSpeed.mc.player.connection.sendPacket(new CPacketPlayer.Position(TPSpeed.mc.player.posX + TPSpeed.mc.player.motionX, 0.0, TPSpeed.mc.player.posZ + TPSpeed.mc.player.motionZ, TPSpeed.mc.player.onGround));
        }
    }

    public enum Mode {
        NORMAL,
        STEP

    }
}

