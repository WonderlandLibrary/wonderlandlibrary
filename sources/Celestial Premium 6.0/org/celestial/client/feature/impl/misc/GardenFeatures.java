/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.misc;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSoulSand;
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
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.packet.EventReceivePacket;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.event.events.impl.render.EventRender3D;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.math.RotationHelper;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.helpers.render.RenderHelper;
import org.celestial.client.helpers.world.BlockHelper;
import org.celestial.client.helpers.world.EntityHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ColorSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class GardenFeatures
extends Feature {
    private final NumberSetting delay;
    private final NumberSetting radius;
    private final BooleanSetting autoHoe;
    private final BooleanSetting farmESP;
    private final ColorSetting color;
    private final ListSetting farmMode;
    ArrayList<BlockPos> crops = new ArrayList();
    ArrayList<BlockPos> check = new ArrayList();
    TimerHelper timerHelper = new TimerHelper();
    TimerHelper timerHelper2 = new TimerHelper();
    private BooleanSetting autoFarm;

    public GardenFeatures() {
        super("GardenFeatures", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0441\u0430\u0434\u0438\u0442 \u0438 \u043b\u043e\u043c\u0430\u0435\u0442 \u0443\u0440\u043e\u0436\u0430\u0439", Type.Misc);
        this.farmMode = new ListSetting("Farm Mode", "Harvest", () -> this.autoFarm.getCurrentValue(), "Harvest", "Plant");
        this.autoFarm = new BooleanSetting("Auto Farm", true, () -> true);
        this.farmESP = new BooleanSetting("Farm ESP", true, () -> this.autoFarm.getCurrentValue() && this.farmMode.currentMode.equals("Harvest"));
        this.color = new ColorSetting("Farm Color", new Color(0xFFFFFF).getRGB(), this.farmESP::getCurrentValue);
        this.autoHoe = new BooleanSetting("Auto Hoe", false, () -> true);
        this.delay = new NumberSetting("Farm Delay", 2.0f, 0.0f, 10.0f, 0.1f, () -> true);
        this.radius = new NumberSetting("Farm Radius", 4.0f, 1.0f, 7.0f, 0.1f, () -> true);
        this.addSettings(this.farmMode, this.autoFarm, this.farmESP, this.color, this.autoHoe, this.delay, this.radius);
    }

    public static boolean doesHaveSeeds() {
        for (int i = 0; i < 9; ++i) {
            GardenFeatures.mc.player.inventory.getStackInSlot(i);
            if (!(GardenFeatures.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemSeeds)) continue;
            return true;
        }
        return false;
    }

    public static int searchSeeds() {
        for (int i = 0; i < 45; ++i) {
            ItemStack itemStack = GardenFeatures.mc.player.inventoryContainer.getSlot(i).getStack();
            if (!(itemStack.getItem() instanceof ItemSeeds)) continue;
            return i;
        }
        return -1;
    }

    public static int getSlotWithSeeds() {
        for (int i = 0; i < 9; ++i) {
            GardenFeatures.mc.player.inventory.getStackInSlot(i);
            if (!(GardenFeatures.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemSeeds)) continue;
            return i;
        }
        return 0;
    }

    @Override
    public void onEnable() {
        this.crops.clear();
        this.check.clear();
        super.onEnable();
    }

    private boolean isOnCrops() {
        for (double x = GardenFeatures.mc.player.boundingBox.minX; x < GardenFeatures.mc.player.boundingBox.maxX; x += (double)0.01f) {
            for (double z = GardenFeatures.mc.player.boundingBox.minZ; z < GardenFeatures.mc.player.boundingBox.maxZ; z += (double)0.01f) {
                Block block = GardenFeatures.mc.world.getBlockState(new BlockPos(x, GardenFeatures.mc.player.posY - 0.1, z)).getBlock();
                if (block instanceof BlockFarmland || block instanceof BlockSoulSand || block instanceof BlockSand || block instanceof BlockAir) continue;
                return false;
            }
        }
        return true;
    }

    private boolean IsValidBlockPos(BlockPos pos) {
        IBlockState state = GardenFeatures.mc.world.getBlockState(pos);
        if (state.getBlock() instanceof BlockFarmland || state.getBlock() instanceof BlockSand || state.getBlock() instanceof BlockSoulSand) {
            return GardenFeatures.mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR;
        }
        return false;
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (GardenFeatures.mc.player == null || GardenFeatures.mc.world == null) {
            return;
        }
        if (this.farmESP.getCurrentValue() && this.farmMode.currentMode.equals("Harvest")) {
            ArrayList<BlockPos> blockPositions = this.getBlocks(this.radius.getCurrentValue(), 0.0f, this.radius.getCurrentValue());
            for (BlockPos pos : blockPositions) {
                Color cropsColor = new Color(this.color.getColor());
                BlockPos blockPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
                RenderHelper.blockEsp(blockPos, cropsColor, false, 1.0, 1.0);
            }
        }
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        BlockPos pos;
        if (GardenFeatures.mc.player == null && GardenFeatures.mc.world == null) {
            return;
        }
        if (this.autoHoe.getCurrentValue() && (pos = (BlockPos)BlockHelper.getSphere(BlockHelper.getPlayerPosLocal(), this.radius.getCurrentValue(), 6, false, true, 0).stream().filter(BlockHelper::IsValidBlockPos).min(Comparator.comparing(blockPos -> EntityHelper.getDistanceOfEntityToBlock(GardenFeatures.mc.player, blockPos))).orElse(null)) != null && GardenFeatures.mc.player.getHeldItemMainhand().getItem() instanceof ItemHoe) {
            float[] rots = RotationHelper.getRotationVector(new Vec3d((float)pos.getX() + 0.5f, (float)pos.getY() + 0.5f, (float)pos.getZ() + 0.5f));
            event.setYaw(rots[0]);
            event.setPitch(rots[1]);
            GardenFeatures.mc.player.renderYawOffset = rots[0];
            GardenFeatures.mc.player.rotationYawHead = rots[0];
            GardenFeatures.mc.player.rotationPitchHead = rots[1];
            if (this.timerHelper2.hasReached(this.delay.getCurrentValue() * 100.0f)) {
                GardenFeatures.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                GardenFeatures.mc.player.swingArm(EnumHand.MAIN_HAND);
                this.timerHelper2.reset();
            }
        }
        if (this.farmMode.currentMode.equals("Plant") && !GardenFeatures.doesHaveSeeds() && GardenFeatures.searchSeeds() != -1) {
            GardenFeatures.mc.playerController.windowClick(0, GardenFeatures.searchSeeds(), 1, ClickType.QUICK_MOVE, GardenFeatures.mc.player);
        }
    }

    @EventTarget
    public void onPre(EventPreMotion e) {
        if (this.autoFarm.getCurrentValue()) {
            String mode = this.farmMode.getOptions();
            if (mode.equalsIgnoreCase("Harvest")) {
                ArrayList<BlockPos> blockPositions = this.getBlocks(this.radius.getCurrentValue(), this.radius.getCurrentValue(), this.radius.getCurrentValue());
                for (BlockPos pos : blockPositions) {
                    BlockCrops crop;
                    IBlockState state = BlockHelper.getState(pos);
                    if (!this.isCheck(Block.getIdFromBlock(state.getBlock()))) continue;
                    if (!this.isCheck(0)) {
                        this.check.add(pos);
                    }
                    Block block = GardenFeatures.mc.world.getBlockState(pos).getBlock();
                    BlockPos downPos = pos.down(1);
                    if (!(block instanceof BlockCrops) || (crop = (BlockCrops)block).canGrow(GardenFeatures.mc.world, pos, state, true) || !this.timerHelper.hasReached(this.delay.getCurrentValue() * 100.0f) || pos == null) continue;
                    float[] rots = RotationHelper.getRotationVector(new Vec3d((float)pos.getX() + 0.5f, (float)pos.getY() + 0.5f, (float)pos.getZ() + 0.5f));
                    e.setYaw(rots[0]);
                    e.setPitch(rots[1]);
                    GardenFeatures.mc.player.renderYawOffset = rots[0];
                    GardenFeatures.mc.player.rotationYawHead = rots[0];
                    GardenFeatures.mc.player.rotationPitchHead = rots[1];
                    GardenFeatures.mc.playerController.onPlayerDamageBlock(pos, GardenFeatures.mc.player.getHorizontalFacing());
                    GardenFeatures.mc.player.swingArm(EnumHand.MAIN_HAND);
                    if (GardenFeatures.doesHaveSeeds()) {
                        GardenFeatures.mc.player.connection.sendPacket(new CPacketHeldItemChange(GardenFeatures.getSlotWithSeeds()));
                        GardenFeatures.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(downPos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                        GardenFeatures.mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                    this.timerHelper.reset();
                }
            } else if (mode.equalsIgnoreCase("Plant")) {
                BlockPos pos = BlockHelper.getSphere(BlockHelper.getPlayerPosLocal(), this.radius.getCurrentValue(), 6, false, true, 0).stream().filter(this::IsValidBlockPos).min(Comparator.comparing(blockPos -> EntityHelper.getDistanceOfEntityToBlock(GardenFeatures.mc.player, blockPos))).orElse(null);
                Vec3d vec = new Vec3d(0.0, 0.0, 0.0);
                if (this.timerHelper.hasReached(this.delay.getCurrentValue() * 100.0f) && this.isOnCrops() && pos != null && GardenFeatures.doesHaveSeeds()) {
                    float[] rots = RotationHelper.getRotationVector(new Vec3d((float)pos.getX() + 0.5f, (float)pos.getY() + 0.5f, (float)pos.getZ() + 0.5f));
                    e.setYaw(rots[0]);
                    e.setPitch(rots[1]);
                    GardenFeatures.mc.player.renderYawOffset = rots[0];
                    GardenFeatures.mc.player.rotationYawHead = rots[0];
                    GardenFeatures.mc.player.rotationPitchHead = rots[1];
                    GardenFeatures.mc.player.connection.sendPacket(new CPacketHeldItemChange(GardenFeatures.getSlotWithSeeds()));
                    GardenFeatures.mc.playerController.processRightClickBlock(GardenFeatures.mc.player, GardenFeatures.mc.world, pos, EnumFacing.VALUES[0].getOpposite(), vec, EnumHand.MAIN_HAND);
                    this.timerHelper.reset();
                }
            }
        }
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket e) {
        if (this.autoFarm.getCurrentValue()) {
            if (e.getPacket() instanceof SPacketBlockChange) {
                SPacketBlockChange p = (SPacketBlockChange)e.getPacket();
                if (this.isEnabled(Block.getIdFromBlock(p.getBlockState().getBlock()))) {
                    this.crops.add(p.getBlockPosition());
                }
            } else if (e.getPacket() instanceof SPacketMultiBlockChange) {
                SPacketMultiBlockChange p = (SPacketMultiBlockChange)e.getPacket();
                for (SPacketMultiBlockChange.BlockUpdateData dat : p.getChangedBlocks()) {
                    if (!this.isEnabled(Block.getIdFromBlock(dat.getBlockState().getBlock()))) continue;
                    this.crops.add(dat.getPos());
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
        BlockPos min = new BlockPos(GardenFeatures.mc.player.posX - (double)x, GardenFeatures.mc.player.posY - (double)y, GardenFeatures.mc.player.posZ - (double)z);
        BlockPos max = new BlockPos(GardenFeatures.mc.player.posX + (double)x, GardenFeatures.mc.player.posY + (double)y, GardenFeatures.mc.player.posZ + (double)z);
        return BlockHelper.getAllInBox(min, max);
    }
}

