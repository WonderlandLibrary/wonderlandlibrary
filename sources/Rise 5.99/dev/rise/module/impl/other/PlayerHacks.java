/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.other;

import dev.rise.Rise;
import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.other.AttackEvent;
import dev.rise.event.impl.packet.PacketReceiveEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.module.impl.render.Freecam;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.ModeSetting;
import dev.rise.setting.impl.NoteSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.TimeUtil;
import dev.rise.util.pathfinding.MainPathFinder;
import dev.rise.util.pathfinding.Vec3;
import dev.rise.util.player.PacketUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

import java.util.*;
import java.util.stream.Collectors;

@ModuleInfo(name = "PlayerHacks", description = "Gives another player hacks", category = Category.OTHER)
public final class PlayerHacks extends Module {

    private final NoteSetting modeSettings = new NoteSetting("Mode Settings", this);
    private final ModeSetting mode = new ModeSetting("Mode", this, "Aura", "Aura", "Nuker");
    private final ModeSetting auraMode = new ModeSetting("Aura Mode", this, "Single", "Single", "Multi");
    private final NoteSetting nukerSettings = new NoteSetting("Nuker Settings", this);
    private final NumberSetting delay = new NumberSetting("Delay", this, 100, 0, 5000, 50);
    private final NumberSetting nukerRange = new NumberSetting("Nuker Range", this, 4, 1, 7, 0.5);
    private final BooleanSetting instantOnly = new BooleanSetting("Instant Only", this, false);
    private final BooleanSetting scatter = new BooleanSetting("Scatter", this, false);
    private final NoteSetting auraSettings = new NoteSetting("Aura Settings", this);
    private final NumberSetting cps = new NumberSetting("CPS", this, 5, 1, 20, 1);
    private final NumberSetting auraRange = new NumberSetting("Aura Range", this, 8, 4, 120, 1);
    private final NumberSetting maxTargets = new NumberSetting("Max Targets", this, 25, 2, 50, 1);
    private final NoteSetting generalSettings = new NoteSetting("General Settings", this);
    private final BooleanSetting onlyOnSwing = new BooleanSetting("Only On Swing", this, true);
    private final BooleanSetting swing = new BooleanSetting("Swing", this, true);

    private final TimeUtil timer = new TimeUtil();
    public static String master = "";
    private boolean swung;

    @Override
    public void onUpdateAlwaysInGui() {
        switch (mode.getMode()) {
            case "Aura":
                auraSettings.hidden = false;
                auraMode.hidden = false;
                cps.hidden = false;
                auraRange.hidden = false;
                maxTargets.hidden = !auraMode.is("Multi");

                nukerSettings.hidden = true;
                delay.hidden = true;
                nukerRange.hidden = true;
                instantOnly.hidden = true;
                scatter.hidden = true;
                break;

            case "Nuker":
                nukerSettings.hidden = false;
                delay.hidden = false;
                nukerRange.hidden = false;
                instantOnly.hidden = false;
                scatter.hidden = false;

                auraSettings.hidden = true;
                auraMode.hidden = true;
                cps.hidden = true;
                auraRange.hidden = true;
                maxTargets.hidden = true;
                break;
        }
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        if (mc.theWorld.getPlayerEntityByName(master) == null || (!swung && onlyOnSwing.isEnabled()))
            return;

        final Entity masterEntity = mc.theWorld.getPlayerEntityByName(master);
        final EntityPlayer thePlayer = mc.thePlayer;

        final boolean freecamEnabled = Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getModule("Freecam")).isEnabled();
        double x = thePlayer.posX;
        double y = thePlayer.posY;
        double z = thePlayer.posZ;

        if (freecamEnabled) {
            x = Freecam.startX;
            y = Freecam.startY;
            z = Freecam.startZ;
        }

        final double finalZ = z;
        final double finalY = y;
        final double finalX = x;

        switch (mode.getMode()) {
            case "Aura": {
                if (!timer.hasReached((long) (((20 - cps.getValue()) * 50) - Math.random() * 100)))
                    return;

                final List<EntityPlayer> targets = getEntities(masterEntity, auraRange.getValue());

                final int maxTargets = (int) Math.round(this.maxTargets.getValue());

                if (auraMode.is("Multi") && targets.size() > maxTargets)
                    targets.subList(maxTargets, targets.size()).clear();

                if (targets.isEmpty())
                    return;

                timer.reset();

                final EntityPlayer target = targets.get(0);

                /* Creating a new thread */
                switch (auraMode.getMode()) {
                    case "Single": {
                        final double targetX = target.posX;
                        final double targetY = target.posY;
                        final double targetZ = target.posZ;
                        new Thread(() -> {
                            /* Getting path */
                            final ArrayList<Vec3> path = MainPathFinder.computePath(new Vec3(finalX, finalY, finalZ), new Vec3(targetX, targetY, targetZ));

                            for (final Vec3 vec : path)
                                PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));

                            if (swing.isEnabled())
                                mc.thePlayer.swingItem();

                            /*
                             * Calls attack event so other modules can use information from the entity
                             * When the C02 packet is sent the attack event does not
                             * get called, so we have to manually call it ourselves in here.
                             */
                            final AttackEvent attackEvent = new AttackEvent(target);
                            attackEvent.call();

                            if (!attackEvent.isCancelled())
                                PacketUtil.sendPacketWithoutEvent(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));

                            Collections.reverse(path);

                            for (final Vec3 vec : path)
                                PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));
                        }).start();
                        break;
                    }

                    case "Multi": {
                        for (final EntityPlayer player : targets) {
                            final double targetX = player.posX;
                            final double targetY = player.posY;
                            final double targetZ = player.posZ;
                            new Thread(() -> {
                                /* Getting path */
                                final ArrayList<Vec3> path = MainPathFinder.computePath(new Vec3(finalX, finalY, finalZ), new Vec3(targetX, targetY, targetZ));

                                for (final Vec3 vec : path)
                                    PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));

                                if (swing.isEnabled())
                                    mc.thePlayer.swingItem();

                                /*
                                 * Calls attack event so other modules can use information from the entity
                                 * When the C02 packet is sent the attack event does not
                                 * get called, so we have to manually call it ourselves in here.
                                 */
                                final AttackEvent attackEvent = new AttackEvent(player);
                                attackEvent.call();

                                if (!attackEvent.isCancelled())
                                    PacketUtil.sendPacketWithoutEvent(new C02PacketUseEntity(player, C02PacketUseEntity.Action.ATTACK));

                                Collections.reverse(path);

                                for (final Vec3 vec : path)
                                    PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));
                            }).start();
                        }
                        break;
                    }
                }
                swung = false;
                break;
            }

            case "Nuker": {
                if (!timer.hasReached((long) delay.getValue()))
                    return;

                final double radius = nukerRange.getValue() - 1;

                timer.reset();

                /* Creating a new thread */
                final double masterX = masterEntity.posX;
                final double masterY = masterEntity.posY;
                final double masterZ = masterEntity.posZ;
                new Thread(() -> {
                    /* Getting path */
                    final ArrayList<Vec3> path = MainPathFinder.computePath(new Vec3(finalX, finalY, finalZ), new Vec3(masterX, masterY, masterZ));

                    for (final Vec3 vec : path)
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));

                    nuke(radius, masterX, masterY, masterZ);

                    Collections.reverse(path);

                    for (final Vec3 vec : path)
                        PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(), vec.getY(), vec.getZ(), true));
                }).start();
                swung = false;
                break;
            }
        }
    }

    @Override
    public void onPacketReceive(final PacketReceiveEvent event) {
        if (mc.theWorld.getPlayerEntityByName(master) == null)
            return;

        final Packet<?> p = event.getPacket();

        if (p instanceof S0BPacketAnimation) {
            final S0BPacketAnimation packetAnimation = (S0BPacketAnimation) p;
            if (packetAnimation.getEntityID() == mc.theWorld.getPlayerEntityByName(master).getEntityId())
                swung = true;
        }

        if (mode.is("Nuker") && p instanceof S02PacketChat) {
            final S02PacketChat packetChat = (S02PacketChat) p;
            final String message = packetChat.getChatComponent().getUnformattedText();
            if (!packetChat.isChat() && message.equals("You can't build outside the plot!"))
                event.setCancelled(true);
        }
    }

    private List<EntityPlayer> getEntities(final Entity master, final double range) {
        // Returns the list of entities
        return mc.theWorld.playerEntities
                // Stream our entity list.
                .stream()

                // Only get the entities we can attack.
                .filter(entity -> {
                    if (entity.isDead) return false;

                    if (entity.deathTime != 0) return false;

                    if (entity.ticksExisted < 2) return false;

                    for (final String name : Rise.INSTANCE.getFriends()) {
                        if (name.equalsIgnoreCase(entity.getGameProfile().getName()))
                            return false;
                    }

                    return master != entity && mc.thePlayer != entity;
                })

                // Do a proper distance calculation to get entities we can reach.
                .filter(entity -> {
                    // DO NOT TOUCH THIS VALUE ITS CALCULATED WITH MATH
                    final double girth = 0.5657;

                    // See if the other entity is in our range.
                    return master.getDistanceToEntity(entity) - girth < range;
                })

                // Sort out potential targets with the algorithm provided as a setting.
                .sorted(Comparator.comparingDouble(master::getDistanceSqToEntity))

                .sorted(Comparator.comparingDouble(entity -> {
                    final float[] rotation = rotateToEye(entity);
                    final float pitch = rotation[1];
                    final float yaw = rotation[0];
                    return Math.abs(pitch - master.rotationPitch) + Math.abs(yaw - master.rotationYaw);
                }))

                // Sort out all the specified targets.
                .sorted(Comparator.comparing(entity -> !Rise.INSTANCE.getTargets().contains(entity.getName())))

                // Get the possible targets and put them in a list.
                .collect(Collectors.toList());
    }

    private float[] rotateToEye(final EntityLivingBase entityLivingBase) {
        final double x = entityLivingBase.posX;
        final double y = entityLivingBase.posY + entityLivingBase.getEyeHeight() / 2.0f;
        final double z = entityLivingBase.posZ;
        return getRotationFromPosition(x, y, z);
    }

    private float[] getRotationFromPosition(final double x, final double y, final double z) {
        final double xDiff = x - mc.theWorld.getPlayerEntityByName(master).posX;
        final double zDiff = z - mc.theWorld.getPlayerEntityByName(master).posZ;
        final double yDiff = y - mc.theWorld.getPlayerEntityByName(master).posY - 1.2;
        final double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0F;
        final float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / Math.PI);
        return new float[]{yaw, pitch};
    }

    private void nuke(final double range, final double xPos, final double yPos, final double zPos) {
        if (range == 0) {
            final BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
            final Block block = mc.theWorld.getBlockState(blockPos).getBlock();

            if (block instanceof BlockAir || (block.getBlockHardness() > 0 && instantOnly.isEnabled()))
                return;

            PacketUtil.sendPacketWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
            PacketUtil.sendPacketWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));

            if (swing.isEnabled())
                mc.thePlayer.swingItem();
        } else {
            for (double x = -range; x < range; x++) {
                for (double y = range; y > -range; y--) {
                    for (double z = -range; z < range; z++) {
                        if (scatter.isEnabled() && !((mc.thePlayer.ticksExisted % 2 == 0 ? x : z) % 2 == 0))
                            continue;

                        final BlockPos blockPos = new BlockPos(xPos + x, yPos + y, zPos + z);
                        final Block block = mc.theWorld.getBlockState(blockPos).getBlock();

                        if (block instanceof BlockAir || (block.getBlockHardness() > 0 && instantOnly.isEnabled()))
                            continue;

                        PacketUtil.sendPacketWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));
                        PacketUtil.sendPacketWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, EnumFacing.NORTH));

                        if (swing.isEnabled())
                            mc.thePlayer.swingItem();
                    }
                }
            }
        }
    }

    @Override
    protected void onEnable() {
        if (master.isEmpty())
            Rise.addChatMessage("Use the PlayerHacks command to set the target.");
        swung = false;
        timer.reset();
    }
}