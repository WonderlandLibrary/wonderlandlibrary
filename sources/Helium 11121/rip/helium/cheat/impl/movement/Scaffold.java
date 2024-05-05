package rip.helium.cheat.impl.movement;

import net.minecraft.util.Vec3;
import rip.helium.utils.property.impl.*;
import rip.helium.cheat.*;
import net.minecraft.init.*;
import java.util.*;
import rip.helium.utils.property.abs.*;
import rip.helium.cheat.impl.combat.aura.*;
import net.minecraft.client.entity.*;
import me.hippo.systems.lwjeb.annotation.*;
import rip.helium.event.minecraft.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.block.state.*;
import net.minecraft.block.material.*;
import rip.helium.utils.*;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;

/**
 * Made by Niada/JonathanH/Spec ------------------------------- ethereal.rip @
 * 9:53PM - 3/19/2019 -------------------------------
 **/
public class Scaffold extends Cheat
{
    BooleanProperty tower;
    BooleanProperty swing;
    BooleanProperty delayedplacement;
    DoubleProperty prop_delay;
    int heldItem;
    int warnthisnibba;
    int places;
    float serverSidedPitch;
    float serverSidedYaw;
    float placedelay;
    private Stopwatch towerStopwatch;
    BlockData blockdata;
    private Stopwatch timer;
    public float yaw;
    public float pitch;
    private List<Block> blacklistedBlocks;
    private List<Block> invalidBlocks;
    private long lastPlace;

    public List<Block> getBlacklistedBlocks() {
        return this.blacklistedBlocks;
    }

    public Scaffold() {
        super("Scaffold", "Places blocks under you as you walk.", CheatCategory.MOVEMENT);
        this.tower = new BooleanProperty("Tower", "Quickly places blocks under you when you are holding your jump key.", null, true);
        this.swing = new BooleanProperty("Swing", "Swing when you place a block.", null, false);
        this.delayedplacement = new BooleanProperty("Delay", "Delay placing blocks", null, true);
        this.prop_delay = new DoubleProperty("Place Delay", "Delay between blockplaces", () -> this.delayedplacement.getValue(), 60.0, 2.0, 200.0, 1.0, null);
        this.yaw = 999.0f;
        this.pitch = 999.0f;
        this.invalidBlocks = Arrays.asList(Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.snow_layer, Blocks.chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.crafting_table, Blocks.furnace, Blocks.stone_slab, Blocks.wooden_slab, Blocks.stone_slab2);
        this.registerProperties(this.tower, this.swing, this.delayedplacement, this.prop_delay);
        this.towerStopwatch = new Stopwatch();
        this.timer = new Stopwatch();
        this.blacklistedBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.trapped_chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook, Blocks.rail, Blocks.waterlily, Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine, Blocks.trapdoor, Blocks.yellow_flower, Blocks.ladder, Blocks.furnace, Blocks.sand, Blocks.cactus, Blocks.dispenser, Blocks.noteblock, Blocks.dropper, Blocks.crafting_table, Blocks.web, Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall, Blocks.oak_fence);
    }

    public void onDisable() {
        this.mc.timer.timerSpeed = 1.0f;
        this.mc.gameSettings.keyBindSneak.pressed = false;
        this.getPlayer().movementInput.sneak = false;
        this.getPlayer().inventory.currentItem = this.heldItem;
    }

    public void onEnable() {
        this.mc.timer.timerSpeed = 1.0f;
        this.placedelay = 95.0f;
        if (this.mc.thePlayer != null) {
            this.serverSidedYaw = this.getPlayer().rotationYaw;
            this.serverSidedPitch = this.getPlayer().rotationPitch;
            this.heldItem = this.getPlayer().inventory.currentItem;
            this.warnthisnibba = 0;
            this.lastPlace = 420L;
        }
    }

    @Collect
    public void onPlayerJump(final PlayerJumpEvent playerJumpEvent) {
        if (Aura.targetIndex != -1) {
            return;
        }
        if (this.mc.thePlayer.isMoving() && this.mc.thePlayer.isSprinting()) {
            final float f = this.mc.thePlayer.rotationYaw * 0.017453292f;
            final EntityPlayerSP thePlayer = this.mc.thePlayer;
            thePlayer.motionX -= MathHelper.sin(f) * 0.2f / 1.5;
            final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
            ((EntityPlayerSP) thePlayer2).motionZ += MathHelper.cos(f) * 0.2f / 1.5;
        }
    }

    @Collect
    public void onPlayerUpdate(final PlayerUpdateEvent em) {
        if (Aura.targetIndex != -1) {
            return;
        }
        double x = this.mc.thePlayer.posX;
        double z = this.mc.thePlayer.posZ;
        if (this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ)).getBlock().getMaterial().isReplaceable()) {
            x = this.mc.thePlayer.posX;
            z = this.mc.thePlayer.posZ;
        }
        final BlockPos underPos = new BlockPos(x, this.mc.thePlayer.posY - 1.0, z);
        final BlockData data = this.getBlockData(underPos);
        if (em.isPre() && this.getBlockSlot() != -1 && this.mc.gameSettings.keyBindJump.isKeyDown() && !this.mc.thePlayer.isMoving() && this.tower.getValue()) {
            this.mc.thePlayer.setSpeed(0.0);
            if (this.mc.thePlayer.onGround) {
                if (this.isOnGround(0.76) && !this.isOnGround(0.75) && this.mc.thePlayer.motionY > 0.23 && this.mc.thePlayer.motionY < 0.25) {
                    this.mc.thePlayer.motionY = Math.round(this.mc.thePlayer.posY) - this.mc.thePlayer.posY;
                }
                if (this.isOnGround(1.0E-4)) {
                    this.mc.thePlayer.motionY = 0.41999998688697815;
                    if (this.timer.hasPassed(1500.0)) {
                        this.mc.thePlayer.motionY = -0.28;
                        this.timer.reset();
                    }
                }
                else if (this.mc.thePlayer.posY >= Math.round(this.mc.thePlayer.posY) - 1.0E-4 && this.mc.thePlayer.posY <= Math.round(this.mc.thePlayer.posY) + 1.0E-4) {
                    this.mc.thePlayer.motionY = 0.0;
                }
            }
            else if (this.mc.theWorld.getBlockState(underPos).getBlock().getMaterial().isReplaceable() && data != null) {
                this.mc.thePlayer.motionY = 0.41955;
            }
        }
        if (this.mc.theWorld.getBlockState(underPos).getBlock().getMaterial().isReplaceable() && data != null) {
            if (em.isPre()) {
                final float[] rot = this.getRotations(data.position, data.face);
                em.setYaw(rot[0]);
                em.setPitch(rot[1]);
                this.yaw = rot[0];
                this.pitch = rot[1];
                if (!this.mc.gameSettings.keyBindJump.isKeyDown() && this.mc.thePlayer.onGround && this.mc.thePlayer.isCollidedVertically) {
                    em.setOnGround(false);
                }
            }
            else if (this.getBlockSlot() != -1) {
                final int slot = this.mc.thePlayer.inventory.currentItem;
                if (!this.mc.gameSettings.keyBindJump.isKeyDown() && this.mc.thePlayer.onGround && this.mc.thePlayer.isCollidedVertically) {
                    em.setOnGround(false);
                }
                this.mc.thePlayer.inventory.currentItem = this.getBlockSlot();
                this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getCurrentItem(), data.position, data.face, this.getVec3(data.position, data.face));
                if (this.swing.getValue()) {
                    this.mc.thePlayer.swingItem();
                }
                else {
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                }
                this.mc.thePlayer.inventory.currentItem = slot;
            }
        }
        else {
            em.setYaw(this.yaw);
            em.setPitch(this.pitch);
        }
    }

    public BlockPos getSideBlock(final BlockPos currentPos) {
        BlockPos pos = currentPos;
        if (this.getBlock(currentPos.add(0, -1, 0)) != Blocks.air && !(this.getBlock(currentPos.add(0, -1, 0)) instanceof BlockLiquid)) {
            return currentPos.add(0, -1, 0);
        }
        double dist = 20.0;
        for (int x = -2; x <= 2; ++x) {
            for (int y = -2; y <= 1; ++y) {
                for (int z = -2; z <= 2; ++z) {
                    final BlockPos newPos = currentPos.add(x, 0, z);
                    final double newDist = MathHelper.sqrt_double(x * x + y * y + z * z);
                    if (this.getBlock(newPos) != Blocks.air && !(this.getBlock(newPos) instanceof BlockLiquid) && this.getBlock(newPos).getMaterial().isSolid() && newDist <= dist) {
                        pos = currentPos.add(x, y, z);
                        dist = newDist;
                    }
                }
            }
        }
        return pos;
    }

    public EnumFacing getSideHit(final BlockPos currentPos, final BlockPos sideBlock) {
        final int xDiff = sideBlock.getX() - currentPos.getX();
        final int yDiff = sideBlock.getY() - currentPos.getY();
        final int zDiff = sideBlock.getZ() - currentPos.getZ();
        return (yDiff != 0) ? EnumFacing.UP : ((xDiff <= -1) ? EnumFacing.EAST : ((xDiff >= 1) ? EnumFacing.WEST : ((zDiff <= -1) ? EnumFacing.SOUTH : ((zDiff >= 1) ? EnumFacing.NORTH : EnumFacing.DOWN))));
    }

    public Vec3 getVectorForRotation(final float pitch1, final float yaw1) {
        final float yaw2 = -yaw1 + 180.0f;
        final float pitch2 = pitch1;
        final double x = MathHelper.cos((float)Math.toRadians(yaw2 + 90.0));
        final double y = -MathHelper.sin((float)Math.toRadians(pitch2));
        final double yDecrement = MathHelper.cos((float)Math.toRadians(pitch2));
        final double z = -MathHelper.cos((float)Math.toRadians(yaw2));
        return new Vec3(x * yDecrement, y, z * yDecrement);
    }

    public float[] getRotationToBlock(final BlockPos pos, final EnumFacing facing) {
        final double xDiff = pos.getX() + 0.5 - this.getPlayer().posX + facing.getDirectionVec().getX() / 2.0;
        final double zDiff = pos.getZ() + 0.5 - this.getPlayer().posZ + facing.getDirectionVec().getZ() / 2.0;
        final double yDiff = pos.getY() - this.getPlayer().getEntityBoundingBox().minY - 1.0;
        final double distance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)(-Math.toDegrees(Math.atan2(xDiff, zDiff)));
        final float pitch = (float)(-Math.toDegrees(Math.atan(yDiff / distance)));
        return new float[] { (Math.abs(yaw - this.mc.thePlayer.rotationYaw) < 0.1) ? this.mc.thePlayer.rotationYaw : yaw, (Math.abs(pitch - this.mc.thePlayer.rotationPitch) < 0.1) ? this.mc.thePlayer.rotationPitch : pitch };
    }

    public MovingObjectPosition getMovingObjectPosition(final BlockPos blockpos, final double reach, final float pitch, final float yaw) {
        final AxisAlignedBB bb = new AxisAlignedBB(blockpos.getX(), blockpos.getY(), blockpos.getZ(), blockpos.getX() + 1, blockpos.getY() + 1, blockpos.getZ() + 1);
        final Vec3 vectorEyes = new Vec3(this.getPlayer().posX, this.getPlayer().posY + this.getPlayer().getEyeHeight(), this.getPlayer().posZ);
        final Vec3 vectorRotation = this.getVectorForRotation(pitch, yaw - 180.0f);
        final Vec3 vectorReach = vectorEyes.addVector(vectorRotation.xCoord * reach, vectorRotation.yCoord * reach, vectorRotation.zCoord * reach);
        return bb.calculateIntercept(vectorEyes, vectorReach);
    }

    public Vec3 getLook(final float p_174806_1_, final float p_174806_2_) {
        final float var3 = MathHelper.cos(-p_174806_2_ * 0.017453292f - 3.1415927f);
        final float var4 = MathHelper.sin(-p_174806_2_ * 0.017453292f - 3.1415927f);
        final float var5 = -MathHelper.cos(-p_174806_1_ * 0.017453292f);
        final float var6 = MathHelper.sin(-p_174806_1_ * 0.017453292f);
        return new Vec3(var4 * var5, var6, var3 * var5);
    }

    IBlockState blockState(final BlockPos pos) {
        return this.mc.theWorld.getBlockState(pos);
    }

    Block getBlock(final BlockPos pos) {
        return this.blockState(pos).getBlock();
    }

    Material getMaterial(final BlockPos pos) {
        return this.getBlock(pos).getMaterial();
    }

    int getBlockSlot() {
        for (int i = 36; i < 45; ++i) {
            if (this.getPlayer().inventoryContainer.getSlot(i).getStack() != null && this.getPlayer().inventoryContainer.getSlot(i).getStack().getItem() instanceof ItemBlock && !this.invalidBlocks.contains(((ItemBlock)this.getPlayer().inventoryContainer.getSlot(i).getStack().getItem()).getBlock())) {
                return i - 36;
            }
        }
        return -1;
    }

    private BlockData getBlockData(final BlockPos pos) {
        if (this.isPosSolid(pos.add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(-1, 0, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(-1, 0, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(-1, 0, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(-1, 0, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(-1, 0, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(1, 0, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(1, 0, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(1, 0, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(1, 0, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(1, 0, 0).add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(1, 0, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, 1).add(0, -1, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, 1).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, 1).add(1, 0, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, 1).add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1).add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, 1).add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, 1).add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, -1).add(0, -1, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, -1).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, -1).add(1, 0, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, -1).add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, -1).add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, -1).add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1).add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(-1, 0, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(-1, 0, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(-1, 0, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(-1, 0, 0).add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(-1, 0, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(-1, 0, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(-1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(1, 0, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(1, 0, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(1, 0, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(1, 0, 0).add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(1, 0, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(1, 0, 0).add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(1, 0, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(1, 0, 0).add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, 1).add(0, -1, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, 1).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, 1).add(1, 0, 0))) {
            return new BlockData(pos.add(0, 0, 1).add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, 1).add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, 1).add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, 1).add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, 1).add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, -1).add(0, -1, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, -1).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, -1).add(1, 0, 0))) {
            return new BlockData(pos.add(0, 0, -1).add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, -1).add(0, 0, 1))) {
            return new BlockData(pos.add(0, 0, -1).add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, 0, -1).add(0, 0, -1))) {
            return new BlockData(pos.add(0, 0, -1).add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, -1, 0).add(0, -1, 0))) {
            return new BlockData(pos.add(0, -1, 0).add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, -1, 0).add(-1, 0, 0))) {
            return new BlockData(pos.add(0, -1, 0).add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, -1, 0).add(1, 0, 0))) {
            return new BlockData(pos.add(0, -1, 0).add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, -1, 0).add(0, 0, 1))) {
            return new BlockData(pos.add(0, -1, 0).add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (this.isPosSolid(pos.add(0, -1, 0).add(0, 0, -1))) {
            return new BlockData(pos.add(0, -1, 0).add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        final BlockPos pos2 = pos.add(0, -1, 0).add(1, 0, 0);
        final BlockPos pos3 = pos.add(0, -1, 0).add(0, 0, 1);
        final BlockPos pos4 = pos.add(0, -1, 0).add(-1, 0, 0);
        final BlockPos pos5 = pos.add(0, -1, 0).add(0, 0, -1);
        if (this.isPosSolid(pos2.add(0, -1, 0))) {
            return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (this.isPosSolid(pos2.add(-1, 0, 0))) {
            return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (this.isPosSolid(pos2.add(1, 0, 0))) {
            return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (this.isPosSolid(pos2.add(0, 0, 1))) {
            return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (this.isPosSolid(pos2.add(0, 0, -1))) {
            return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (this.isPosSolid(pos4.add(0, -1, 0))) {
            return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (this.isPosSolid(pos4.add(-1, 0, 0))) {
            return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (this.isPosSolid(pos4.add(1, 0, 0))) {
            return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (this.isPosSolid(pos4.add(0, 0, 1))) {
            return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (this.isPosSolid(pos4.add(0, 0, -1))) {
            return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (this.isPosSolid(pos3.add(0, -1, 0))) {
            return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (this.isPosSolid(pos3.add(-1, 0, 0))) {
            return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (this.isPosSolid(pos3.add(1, 0, 0))) {
            return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (this.isPosSolid(pos3.add(0, 0, 1))) {
            return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (this.isPosSolid(pos3.add(0, 0, -1))) {
            return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        if (this.isPosSolid(pos5.add(0, -1, 0))) {
            return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP, (BlockData)null);
        }
        if (this.isPosSolid(pos5.add(-1, 0, 0))) {
            return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST, (BlockData)null);
        }
        if (this.isPosSolid(pos5.add(1, 0, 0))) {
            return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST, (BlockData)null);
        }
        if (this.isPosSolid(pos5.add(0, 0, 1))) {
            return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH, (BlockData)null);
        }
        if (this.isPosSolid(pos5.add(0, 0, -1))) {
            return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH, (BlockData)null);
        }
        return null;
    }

    public float[] getRotations(final BlockPos block, final EnumFacing face) {
        final double x = block.getX() + 0.5 - this.mc.thePlayer.posX + face.getFrontOffsetX() / 2.0;
        final double z = block.getZ() + 0.5 - this.mc.thePlayer.posZ + face.getFrontOffsetZ() / 2.0;
        final double d1 = this.mc.thePlayer.posY + this.mc.thePlayer.getEyeHeight() - (block.getY() + 0.5);
        final double d2 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(Math.atan2(d1, d2) * 180.0 / 3.141592653589793);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[] { yaw, pitch };
    }

    public Vec3 getVec3(final BlockPos pos, final EnumFacing face) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        x += face.getFrontOffsetX() / 2.0;
        z += face.getFrontOffsetZ() / 2.0;
        y += face.getFrontOffsetY() / 2.0;
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
            x += Mafs.getRandomInRange(0.25, -0.25);
            z += Mafs.getRandomInRange(0.25, -0.25);
        }
        else {
            y += Mafs.getRandomInRange(0.25, -0.25);
        }
        if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
            z += Mafs.getRandomInRange(0.25, -0.25);
        }
        if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
            x += Mafs.getRandomInRange(0.25, -0.25);
        }
        return new Vec3(x, y, z);
    }

    private boolean isPosSolid(final BlockPos pos) {
        final Block block = this.mc.theWorld.getBlockState(pos).getBlock();
        return (block.getMaterial().isSolid() || !block.isTranslucent() || block.isSolidFullCube() || block instanceof BlockLadder || block instanceof BlockCarpet || block instanceof BlockSnow || block instanceof BlockSkull) && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer);
    }

    public boolean isOnGround(final double height) {
        return !this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }

    private boolean isValid(final Item item) {
        if (!(item instanceof ItemBlock)) {
            return false;
        }
        final ItemBlock iBlock = (ItemBlock)item;
        final Block block = iBlock.getBlock();
        return !this.blacklistedBlocks.contains(block);
    }

    private class BlockData
    {
        public BlockPos position;
        public EnumFacing face;

        private BlockData(final BlockPos position, final EnumFacing face, BlockData blockData) {
            this.position = position;
            this.face = face;
        }
    }
}
