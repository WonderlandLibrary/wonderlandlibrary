/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.cmd.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayer;
import org.apache.commons.lang3.math.NumberUtils;
import org.celestial.client.cmd.CommandAbstract;
import org.celestial.client.helpers.misc.ChatHelper;
import org.celestial.client.ui.notification.NotificationManager;
import org.celestial.client.ui.notification.NotificationType;

public class TPCommand
extends CommandAbstract {
    public TPCommand() {
        super("tp", "tp", "\u00a76.tp name | X Y Z", "tp", "tp");
    }

    @Override
    public void execute(String ... args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("tp")) {
                try {
                    if (!NumberUtils.isNumber(args[1])) {
                        EntityPlayer entityPlayer = TPCommand.mc.world.getPlayerEntityByName(args[1]);
                        if (entityPlayer == null) {
                            ChatHelper.addChatMessage("This" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " player " + (Object)((Object)ChatFormatting.WHITE) + "is" + (Object)((Object)ChatFormatting.RED) + " far" + (Object)((Object)ChatFormatting.WHITE) + " from" + (Object)((Object)ChatFormatting.GREEN) + " you!");
                            NotificationManager.publicity("TP Manager", "This" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + " player " + (Object)((Object)ChatFormatting.WHITE) + "is" + (Object)((Object)ChatFormatting.RED) + " far" + (Object)((Object)ChatFormatting.WHITE) + " from" + (Object)((Object)ChatFormatting.GREEN) + " you!", 4, NotificationType.ERROR);
                            return;
                        }
                        if (args[1].equals(entityPlayer.getName())) {
                            double x = entityPlayer.posX;
                            double y = entityPlayer.posY;
                            double z = entityPlayer.posZ;
                            for (int i = 0; i < 3; ++i) {
                                TPCommand.mc.player.connection.sendPacket(new CPacketPlayer.Position(x + 0.5, y + 0.1, z - 0.5, false));
                                TPCommand.mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, true));
                                TPCommand.mc.player.connection.sendPacket(new CPacketPlayer.Position(x + 0.5, y + 0.1, z - 0.5, true));
                            }
                        }
                    }
                    if (NumberUtils.isNumber(args[1])) {
                        double x = Double.parseDouble(args[1]);
                        double y = Double.parseDouble(args[2]);
                        double z = Double.parseDouble(args[3]);
                        TPCommand.mc.player.connection.sendPacket(new CPacketPlayer.Position(x + 0.5, y + 0.1, z + 0.5, false));
                        TPCommand.mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, true));
                        TPCommand.mc.player.connection.sendPacket(new CPacketPlayer.Position(x + 0.5, y + 0.1, z + 0.5, true));
                    }
                }
                catch (Exception exception) {}
            }
        } else {
            ChatHelper.addChatMessage(this.getUsage());
        }
    }
}

