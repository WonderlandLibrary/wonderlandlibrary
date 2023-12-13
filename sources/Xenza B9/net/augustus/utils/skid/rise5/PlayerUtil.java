// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils.skid.rise5;

import java.util.HashMap;
import java.util.Iterator;
import net.minecraft.util.Vec3;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.util.EntitySelectors;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Comparator;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;
import net.minecraft.item.ItemFood;
import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.BlockTNT;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemSword;
import java.util.Map;
import net.augustus.utils.interfaces.MC;

public final class PlayerUtil implements MC
{
    public static final Map<String, Boolean> serverResponses;
    public static boolean sentEmail;
    public static boolean firstWorld;
    public static int worldChanges;
    
    public static boolean isHoldingSword() {
        return PlayerUtil.mc.thePlayer.ticksExisted > 3 && PlayerUtil.mc.thePlayer.getCurrentEquippedItem() != null && PlayerUtil.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword;
    }
    
    public static int findSword() {
        float bestSwordDamage = -1.0f;
        float bestSwordDurability = -1.0f;
        int bestSlot = -1;
        for (int i = 0; i < 8; ++i) {
            final ItemStack itemStack = PlayerUtil.mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack != null) {
                if (itemStack.getItem() != null) {
                    if (itemStack.getItem() instanceof ItemSword) {
                        final ItemSword sword = (ItemSword)itemStack.getItem();
                        final int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack);
                        final float damageLevel = (float)(sword.getDamageVsEntity() + level * 1.25);
                        if (bestSwordDamage < damageLevel) {
                            bestSwordDamage = damageLevel;
                            bestSwordDurability = sword.getDamageVsEntity();
                            bestSlot = i;
                        }
                        if (damageLevel == bestSwordDamage && sword.getDamageVsEntity() < bestSwordDurability) {
                            bestSwordDurability = sword.getDamageVsEntity();
                            bestSlot = i;
                        }
                    }
                }
            }
        }
        return bestSlot;
    }
    
    public static Integer findItem(final Item item) {
        for (int i = 0; i < 9; ++i) {
            final ItemStack itemStack = PlayerUtil.mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null || itemStack.getItem() == null) {
                if (item == null) {
                    return i;
                }
            }
            else if (itemStack.getItem() == item) {
                return i;
            }
        }
        return null;
    }
    
    public static Integer findTnt() {
        for (int i = 0; i < 8; ++i) {
            final ItemStack itemStack = PlayerUtil.mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack != null) {
                if (itemStack.getItem() != null) {
                    if (itemStack.getItem() instanceof ItemBlock) {
                        final ItemBlock block = (ItemBlock)itemStack.getItem();
                        if (block.getBlock() instanceof BlockTNT) {
                            return i;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public static boolean isOnSameTeam(final EntityLivingBase entity) {
        if (entity.getTeam() != null && PlayerUtil.mc.thePlayer.getTeam() != null) {
            final char c1 = entity.getDisplayName().getFormattedText().charAt(1);
            final char c2 = PlayerUtil.mc.thePlayer.getDisplayName().getFormattedText().charAt(1);
            return c1 == c2;
        }
        return false;
    }
    
    public static boolean isBlockUnder(final double xOffset, final double zOffset) {
        for (int offset = 0; offset < PlayerUtil.mc.thePlayer.posY + PlayerUtil.mc.thePlayer.getEyeHeight(); offset += 2) {
            final AxisAlignedBB boundingBox = PlayerUtil.mc.thePlayer.getEntityBoundingBox().offset(xOffset, -offset, zOffset);
            if (!PlayerUtil.mc.theWorld.getCollidingBoundingBoxes(PlayerUtil.mc.thePlayer, boundingBox).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isBlockUnder() {
        for (int offset = 0; offset < PlayerUtil.mc.thePlayer.posY + PlayerUtil.mc.thePlayer.getEyeHeight(); offset += 2) {
            final AxisAlignedBB boundingBox = PlayerUtil.mc.thePlayer.getEntityBoundingBox().offset(0.0, -offset, 0.0);
            if (!PlayerUtil.mc.theWorld.getCollidingBoundingBoxes(PlayerUtil.mc.thePlayer, boundingBox).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean generalAntiPacketLog() {
        return PlayerUtil.worldChanges > 1;
    }
    
    public static boolean isOnServer(final String server) {
        return false;
    }
    
    public static boolean isOnLiquid() {
        boolean onLiquid = false;
        final AxisAlignedBB playerBB = PlayerUtil.mc.thePlayer.getEntityBoundingBox();
        final WorldClient world = PlayerUtil.mc.theWorld;
        final int y = (int)playerBB.offset(0.0, -0.01, 0.0).minY;
        for (int x = MathHelper.floor_double(playerBB.minX); x < MathHelper.floor_double(playerBB.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(playerBB.minZ); z < MathHelper.floor_double(playerBB.maxZ) + 1; ++z) {
                final Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }
    
    public static int findGap() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = PlayerUtil.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getDisplayName().contains("Golden") && itemStack.stackSize > 0 && itemStack.getItem() instanceof ItemFood) {
                return i;
            }
        }
        return -1;
    }
    
    public static int findSoup() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = PlayerUtil.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getDisplayName().contains("Stew") && itemStack.stackSize > 0 && itemStack.getItem() instanceof ItemFood) {
                return i;
            }
        }
        return -1;
    }
    
    public static int findHead() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = PlayerUtil.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack != null && itemStack.getDisplayName().contains("Head") && itemStack.stackSize > 0) {
                return i;
            }
        }
        return -1;
    }
    
    public static int findEmptySlot() {
        for (int i = 36; i < 45; ++i) {
            final ItemStack itemStack = PlayerUtil.mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (itemStack == null) {
                return i - 36;
            }
        }
        return -1;
    }
    
    public static boolean isInsideBlock() {
        if (PlayerUtil.mc.thePlayer.ticksExisted < 5) {
            return false;
        }
        final EntityPlayerSP player = PlayerUtil.mc.thePlayer;
        final WorldClient world = PlayerUtil.mc.theWorld;
        final AxisAlignedBB bb = player.getEntityBoundingBox();
        for (int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(bb.minY); y < MathHelper.floor_double(bb.maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; ++z) {
                    final Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                    final AxisAlignedBB boundingBox;
                    if (block != null && !(block instanceof BlockAir) && (boundingBox = block.getCollisionBoundingBox(world, new BlockPos(x, y, z), world.getBlockState(new BlockPos(x, y, z)))) != null && player.getEntityBoundingBox().intersectsWith(boundingBox)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public static Block getBlockRelativeToPlayer(final double offsetX, final double offsetY, final double offsetZ) {
        return PlayerUtil.mc.theWorld.getBlockState(new BlockPos(PlayerUtil.mc.thePlayer.posX + offsetX, PlayerUtil.mc.thePlayer.posY + offsetY, PlayerUtil.mc.thePlayer.posZ + offsetZ)).getBlock();
    }
    
    public static Block getBlock(final double offsetX, final double offsetY, final double offsetZ) {
        return PlayerUtil.mc.theWorld.getBlockState(new BlockPos(offsetX, offsetY, offsetZ)).getBlock();
    }
    
    public static List<EntityLivingBase> getEntities(final double range, final boolean players, final boolean nonPlayers, final boolean dead, final boolean invisibles, final boolean ignoreTeammates) {
        return PlayerUtil.mc.theWorld.loadedEntityList.stream().filter(entity -> entity instanceof EntityLivingBase).map(entity -> entity).filter(entity -> {
            if (entity instanceof EntityPlayer && !players) {
                return false;
            }
            else if (!(entity instanceof EntityPlayer) && !nonPlayers) {
                return false;
            }
            else if (entity.isInvisible() && !invisibles) {
                return false;
            }
            else if (isOnSameTeam(entity) && ignoreTeammates) {
                return false;
            }
            else if (entity.isDead && !dead) {
                return false;
            }
            else if (entity.deathTime != 0 && !dead) {
                return false;
            }
            else if (entity.ticksExisted < 2) {
                return false;
            }
            else {
                return PlayerUtil.mc.thePlayer != entity;
            }
        }).filter(entity -> {
            final double girth = 0.5657;
            return PlayerUtil.mc.thePlayer.getDistanceToEntity(entity) - 0.5657 < range;
        }).sorted(Comparator.comparingDouble(entity -> PlayerUtil.mc.thePlayer.getDistanceSqToEntity(entity))).sorted(Comparator.comparing(entity -> entity instanceof EntityPlayer)).collect((Collector<? super Object, ?, List<EntityLivingBase>>)Collectors.toList());
    }
    
    public static boolean isMouseOver(final float yaw, final float pitch, final Entity target, final float range) {
        final float partialTicks = PlayerUtil.mc.timer.renderPartialTicks;
        final Entity entity = PlayerUtil.mc.getRenderViewEntity();
        Entity mcPointedEntity = null;
        if (entity == null || PlayerUtil.mc.theWorld == null) {
            return false;
        }
        PlayerUtil.mc.mcProfiler.startSection("pick");
        final double d0 = PlayerUtil.mc.playerController.getBlockReachDistance();
        MovingObjectPosition objectMouseOver = entity.rayTrace(d0, partialTicks);
        double d2 = d0;
        final Vec3 vec3 = entity.getPositionEyes(partialTicks);
        final boolean flag = d0 > range;
        if (objectMouseOver != null) {
            d2 = objectMouseOver.hitVec.distanceTo(vec3);
        }
        final Vec3 vec4 = PlayerUtil.mc.thePlayer.getVectorForRotation(pitch, yaw);
        final Vec3 vec5 = vec3.addVector(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0);
        Entity pointedEntity = null;
        Vec3 vec6 = null;
        final float f = 1.0f;
        final List<Entity> list = PlayerUtil.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0).expand(1.0, 1.0, 1.0), Predicates.and((Predicate<? super Entity>)EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
        double d3 = d2;
        for (final Entity entity2 : list) {
            final float f2 = entity2.getCollisionBorderSize();
            final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().expand(f2, f2, f2);
            final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec5);
            if (axisalignedbb.isVecInside(vec3)) {
                if (d3 < 0.0) {
                    continue;
                }
                pointedEntity = entity2;
                vec6 = ((movingobjectposition == null) ? vec3 : movingobjectposition.hitVec);
                d3 = 0.0;
            }
            else {
                if (movingobjectposition == null) {
                    continue;
                }
                final double d4 = vec3.distanceTo(movingobjectposition.hitVec);
                if (d4 >= d3 && d3 != 0.0) {
                    continue;
                }
                pointedEntity = entity2;
                vec6 = movingobjectposition.hitVec;
                d3 = d4;
            }
        }
        if (pointedEntity != null && flag && vec3.distanceTo(vec6) > range) {
            pointedEntity = null;
            objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec6, null, new BlockPos(vec6));
        }
        if (pointedEntity != null && (d3 < d2 || objectMouseOver == null)) {
            objectMouseOver = new MovingObjectPosition(pointedEntity, vec6);
            if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                mcPointedEntity = pointedEntity;
            }
        }
        PlayerUtil.mc.mcProfiler.endSection();
        assert objectMouseOver != null;
        return mcPointedEntity == target;
    }
    
    public static MovingObjectPosition getMouseOver(final float yaw, final float pitch, final float range) {
        final float partialTicks = PlayerUtil.mc.timer.renderPartialTicks;
        final Entity entity = PlayerUtil.mc.getRenderViewEntity();
        Entity mcPointedEntity = null;
        if (entity == null || PlayerUtil.mc.theWorld == null) {
            return null;
        }
        PlayerUtil.mc.mcProfiler.startSection("pick");
        final double d0 = PlayerUtil.mc.playerController.getBlockReachDistance();
        MovingObjectPosition objectMouseOver = entity.rayTrace(d0, partialTicks);
        double d2 = d0;
        final Vec3 vec3 = entity.getPositionEyes(partialTicks);
        final boolean flag = d0 > range;
        if (objectMouseOver != null) {
            d2 = objectMouseOver.hitVec.distanceTo(vec3);
        }
        final Vec3 vec4 = PlayerUtil.mc.thePlayer.getVectorForRotation(pitch, yaw);
        final Vec3 vec5 = vec3.addVector(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0);
        Entity pointedEntity = null;
        Vec3 vec6 = null;
        final float f = 1.0f;
        final List<Entity> list = PlayerUtil.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec4.xCoord * d0, vec4.yCoord * d0, vec4.zCoord * d0).expand(1.0, 1.0, 1.0), Predicates.and((Predicate<? super Entity>)EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
        double d3 = d2;
        for (final Entity entity2 : list) {
            final float f2 = entity2.getCollisionBorderSize();
            final AxisAlignedBB axisalignedbb = entity2.getEntityBoundingBox().expand(f2, f2, f2);
            final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec5);
            if (axisalignedbb.isVecInside(vec3)) {
                if (d3 < 0.0) {
                    continue;
                }
                pointedEntity = entity2;
                vec6 = ((movingobjectposition == null) ? vec3 : movingobjectposition.hitVec);
                d3 = 0.0;
            }
            else {
                if (movingobjectposition == null) {
                    continue;
                }
                final double d4 = vec3.distanceTo(movingobjectposition.hitVec);
                if (d4 >= d3 && d3 != 0.0) {
                    continue;
                }
                pointedEntity = entity2;
                vec6 = movingobjectposition.hitVec;
                d3 = d4;
            }
        }
        if (pointedEntity != null && flag && vec3.distanceTo(vec6) > range) {
            pointedEntity = null;
            objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec6, null, new BlockPos(vec6));
        }
        if (pointedEntity != null && (d3 < d2 || objectMouseOver == null)) {
            objectMouseOver = new MovingObjectPosition(pointedEntity, vec6);
            if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                mcPointedEntity = pointedEntity;
            }
        }
        PlayerUtil.mc.mcProfiler.endSection();
        assert objectMouseOver != null;
        return objectMouseOver;
    }
    
    static {
        serverResponses = new HashMap<String, Boolean>();
    }
}
