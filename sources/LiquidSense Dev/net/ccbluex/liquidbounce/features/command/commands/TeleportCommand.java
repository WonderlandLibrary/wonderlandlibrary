package net.ccbluex.liquidbounce.features.command.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.AquaVit.liquidSense.modules.world.LightningCheck;
import me.AquaVit.liquidSense.modules.world.LookTp;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.event.MoveEvent;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.module.modules.render.Rotations;
import net.ccbluex.liquidbounce.utils.AStarCustomPathFinder;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.CustomVec3;
import net.ccbluex.liquidbounce.utils.MovementUtils;
import net.ccbluex.liquidbounce.utils.timer.TimeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.CommandException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TeleportCommand extends Command {
    public static BlockPos target;
    public Entity targetEntity;
    public TimeUtils timer = new TimeUtils();
    public TimeUtils tpreset = new TimeUtils();
    public boolean tp;
    int PlayerY;
    private ArrayList<CustomVec3> path = new ArrayList<>();
    private boolean tryCancelPacket;


    public TeleportCommand() {
        super("tp", new String[0]);
    }

    @Override
    public void execute(@NotNull String[] args) {
        if (args.length == 0) {
            ClientUtils.displayChatMessage("[Teleport] Please type X Y Z");
        }
        try {
            timer.reset();
            tp = false;
            targetEntity = null;
            target = null;
            if (args.length == 3) {
                target = new BlockPos(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            } else if (args.length == 2) {
                if (!args[0].contains("LN")) {
                    for (Entity entity : mc.theWorld.loadedEntityList) {
                        if (entity instanceof EntityPlayer && mc.thePlayer != entity) {
                            if (entity.getName().equalsIgnoreCase(args[0])) {
                                targetEntity = entity;
                                PlayerY = Integer.parseInt(args[1]);
                                break;
                            }
                        }
                    }

                    if (targetEntity == null) {
                        ClientUtils.displayChatMessage("Can not locate " + args[0]);
                        return;
                    } else {
                        ClientUtils.displayChatMessage("Located Player " + targetEntity.getName() + "And Pos Y Added " + PlayerY + " !");
                    }
                }
            } else if (args.length == 1) {

                if (args[0].contains("FN")) {
                    target = new BlockPos(LightningCheck.x, LightningCheck.y, LightningCheck.z);
                    ClientUtils.displayChatMessage("Located Lighting " + "!");
                }
                /*
                if (args[0].contains("TCK")) {
                    target = new BlockPos(LookTp., ClickTP.y_new, ClickTP.z_new);
                    ClientUtils.displayChatMessage("Located Click Point " + "!");
                }
                 */
                if (!args[0].contains("LN") && !args[0].contains("TCK")) {
                    for (Entity entity : mc.theWorld.loadedEntityList) {
                        if (entity instanceof EntityPlayer && mc.thePlayer != entity) {
                            if (entity.getName().equalsIgnoreCase(args[0])) {
                                targetEntity = entity;
                                PlayerY = 0;
                                break;
                            }
                        }
                    }

                    if (targetEntity == null) {
                        ClientUtils.displayChatMessage("Can not locate " + args[0]);
                        return;
                    } else {
                        ClientUtils.displayChatMessage("Located Player " + targetEntity.getName() + "And Pos Y Added " + PlayerY + " !");
                    }
                }


            }

            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + 0.41999998688697815, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + 0.675319998688697815, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
            tryCancelPacket = true;


        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @EventTarget
    public void onMove(MoveEvent e) {
        if (tryCancelPacket) {
            e.cancelEvent();
        }
    }

    @EventTarget
    public void onPacket(PacketEvent e) {
        if (tryCancelPacket && (e.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition || e.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook || e.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook)) {
            e.cancelEvent();
        }

        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) e.getPacket();
            packet.pitch = mc.thePlayer.rotationPitch;
            packet.yaw = mc.thePlayer.rotationYaw;
            tryCancelPacket = false;

            if (e.getPacket() instanceof S08PacketPlayerPosLook) {

                if (!tp) {
                    ClientUtils.displayChatMessage("暂时绕过反作弊!");
                    tp = true;
                    e.cancelEvent();
                }
            }
        }
    }

    @EventTarget
    private void onUpdate(MotionEvent event) {
        if (timer.hasReached(8500)) {
            ClientUtils.displayChatMessage("Failed to teleport.");
        }

        if (tp) {
            tp = false;

            CustomVec3 topFrom = new CustomVec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

            if (target == null)
                target = new BlockPos(targetEntity.posX, targetEntity.posY + PlayerY, targetEntity.posZ);

            CustomVec3 to = new CustomVec3(target.getX(), target.getY(), target.getZ());
            path = computePath(topFrom, to);
            tpreset.reset();
            ClientUtils.displayChatMessage("Teleporting to X:" + target.getX() + " Y:" + target.getY() + " Z:" + target.getZ());

            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(1000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }

                    for (CustomVec3 pathElm : path) {
                        mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));
                        C13PacketPlayerAbilities packet = new C13PacketPlayerAbilities();
                        packet.setInvulnerable(false);
                        packet.setCreativeMode(false);
                        packet.setFlying(false);
                        packet.setAllowFlying(false);
                        packet.setFlySpeed(1.0E8F);
                        packet.setWalkSpeed(1.0E8F - 1F);
                        mc.getNetHandler().getNetworkManager().sendPacket(packet);
                    }

                    mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(target.getX(), target.getY(), target.getZ(), mc.thePlayer.onGround));
                    mc.thePlayer.setPosition(target.getX(), target.getY(), target.getZ());
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(target.getX(), target.getY() - MovementUtils.getDistanceToGround(mc.thePlayer), target.getZ(), mc.thePlayer.onGround));
                    mc.thePlayer.setPosition(target.getX(), target.getY() - MovementUtils.getDistanceToGround(mc.thePlayer), target.getZ());

                    ClientUtils.displayChatMessage("Teleported!");
                }
            }.start();
        }
    }


    private ArrayList<CustomVec3> computePath(CustomVec3 topFrom, CustomVec3 to) {
        if (!canPassThrow(new BlockPos(topFrom.mc()))) {
            topFrom = topFrom.addVector(0, 1, 0);
        }
        AStarCustomPathFinder pathfinder = new AStarCustomPathFinder(topFrom, to);
        pathfinder.compute();

        int i = 0;
        CustomVec3 lastLoc = null;
        CustomVec3 lastDashLoc = null;
        ArrayList<CustomVec3> path = new ArrayList<CustomVec3>();
        ArrayList<CustomVec3> pathFinderPath = pathfinder.getPath();
        for (CustomVec3 pathElm : pathFinderPath) {
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
                                if (!AStarCustomPathFinder.checkPositionValidity(x, y, z, false)) {
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
        Block block = mc.theWorld.getBlockState(new net.minecraft.util.BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock();
        return block.getMaterial() == Material.air || block.getMaterial() == Material.plants || block.getMaterial() == Material.vine || block == Blocks.ladder || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
    }
}
