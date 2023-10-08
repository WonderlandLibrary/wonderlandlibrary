package dev.rise.util.misc;

import dev.rise.Rise;
import dev.rise.event.impl.other.AttackEvent;
import dev.rise.module.impl.combat.AntiBot;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.util.player.PacketUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;

import java.util.ArrayList;
import java.util.Objects;

public class TPAura {

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean spectator;
    public static ArrayList<Integer> blackList = new ArrayList<>();

    static double x;
    static double y;
    static double z;
    static double xPreEn;
    static double yPreEn;
    static double zPreEn;
    static double xPre;
    static double yPre;
    static double zPre;

    private static final boolean disableAura = false;
    private static final boolean reachExploit = false;
    private static int timercap = 15;
    private static double range = 7;
    private static boolean headsnap = false;
    private static final double chargerange = 8.0;

    public static ArrayList<EntityLivingBase> targets = new ArrayList<>();
    public static ArrayList<EntityLivingBase> blackList2 = new ArrayList<>();

    public static boolean infiniteReach(final double range, final double maxXZTP, final double maxYTP,
                                        final ArrayList<Vec3> positionsBack, final ArrayList<Vec3> positions, final EntityLivingBase en) {

        xPreEn = en.posX;
        yPreEn = en.posY;
        zPreEn = en.posZ;
        xPre = mc.thePlayer.posX;
        yPre = mc.thePlayer.posY;
        zPre = mc.thePlayer.posZ;
        boolean attack = true;
        boolean up = false;

        final boolean sneaking = mc.thePlayer.isSneaking();

        positions.clear();
        positionsBack.clear();

        final double step = maxXZTP / range;
        int steps = 0;
        for (int i = 0; i < range; i++) {
            steps++;
            if (maxXZTP * steps > range) {
                break;
            }
        }
        final MovingObjectPosition rayTrace;
        MovingObjectPosition rayTrace1 = null;
        if ((rayTraceWide(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                new Vec3(en.posX, en.posY, en.posZ), false, false, true))
                || (rayTrace1 = rayTracePos(
                new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
                new Vec3(en.posX, en.posY + mc.thePlayer.getEyeHeight(), en.posZ), false, false,
                true)) != null) {
            if ((rayTrace = rayTracePos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                    new Vec3(en.posX, mc.thePlayer.posY, en.posZ), false, false, true)) != null
                    || (rayTrace1 = rayTracePos(
                    new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
                            mc.thePlayer.posZ),
                    new Vec3(en.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), en.posZ), false, false,
                    true)) != null) {
                MovingObjectPosition trace = null;
                if (rayTrace == null) {
                    trace = rayTrace1;
                }
                if (rayTrace1 == null) {
                    trace = rayTrace;
                }
                if (trace != null) {
                    if (trace.getBlockPos() != null) {
                        final boolean fence;
                        final BlockPos target = trace.getBlockPos();

                        up = true;
                        y = target.up().getY();
                        yPreEn = target.up().getY();
                        Block lastBlock = null;
                        boolean found = false;
                        for (int i = 0; i < maxYTP; i++) {
                            final MovingObjectPosition tr = rayTracePos(
                                    new Vec3(mc.thePlayer.posX, target.getY() + i, mc.thePlayer.posZ),
                                    new Vec3(en.posX, target.getY() + i, en.posZ), false, false, true);
                            if (tr == null) {
                                continue;
                            }
                            if (tr.getBlockPos() == null) {
                                continue;
                            }

                            final BlockPos blockPos = tr.getBlockPos();
                            final Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                            if (block.getMaterial() != Material.air) {
                                lastBlock = block;
                                continue;
                            }
                            fence = lastBlock instanceof BlockFence;
                            y = target.getY() + i;
                            yPreEn = target.getY() + i;
                            if (fence) {
                                y += 1;
                                yPreEn += 1;
                                if (i + 1 > maxYTP) {
                                    found = false;
                                    break;
                                }
                            }
                            found = true;
                            break;
                        }

                        if (!found) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            } else {
                final MovingObjectPosition ent = rayTracePos(
                        new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                        new Vec3(en.posX, en.posY, en.posZ), false, false, false);
                if (ent != null && ent.entityHit == null) {
                    y = mc.thePlayer.posY;
                    yPreEn = mc.thePlayer.posY;
                } else {
                    y = mc.thePlayer.posY;
                    yPreEn = en.posY;
                }

            }
        }
        if (sneaking) {
            PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
        for (int i = 0; i < steps; i++) {
            if (i == 1 && up) {
                x = mc.thePlayer.posX;
                y = yPreEn;
                z = mc.thePlayer.posZ;
                sendPacket(false, positionsBack, positions);
            }
            if (i != steps - 1) {
                {
                    final double difX = mc.thePlayer.posX - xPreEn;
                    final double difY = mc.thePlayer.posY - yPreEn;
                    final double difZ = mc.thePlayer.posZ - zPreEn;
                    final double divider = step * i;
                    x = mc.thePlayer.posX - difX * divider;
                    y = mc.thePlayer.posY - difY * (up ? 1 : divider);
                    z = mc.thePlayer.posZ - difZ * divider;
                }
                sendPacket(false, positionsBack, positions);
            } else {
                // if last teleport
                {
                    final double difX = mc.thePlayer.posX - xPreEn;
                    final double difY = mc.thePlayer.posY - yPreEn;
                    final double difZ = mc.thePlayer.posZ - zPreEn;
                    final double divider = step * i;
                    x = mc.thePlayer.posX - difX * divider;
                    y = mc.thePlayer.posY - difY * (up ? 1 : divider);
                    z = mc.thePlayer.posZ - difZ * divider;
                }
                sendPacket(false, positionsBack, positions);
                final double xDist = x - xPreEn;
                final double zDist = z - zPreEn;
                final double yDist = y - en.posY;
                final double dist = Math.sqrt(xDist * xDist + zDist * zDist);
                if (dist > 4) {
                    x = xPreEn;
                    y = yPreEn;
                    z = zPreEn;
                    sendPacket(false, positionsBack, positions);
                } else if (dist > 0.05 && up) {
                    x = xPreEn;
                    y = yPreEn;
                    z = zPreEn;
                    sendPacket(false, positionsBack, positions);
                }
                if (Math.abs(yDist) < maxYTP && mc.thePlayer.getDistanceToEntity(en) >= 4) {
                    x = xPreEn;
                    y = en.posY;
                    z = zPreEn;
                    sendPacket(false, positionsBack, positions);

                    attackInf(en);
                } else {
                    attack = false;
                }
            }
        }

        // Go back!
        for (int i = positions.size() - 2; i > -1; i--) {
            {
                x = positions.get(i).xCoord;
                y = positions.get(i).yCoord;
                z = positions.get(i).zCoord;
            }
            sendPacket(false, positionsBack, positions);
        }
        x = mc.thePlayer.posX;
        y = mc.thePlayer.posY;
        z = mc.thePlayer.posZ;
        sendPacket(false, positionsBack, positions);
        if (!attack) {
            if (sneaking) {
                PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            }
            positions.clear();
            positionsBack.clear();
            return false;
        }
        if (sneaking) {
            PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
        }
        return true;
    }

    public static boolean infiniteReach(final double range, final double maxXZTP, final double maxYTP,
                                        final ArrayList<Vec3> positionsBack, final ArrayList<Vec3> positions, final BlockPos targetBlockPos) {
        positions.clear();
        positionsBack.clear();
        final double step = maxXZTP / range;
        int steps = 0;
        for (int i = 0; i < range; i++) {
            steps++;
            if (maxXZTP * steps > range) {
                break;
            }
        }

        final double posX = ((double) targetBlockPos.getX()) + 0.5;
        final double posY = ((double) targetBlockPos.getY()) + 1.0;
        final double posZ = ((double) targetBlockPos.getZ()) + 0.5;
        xPreEn = posX;
        yPreEn = posY;
        zPreEn = posZ;
        xPre = mc.thePlayer.posX;
        yPre = mc.thePlayer.posY;
        zPre = mc.thePlayer.posZ;
        boolean attack = true;
        boolean up = false;

        // If something in the way

        final boolean sneaking = mc.thePlayer.isSneaking();
        final MovingObjectPosition rayTrace;
        MovingObjectPosition rayTrace1 = null;
        if ((rayTraceWide(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                new Vec3(posX, posY, posZ), false, false, true))
                || (rayTrace1 = rayTracePos(
                new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
                new Vec3(posX, posY + mc.thePlayer.getEyeHeight(), posZ), false, false,
                true)) != null) {
            if ((rayTrace = rayTracePos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                    new Vec3(posX, mc.thePlayer.posY, posZ), false, false, true)) != null
                    || (rayTrace1 = rayTracePos(
                    new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
                            mc.thePlayer.posZ),
                    new Vec3(posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), posZ), false, false,
                    true)) != null) {
                MovingObjectPosition trace = null;
                if (rayTrace == null) {
                    trace = rayTrace1;
                }
                if (rayTrace1 == null) {
                    trace = rayTrace;
                }
                if (trace != null) {
                    if (trace.getBlockPos() != null) {
                        final boolean fence;
                        final BlockPos target = trace.getBlockPos();

                        up = true;
                        y = target.up().getY();
                        yPreEn = target.up().getY();
                        Block lastBlock = null;
                        boolean found = false;
                        for (int i = 0; i < maxYTP; i++) {
                            final MovingObjectPosition tr = rayTracePos(
                                    new Vec3(mc.thePlayer.posX, target.getY() + i, mc.thePlayer.posZ),
                                    new Vec3(posX, target.getY() + i, posZ), false, false, true);
                            if (tr == null) {
                                continue;
                            }
                            if (tr.getBlockPos() == null) {
                                continue;
                            }

                            final BlockPos blockPos = tr.getBlockPos();
                            final Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                            if (block.getMaterial() != Material.air) {
                                lastBlock = block;
                                continue;
                            }
                            fence = lastBlock instanceof BlockFence;
                            y = target.getY() + i;
                            yPreEn = target.getY() + i;
                            if (fence) {
                                y += 1;
                                yPreEn += 1;
                                if (i + 1 > maxYTP) {
                                    found = false;
                                    break;
                                }
                            }
                            found = true;
                            break;
                        }
                        if (!found) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            } else {
                final MovingObjectPosition ent = rayTracePos(
                        new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                        new Vec3(posX, posY, posZ), false, false, false);
                if (ent != null && ent.entityHit == null) {
                    y = mc.thePlayer.posY;
                    yPreEn = mc.thePlayer.posY;
                } else {
                    y = mc.thePlayer.posY;
                    yPreEn = posY;
                }

            }
        }
        if (sneaking) {
            PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
        for (int i = 0; i < steps; i++) {
            if (i == 1 && up) {
                x = mc.thePlayer.posX;
                y = yPreEn;
                z = mc.thePlayer.posZ;
                sendPacket(false, positionsBack, positions);
            }
            if (i != steps - 1) {
                {
                    final double difX = mc.thePlayer.posX - xPreEn;
                    final double difY = mc.thePlayer.posY - yPreEn;
                    final double difZ = mc.thePlayer.posZ - zPreEn;
                    final double divider = step * i;
                    x = mc.thePlayer.posX - difX * divider;
                    y = mc.thePlayer.posY - difY * (up ? 1 : divider);
                    z = mc.thePlayer.posZ - difZ * divider;
                }
                sendPacket(false, positionsBack, positions);
            } else {
                // if last teleport
                {
                    final double difX = mc.thePlayer.posX - xPreEn;
                    final double difY = mc.thePlayer.posY - yPreEn;
                    final double difZ = mc.thePlayer.posZ - zPreEn;
                    final double divider = step * i;
                    x = mc.thePlayer.posX - difX * divider;
                    y = mc.thePlayer.posY - difY * (up ? 1 : divider);
                    z = mc.thePlayer.posZ - difZ * divider;
                }
                sendPacket(false, positionsBack, positions);
                final double xDist = x - xPreEn;
                final double zDist = z - zPreEn;
                final double yDist = y - posY;
                final double dist = Math.sqrt(xDist * xDist + zDist * zDist);
                if (dist > 4) {
                    x = xPreEn;
                    y = yPreEn;
                    z = zPreEn;
                    sendPacket(false, positionsBack, positions);
                } else if (dist > 0.05 && up) {
                    x = xPreEn;
                    y = yPreEn;
                    z = zPreEn;
                    sendPacket(false, positionsBack, positions);
                }
                if (Math.abs(yDist) < maxYTP && mc.thePlayer.getDistance(posX, posY, posZ) >= 4) {
                    x = xPreEn;
                    y = posY;
                    z = zPreEn;
                    sendPacket(false, positionsBack, positions);
                    mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, targetBlockPos, EnumFacing.UP));
                    mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, targetBlockPos, EnumFacing.UP));
                } else {
                    attack = false;
                }
            }
        }

        // Go back!
        for (int i = positions.size() - 2; i > -1; i--) {
            {
                x = positions.get(i).xCoord;
                y = positions.get(i).yCoord;
                z = positions.get(i).zCoord;
            }
            sendPacket(false, positionsBack, positions);
        }
        x = mc.thePlayer.posX;
        y = mc.thePlayer.posY;
        z = mc.thePlayer.posZ;
        sendPacket(false, positionsBack, positions);
        if (!attack) {
            if (sneaking) {
                PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            }
            positions.clear();
            positionsBack.clear();
            return false;
        }
        if (sneaking) {
            PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
        }
        return true;
    }

    public static boolean tpToLocation(final double range, final double maxXZTP, final double maxYTP,
                                       final ArrayList<Vec3> positionsBack, final ArrayList<Vec3> positions, final BlockPos targetBlockPos) {
        positions.clear();
        positionsBack.clear();
        final double step = maxXZTP / range;
        int steps = 0;
        for (int i = 0; i < range; i++) {
            steps++;
            if (maxXZTP * steps > range) {
                break;
            }
        }

        final double posX = ((double) targetBlockPos.getX()) + 0.5;
        final double posY = ((double) targetBlockPos.getY()) + 1.0;
        final double posZ = ((double) targetBlockPos.getZ()) + 0.5;
        xPreEn = posX;
        yPreEn = posY;
        zPreEn = posZ;
        xPre = mc.thePlayer.posX;
        yPre = mc.thePlayer.posY;
        zPre = mc.thePlayer.posZ;
        boolean attack = true;
        boolean up = false;

        // If something in the way

        final boolean sneaking = mc.thePlayer.isSneaking();
        final MovingObjectPosition rayTrace;
        MovingObjectPosition rayTrace1 = null;
        if ((rayTraceWide(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                new Vec3(posX, posY, posZ), false, false, true))
                || (rayTrace1 = rayTracePos(
                new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
                new Vec3(posX, posY + mc.thePlayer.getEyeHeight(), posZ), false, false,
                true)) != null) {
            if ((rayTrace = rayTracePos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                    new Vec3(posX, mc.thePlayer.posY, posZ), false, false, true)) != null
                    || (rayTrace1 = rayTracePos(
                    new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
                            mc.thePlayer.posZ),
                    new Vec3(posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), posZ), false, false,
                    true)) != null) {
                MovingObjectPosition trace = null;
                if (rayTrace == null) {
                    trace = rayTrace1;
                }
                if (rayTrace1 == null) {
                    trace = rayTrace;
                }
                if (trace != null) {
                    if (trace.getBlockPos() != null) {
                        final boolean fence;
                        final BlockPos target = trace.getBlockPos();

                        up = true;
                        y = target.up().getY();
                        yPreEn = target.up().getY();
                        Block lastBlock = null;
                        boolean found = false;
                        for (int i = 0; i < maxYTP; i++) {
                            final MovingObjectPosition tr = rayTracePos(
                                    new Vec3(mc.thePlayer.posX, target.getY() + i, mc.thePlayer.posZ),
                                    new Vec3(posX, target.getY() + i, posZ), false, false, true);
                            if (tr == null) {
                                continue;
                            }
                            if (tr.getBlockPos() == null) {
                                continue;
                            }

                            final BlockPos blockPos = tr.getBlockPos();
                            final Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                            if (block.getMaterial() != Material.air) {
                                lastBlock = block;
                                continue;
                            }
                            fence = lastBlock instanceof BlockFence;
                            y = target.getY() + i;
                            yPreEn = target.getY() + i;
                            if (fence) {
                                y += 1;
                                yPreEn += 1;
                                if (i + 1 > maxYTP) {
                                    found = false;
                                    break;
                                }
                            }
                            found = true;
                            break;
                        }
                        if (!found) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            } else {
                final MovingObjectPosition ent = rayTracePos(
                        new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                        new Vec3(posX, posY, posZ), false, false, false);
                if (ent != null && ent.entityHit == null) {
                    y = mc.thePlayer.posY;
                    yPreEn = mc.thePlayer.posY;
                } else {
                    y = mc.thePlayer.posY;
                    yPreEn = posY;
                }

            }
        }

        for (int i = 0; i < steps; i++) {
            if (i == 1 && up) {
                x = mc.thePlayer.posX;
                y = yPreEn;
                z = mc.thePlayer.posZ;
                sendPacket(false, positionsBack, positions);
            }
            if (i != steps - 1) {
                {
                    final double difX = mc.thePlayer.posX - xPreEn;
                    final double difY = mc.thePlayer.posY - yPreEn;
                    final double difZ = mc.thePlayer.posZ - zPreEn;
                    final double divider = step * i;
                    x = mc.thePlayer.posX - difX * divider;
                    y = mc.thePlayer.posY - difY * (up ? 1 : divider);
                    z = mc.thePlayer.posZ - difZ * divider;
                }
                sendPacket(false, positionsBack, positions);
            } else {
                // if last teleport
                {
                    final double difX = mc.thePlayer.posX - xPreEn;
                    final double difY = mc.thePlayer.posY - yPreEn;
                    final double difZ = mc.thePlayer.posZ - zPreEn;
                    final double divider = step * i;
                    x = mc.thePlayer.posX - difX * divider;
                    y = mc.thePlayer.posY - difY * (up ? 1 : divider);
                    z = mc.thePlayer.posZ - difZ * divider;
                }
                sendPacket(false, positionsBack, positions);
                final double xDist = x - xPreEn;
                final double zDist = z - zPreEn;
                final double yDist = y - posY;
                final double dist = Math.sqrt(xDist * xDist + zDist * zDist);
                if (dist > 4) {
                    x = xPreEn;
                    y = yPreEn;
                    z = zPreEn;
                    sendPacket(false, positionsBack, positions);
                } else if (dist > 0.05 && up) {
                    x = xPreEn;
                    y = yPreEn;
                    z = zPreEn;
                    sendPacket(false, positionsBack, positions);
                }
                if (Math.abs(yDist) < maxYTP && mc.thePlayer.getDistance(posX, posY, posZ) >= 4) {
                    x = xPreEn;
                    y = posY;
                    z = zPreEn;
                    sendPacket(false, positionsBack, positions);
                    mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, targetBlockPos, EnumFacing.UP));
                    mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, targetBlockPos, EnumFacing.UP));
                } else {
                    attack = false;
                }
            }
        }
        return true;
    }

    public static boolean infiniteReach(final Vec3 dest, final double range, final double maxXZTP, final double maxYTP,
                                        final ArrayList<Vec3> positionsBack, final ArrayList<Vec3> positions) {
        positions.clear();
        positionsBack.clear();

        final double step = maxXZTP / range;
        int steps = 0;
        for (int i = 0; i < range; i++) {
            steps++;
            if (maxXZTP * steps > range) {
                break;
            }
        }
        xPreEn = dest.xCoord;
        yPreEn = dest.yCoord;
        zPreEn = dest.zCoord;
        xPre = mc.thePlayer.posX;
        yPre = mc.thePlayer.posY;
        zPre = mc.thePlayer.posZ;
        boolean attack = true;
        boolean up = false;

        final boolean sneaking = mc.thePlayer.isSneaking();
        final MovingObjectPosition rayTrace;
        MovingObjectPosition rayTrace1 = null;
        if ((rayTraceWide(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                new Vec3(dest.xCoord, dest.yCoord, dest.zCoord), false, false, true))
                || (rayTrace1 = rayTracePos(
                new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ),
                new Vec3(dest.xCoord, dest.yCoord + mc.thePlayer.getEyeHeight(), dest.zCoord), false, false,
                true)) != null) {
            if ((rayTrace = rayTracePos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                    new Vec3(dest.xCoord, mc.thePlayer.posY, dest.zCoord), false, false, true)) != null
                    || (rayTrace1 = rayTracePos(
                    new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
                            mc.thePlayer.posZ),
                    new Vec3(dest.xCoord, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), dest.zCoord), false, false,
                    true)) != null) {
                MovingObjectPosition trace = null;
                if (rayTrace == null) {
                    trace = rayTrace1;
                }
                if (rayTrace1 == null) {
                    trace = rayTrace;
                }
                if (trace != null) {
                    if (trace.getBlockPos() != null) {
                        final boolean fence;
                        final BlockPos target = trace.getBlockPos();

                        up = true;
                        y = target.up().getY();
                        yPreEn = target.up().getY();
                        Block lastBlock = null;
                        boolean found = false;
                        for (int i = 0; i < maxYTP; i++) {
                            final MovingObjectPosition tr = rayTracePos(
                                    new Vec3(mc.thePlayer.posX, target.getY() + i, mc.thePlayer.posZ),
                                    new Vec3(dest.xCoord, target.getY() + i, dest.zCoord), false, false, true);
                            if (tr == null) {
                                continue;
                            }
                            if (tr.getBlockPos() == null) {
                                continue;
                            }

                            final BlockPos blockPos = tr.getBlockPos();
                            final Block block = mc.theWorld.getBlockState(blockPos).getBlock();
                            if (block.getMaterial() != Material.air) {
                                lastBlock = block;
                                continue;
                            }
                            fence = lastBlock instanceof BlockFence;
                            y = target.getY() + i;
                            yPreEn = target.getY() + i;
                            if (fence) {
                                y += 1;
                                yPreEn += 1;
                                if (i + 1 > maxYTP) {
                                    found = false;
                                    break;
                                }
                            }
                            found = true;
                            break;
                        }

                        if (!found) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            } else {
                final MovingObjectPosition ent = rayTracePos(
                        new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                        new Vec3(dest.xCoord, dest.yCoord, dest.zCoord), false, false, false);
                if (ent != null && ent.entityHit == null) {
                    y = mc.thePlayer.posY;
                    yPreEn = mc.thePlayer.posY;
                } else {
                    y = mc.thePlayer.posY;
                    yPreEn = dest.yCoord;
                }

            }
        }
        if (sneaking) {
            PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
        }
        for (int i = 0; i < steps; i++) {
            if (i == 1 && up) {
                x = mc.thePlayer.posX;
                y = yPreEn;
                z = mc.thePlayer.posZ;
                sendPacket(false, positionsBack, positions);
            }
            if (i != steps - 1) {
                {
                    final double difX = mc.thePlayer.posX - xPreEn;
                    final double difY = mc.thePlayer.posY - yPreEn;
                    final double difZ = mc.thePlayer.posZ - zPreEn;
                    final double divider = step * i;
                    x = mc.thePlayer.posX - difX * divider;
                    y = mc.thePlayer.posY - difY * (up ? 1 : divider);
                    z = mc.thePlayer.posZ - difZ * divider;
                }
                sendPacket(false, positionsBack, positions);
            } else {
                // if last teleport
                {
                    final double difX = mc.thePlayer.posX - xPreEn;
                    final double difY = mc.thePlayer.posY - yPreEn;
                    final double difZ = mc.thePlayer.posZ - zPreEn;
                    final double divider = step * i;
                    x = mc.thePlayer.posX - difX * divider;
                    y = mc.thePlayer.posY - difY * (up ? 1 : divider);
                    z = mc.thePlayer.posZ - difZ * divider;
                }
                sendPacket(false, positionsBack, positions);
                final double xDist = x - xPreEn;
                final double zDist = z - zPreEn;
                final double yDist = y - dest.yCoord;
                final double dist = Math.sqrt(xDist * xDist + zDist * zDist);
                if (dist > 4) {
                    x = xPreEn;
                    y = yPreEn;
                    z = zPreEn;
                    sendPacket(false, positionsBack, positions);
                } else if (dist > 0.05 && up) {
                    x = xPreEn;
                    y = yPreEn;
                    z = zPreEn;
                    sendPacket(false, positionsBack, positions);
                }
                if (Math.abs(yDist) < maxYTP) {
                    x = xPreEn;
                    y = dest.yCoord;
                    z = zPreEn;
                    sendPacket(false, positionsBack, positions);
                    //Attack / interact

                } else {
                    attack = false;
                }
            }
        }

        // Go back!
        for (int i = positions.size() - 2; i > -1; i--) {
            {
                x = positions.get(i).xCoord;
                y = positions.get(i).yCoord;
                z = positions.get(i).zCoord;
            }
            sendPacket(false, positionsBack, positions);
        }
        x = mc.thePlayer.posX;
        y = mc.thePlayer.posY;
        z = mc.thePlayer.posZ;
        sendPacket(false, positionsBack, positions);
        if (!attack) {
            if (sneaking) {
                PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            }
            positions.clear();
            positionsBack.clear();
            return false;
        }
        if (sneaking) {
            PacketUtil.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
        }
        return true;
    }

    private static void attackInf(final EntityLivingBase en) {

        final AttackEvent e = new AttackEvent(en);
        e.call();

        mc.thePlayer.swingItem();
        PacketUtil.sendPacketWithoutEvent(new C02PacketUseEntity(en, C02PacketUseEntity.Action.ATTACK));


        final float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), en.getCreatureAttribute());
        final boolean vanillaCrit = (mc.thePlayer.fallDistance > 0.0F) && (!mc.thePlayer.onGround)
                && (!mc.thePlayer.isOnLadder()) && (!mc.thePlayer.isInWater())
                && (!mc.thePlayer.isPotionActive(Potion.blindness)) && (mc.thePlayer.ridingEntity == null);
        if ((vanillaCrit)) {
            mc.thePlayer.onCriticalHit(en);
        }
        if (sharpLevel > 0.0F) {
            mc.thePlayer.onEnchantmentCritical(en);
        }
    }

    public static void sendPacket(final boolean goingBack, final ArrayList<Vec3> positionsBack, final ArrayList<Vec3> positions) {
        final C03PacketPlayer.C04PacketPlayerPosition playerPacket = new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false);
        PacketUtil.sendPacketWithoutEvent(playerPacket);
        if (goingBack) {
            positionsBack.add(new Vec3(x, y, z));
            return;
        }
        positions.add(new Vec3(x, y, z));
    }

    public static MovingObjectPosition rayTracePos(final Vec3 vec31, final Vec3 vec32, final boolean stopOnLiquid,
                                                   final boolean ignoreBlockWithoutBoundingBox, final boolean returnLastUncollidableBlock) {
        final float[] rots = getFacePosRemote(vec32, vec31);
        final float yaw = rots[0];
        final double angleA = Math.toRadians(normalizeAngle(yaw));
        final double angleB = Math.toRadians(normalizeAngle(yaw) + 180);
        final double size = 2.1;
        final double size2 = 2.1;
        final Vec3 left = new Vec3(vec31.xCoord + Math.cos(angleA) * size, vec31.yCoord,
                vec31.zCoord + Math.sin(angleA) * size);

        final Vec3 right = new Vec3(vec31.xCoord + Math.cos(angleB) * size, vec31.yCoord,
                vec31.zCoord + Math.sin(angleB) * size);

        final Vec3 left2 = new Vec3(vec32.xCoord + Math.cos(angleA) * size, vec32.yCoord,
                vec32.zCoord + Math.sin(angleA) * size);

        final Vec3 right2 = new Vec3(vec32.xCoord + Math.cos(angleB) * size, vec32.yCoord,
                vec32.zCoord + Math.sin(angleB) * size);

        new Vec3(vec31.xCoord + Math.cos(angleA) * size2, vec31.yCoord,
                vec31.zCoord + Math.sin(angleA) * size2);

        new Vec3(vec31.xCoord + Math.cos(angleB) * size2, vec31.yCoord,
                vec31.zCoord + Math.sin(angleB) * size2);

        new Vec3(vec32.xCoord + Math.cos(angleA) * size2, vec32.yCoord,
                vec32.zCoord + Math.sin(angleA) * size2);

        new Vec3(vec32.xCoord + Math.cos(angleB) * size2, vec32.yCoord,
                vec32.zCoord + Math.sin(angleB) * size2);

        final MovingObjectPosition trace1 = mc.theWorld.rayTraceBlocks(left, left2, stopOnLiquid,
                ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);

        final MovingObjectPosition trace2 = mc.theWorld.rayTraceBlocks(vec31, vec32, stopOnLiquid,
                ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);

        final MovingObjectPosition trace3 = mc.theWorld.rayTraceBlocks(right, right2, stopOnLiquid,
                ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);

        if (trace2 != null || trace1 != null || trace3 != null) {
            if (returnLastUncollidableBlock) {
                if (trace3 != null && (getBlock(trace3.getBlockPos()).getMaterial() != Material.air
                        || trace3.entityHit != null)) {
                    return trace3;
                }
                if (trace1 != null && (getBlock(trace1.getBlockPos()).getMaterial() != Material.air
                        || trace1.entityHit != null)) {
                    return trace1;
                }
                if (trace2 != null && (getBlock(trace2.getBlockPos()).getMaterial() != Material.air
                        || trace2.entityHit != null)) {
                    return trace2;
                }
            } else {
                if (trace3 != null) {
                    return trace3;
                }
                if (trace1 != null) {
                    return trace1;
                }
                return trace2;
            }
        }
        if (trace2 == null) {
            if (trace3 == null) {
                return trace1;
            }
            return trace3;
        }
        return trace2;
    }

    public static boolean rayTraceWide(final Vec3 vec31, final Vec3 vec32, final boolean stopOnLiquid, final boolean ignoreBlockWithoutBoundingBox,
                                       final boolean returnLastUncollidableBlock) {
        float yaw = getFacePosRemote(vec32, vec31)[0];
        yaw = normalizeAngle(yaw);
        yaw += 180;
        yaw = MathHelper.wrapAngleTo180_float(yaw);
        final double angleA = Math.toRadians(yaw);
        final double angleB = Math.toRadians(yaw + 180);
        final double size = 2.1;
        final Vec3 left = new Vec3(vec31.xCoord + Math.cos(angleA) * size, vec31.yCoord,
                vec31.zCoord + Math.sin(angleA) * size);
        final Vec3 right = new Vec3(vec31.xCoord + Math.cos(angleB) * size, vec31.yCoord,
                vec31.zCoord + Math.sin(angleB) * size);
        final Vec3 left2 = new Vec3(vec32.xCoord + Math.cos(angleA) * size, vec32.yCoord,
                vec32.zCoord + Math.sin(angleA) * size);
        final Vec3 right2 = new Vec3(vec32.xCoord + Math.cos(angleB) * size, vec32.yCoord,
                vec32.zCoord + Math.sin(angleB) * size);

        final MovingObjectPosition trace1 = mc.theWorld.rayTraceBlocks(left, left2, stopOnLiquid,
                ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
        final MovingObjectPosition trace2 = mc.theWorld.rayTraceBlocks(vec31, vec32, stopOnLiquid,
                ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);
        final MovingObjectPosition trace3 = mc.theWorld.rayTraceBlocks(right, right2, stopOnLiquid,
                ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock);

        if (returnLastUncollidableBlock) {
            return (trace1 != null && getBlock(trace1.getBlockPos()).getMaterial() != Material.air)
                    || (trace2 != null && getBlock(trace2.getBlockPos()).getMaterial() != Material.air)
                    || (trace3 != null && getBlock(trace3.getBlockPos()).getMaterial() != Material.air);
        } else {
            return trace1 != null || trace2 != null || trace3 != null;
        }

    }

    public static boolean isBlacklisted(final Entity en) {
        for (final int i : blackList) {
            if (en.getEntityId() == i) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<EntityLivingBase> getClosestEntitiesToEntity(final float range, final Entity ent) {
        final ArrayList<EntityLivingBase> entities = new ArrayList<>();
        for (final Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (isNotItem(o) && !ent.isEntityEqual((EntityLivingBase) o)) {
                final EntityLivingBase en = (EntityLivingBase) o;
                if (ent.getDistanceToEntity(en) < range) {
                    entities.add(en);
                }
            }
        }
        return entities;
    }

    /**
     * Returns the distance to the entity. Args: entity
     */
    public float getDistanceToEntityFromEntity(final Entity entityIn, final Entity entityIn2) {
        final float f = (float) (entityIn.posX - entityIn2.posX);
        final float f1 = (float) (entityIn.posY - entityIn2.posY);
        final float f2 = (float) (entityIn.posZ - entityIn2.posZ);
        return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
    }

    public static ArrayList<EntityLivingBase> getClosestEntities(final float range) {
        final ArrayList<EntityLivingBase> entities = new ArrayList<>();
        for (final Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (isNotItem(o) && !(o instanceof EntityPlayerSP)) {
                final EntityLivingBase en = (EntityLivingBase) o;
                if (!validEntity(en)) {
                    continue;
                }
                if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) < range) {
                    entities.add(en);
                }
            }
        }
        return entities;
    }

    public static boolean validEntity(final EntityLivingBase player) {
        if (player.isEntityEqual(Minecraft.getMinecraft().thePlayer)) {
            return false;
        }

        if (player instanceof EntityPlayer && !((BooleanSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("TPAura", "Players"))).isEnabled()) {
            return false;
        }

        if (!(player instanceof EntityPlayer)) {
            if (!((BooleanSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("TPAura", "Non Players"))).isEnabled())
                return false;
        }

        if (player.isInvisible() && !((BooleanSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("TPAura", "Invisibles"))).isEnabled())
            return false;

        if (player.isDead && !((BooleanSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("TPAura", "Attack Dead"))).isEnabled())
            return false;

        if (AntiBot.bots.contains(player))
            return false;

        if (player.isOnSameTeam(mc.thePlayer) && ((BooleanSetting) Objects.requireNonNull(Rise.INSTANCE.getModuleManager().getSetting("TPAura", "Ignore Teammates"))).isEnabled())
            return false;

        if (player.ticksExisted < 2)
            return false;

        if (player instanceof EntityPlayer) {
            for (final String name : Rise.INSTANCE.getFriends()) {
                if (name.equalsIgnoreCase(((EntityPlayer) player).getGameProfile().getName())) {
                    return false;
                }
            }
        }

        return !isBlacklisted(player);
    }

    public static EntityLivingBase getClosestEntity(final float range) {
        EntityLivingBase closestEntity = null;
        float mindistance = range;
        for (final Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (isNotItem(o) && !(o instanceof EntityPlayerSP)) {
                final EntityLivingBase en = (EntityLivingBase) o;
                if (!validEntity(en)) {
                    continue;
                }
                if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) < mindistance) {
                    mindistance = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en);
                    closestEntity = en;
                }
            }
        }
        return closestEntity;
    }

    public static EntityLivingBase getClosestEntitySkipValidCheck(final float range) {
        EntityLivingBase closestEntity = null;
        float mindistance = range;
        for (final Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (isNotItem(o) && !(o instanceof EntityPlayerSP)) {
                final EntityLivingBase en = (EntityLivingBase) o;
                if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en) < mindistance) {
                    mindistance = Minecraft.getMinecraft().thePlayer.getDistanceToEntity(en);
                    closestEntity = en;
                }
            }
        }
        return closestEntity;
    }

    public static EntityLivingBase getClosestEntityToEntity(final float range, final Entity ent) {
        EntityLivingBase closestEntity = null;
        float mindistance = range;
        for (final Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            if (isNotItem(o) && !ent.isEntityEqual((EntityLivingBase) o)) {
                final EntityLivingBase en = (EntityLivingBase) o;
                if (ent.getDistanceToEntity(en) < mindistance) {
                    mindistance = ent.getDistanceToEntity(en);
                    closestEntity = en;
                }
            }
        }
        return closestEntity;
    }

    public static boolean isNotItem(final Object o) {
        return o instanceof EntityLivingBase;
    }

    public static void faceEntity(final Entity en) {
        facePos(new Vec3(en.posX - 0.5, en.posY + (en.getEyeHeight() - en.height / 1.5), en.posZ - 0.5));

    }

    public static void faceBlock(final BlockPos blockPos) {
        facePos(getVec3(blockPos));
    }

    public static Vec3 getVec3(final BlockPos blockPos) {
        return new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static BlockPos getBlockPos(final Vec3 vec) {
        return new BlockPos(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public static void facePos(final Vec3 vec) {

        final double diffX = vec.xCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posX;
        final double diffY = vec.yCoord + 0.5
                - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        final double diffZ = vec.zCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        final float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
        Minecraft.getMinecraft().thePlayer.rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw
                + MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw);
        Minecraft.getMinecraft().thePlayer.rotationPitch = Minecraft.getMinecraft().thePlayer.rotationPitch
                + MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch);
    }


    public static float[] getFacePos(final Vec3 vec) {
        final double diffX = vec.xCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posX;
        final double diffY = vec.yCoord + 0.5
                - (Minecraft.getMinecraft().thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight());
        final double diffZ = vec.zCoord + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        final float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
        return new float[]{
                Minecraft.getMinecraft().thePlayer.rotationYaw
                        + MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw),
                Minecraft.getMinecraft().thePlayer.rotationPitch
                        + MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch)};
    }

    public static float[] getFacePosRemote(final Vec3 src, final Vec3 dest) {
        final double diffX = dest.xCoord - src.xCoord;
        final double diffY = dest.yCoord - (src.yCoord);
        final double diffZ = dest.zCoord - src.zCoord;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
        final float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
        return new float[]{MathHelper.wrapAngleTo180_float(yaw),
                MathHelper.wrapAngleTo180_float(pitch)};
    }


    public static float[] getFacePosEntity(final Entity en) {
        if (en == null) {
            return new float[]{Minecraft.getMinecraft().thePlayer.rotationYawHead,
                    Minecraft.getMinecraft().thePlayer.rotationPitch};
        }
        return getFacePos(new Vec3(en.posX - 0.5, en.posY + (en.getEyeHeight() - en.height / 1.5), en.posZ - 0.5));
    }


    public static float[] getFacePosEntityRemote(final EntityLivingBase facing, final Entity en) {
        if (en == null) {
            return new float[]{facing.rotationYawHead, facing.rotationPitch};
        }
        return getFacePosRemote(new Vec3(facing.posX, facing.posY + en.getEyeHeight(), facing.posZ),
                new Vec3(en.posX, en.posY + en.getEyeHeight(), en.posZ));
    }

    public static float getPlayerBlockDistance(final BlockPos blockPos) {
        return getPlayerBlockDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static float getPlayerBlockDistance(final double posX, final double posY, final double posZ) {
        final float xDiff = (float) (Minecraft.getMinecraft().thePlayer.posX - posX);
        final float yDiff = (float) (Minecraft.getMinecraft().thePlayer.posY - posY);
        final float zDiff = (float) (Minecraft.getMinecraft().thePlayer.posZ - posZ);
        return getBlockDistance(xDiff, yDiff, zDiff);
    }

    public static float getBlockDistance(final float xDiff, final float yDiff, final float zDiff) {
        return MathHelper.sqrt_float(
                (xDiff - 0.5F) * (xDiff - 0.5F) + (yDiff - 0.5F) * (yDiff - 0.5F) + (zDiff - 0.5F) * (zDiff - 0.5F));
    }

    public static ArrayList<EntityItem> getNearbyItems(final int range) {
        final ArrayList<EntityItem> eList = new ArrayList<>();
        for (final Object o : Minecraft.getMinecraft().theWorld.getLoadedEntityList()) {
            if (!(o instanceof EntityItem)) {
                continue;
            }
            final EntityItem e = (EntityItem) o;
            if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(e) >= range) {
                continue;
            }

            eList.add(e);
        }
        return eList;
    }

    public static EntityItem getClosestItem(final float range) {
        EntityItem ee = null;
        for (final Object o : Minecraft.getMinecraft().theWorld.getLoadedEntityList()) {
            if (!(o instanceof EntityItem)) {
                continue;
            }
            final EntityItem e = (EntityItem) o;
            if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(e) >= range) {
                continue;
            }
            ee = e;
        }
        return ee;
    }

    public static Entity getClosestItemOrXPOrb(final float range) {
        Entity ee = null;
        for (final Object o : Minecraft.getMinecraft().theWorld.getLoadedEntityList()) {
            if (!(o instanceof EntityItem) && !(o instanceof EntityXPOrb)) {
                continue;
            }
            final Entity e = (Entity) o;
            if (Minecraft.getMinecraft().thePlayer.getDistanceToEntity(e) >= range) {
                continue;
            }
            ee = e;
        }
        return ee;
    }

    public static double normalizeAngle(final double angle) {
        return (angle + 360) % 360;
    }

    public static float normalizeAngle(final float angle) {
        return (angle + 360) % 360;
    }

    public static int getItemIndexHotbar(final int itemID) {
        for (int i = 0; i < 9; i++) {
            final ItemStack stackInSlot = mc.thePlayer.inventory.getStackInSlot(i);
            if (stackInSlot == null) {
                continue;
            }
            if (itemID == Item.getIdFromItem(stackInSlot.getItem())) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isBlockPosAir(final BlockPos blockPos) {
        return mc.theWorld.getBlockState(blockPos).getBlock().getMaterial() == Material.air;
    }

    public static Block getBlockRelativeToEntity(final Entity en, final double d) {
        return getBlock(new BlockPos(en.posX, en.posY + d, en.posZ));
    }

    public static BlockPos getBlockPosRelativeToEntity(final Entity en, final double d) {
        return new BlockPos(en.posX, en.posY + d, en.posZ);
    }

    public static Block getBlock(final BlockPos pos) {
        return mc.theWorld.getBlockState(pos).getBlock();
    }

    private static Vec3 lastLoc = null;

    public static Vec3 getLastGroundLocation() {
        return lastLoc;

    }

    public static void updateLastGroundLocation() {
        if (mc.thePlayer.onGround) {
            lastLoc = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        }
    }


    public static IBlockState getBlockState(final BlockPos blockPos) {
        return mc.theWorld.getBlockState(blockPos);
    }

    public static boolean hasEntity(final Entity en) {
        if (en == null) {
            return false;
        }
        if (!targets.isEmpty()) {
            for (final EntityLivingBase en1 : targets) {
                if (en1 == null) {
                    continue;
                }
                if (en1.isEntityEqual(en)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean blackEntity(final Entity en) {
        if (en == null) {
            return false;
        }
        if (!blackList.isEmpty()) {
            for (final EntityLivingBase en1 : blackList2) {
                if (en1 == null) {
                    continue;
                }
                if (en1.isEntityEqual(en)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean getDisableAura() {
        return disableAura;
    }

    public static boolean isReachExploit() {
        return reachExploit;
    }

    private static final double packetTPRange = 10;

    public static double getPacketTPRange() {
        return packetTPRange;
    }

    public static double getRange() {
        return range;
    }

    public static boolean getHeadsnap() {
        return headsnap;
    }

    public static int getAPS() {
        return timercap;
    }

    public static void setTimer(final int set) {
        timercap = set;
    }

    public static void setRange(final double value) {
        range = value;
    }

    public static void setHeadSnap(final boolean selected) {
        headsnap = selected;
    }

    public static double getChargeRange() {
        return chargerange;
    }


}
