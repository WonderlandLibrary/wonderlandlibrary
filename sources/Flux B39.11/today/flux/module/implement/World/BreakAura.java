package today.flux.module.implement.World;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.Priority;
import net.minecraft.block.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import today.flux.event.PostUpdateEvent;
import today.flux.event.PreUpdateEvent;
import today.flux.module.Category;
import today.flux.module.Module;
import today.flux.module.ModuleManager;
import today.flux.module.implement.Combat.KillAura;
import today.flux.module.value.BooleanValue;
import today.flux.module.value.FloatValue;
import today.flux.utility.BlockUtils;
import today.flux.utility.TimeHelper;

public class BreakAura extends Module {
    private BlockPos globalPos;

    public static FloatValue range = new FloatValue("BreakAura", "Range", 4.0f, 1.0f, 6.0f, 1f);
    public static BooleanValue EnderStone = new BooleanValue("BreakAura", "EnderStone", false);
    public static BooleanValue Bed = new BooleanValue("BreakAura", "Bed", false);
    public static BooleanValue Cake = new BooleanValue("BreakAura", "Cake", false);
    public static BooleanValue Egg = new BooleanValue("BreakAura", "DragonEgg", false);
    public static BooleanValue Chest = new BooleanValue("BreakAura", "Chest", false);
    public static BooleanValue Ore = new BooleanValue("BreakAura", "Ore", false);
    public static TimeHelper timer = new TimeHelper();
    public BreakAura() {
        super("BreakAura", Category.World, false);
    }

    @EventTarget(Priority.LOW)
    private void onUpdatePre(final PreUpdateEvent event) {
        if (event.isModified()) {
            globalPos = null;
            return;
        }

        globalPos = null;

        if (event.isModified() || mc.thePlayer.ticksExisted % 20 == 0 || KillAura.target != null || mc.currentScreen instanceof GuiContainer) {
            return;
        }

        float radius;
        float y = radius = range.getValue() + 2;
        while (y >= -radius) {
            float x = -radius;
            while (x <= radius) {
                float z = -radius;
                while (z <= radius) {
                    BlockPos pos = new BlockPos(mc.thePlayer.posX - 0.5 + (double) x, mc.thePlayer.posY - 0.5 + (double) y, mc.thePlayer.posZ - 0.5 + (double) z);
                    Block block = mc.theWorld.getBlockState(pos).getBlock();
                    if (this.getFacingDirection(pos) != null && mc.thePlayer.getDistance(mc.thePlayer.posX + (double) x, mc.thePlayer.posY + (double) y, mc.thePlayer.posZ + (double) z) < (double) mc.playerController.getBlockReachDistance() && isValidBlock(block)) {
                        float[] rotations = BlockUtils.getBlockRotations(pos.getX(), pos.getY(), pos.getZ());
                        event.setYaw(rotations[0]);
                        event.setPitch(rotations[1]);
                        this.globalPos = pos;
                        return;
                    }
                    ++z;
                }
                ++x;
            }
            --y;
        }

    }

    @EventTarget
    public void onUpdatePost(PostUpdateEvent event) {
        if (this.globalPos != null && !(mc.currentScreen instanceof GuiContainer)/*&& !DevAura.isEntityValid(DevAura.currentTarget)*/) {
            ModuleManager.killAuraMod.disableHelper.reset();
            EnumFacing direction;
            if ((direction = this.getFacingDirection(this.globalPos)) != null) {
                if (this.mc.playerController.getBlockHitDelay() > 1) {
                    this.mc.playerController.setBlockHitDelay(1);
                }

                if (timer.isDelayComplete(500)) {
                    //destroy!
                    mc.thePlayer.sendQueue.addToSendQueue(
                            new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, globalPos, EnumFacing.DOWN));
                    mc.thePlayer.sendQueue
                            .addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, globalPos, EnumFacing.DOWN));
                    mc.thePlayer.sendQueue.addToSendQueue(
                            new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, globalPos, EnumFacing.DOWN));
                    mc.thePlayer.swingItem();
                    timer.reset();
                }
            }
        }
    }

    private boolean isValidBlock(Block block) {

        if (Block.getIdFromBlock(block) == 121 && EnderStone.getValue())
            return true;

        if (Block.getIdFromBlock(block) == 26 && Bed.getValue())
            return true;

        if (block instanceof BlockDragonEgg && Egg.getValue())
            return true;

        if (block instanceof BlockCake && Cake.getValue())
            return true;

        if (block instanceof BlockOre && Ore.getValue())
            return true;

        if (block instanceof BlockChest && Chest.getValue())
            return true;

        return false;
    }

    private EnumFacing getFacingDirection(BlockPos pos) {
        EnumFacing direction = null;
        if (!mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isBlockNormalCube()) {
            direction = EnumFacing.UP;
        } else if (!mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock().isBlockNormalCube()) {
            direction = EnumFacing.DOWN;
        } else if (!mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock().isBlockNormalCube()) {
            direction = EnumFacing.EAST;
        } else if (!mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock().isBlockNormalCube()) {
            direction = EnumFacing.WEST;
        } else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isBlockNormalCube()) {
            direction = EnumFacing.SOUTH;
        } else if (!mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock().isBlockNormalCube()) {
            direction = EnumFacing.NORTH;
        }
        return direction;
    }
}
