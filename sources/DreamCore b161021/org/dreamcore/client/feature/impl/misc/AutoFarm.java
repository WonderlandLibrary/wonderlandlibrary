package org.dreamcore.client.feature.impl.misc;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.dreamcore.client.event.EventTarget;
import org.dreamcore.client.event.events.impl.packet.EventReceivePacket;
import org.dreamcore.client.event.events.impl.player.EventPreMotion;
import org.dreamcore.client.feature.Feature;
import org.dreamcore.client.feature.impl.Type;
import org.dreamcore.client.helpers.math.RotationHelper;
import org.dreamcore.client.helpers.misc.TimerHelper;
import org.dreamcore.client.helpers.world.BlockHelper;
import org.dreamcore.client.helpers.world.EntityHelper;
import org.dreamcore.client.settings.impl.BooleanSetting;
import org.dreamcore.client.settings.impl.NumberSetting;

import java.util.ArrayList;
import java.util.Comparator;

public class AutoFarm extends Feature {

    private final NumberSetting delay;
    private final NumberSetting radius;
    private final BooleanSetting autoHoe;
    private final BooleanSetting autoFarm;
    ArrayList<BlockPos> crops = new ArrayList<>();
    ArrayList<BlockPos> check = new ArrayList<>();
    TimerHelper timerHelper = new TimerHelper();
    TimerHelper timerHelper2 = new TimerHelper();

    public AutoFarm() {
        super("Auto Farm", "Автоматически садит и ломает урожай", Type.Misc);
        autoFarm = new BooleanSetting("Auto Farm", true, () -> true);
        autoHoe = new BooleanSetting("Auto Hoe", false, () -> true);
        delay = new NumberSetting("Farm Delay", 2, 0, 10, 0.1F, () -> true);
        radius = new NumberSetting("Farm Radius", 4, 1, 7, 0.1F, () -> true);
        addSettings(autoFarm, autoHoe, delay, radius);
    }

    public static boolean doesHaveSeeds() {
        for (int i = 0; i < 9; ++i) {
            mc.player.inventory.getStackInSlot(i);
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemSeeds) {
                return true;
            }
        }
        return false;
    }

    public static int searchSeeds() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() instanceof ItemSeeds) {
                return i;
            }
        }
        return -1;
    }

    public static int getSlotWithSeeds() {
        for (int i = 0; i < 9; ++i) {
            mc.player.inventory.getStackInSlot(i);
            if (mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemSeeds) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onEnable() {
        crops.clear();
        check.clear();
        super.onEnable();
    }

    private boolean isOnCrops() {
        for (double x = mc.player.boundingBox.minX; x < mc.player.boundingBox.maxX; x += 0.01f) {
            for (double z = mc.player.boundingBox.minZ; z < mc.player.boundingBox.maxZ; z += 0.01f) {
                Block block = mc.world.getBlockState(new BlockPos(x, mc.player.posY - 0.1, z)).getBlock();
                if (!(block instanceof BlockFarmland || block instanceof BlockCarrot || block instanceof BlockSoulSand || block instanceof BlockSand) && !(block instanceof BlockAir)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean IsValidBlockPos(BlockPos pos) {
        IBlockState state = mc.world.getBlockState(pos);
        if ((state).getBlock() instanceof BlockFarmland || state.getBlock() instanceof BlockSand || state.getBlock() instanceof BlockSoulSand)
            return mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR;
        return false;
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (mc.player == null && mc.world == null) return;
        BlockPos pos = BlockHelper.getSphere(BlockHelper.getPlayerPos(), radius.getNumberValue(), 6, false, true).stream().filter(BlockHelper::IsValidBlockPos).min(Comparator.comparing(blockPos -> EntityHelper.getDistanceOfEntityToBlock(mc.player, blockPos))).orElse(null);
        if (pos != null && mc.player.getHeldItemMainhand().getItem() instanceof ItemHoe) {
            float[] rots = RotationHelper.getRotationVector(new Vec3d(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F), true, 2, 2, 360);
            event.setYaw(rots[0]);
            event.setPitch(rots[1]);
            mc.player.renderYawOffset = rots[0];
            mc.player.rotationYawHead = rots[0];
            mc.player.rotationPitchHead = rots[1];
            if (timerHelper2.hasReached(delay.getNumberValue() * 100)) {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.MAIN_HAND, 0, 0, 0));
                mc.player.swingArm(EnumHand.MAIN_HAND);
                timerHelper2.reset();
            }
        }


        if (!doesHaveSeeds()) {
            if (searchSeeds() != -1) {
                mc.playerController.windowClick(0, searchSeeds(), 1, ClickType.QUICK_MOVE, mc.player);
            }
        }
    }

    @EventTarget
    public void onPre(EventPreMotion e) {
        if (autoFarm.getBoolValue()) {
            ArrayList<BlockPos> blockPositions = getBlocks(radius.getNumberValue(), radius.getNumberValue(), radius.getNumberValue());
            for (BlockPos pos : blockPositions) {
                IBlockState state = mc.world.getBlockState(pos);
                if (isCheck(Block.getIdFromBlock(state.getBlock()))) {
                    if (!isCheck(0)) {
                        check.add(pos);
                    }
                    Block block = mc.world.getBlockState(pos).getBlock();
                    BlockPos downPos = pos.down(1);
                    if (block instanceof BlockCrops || block instanceof BlockCarrot) {
                        BlockCrops crop = (BlockCrops) block;
                        if (!crop.canGrow(mc.world, pos, state, true)) {
                            if (timerHelper.hasReached(delay.getNumberValue() * 100)) {
                                if (pos != null) {
                                    float[] rots = RotationHelper.getRotationVector(new Vec3d(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F), true, 2, 2, 360);
                                    e.setYaw(rots[0]);
                                    e.setPitch(rots[1]);
                                    mc.player.renderYawOffset = rots[0];
                                    mc.player.rotationYawHead = rots[0];
                                    mc.player.rotationPitchHead = rots[1];
                                    mc.playerController.onPlayerDamageBlock(pos, mc.player.getHorizontalFacing());
                                    mc.player.swingArm(EnumHand.MAIN_HAND);

                                    if (doesHaveSeeds()) {
                                        mc.player.connection.sendPacket(new CPacketHeldItemChange(getSlotWithSeeds()));
                                        mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(downPos, EnumFacing.UP, EnumHand.MAIN_HAND, 0, 0, 0));
                                        mc.player.swingArm(EnumHand.MAIN_HAND);
                                    }

                                    timerHelper.reset();
                                }
                            }
                        }
                    }
                }
            }
        }
        BlockPos pos = BlockHelper.getSphere(BlockHelper.getPlayerPos(), radius.getNumberValue(), 6, false, true).stream().filter(this::IsValidBlockPos).min(Comparator.comparing(blockPos -> EntityHelper.getDistanceOfEntityToBlock(mc.player, blockPos))).orElse(null);
        Vec3d vec = new Vec3d(0, 0, 0);
        if (timerHelper.hasReached(delay.getNumberValue() * 100)) {
            if (isOnCrops()) {
                if (pos != null) {
                    if (doesHaveSeeds()) {
                        float[] rots = RotationHelper.getRotationVector(new Vec3d(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F), true, 2, 2, 360);
                        e.setYaw(rots[0]);
                        e.setPitch(rots[1]);
                        mc.player.renderYawOffset = rots[0];
                        mc.player.rotationYawHead = rots[0];
                        mc.player.rotationPitchHead = rots[1];
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(getSlotWithSeeds()));
                        mc.playerController.processRightClickBlock(mc.player, mc.world, pos, EnumFacing.VALUES[0].getOpposite(), vec, EnumHand.MAIN_HAND);
                        timerHelper.reset();
                    }
                }
            }
        }
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket e) {
        if (autoFarm.getBoolValue()) {
            if (e.getPacket() instanceof SPacketBlockChange) {
                SPacketBlockChange p = (SPacketBlockChange) e.getPacket();

                if (isEnabled(Block.getIdFromBlock(p.getBlockState().getBlock()))) {
                    crops.add(p.getBlockPosition());
                }
            } else if (e.getPacket() instanceof SPacketMultiBlockChange) {
                SPacketMultiBlockChange p = (SPacketMultiBlockChange) e.getPacket();

                for (SPacketMultiBlockChange.BlockUpdateData dat : p.getChangedBlocks()) {
                    if (isEnabled(Block.getIdFromBlock(dat.getBlockState().getBlock()))) {
                        crops.add(dat.getPos());
                    }
                }
            }
        }
    }

    private boolean isCheck(int id) {
        int check = 0;
        if (id != 0) {
            check = 59;
        }
        if (id == 0) {
            return false;
        }
        return id == check;
    }

    private boolean isEnabled(int id) {
        int check = 0;
        if (id != 0) {
            check = 59;
        }
        if (id == 0) {
            return false;
        }
        return id == check;
    }

    private ArrayList<BlockPos> getBlocks(float x, float y, float z) {
        BlockPos min = new BlockPos(mc.player.posX - x, mc.player.posY - y, mc.player.posZ - z);
        BlockPos max = new BlockPos(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z);

        return BlockHelper.getAllInBox(min, max);
    }
}
