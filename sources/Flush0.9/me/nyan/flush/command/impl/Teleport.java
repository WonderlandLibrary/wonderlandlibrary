package me.nyan.flush.command.impl;

import me.nyan.flush.command.Command;
import me.nyan.flush.utils.movement.PathFinder;
import me.nyan.flush.utils.other.ChatUtils;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Vec3;

public class Teleport extends Command {
    public Teleport() {
        super("Teleport", "Teleports the player to choosen coordinates.", "teleport <posX> <posY> <posZ> | teleport <playerName>", "tp");
    }

    @Override
    public void onCommand(String[] args, String message) {
        if (args.length == 3) {
            try {
                double x = Double.parseDouble(args[0]);
                double y = Double.parseDouble(args[1]);
                double z = Double.parseDouble(args[2]);
                for (Vec3 vec3 : PathFinder.findPathTo(new Vec3(x, y + 2, z), 8)) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec3.xCoord, vec3.yCoord,
                                                                                                      vec3.zCoord, true));
                }
                mc.thePlayer.setPosition(x, y, z);
                ChatUtils.println(("Teleported you to " + x + ", " + y + ", " + z + ".").replace(".0", ""));
            } catch (Exception e) {
                ChatUtils.println("§4Invalid coordinates.");
            }
        } else if (args.length == 1) {
            EntityPlayer player = null;

            for (NetworkPlayerInfo networkplayerinfo : mc.getNetHandler().playerInfoMap.values()) {
                if (networkplayerinfo.getGameProfile().getName().equalsIgnoreCase(args[0]))
                    player = mc.theWorld.getPlayerEntityByUUID(networkplayerinfo.getGameProfile().getId());
            }

            if (player != null) {
                if (player == mc.thePlayer) {
                    ChatUtils.println("§aWhy are you trying to teleport to yourself?");
                } else {
                    for (Vec3 vec3 : PathFinder.findPathTo(player.getPositionVector().addVector(0, 2, 0), 8)) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec3.xCoord, vec3.yCoord,
                                                                                                          vec3.zCoord, true));
                    }
                    mc.thePlayer.setPosition(player.posX, player.posY + 2, player.posZ);
                    ChatUtils.println(("§9Teleported you to " + player.getName() + "."));
                }
            } else {
                ChatUtils.println("§4Invalid Player.");
            }
        } else {
            sendSyntaxHelpMessage();
        }
    }
}
