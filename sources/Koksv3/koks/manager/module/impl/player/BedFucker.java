package koks.manager.module.impl.player;

import koks.api.settings.Setting;
import koks.api.util.DestroyType;
import koks.manager.event.Event;
import koks.manager.event.impl.EventJump;
import koks.manager.event.impl.EventMotion;
import koks.manager.event.impl.EventMoveFlying;
import koks.manager.event.impl.EventUpdate;
import koks.manager.module.Module;
import koks.manager.module.ModuleInfo;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

import java.util.HashMap;

/**
 * @author kroko
 * @created on 15.11.2020 : 00:35
 */

@ModuleInfo(name = "BedFucker", category = Module.Category.PLAYER, description = "You break automatically the block")
public class BedFucker extends Module {

    public HashMap<Block, DestroyType> blocks = new HashMap<>();

    public BlockPos curPos;
    public float curYaw, curPitch;

    public Setting range = new Setting("Range", 10, 5, 30, true, this);
    public Setting delay = new Setting("Delay", 10, 0, 300, true, this);

    //TODO: ThroughWalls einstellung, Intelligent machen und nur abbauen wenn er ihn anguckt

    public BedFucker() {
        blocks.put(Blocks.bed, DestroyType.BREAK);
        blocks.put(Blocks.dragon_egg, DestroyType.CLICK);
        blocks.put(Blocks.cake, DestroyType.CLICK);
        blocks.put(Blocks.beacon, DestroyType.BREAK);
    }

    @Override
    public void onEvent(Event event) {

        if (!this.isToggled())
            return;

        if (event instanceof EventMotion) {
            if (curPos != null) {
                if (((EventMotion) event).getType() == EventMotion.Type.PRE) {
                    float[] rots = rotationUtil.faceBlock(curPos, 0.0F, curYaw, curPitch, 360);
                    ((EventMotion) event).setYaw(rots[0]);
                    ((EventMotion) event).setPitch(rots[1]);
                    curYaw = rots[0];
                    curPitch = rots[1];
                }
            }
        }

        if (event instanceof EventMoveFlying) {
            if (curPos != null)
                ((EventMoveFlying) event).setYaw(curYaw);
        }

        if (event instanceof EventJump) {
            if (curPos != null)
                ((EventJump) event).setYaw(curYaw);
        }

        if (event instanceof EventUpdate) {
            int range = (int) this.range.getCurrentValue();
            if (curPos == null) {
                timeHelper.reset();
                for (int x = -range; x < range; x++)
                    for (int y = -range; y < range; y++)
                        for (int z = -range; z < range; z++) {
                            BlockPos pos = getPosition().add(x, y, z);
                            Block block = getWorld().getBlockState(pos).getBlock();
                            if (blocks.containsKey(block)) {
                                curPos = pos;
                            }
                        }
            } else {

                if (getPlayer().getDistance(curPos.getX(), curPos.getY(), curPos.getZ()) > range)
                    curPos = null;

                if (curPos != null) {
                    if (blocks.containsKey(getWorld().getBlockState(curPos).getBlock())) {
                        if (blocks.get(getWorld().getBlockState(curPos).getBlock()).equals(DestroyType.BREAK)) {
                            getPlayer().sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, curPos, EnumFacing.DOWN));
                            if (timeHelper.hasReached((long) delay.getCurrentValue())) {
                                getPlayer().sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, curPos, EnumFacing.DOWN));
                                if (getWorld().getBlockState(curPos).getBlock() == Blocks.air)
                                    curPos = null;
                                timeHelper.reset();
                            }
                        } else if (blocks.get(getWorld().getBlockState(curPos).getBlock()).equals(DestroyType.CLICK)) {
                            if (timeHelper.hasReached((long) delay.getCurrentValue())) {
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
        timeHelper.reset();
        curPos = null;
        curYaw = getPlayer().rotationYaw;
        curPitch = getPlayer().rotationPitch;
    }

    @Override
    public void onDisable() {

    }
}
