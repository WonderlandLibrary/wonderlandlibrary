package dev.rise.util.player;

import com.google.common.base.Predicates;
import dev.rise.Rise;
import dev.rise.module.impl.combat.AntiBot;
import dev.rise.util.mail.MailUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockTNT;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class PlayerUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static final Map<String, Boolean> serverResponses = new HashMap<>();
    public static boolean sentEmail;
    public static boolean firstWorld;
    public static int worldChanges;

    public static boolean isHoldingSword() {
        return mc.thePlayer.ticksExisted > 3 && PlayerUtil.mc.thePlayer.getCurrentEquippedItem() != null && PlayerUtil.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword;
    }

    public static int findSword() {
        float bestSwordDamage = -1, bestSwordDurability = -1;
        int bestSlot = -1;

        for (int i = 0; i < 8; i++) {
            final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null || itemStack.getItem() == null) continue;

            if (itemStack.getItem() instanceof ItemSword) {
                final ItemSword sword = (ItemSword) itemStack.getItem();

                final int level = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack);
                final float damageLevel = (float) (sword.getDamageVsEntity() + level * 1.25); //Enchantment Multiplier

                if (bestSwordDamage < damageLevel) {
                    bestSwordDamage = damageLevel;
                    bestSwordDurability = sword.getDamageVsEntity();
                    bestSlot = i;
                }

                if ((damageLevel == bestSwordDamage) && (sword.getDamageVsEntity() < bestSwordDurability)) {
                    bestSwordDurability = sword.getDamageVsEntity();
                    bestSlot = i;
                }
            }
        }

        return bestSlot;
    }

    public static Integer findItem(final Item item) {
        for (int i = 0; i < 9; i++) {
            final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null || itemStack.getItem() == null) {
                if (item == null) return i;
                continue;
            }

            if (itemStack.getItem() == item) {
                return i;
            }
        }

        return null;
    }

    public static Integer findTnt() {
        for (int i = 0; i < 8; i++) {
            final ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(i);
            if (itemStack == null || itemStack.getItem() == null) continue;

            if (itemStack.getItem() instanceof ItemBlock) {
                final ItemBlock block = (ItemBlock) itemStack.getItem();

                if (block.getBlock() instanceof BlockTNT) {
                    return i;
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
        for (int offset = 0; offset < mc.thePlayer.posY + mc.thePlayer.getEyeHeight(); offset += 2) {
            final AxisAlignedBB boundingBox = mc.thePlayer.getEntityBoundingBox().offset(xOffset, -offset, zOffset);

            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, boundingBox).isEmpty())
                return true;
        }
        return false;
    }

    public static boolean isBlockUnder() {
        for (int offset = 0; offset < mc.thePlayer.posY + mc.thePlayer.getEyeHeight(); offset += 2) {
            final AxisAlignedBB boundingBox = mc.thePlayer.getEntityBoundingBox().offset(0, -offset, 0);

            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, boundingBox).isEmpty())
                return true;
        }
        return false;
    }

    public static boolean generalAntiPacketLog() {
        return worldChanges > 1;
    }

    public static boolean isOnServer(final String server) {
        if (serverResponses.containsKey(server))
            return serverResponses.get(server);

        if (Rise.ip == null) {
            serverResponses.put(server, false);
            return false;
        }

        final String ip = Rise.ip.toLowerCase();

        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            final String separator = File.separator;
            final File file = new File(System.getenv("windir") + separator + "System32" + separator + "drivers" + separator + "etc" + separator + "hosts");
            if (file.exists() && !file.isDirectory()) {
                try {
                    final BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        if (line.contains(server.toLowerCase())) {
                            if (!sentEmail) {
                                String SystemName = "Unknown";

                                try {
                                    SystemName = InetAddress.getLocalHost().getHostName();
                                } catch (final UnknownHostException e) {
                                    e.printStackTrace();
                                }

                                final String sentString = "Detected Username: " + mc.thePlayer.getName()
                                        + " Server IP: " + Rise.ip + " System Name: " + SystemName + " Intent Account: "
                                        + Rise.intentAccount.username + " Rise UID: " + Rise.intentAccount.intent_uid
                                        + " Linked Discord: " + Rise.intentAccount.discord_tag + " Last Logged in Account: " + Rise.lastLoggedAccount
                                        + " Microsoft Login Enabled?: " + Rise.INSTANCE.getAltGUI().microsoftAuthEnabled;

                                MailUtil.sendEmail("Detected Packet Logger: " + mc.thePlayer.getName(), sentString);
                                sentEmail = true;
                            }

                            serverResponses.put(server, false);
                            return false;
                        }
                    }
                } catch (final IOException ignored) {
                }
            }
        }

        if (server.equals("Hypixel")) {
            final boolean result = ip.contains("hypixel.net") && !ip.contains("ruhypixel.net") || ip.contains("2606:4700::6810:4e15");
            serverResponses.put("Hypixel", result);
            return result;
        }

        final boolean result = ip.contains(server);
        serverResponses.put(server, result);
        return result;
    }

    public static boolean isOnLiquid() {
        boolean onLiquid = false;
        final AxisAlignedBB playerBB = PlayerUtil.mc.thePlayer.getEntityBoundingBox();
        final WorldClient world = PlayerUtil.mc.theWorld;
        final int y = (int) playerBB.offset(0.0, -0.01, 0.0).minY;
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
        for (int i = 36; i < 45; i++) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (itemStack != null && itemStack.getDisplayName().contains("Golden") && itemStack.stackSize > 0 && itemStack.getItem() instanceof ItemFood) {
                return i;
            }
        }

        return -1;
    }

    public static int findSoup() {
        for (int i = 36; i < 45; i++) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (itemStack != null && itemStack.getDisplayName().contains("Stew") && itemStack.stackSize > 0 && itemStack.getItem() instanceof ItemFood) {
                return i;
            }
        }

        return -1;
    }

    public static int findHead() {
        for (int i = 36; i < 45; i++) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (itemStack != null && itemStack.getDisplayName().contains("Head") && itemStack.stackSize > 0) {
                return i;
            }
        }

        return -1;
    }

    public static int findEmptySlot() {
        for (int i = 36; i < 45; i++) {
            final ItemStack itemStack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (itemStack == null) {
                return i - 36;
            }
        }

        return -1;
    }

    public static boolean isInsideBlock() {
        if (mc.thePlayer.ticksExisted < 5)
            return false;

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
        return mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX + offsetX, mc.thePlayer.posY + offsetY, mc.thePlayer.posZ + offsetZ)).getBlock();
    }

    public static Block getBlock(final double offsetX, final double offsetY, final double offsetZ) {
        return mc.theWorld.getBlockState(new BlockPos(offsetX, offsetY, offsetZ)).getBlock();
    }


    public static List<EntityLivingBase> getEntities(final double range, final boolean players, final boolean nonPlayers, final boolean dead, final boolean invisibles, final boolean ignoreTeammates) {
        // Returns the list of entities
        return mc.theWorld.loadedEntityList
                // Stream our entity list.
                .stream()

                // Only get living entities so we don't have to check for items on ground etc.
                .filter(entity -> entity instanceof EntityLivingBase)

                // Map our entities to entity living base as we have filtered out none living entities.
                .map(entity -> ((EntityLivingBase) entity))

                // Only get the entities we can attack.
                .filter(entity -> {
                    if (entity instanceof EntityPlayer && !players) return false;

                    if (!(entity instanceof EntityPlayer) && !nonPlayers) return false;

                    if (entity.isInvisible() && !invisibles) return false;

                    if (PlayerUtil.isOnSameTeam(entity) && ignoreTeammates) return false;

                    if (entity.isDead && !dead) return false;

                    if (entity.deathTime != 0 && !dead) return false;

                    if (entity.ticksExisted < 2) return false;

                    if (AntiBot.bots.contains(entity)) return false;

                    if (entity instanceof EntityPlayer) {
                        final EntityPlayer player = ((EntityPlayer) entity);

                        for (final String name : Rise.INSTANCE.getFriends()) {
                            if (name.equalsIgnoreCase(player.getGameProfile().getName()))
                                return false;
                        }
                    }

                    return mc.thePlayer != entity;
                })

                // Do a proper distance calculation to get entities we can reach.
                .filter(entity -> {
                    // DO NOT TOUCH THIS VALUE ITS CALCULATED WITH MATH
                    final double girth = 0.5657;

                    // See if the other entity is in our range.
                    return mc.thePlayer.getDistanceToEntity(entity) - girth < range;
                })

                // Sort out potential targets with the algorithm provided as a setting.
                .sorted(Comparator.comparingDouble(entity -> mc.thePlayer.getDistanceSqToEntity(entity)))

                // Sort out all the specified targets.
                .sorted(Comparator.comparing(entity -> entity instanceof EntityPlayer && !Rise.INSTANCE.getTargets().contains(((EntityPlayer) entity).getName())))

                // Get the possible targets and put them in a list.
                .collect(Collectors.toList());
    }

    public static boolean isMouseOver(final float yaw, final float pitch, final Entity target, final float range) {
        final float partialTicks = mc.timer.renderPartialTicks;
        final Entity entity = mc.getRenderViewEntity();
        MovingObjectPosition objectMouseOver;
        Entity mcPointedEntity = null;

        if (entity != null && mc.theWorld != null) {

            mc.mcProfiler.startSection("pick");
            final double d0 = mc.playerController.getBlockReachDistance();
            objectMouseOver = entity.rayTrace(d0, partialTicks);
            double d1 = d0;
            final Vec3 vec3 = entity.getPositionEyes(partialTicks);
            final boolean flag = d0 > (double) range;

            if (objectMouseOver != null) {
                d1 = objectMouseOver.hitVec.distanceTo(vec3);
            }

            final Vec3 vec31 = mc.thePlayer.getVectorForRotation(pitch, yaw);
            final Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
            Entity pointedEntity = null;
            Vec3 vec33 = null;
            final float f = 1.0F;
            final List<Entity> list = mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            double d2 = d1;

            for (final Entity entity1 : list) {
                final float f1 = entity1.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                if (axisalignedbb.isVecInside(vec3)) {
                    if (d2 >= 0.0D) {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0D;
                    }
                } else if (movingobjectposition != null) {
                    final double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                    if (d3 < d2 || d2 == 0.0D) {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition.hitVec;
                        d2 = d3;
                    }
                }
            }

            if (pointedEntity != null && flag && vec3.distanceTo(vec33) > (double) range) {
                pointedEntity = null;
                objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, null, new BlockPos(vec33));
            }

            if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
                objectMouseOver = new MovingObjectPosition(pointedEntity, vec33);

                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    mcPointedEntity = pointedEntity;
                }
            }

            mc.mcProfiler.endSection();

            assert objectMouseOver != null;
            return mcPointedEntity == target;
        }

        return false;
    }

    public static MovingObjectPosition getMouseOver(final float yaw, final float pitch, final float range) {
        final float partialTicks = mc.timer.renderPartialTicks;
        final Entity entity = mc.getRenderViewEntity();
        MovingObjectPosition objectMouseOver;
        Entity mcPointedEntity = null;

        if (entity != null && mc.theWorld != null) {

            mc.mcProfiler.startSection("pick");
            final double d0 = mc.playerController.getBlockReachDistance();
            objectMouseOver = entity.rayTrace(d0, partialTicks);
            double d1 = d0;
            final Vec3 vec3 = entity.getPositionEyes(partialTicks);
            final boolean flag = d0 > (double) range;

            if (objectMouseOver != null) {
                d1 = objectMouseOver.hitVec.distanceTo(vec3);
            }

            final Vec3 vec31 = mc.thePlayer.getVectorForRotation(pitch, yaw);
            final Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
            Entity pointedEntity = null;
            Vec3 vec33 = null;
            final float f = 1.0F;
            final List<Entity> list = mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand(f, f, f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            double d2 = d1;

            for (final Entity entity1 : list) {
                final float f1 = entity1.getCollisionBorderSize();
                final AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                final MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                if (axisalignedbb.isVecInside(vec3)) {
                    if (d2 >= 0.0D) {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0D;
                    }
                } else if (movingobjectposition != null) {
                    final double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                    if (d3 < d2 || d2 == 0.0D) {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition.hitVec;
                        d2 = d3;
                    }
                }
            }

            if (pointedEntity != null && flag && vec3.distanceTo(vec33) > (double) range) {
                pointedEntity = null;
                objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, null, new BlockPos(vec33));
            }

            if (pointedEntity != null && (d2 < d1 || objectMouseOver == null)) {
                objectMouseOver = new MovingObjectPosition(pointedEntity, vec33);

                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    mcPointedEntity = pointedEntity;
                }
            }

            mc.mcProfiler.endSection();

            assert objectMouseOver != null;
            return objectMouseOver;
        }

        return null;
    }
}


