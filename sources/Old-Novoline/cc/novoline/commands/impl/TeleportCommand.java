package cc.novoline.commands.impl;

import cc.novoline.Novoline;
import cc.novoline.commands.NovoCommand;
import cc.novoline.events.EventManager;
import cc.novoline.events.EventTarget;
import cc.novoline.events.events.MotionUpdateEvent;
import cc.novoline.events.events.MoveEvent;
import cc.novoline.events.events.PacketEvent;
import cc.novoline.utils.ServerUtils;
import cc.novoline.utils.Servers;
import cc.novoline.utils.Timer;
import cc.novoline.utils.notifications.NotificationType;
import cc.novoline.utils.pathfinding.AStarCustomPathfinder;
import cc.novoline.utils.pathfinding.Vec3;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeleportCommand extends NovoCommand {

    public static boolean cancel;
    private float tempYDiff = 0, tempXDiff = 0, tempZDiff = 0;
    private Minecraft mc = Minecraft.getInstance();
    private Vec3 tpVec = new Vec3(0, 0, 0);
    private Timer timer = new Timer();
    private boolean airFreeze, playerTP;

    public TeleportCommand(@NonNull Novoline novoline) {
        super(novoline, "teleport", Arrays.asList("tp", "teleport"));
    }

    public EntityPlayer player;

    @Override
    public void process(String[] args) {
        if (args.length < 4 || !args[3].equals("goteleportnigga") || !ServerUtils.serverIs(Servers.PIT) && !ServerUtils.serverIs(Servers.BW)) {
            novoline.getNotificationManager().pop("Teleport", "Disabled!", NotificationType.ERROR);
            return;
        }

        EventManager.register(this);
        double posX = mc.player.posX, posY = mc.player.posY, posZ = mc.player.posZ;
        playerTP = false;
        player = null;

        switch (args.length) {
            case 4:
                novoline.getNotificationManager().pop("Teleporting to " + args[0] + "/" + args[1] + "/" + args[2], 5100, NotificationType.INFO);
            case 3:
                novoline.getNotificationManager().pop("Teleporting to " + args[0] + "/" + args[1] + "/" + args[2], 5100, NotificationType.INFO);
                mc.player.connection.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 0.41999998688698, posZ, false));
                mc.player.connection.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 0.7531999805212, posZ, false));
                break;
            case 1:
                for (Entity entity : Minecraft.getInstance().world.getLoadedEntityList()) {
                    if (entity.getName().equalsIgnoreCase(args[0]) && entity instanceof EntityPlayer) {
                        novoline.getNotificationManager().pop("Teleporting to player " + args[0], 5100, NotificationType.INFO);
                        mc.player.connection.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 0.41999998688698, posZ, false));
                        mc.player.connection.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY + 0.7531999805212, posZ, false));
                        playerTP = true;
                        player = (EntityPlayer) entity;
                        break;
                    }
                }
                if (!playerTP) {
                    novoline.getNotificationManager().pop("Unable to locate a player " + args[0] + " in your world", 1000, NotificationType.INFO);
                }
                break;
            default:
                if (!playerTP) {
                    novoline.getNotificationManager().pop("Unable to parse the coordinates!", "Please specify a valid coordinates!", 5000, NotificationType.ERROR);
                }
        }
        airFreeze = false;
        cancel = true;
        tempYDiff = 0;
        tempXDiff = 0;
        tempZDiff = 0;
        if (args.length != 1) {
            tpVec = new Vec3(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        }
        toRender.clear();
    }

    protected final net.minecraft.util.Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.cos(Math.toRadians(-yaw) - (float) Math.PI);
        float f1 = MathHelper.sin(Math.toRadians(-yaw) - (float) Math.PI);
        float f2 = -MathHelper.cos(Math.toRadians(-pitch));
        float f3 = MathHelper.sin(Math.toRadians(-pitch));
        return new net.minecraft.util.Vec3(f1 * f2, f3, f * f2);
    }

    @EventTarget
    public void onPacketReceived(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.INCOMING)) {
            if (cancel && event.getPacket() instanceof S08PacketPlayerPosLook) {
                cancel = false;
            }

        } else {
            if (event.getPacket() instanceof C03PacketPlayer && cancel) {
                event.setCancelled(true);
            }
        }
    }

    @EventTarget
    public void onMove(MoveEvent moveEvent) {
        // Фризить игрока в воздухе на некоторое время во избежании morepackets
        if (airFreeze) {
            moveEvent.setX(mc.player.motionX = 0);
            moveEvent.setY(mc.player.motionY = 0);
            moveEvent.setZ(mc.player.motionZ = 0);

            if (timer.delay(5000)) {
                airFreeze = false;
                EventManager.unregister(this);
            }
        }
    }

    List<Vec3> toRender = new ArrayList<>();

    public static void blockESPBox(BlockPos blockPos) {
        double x = blockPos.getX() - Minecraft.getInstance().getRenderManager().renderPosX;
        double y = blockPos.getY() - Minecraft.getInstance().getRenderManager().renderPosY;
        double z = blockPos.getZ() - Minecraft.getInstance().getRenderManager().renderPosZ;
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glLineWidth(2.0F);
        GL11.glColor4d(255, 12, 0.1, 0.15F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        //drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glColor4d(255, 1, 1, 0.5F);
        RenderGlobal.func_181561_a(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1, 1, 1, 1);
    }


    @EventTarget
    public void onHypixelPre(MotionUpdateEvent event) {
        if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
            // todo: если что-то меняешь, то посылать пакет на x + 0.5, y + 0.0125, z + 0.5, а не прямо на точку!!!

            if (mc.player.isMoving()) {
                EventManager.unregister(this);


                if (cancel) {
                    novoline.getNotificationManager().pop("Teleport cancelled due to movement!", 5000, NotificationType.WARNING);
                    airFreeze = true;
                    cancel = false;
                }
            }

            if (!airFreeze) {
                if (playerTP) {
                    teleportToLocation(new Vec3(mc.player.posX, mc.player.posY, mc.player.posZ), new Vec3(player.posX, player.posY, player.posZ), true, true);
                } else {
                    teleportToLocation(new Vec3(mc.player.posX, mc.player.posY, mc.player.posZ), new Vec3(tpVec.getX(), tpVec.getY(), tpVec.getZ()), true, true);
                }
            }
        }
    }

    // Если точка в пути "соприкосается" с блоком, то нужно обязательно послать пакет
    private boolean blocksAround(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock() != Blocks.air &&
                mc.world.getBlockState(pos).getBlock() != Blocks.tallgrass &&
                mc.world.getBlockState(pos).getBlock() != Blocks.double_plant &&
                mc.world.getBlockState(pos).getBlock() != Blocks.yellow_flower &&
                mc.world.getBlockState(pos).getBlock() != Blocks.red_flower;
    }


    public void teleportToLocation(Vec3 startVec, Vec3 endVec, boolean setPosition, boolean freeze) {
        // Добавлять все точки для посылания пакетов на эти точки
        for (Vec3 vec3 : computePath(startVec, endVec)) {
            mc.player.connection.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(vec3.getX(), vec3.getY(), vec3.getZ(), vec3.getY() % 0.125 == 0));
        }

        // Последний пакет на точку для подстраховки
        mc.player.connection.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(endVec.getX(), endVec.getY(), endVec.getZ(), endVec.getY() % 0.125 == 0));

        if (setPosition) {
            mc.player.setPositionAndUpdate(endVec.getX(), endVec.getY(), endVec.getZ());
        }

        // Сброс таймера фриза
        if (freeze) {
            timer.reset();
            airFreeze = true;
        } else {
            EventManager.unregister(this);
        }
    }

    private ArrayList<Vec3> computePath(Vec3 topFrom, Vec3 to) {
        if (!canPassThrow(new BlockPos(topFrom.mc()))) {
            topFrom = topFrom.addVector(0, 1, 0);
        }
        AStarCustomPathfinder pathfinder = new AStarCustomPathfinder(topFrom, to);
        pathfinder.compute();

        int i = 0;
        Vec3 lastLoc = null;
        Vec3 lastDashLoc = null;
        ArrayList<Vec3> path = new ArrayList<>();
        ArrayList<Vec3> pathFinderPath = pathfinder.getPath();
        for (Vec3 pathElm : pathFinderPath) {
            if (i == 0 || i == pathFinderPath.size() - 1) {
                if (lastLoc != null) {
                    path.add(lastLoc.addVector(0.5, 0, 0.5));
                }
                path.add(pathElm.addVector(0.5, 0, 0.5));
                lastDashLoc = pathElm;
            } else {
                boolean canContinue = true;
                if (pathElm.squareDistanceTo(lastDashLoc) > 5 * 5) {
                    canContinue = false;
                } else {
                    double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
                    double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
                    double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
                    double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
                    double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
                    double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());
                    cordsLoop:
                    for (int x = (int) smallX; x <= bigX; x++) {
                        for (int y = (int) smallY; y <= bigY; y++) {
                            for (int z = (int) smallZ; z <= bigZ; z++) {
                                if (!AStarCustomPathfinder.checkPositionValidity(x, y, z, false)) {
                                    canContinue = false;
                                    break cordsLoop;
                                }
                            }
                        }
                    }
                }
                if (!canContinue) {
                    path.add(lastLoc.addVector(0.5, 0, 0.5));
                    lastDashLoc = lastLoc;
                }
            }
            lastLoc = pathElm;
            i++;
        }
        return path;
    }

    private boolean canPassThrow(BlockPos pos) {
        Block block = Minecraft.getInstance().world.getBlockState(new net.minecraft.util.BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock();
        return block.getMaterial() == Material.air || block.getMaterial() == Material.plants
                || block.getMaterial() == Material.vine || block == Blocks.ladder
                || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
    }
}
