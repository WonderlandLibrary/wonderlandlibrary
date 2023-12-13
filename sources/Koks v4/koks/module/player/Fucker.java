package koks.module.player;

import koks.api.event.Event;
import koks.api.registry.module.Module;
import koks.api.utils.DestroyType;
import koks.api.utils.RotationUtil;
import koks.api.utils.TimeHelper;
import koks.api.manager.value.annotation.Value;
import koks.event.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;

import java.util.HashMap;

/**
 * Copyright 2020, Koks Team
 * Please don't use the code
 */

@Module.Info(name = "Fucker", category = Module.Category.PLAYER, description = "You break automatically the block")
public class Fucker extends Module {

    private BlockPos curPos;
    private final TimeHelper timeHelper = new TimeHelper();

    boolean started;

    private final HashMap<Block, DestroyType> blocks = new HashMap<>();

    @Value(name = "Range", minimum = 5, maximum = 30)
    int range = 10;

    @Value(name = "Delay", minimum = 0, maximum = 300)
    int delay = 10;

    @Value(name = "AutomaticDelay")
    boolean automaticDelay = true;

    @Value(name = "RayCast")
    boolean rayCast = true;

    @Value(name = "ThroughWalls")
    boolean throughWalls = false;

    @Value(name = "Clamp Yaw")
    boolean clampYaw = false;

    @Value(name = "MoveFix")
    boolean moveFix = true;

    @Value(name = "NoSwing")
    boolean noSwing = false;

    @Value(name = "Bed")
    boolean bed = true;

    @Value(name = "DragonEgg")
    boolean dragonEgg = false;

    @Value(name = "Cake")
    boolean cake = false;

    @Value(name = "Beacon")
    boolean beacon = false;

    boolean hasSilentRotations;

    //TODO: ThroughWalls einstellung, Intelligent machen


    @Override
    public boolean isVisible(koks.api.manager.value.Value<?> value, String name) {
        return switch (name) {
            case "Delay" -> !automaticDelay;
            case "ThroughWalls" -> !rayCast;
            default -> super.isVisible(value, name);
        };
    }

    @Override
    @Event.Info
    public void onEvent(Event event) {
        final RotationUtil rotationUtil = RotationUtil.getInstance();

        if (event instanceof final RotationEvent rotationEvent) {
            if (curPos != null) {
                final float[] rots = rotationUtil.faceBlock(curPos, 0.0F, false, true, true, false, false, clampYaw, 360);
                rotationEvent.setYaw(rots[0]);
                rotationEvent.setPitch(rots[1]);
                hasSilentRotations = true;
            } else {
                if (hasSilentRotations) {
                    resetRotations(getYaw(), getPitch(), true);
                    hasSilentRotations = false;
                }
            }
        }

        if (event instanceof final MoveEvent moveEvent) {
            if (curPos != null && moveFix) {
                moveEvent.setYaw(getYaw());
            }
        }

        if (event instanceof final JumpEvent jumpEvent) {
            if (curPos != null && moveFix) {
                jumpEvent.setYaw(getYaw());
            }
        }

        if (event instanceof final UpdatePlayerMovementState updatePlayerMovementState) {
            if (curPos != null && moveFix) {
                updatePlayerMovementState.setSilentMoveFix(true);
                updatePlayerMovementState.setYaw(getYaw());
            }
        }

        if (event instanceof UpdateEvent) {
            if (curPos == null) {
                timeHelper.reset();
                for (int x = -range; x < range; x++)
                    for (int y = -range; y < range; y++)
                        for (int z = -range; z < range; z++) {
                            final BlockPos pos = getPlayer().getPosition().add(x, y, z);
                            final Block block = getWorld().getBlockState(pos).getBlock();
                            if (blocks.containsKey(block) && (!rayCast || getPlayer().canBlockBeSeen(pos))) {
                                curPos = pos;
                                started = false;
                            }
                        }
            } else {
                if (getPlayer().getDistance(curPos.getX(), curPos.getY(), curPos.getZ()) > range || getWorld().getBlockState(curPos).getBlock() instanceof BlockAir)
                    curPos = null;

                if (curPos != null) {
                    if (blocks.containsKey(getWorld().getBlockState(curPos).getBlock())) {
                        if (blocks.get(getWorld().getBlockState(curPos).getBlock()).equals(DestroyType.BREAK)) {
                            if (!rayCast && !throughWalls || mc.objectMouseOver.getBlockPos() != null && (throughWalls && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK || mc.objectMouseOver.getBlockPos().equals(curPos) || blocks.containsKey(getWorld().getBlockState(mc.objectMouseOver.getBlockPos()).getBlock()))) {

                                if (!started) {
                                    sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, curPos, mc.objectMouseOver.sideHit));
                                    started = true;
                                }

                                if (!noSwing)
                                    getPlayer().swingItem();

                                if (automaticDelay) {
                                    getPlayerController().onPlayerDamageBlock(curPos, mc.objectMouseOver.sideHit);
                                } else if (timeHelper.hasReached(delay)) {
                                    getPlayer().sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, curPos, mc.objectMouseOver.sideHit));
                                    timeHelper.reset();
                                }
                            }
                            if (getWorld().getBlockState(curPos).getBlock() == Blocks.air)
                                curPos = null;
                        } else if (blocks.get(getWorld().getBlockState(curPos).getBlock()).equals(DestroyType.CLICK)) {
                            if (timeHelper.hasReached(delay)) {
                                getPlayerController().clickBlock(curPos, EnumFacing.DOWN);
                                if (getWorld().getBlockState(curPos).getBlock() == Blocks.air)
                                    curPos = null;
                                timeHelper.reset();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onEnable() {
        started = false;
        curPos = null;
        blocks.clear();
        if (bed)
            blocks.put(Blocks.bed, DestroyType.BREAK);
        if (dragonEgg)
            blocks.put(Blocks.dragon_egg, DestroyType.CLICK);
        if (cake)
            blocks.put(Blocks.cake, DestroyType.CLICK);
        if (beacon)
            blocks.put(Blocks.beacon, DestroyType.BREAK);
    }

    @Override
    public void onDisable() {
        if (hasSilentRotations)
            getPlayer().rotationYaw = getYaw() - getYaw() % 360 + getPlayer().rotationYaw % 360;
        hasSilentRotations = false;
    }
}
