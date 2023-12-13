package today.flux.module.implement.Command;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.soterdev.SoterObfuscator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import today.flux.Flux;
import today.flux.event.PacketReceiveEvent;
import today.flux.event.PacketSendEvent;
import today.flux.event.PreUpdateEvent;
import today.flux.module.Command;
import today.flux.utility.*;

import java.util.ArrayList;

@Command.Info(name = "tp", syntax = { "<Pos>" }, help = "Teleport.")
public class TPCmd extends Command {
    private ArrayList<Vec3> path = new ArrayList<>();
    public boolean tp;
    public BlockPos target;
    public Entity targetEntity;
    public TimeHelper timer = new TimeHelper();

    // @SoterObfuscator.Obfuscation(flags = "+native")
    @Override
    public void execute(String[] args) throws Error {
        timer = new TimeHelper();
        if (args.length != 3 && args.length != 1) {
            this.syntaxError();
        } else {
            if (!ServerUtils.INSTANCE.isOnHypixel()) {
                PlayerUtils.tellPlayer("Teleport is only for Hypixel!");
                return;
            }

            PlayerUtils.tellPlayer("Please Wait...");
            timer.reset();
            tp = false;
            targetEntity = null;
            target = null;

            if (args.length == 3) {
                target = new BlockPos(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));

                if (mc.theWorld.getBlockState(target).getBlock().getMaterial() != Material.air ||
                    mc.theWorld.getBlockState(target.up()).getBlock().getMaterial() != Material.air) {
                    PlayerUtils.tellPlayer("Can not teleport because you are trying to teleport to a full block!");
                    return;
                }

                if (target.getY() < 20) {
                    PlayerUtils.tellPlayer("PosY can not lower than 20!");
                    return;
                }

                while (mc.theWorld.getBlockState(target).getBlock().getMaterial() == Material.air && target.getY() > -20) {
                    target = target.down();
                }

                target = target.up(2);

                if (mc.theWorld.getBlockState(target).getBlock().getMaterial() == Material.air) {
                    target = new BlockPos(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
                }

            } else {
                for (Entity entity : mc.theWorld.loadedEntityList) {
                    if (entity instanceof EntityPlayer && mc.thePlayer != entity) {
                        if (entity.getName().equalsIgnoreCase(args[0])) {
                            targetEntity = entity;
                            break;
                        }
                    }
                }

                if (targetEntity == null) {
                    PlayerUtils.tellPlayer("Can not locate " + args[0]);
                    return;
                } else {
                    PlayerUtils.tellPlayer("Located Player " + targetEntity.getName() + "!");
                }
            }
            ChatUtils.debug("Disabling...");
            PlayerCapabilities playerCapabilities = new PlayerCapabilities();
            playerCapabilities.isFlying = true;
            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C0FPacketConfirmTransaction(0, (short)(-1), false));
            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C13PacketPlayerAbilities(playerCapabilities));
            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.41999998688697815, mc.thePlayer.posZ, false));
            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.7531999805212015, mc.thePlayer.posZ, false));
            EventManager.register(this);
        }
    }


    @EventTarget
    public void onSendPacket(PacketSendEvent e) {
        if (e.getPacket() instanceof C03PacketPlayer) {
            e.setCancelled(!tp);
        }
    }

    @EventTarget
    public void onRePacket(PacketReceiveEvent e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            if (!tp) {
                ChatUtils.debug("Disabled!");
                tp = true;
                e.setCancelled(true);
            }
        }
    }

    // @SoterObfuscator.Obfuscation(flags = "+native")
    @EventTarget
    public void onPre(PreUpdateEvent e) {
        if (timer.isDelayComplete(8500)) {
            PlayerUtils.tellPlayer("Failed to teleport. Please make sure you are in Hypixel Gaming!");
            EventManager.unregister(this);
        }

        if (tp) {
            tp = false;

            Vec3 topFrom = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

            if (target == null) target = new BlockPos(targetEntity.posX, targetEntity.posY + 1, targetEntity.posZ);

            Vec3 to = new Vec3(target.getX(), target.getY(), target.getZ());
            path = computePath(topFrom, to);

            PlayerUtils.tellPlayer("Teleporting to X:" + target.getX() + " Y:" + target.getY() + " Z:" + target.getZ());

            EventManager.unregister(this);

            new Thread() {
                // @SoterObfuscator.Obfuscation(flags = "+native")
                @Override
                public void run() {
                    try {
                        sleep(1000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }

                    for (Vec3 pathElm : path) {
                        mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(pathElm.getX(), pathElm.getY(), pathElm.getZ(), true));
                    }

                    mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(target.getX(), target.getY(), target.getZ(), true));
                    mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer(true));
                    mc.thePlayer.setPosition(target.getX(), target.getY(), target.getZ());

                    PlayerUtils.tellPlayer("Teleported!");
                    PlayerCapabilities playerCapabilities = new PlayerCapabilities();
                    playerCapabilities.isFlying = false;
                    mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C13PacketPlayerAbilities(playerCapabilities));
                    mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                }
            }.start();
        }
    }

    private ArrayList<Vec3> computePath(Vec3 topFrom, Vec3 to) {
        if (!canPassThrow(new BlockPos(topFrom.mc()))) {
            topFrom = topFrom.addVector(0, 1, 0);
        }
        AStarCustomPathFinder pathfinder = new AStarCustomPathFinder(topFrom, to);
        pathfinder.compute();

        int i = 0;
        Vec3 lastLoc = null;
        Vec3 lastDashLoc = null;
        ArrayList<Vec3> path = new ArrayList<Vec3>();
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
