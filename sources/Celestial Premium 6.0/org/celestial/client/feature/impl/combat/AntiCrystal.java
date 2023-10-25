/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.combat;

import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.player.EventPreMotion;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.helpers.math.RotationHelper;
import org.celestial.client.helpers.misc.TimerHelper;
import org.celestial.client.helpers.player.InventoryHelper;
import org.celestial.client.helpers.world.BlockHelper;
import org.celestial.client.helpers.world.EntityHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.NumberSetting;

public class AntiCrystal
extends Feature {
    private final NumberSetting rangeToBlock;
    private final NumberSetting delay;
    private final BooleanSetting throughWalls;
    private final BooleanSetting bedrockCheck;
    private final BooleanSetting obsidianCheck;
    private final TimerHelper timerHelper = new TimerHelper();
    private final ArrayList<BlockPos> invalidPositions = new ArrayList();

    public AntiCrystal() {
        super("AntiCrystal", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0441\u0442\u0430\u0432\u0438\u0442 \u0431\u043b\u043e\u043a \u043d\u0430 \u043e\u0431\u0441\u0438\u0434\u0438\u0430\u043d/\u0431\u0435\u0434\u0440\u043e\u043a \u0432 \u0440\u0430\u0434\u0438\u0443\u0441\u0435", Type.Combat);
        this.throughWalls = new BooleanSetting("Through Walls", true, () -> true);
        this.obsidianCheck = new BooleanSetting("Obsidian Check", true, () -> true);
        this.bedrockCheck = new BooleanSetting("Bedrock Check", false, () -> true);
        this.rangeToBlock = new NumberSetting("Range To Block", 5.0f, 3.0f, 6.0f, 0.1f, () -> true);
        this.delay = new NumberSetting("Place Delay", 0.0f, 0.0f, 2000.0f, 100.0f, () -> true);
        this.addSettings(this.obsidianCheck, this.bedrockCheck, this.throughWalls, this.rangeToBlock, this.delay);
    }

    public static int getSlotWithBlock() {
        for (int i = 0; i < 9; ++i) {
            AntiCrystal.mc.player.inventory.getStackInSlot(i);
            if (!(AntiCrystal.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock)) continue;
            return i;
        }
        return -1;
    }

    private boolean IsValidBlockPos(BlockPos pos) {
        IBlockState state = AntiCrystal.mc.world.getBlockState(pos);
        if (state.getBlock() instanceof BlockObsidian && this.obsidianCheck.getCurrentValue() || state.getBlock() == Block.getBlockById(7) && this.bedrockCheck.getCurrentValue()) {
            return AntiCrystal.mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR;
        }
        return false;
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        this.setSuffix("" + (int)this.rangeToBlock.getCurrentValue());
        int oldSlot = AntiCrystal.mc.player.inventory.currentItem;
        BlockPos pos = BlockHelper.getSphere(BlockHelper.getPlayerPosLocal(), this.rangeToBlock.getCurrentValue(), 6, false, true, 0).stream().filter(this::IsValidBlockPos).min(Comparator.comparing(blockPos -> EntityHelper.getDistanceOfEntityToBlock(AntiCrystal.mc.player, blockPos))).orElse(null);
        if (InventoryHelper.doesHotbarHaveBlock() && pos != null && this.timerHelper.hasReached(this.delay.getCurrentValue()) && AntiCrystal.getSlotWithBlock() != -1) {
            if (!AntiCrystal.mc.world.isAirBlock(pos.up(1))) {
                this.invalidPositions.add(pos);
            }
            for (Entity e : AntiCrystal.mc.world.loadedEntityList) {
                if (e == null || !(e instanceof EntityEnderCrystal) || e.getPosition().getX() != pos.getX() || e.getPosition().getZ() != pos.getZ()) continue;
                return;
            }
            if (!this.invalidPositions.contains(pos)) {
                if (AntiCrystal.mc.world.rayTraceBlocks(new Vec3d(AntiCrystal.mc.player.posX, AntiCrystal.mc.player.posY + (double)AntiCrystal.mc.player.getEyeHeight(), AntiCrystal.mc.player.posZ), new Vec3d(pos.getX(), pos.getY(), pos.getZ()), false, true, false) != null && !this.throughWalls.getCurrentValue()) {
                    return;
                }
                float[] rots = RotationHelper.getRotationVector(new Vec3d((double)pos.getX() + 0.5, (double)pos.getY() + 1.4, (double)pos.getZ() + 0.5));
                event.setYaw(rots[0]);
                event.setPitch(rots[1]);
                AntiCrystal.mc.player.renderYawOffset = rots[0];
                AntiCrystal.mc.player.rotationYawHead = rots[0];
                AntiCrystal.mc.player.rotationPitchHead = rots[1];
                AntiCrystal.mc.player.inventory.currentItem = AntiCrystal.getSlotWithBlock();
                AntiCrystal.mc.playerController.processRightClickBlock(AntiCrystal.mc.player, AntiCrystal.mc.world, pos, EnumFacing.UP, new Vec3d(pos.getX(), pos.getY(), pos.getZ()), EnumHand.MAIN_HAND);
                AntiCrystal.mc.player.swingArm(EnumHand.MAIN_HAND);
                AntiCrystal.mc.player.inventory.currentItem = oldSlot;
                AntiCrystal.mc.player.resetCooldown();
                this.timerHelper.reset();
            }
        }
    }
}

