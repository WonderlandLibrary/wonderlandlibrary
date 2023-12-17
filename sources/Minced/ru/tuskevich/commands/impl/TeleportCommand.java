// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.commands.impl;

import net.minecraft.client.network.NetHandlerPlayClient;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiBossOverlay;
import org.apache.commons.lang3.math.NumberUtils;
import ru.tuskevich.commands.Command;
import ru.tuskevich.commands.CommandAbstract;

@Command(name = "tp", description = "Allows you to teleport")
public class TeleportCommand extends CommandAbstract
{
    @Override
    public void execute(final String[] args) throws Exception {
        if (args.length >= 2) {
            if (!NumberUtils.isNumber(args[1]) && args[1].equals("air")) {
                final int x = GuiBossOverlay.xWay;
                final int y = 100;
                int z = GuiBossOverlay.zWay;
                if (x == 0 && z == 0) {
                    this.sendMessage("Airdrop not founded");
                }
                else {
                    z += 150;
                    for (int i = 0; i <= 24; ++i) {
                        final Minecraft mc = TeleportCommand.mc;
                        Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(55555.0, 180.0, 55555.0, true));
                    }
                    for (int i = 0; i <= 24; ++i) {
                        final Minecraft mc2 = TeleportCommand.mc;
                        Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(x, 180.0, z, true));
                    }
                    for (int i = 0; i <= 24; ++i) {
                        final Minecraft mc3 = TeleportCommand.mc;
                        final NetHandlerPlayClient connection = Minecraft.player.connection;
                        final Minecraft mc4 = TeleportCommand.mc;
                        final double posX = Minecraft.player.posX;
                        final double yIn = 180.0;
                        final Minecraft mc5 = TeleportCommand.mc;
                        connection.sendPacket(new CPacketPlayer.Position(posX, yIn, Minecraft.player.posZ, true));
                    }
                    final Minecraft mc6 = TeleportCommand.mc;
                    final NetHandlerPlayClient connection2 = Minecraft.player.connection;
                    final Minecraft mc7 = TeleportCommand.mc;
                    final double posX2 = Minecraft.player.posX;
                    final Minecraft mc8 = TeleportCommand.mc;
                    final double yIn2 = Minecraft.player.posY + 0.121599998474121;
                    final Minecraft mc9 = TeleportCommand.mc;
                    connection2.sendPacket(new CPacketPlayer.Position(posX2, yIn2, Minecraft.player.posZ, false));
                    final Minecraft mc10 = TeleportCommand.mc;
                    final NetHandlerPlayClient connection3 = Minecraft.player.connection;
                    final Minecraft mc11 = TeleportCommand.mc;
                    final double posX3 = Minecraft.player.posX;
                    final Minecraft mc12 = TeleportCommand.mc;
                    final double yIn3 = Minecraft.player.posY + 0.1623679977722166;
                    final Minecraft mc13 = TeleportCommand.mc;
                    connection3.sendPacket(new CPacketPlayer.Position(posX3, yIn3, Minecraft.player.posZ, false));
                    final Minecraft mc14 = TeleportCommand.mc;
                    final NetHandlerPlayClient connection4 = Minecraft.player.connection;
                    final Minecraft mc15 = TeleportCommand.mc;
                    final double posX4 = Minecraft.player.posX;
                    final Minecraft mc16 = TeleportCommand.mc;
                    final double yIn4 = Minecraft.player.posY + 0.123920636336059;
                    final Minecraft mc17 = TeleportCommand.mc;
                    connection4.sendPacket(new CPacketPlayer.Position(posX4, yIn4, Minecraft.player.posZ, false));
                    final Minecraft mc18 = TeleportCommand.mc;
                    final NetHandlerPlayClient connection5 = Minecraft.player.connection;
                    final Minecraft mc19 = TeleportCommand.mc;
                    final double posX5 = Minecraft.player.posX;
                    final Minecraft mc20 = TeleportCommand.mc;
                    final double yIn5 = Minecraft.player.posY + 0.0078422198694215;
                    final Minecraft mc21 = TeleportCommand.mc;
                    connection5.sendPacket(new CPacketPlayer.Position(posX5, yIn5, Minecraft.player.posZ, false));
                    this.sendMessage("Teleported to airdrop " + ChatFormatting.BLUE + "successfully");
                }
            }
            if (NumberUtils.isNumber(args[1])) {
                final int x = Integer.parseInt(args[1]);
                final int y = Integer.parseInt(args[2]);
                final int z = Integer.parseInt(args[3]);
                for (int i = 0; i <= 24; ++i) {
                    final Minecraft mc22 = TeleportCommand.mc;
                    Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(55555.0, 180.0, 55555.0, true));
                }
                for (int i = 0; i <= 24; ++i) {
                    final Minecraft mc23 = TeleportCommand.mc;
                    Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(x, 180.0, z, true));
                }
                for (int i = 0; i <= 24; ++i) {
                    final Minecraft mc24 = TeleportCommand.mc;
                    final NetHandlerPlayClient connection6 = Minecraft.player.connection;
                    final Minecraft mc25 = TeleportCommand.mc;
                    final double posX6 = Minecraft.player.posX;
                    final double yIn6 = 180.0;
                    final Minecraft mc26 = TeleportCommand.mc;
                    connection6.sendPacket(new CPacketPlayer.Position(posX6, yIn6, Minecraft.player.posZ, true));
                }
                final Minecraft mc27 = TeleportCommand.mc;
                final NetHandlerPlayClient connection7 = Minecraft.player.connection;
                final Minecraft mc28 = TeleportCommand.mc;
                final double posX7 = Minecraft.player.posX;
                final Minecraft mc29 = TeleportCommand.mc;
                final double yIn7 = Minecraft.player.posY + 0.121599998474121;
                final Minecraft mc30 = TeleportCommand.mc;
                connection7.sendPacket(new CPacketPlayer.Position(posX7, yIn7, Minecraft.player.posZ, false));
                final Minecraft mc31 = TeleportCommand.mc;
                final NetHandlerPlayClient connection8 = Minecraft.player.connection;
                final Minecraft mc32 = TeleportCommand.mc;
                final double posX8 = Minecraft.player.posX;
                final Minecraft mc33 = TeleportCommand.mc;
                final double yIn8 = Minecraft.player.posY + 0.1623679977722166;
                final Minecraft mc34 = TeleportCommand.mc;
                connection8.sendPacket(new CPacketPlayer.Position(posX8, yIn8, Minecraft.player.posZ, false));
                final Minecraft mc35 = TeleportCommand.mc;
                final NetHandlerPlayClient connection9 = Minecraft.player.connection;
                final Minecraft mc36 = TeleportCommand.mc;
                final double posX9 = Minecraft.player.posX;
                final Minecraft mc37 = TeleportCommand.mc;
                final double yIn9 = Minecraft.player.posY + 0.123920636336059;
                final Minecraft mc38 = TeleportCommand.mc;
                connection9.sendPacket(new CPacketPlayer.Position(posX9, yIn9, Minecraft.player.posZ, false));
                final Minecraft mc39 = TeleportCommand.mc;
                final NetHandlerPlayClient connection10 = Minecraft.player.connection;
                final Minecraft mc40 = TeleportCommand.mc;
                final double posX10 = Minecraft.player.posX;
                final Minecraft mc41 = TeleportCommand.mc;
                final double yIn10 = Minecraft.player.posY + 0.0078422198694215;
                final Minecraft mc42 = TeleportCommand.mc;
                connection10.sendPacket(new CPacketPlayer.Position(posX10, yIn10, Minecraft.player.posZ, false));
                this.sendMessage("Teleported " + ChatFormatting.BLUE + "successfully" + ChatFormatting.WHITE + " to " + x + " " + 180 + " " + z);
            }
        }
        else {
            this.error();
        }
    }
    
    @Override
    public void error() {
        this.sendMessage(ChatFormatting.GRAY + "Command use" + ChatFormatting.WHITE + ":");
        this.sendMessage(".tp" + ChatFormatting.BLUE + " <x> <y> <z>");
        this.sendMessage(".tp air");
    }
}
