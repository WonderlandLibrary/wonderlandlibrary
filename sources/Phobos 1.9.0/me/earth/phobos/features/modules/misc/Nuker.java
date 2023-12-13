package me.earth.phobos.features.modules.misc;

import me.earth.phobos.Phobos;
import me.earth.phobos.event.events.BlockEvent;
import me.earth.phobos.event.events.UpdateWalkingPlayerEvent;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;
import me.earth.phobos.util.BlockUtil;
import me.earth.phobos.util.MathUtil;
import me.earth.phobos.util.Timer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class Nuker
        extends Module {
    private final Timer timer = new Timer();
    public Setting<Boolean> rotate = this.register(new Setting<Boolean>("Rotate", false));
    public Setting<Float> distance = this.register(new Setting<Float>("Range", Float.valueOf(6.0f), Float.valueOf(0.1f), Float.valueOf(10.0f)));
    public Setting<Integer> blockPerTick = this.register(new Setting<Integer>("Blocks/Attack", 50, 1, 100));
    public Setting<Integer> delay = this.register(new Setting<Integer>("Delay/Attack", 50, 1, 1000));
    public Setting<Boolean> nuke = this.register(new Setting<Boolean>("Nuke", false));
    public Setting<Mode> mode = this.register(new Setting<Object>("Mode", Mode.NUKE, v -> this.nuke.getValue()));
    public Setting<Boolean> antiRegear = this.register(new Setting<Boolean>("AntiRegear", false));
    public Setting<Boolean> hopperNuker = this.register(new Setting<Boolean>("HopperAura", false));
    private final Setting<Boolean> autoSwitch = this.register(new Setting<Boolean>("AutoSwitch", false));
    private int oldSlot = -1;
    private boolean isMining = false;
    private Block selected;

    public Nuker() {
        super("Nuker", "Mines many blocks", Module.Category.MISC, true, false, false);
    }

    @Override
    public void onToggle() {
        this.selected = null;
    }

    @SubscribeEvent
    public void onClickBlock(BlockEvent event) {
        Block block;
        if (event.getStage() == 3 && (this.mode.getValue() == Mode.SELECTION || this.mode.getValue() == Mode.NUKE) && (block = Nuker.mc.world.getBlockState(event.pos).getBlock()) != null && block != this.selected) {
            this.selected = block;
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onUpdateWalkingPlayerPre(UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0) {
            if (this.nuke.getValue().booleanValue()) {
                BlockPos pos = null;
                switch (this.mode.getValue()) {
                    case SELECTION:
                    case NUKE: {
                        pos = this.getClosestBlockSelection();
                        break;
                    }
                    case ALL: {
                        pos = this.getClosestBlockAll();
                        break;
                    }
                }
                if (pos != null) {
                    if (this.mode.getValue() == Mode.SELECTION || this.mode.getValue() == Mode.ALL) {
                        if (this.rotate.getValue().booleanValue()) {
                            float[] angle = MathUtil.calcAngle(Nuker.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f));
                            Phobos.rotationManager.setPlayerRotations(angle[0], angle[1]);
                        }
                        if (this.canBreak(pos)) {
                            Nuker.mc.playerController.onPlayerDamageBlock(pos, Nuker.mc.player.getHorizontalFacing());
                            Nuker.mc.player.swingArm(EnumHand.MAIN_HAND);
                        }
                    } else {
                        for (int i = 0; i < this.blockPerTick.getValue(); ++i) {
                            pos = this.getClosestBlockSelection();
                            if (pos == null) continue;
                            if (this.rotate.getValue().booleanValue()) {
                                float[] angle = MathUtil.calcAngle(Nuker.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f));
                                Phobos.rotationManager.setPlayerRotations(angle[0], angle[1]);
                            }
                            if (!this.timer.passedMs(this.delay.getValue().intValue())) continue;
                            Nuker.mc.playerController.onPlayerDamageBlock(pos, Nuker.mc.player.getHorizontalFacing());
                            Nuker.mc.player.swingArm(EnumHand.MAIN_HAND);
                            this.timer.reset();
                        }
                    }
                }
            }
            if (this.antiRegear.getValue().booleanValue()) {
                this.breakBlocks(BlockUtil.shulkerList);
            }
            if (this.hopperNuker.getValue().booleanValue()) {
                ArrayList<Block> blocklist = new ArrayList<Block>();
                blocklist.add(Blocks.HOPPER);
                this.breakBlocks(blocklist);
            }
        }
    }

    public void breakBlocks(List<Block> blocks) {
        BlockPos pos = this.getNearestBlock(blocks);
        if (pos != null) {
            if (!this.isMining) {
                this.oldSlot = Nuker.mc.player.inventory.currentItem;
                this.isMining = true;
            }
            if (this.rotate.getValue().booleanValue()) {
                float[] angle = MathUtil.calcAngle(Nuker.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float) pos.getX() + 0.5f, (float) pos.getY() + 0.5f, (float) pos.getZ() + 0.5f));
                Phobos.rotationManager.setPlayerRotations(angle[0], angle[1]);
            }
            if (this.canBreak(pos)) {
                if (this.autoSwitch.getValue().booleanValue()) {
                    int newSlot = -1;
                    for (int i = 0; i < 9; ++i) {
                        ItemStack stack = Nuker.mc.player.inventory.getStackInSlot(i);
                        if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemPickaxe)) continue;
                        newSlot = i;
                        break;
                    }
                    if (newSlot != -1) {
                        Nuker.mc.player.inventory.currentItem = newSlot;
                    }
                }
                Nuker.mc.playerController.onPlayerDamageBlock(pos, Nuker.mc.player.getHorizontalFacing());
                Nuker.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        } else if (this.autoSwitch.getValue().booleanValue() && this.oldSlot != -1) {
            Nuker.mc.player.inventory.currentItem = this.oldSlot;
            this.oldSlot = -1;
            this.isMining = false;
        }
    }

    private boolean canBreak(BlockPos pos) {
        IBlockState blockState = Nuker.mc.world.getBlockState(pos);
        Block block = blockState.getBlock();
        return block.getBlockHardness(blockState, Nuker.mc.world, pos) != -1.0f;
    }

    private BlockPos getNearestBlock(List<Block> blocks) {
        double maxDist = MathUtil.square(this.distance.getValue().floatValue());
        BlockPos ret = null;
        for (double x = maxDist; x >= -maxDist; x -= 1.0) {
            for (double y = maxDist; y >= -maxDist; y -= 1.0) {
                for (double z = maxDist; z >= -maxDist; z -= 1.0) {
                    BlockPos pos = new BlockPos(Nuker.mc.player.posX + x, Nuker.mc.player.posY + y, Nuker.mc.player.posZ + z);
                    double dist = Nuker.mc.player.getDistanceSq(pos.getX(), pos.getY(), pos.getZ());
                    if (!(dist <= maxDist) || !blocks.contains(Nuker.mc.world.getBlockState(pos).getBlock()) || !this.canBreak(pos))
                        continue;
                    maxDist = dist;
                    ret = pos;
                }
            }
        }
        return ret;
    }

    private BlockPos getClosestBlockAll() {
        float maxDist = this.distance.getValue().floatValue();
        BlockPos ret = null;
        for (float x = maxDist; x >= -maxDist; x -= 1.0f) {
            for (float y = maxDist; y >= -maxDist; y -= 1.0f) {
                for (float z = maxDist; z >= -maxDist; z -= 1.0f) {
                    BlockPos pos = new BlockPos(Nuker.mc.player.posX + (double) x, Nuker.mc.player.posY + (double) y, Nuker.mc.player.posZ + (double) z);
                    double dist = Nuker.mc.player.getDistance(pos.getX(), pos.getY(), pos.getZ());
                    if (!(dist <= (double) maxDist) || Nuker.mc.world.getBlockState(pos).getBlock() == Blocks.AIR || Nuker.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid || !this.canBreak(pos) || !((double) pos.getY() >= Nuker.mc.player.posY))
                        continue;
                    maxDist = (float) dist;
                    ret = pos;
                }
            }
        }
        return ret;
    }

    private BlockPos getClosestBlockSelection() {
        float maxDist = this.distance.getValue().floatValue();
        BlockPos ret = null;
        for (float x = maxDist; x >= -maxDist; x -= 1.0f) {
            for (float y = maxDist; y >= -maxDist; y -= 1.0f) {
                for (float z = maxDist; z >= -maxDist; z -= 1.0f) {
                    BlockPos pos = new BlockPos(Nuker.mc.player.posX + (double) x, Nuker.mc.player.posY + (double) y, Nuker.mc.player.posZ + (double) z);
                    double dist = Nuker.mc.player.getDistance(pos.getX(), pos.getY(), pos.getZ());
                    if (!(dist <= (double) maxDist) || Nuker.mc.world.getBlockState(pos).getBlock() == Blocks.AIR || Nuker.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid || Nuker.mc.world.getBlockState(pos).getBlock() != this.selected || !this.canBreak(pos) || !((double) pos.getY() >= Nuker.mc.player.posY))
                        continue;
                    maxDist = (float) dist;
                    ret = pos;
                }
            }
        }
        return ret;
    }

    public enum Mode {
        SELECTION,
        ALL,
        NUKE

    }
}

