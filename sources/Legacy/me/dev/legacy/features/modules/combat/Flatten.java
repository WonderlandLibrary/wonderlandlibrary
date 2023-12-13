package me.dev.legacy.features.modules.combat;

import me.dev.legacy.Legacy;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.BlockUtil;
import me.dev.legacy.util.InventoryUtil;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Flatten extends Module
{
    private final Setting<Integer> blocksPerTick = this.register(new Setting("BlocksPerTick", 8, 1, 30));
    private final Setting<Boolean> rotate = this.register(new Setting("Rotate", false));
    private final Setting<Boolean> packet = this.register(new Setting("PacketPlace", false));
    private final Setting<Float> distance = this.register(new Setting("TargetDistance", 5F, 0F, 10F));
    private final Setting<Boolean> autoDisable = this.register(new Setting("AutoDisable", true));
    private final Vec3d[] offsetsDefault = new Vec3d[] { new Vec3d(0.0, 0.0, -1.0), new Vec3d(0.0, 0.0, 1.0), new Vec3d(1.0, 0.0, 0.0), new Vec3d(-1.0, 0.0, 0.0) };
    private EntityPlayer target;
    private int offsetStep;
    private int oldSlot;
    private boolean placing;

    public Flatten() {
        super("Flatten", "place blocks under player", Module.Category.COMBAT, true, false, false);
        this.offsetStep = 0;
        this.oldSlot = -1;
        this.placing = false;
    }

    public void onEnable() {
        this.oldSlot = Flatten.mc.player.inventory.currentItem;
    }

    public void onDisable() {
        this.oldSlot = -1;
    }

    public void onUpdate() {
        final EntityPlayer closest_target = this.findClosestTarget();
        final int obbySlot = InventoryUtil.findHotbarBlock((Class) BlockObsidian.class);
        if (closest_target == null) {
            this.disable();
            return;
        }
        final List<Vec3d> place_targets = new ArrayList<Vec3d>();
        Collections.addAll(place_targets, this.offsetsDefault);
        int blocks_placed = 0;
        while (blocks_placed < (int)this.blocksPerTick.getValue()) {
            if (this.offsetStep >= place_targets.size()) {
                this.offsetStep = 0;
                break;
            }
            this.placing = true;
            final BlockPos offset_pos = new BlockPos((Vec3d)place_targets.get(this.offsetStep));
            final BlockPos target_pos = new BlockPos(closest_target.getPositionVector()).down().add(offset_pos.getX(), offset_pos.getY(), offset_pos.getZ());
            boolean should_try_place = Flatten.mc.world.getBlockState(target_pos).getMaterial().isReplaceable();
            for (final Entity entity : Flatten.mc.world.getEntitiesWithinAABBExcludingEntity((Entity)null, new AxisAlignedBB(target_pos))) {
                if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
                    should_try_place = false;
                    break;
                }
            }
            if (should_try_place) {
                this.place(target_pos, obbySlot, this.oldSlot);
                ++blocks_placed;
            }
            ++this.offsetStep;
            this.placing = false;
        }
        if (this.autoDisable.getValue()) {
            this.disable();
        }
    }

    private void place(final BlockPos pos, final int slot, final int oldSlot) {
        Flatten.mc.player.inventory.currentItem = slot;
        Flatten.mc.playerController.updateController();
        BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, (boolean)this.rotate.getValue(), (boolean)this.packet.getValue(), Flatten.mc.player.isSneaking());
        Flatten.mc.player.inventory.currentItem = oldSlot;
        Flatten.mc.playerController.updateController();
    }

    private EntityPlayer findClosestTarget() {
        if (Flatten.mc.world.playerEntities.isEmpty()) {
            return null;
        }
        EntityPlayer closestTarget = null;
        for (final EntityPlayer target : Flatten.mc.world.playerEntities) {
            if (target != Flatten.mc.player) {
                if (!target.isEntityAlive()) {
                    continue;
                }
                if (Legacy.friendManager.isFriend(target.getName())) {
                    continue;
                }
                if (target.getHealth() <= 0.0f) {
                    continue;
                }
                if (Flatten.mc.player.getDistance((Entity)target) > this.distance.getValue()) {
                    continue;
                }
                if (closestTarget != null && Flatten.mc.player.getDistance((Entity)target) > Flatten.mc.player.getDistance((Entity)closestTarget)) {
                    continue;
                }
                closestTarget = target;
            }
        }
        return closestTarget;
    }
}
