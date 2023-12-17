// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.commands.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import org.apache.commons.lang3.math.NumberUtils;
import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;
import ru.tuskevich.commands.Command;
import ru.tuskevich.commands.CommandAbstract;

@Command(name = "vclip", description = "Allows you to clip vertical")
public class VClipCommand extends CommandAbstract
{
    @Override
    public void execute(final String[] args) throws Exception {
        float y = 0.0f;
        if (args[1].equals("down")) {
            for (int i = 0; i < 255; ++i) {
                final WorldClient world = VClipCommand.mc.world;
                final Minecraft mc = VClipCommand.mc;
                if (world.getBlockState(new BlockPos(Minecraft.player).add(0, -i, 0)) == Blocks.AIR.getDefaultState()) {
                    y = (float)(-i - 1);
                    break;
                }
                final WorldClient world2 = VClipCommand.mc.world;
                final Minecraft mc2 = VClipCommand.mc;
                if (world2.getBlockState(new BlockPos(Minecraft.player).add(0, -i, 0)) == Blocks.BEDROCK.getDefaultState()) {
                    return;
                }
            }
        }
        if (args[1].equals("up")) {
            for (int i = 10; i < 255; ++i) {
                final WorldClient world3 = VClipCommand.mc.world;
                final Minecraft mc3 = VClipCommand.mc;
                if (world3.getBlockState(new BlockPos(Minecraft.player).add(0, i, 0)) == Blocks.AIR.getDefaultState()) {
                    y = (float)(i + 1);
                    break;
                }
            }
        }
        if (y == 0.0f) {
            if (!NumberUtils.isNumber(args[1])) {
                this.sendMessage("Invalid input");
                return;
            }
            y = Float.parseFloat(args[1]);
        }
        for (int i = 0; i < Math.max(y / 1000.0f, 3.0f); ++i) {
            final Minecraft mc4 = VClipCommand.mc;
            final NetHandlerPlayClient connection = Minecraft.player.connection;
            final Minecraft mc5 = VClipCommand.mc;
            connection.sendPacket(new CPacketPlayer(Minecraft.player.onGround));
        }
        final Minecraft mc6 = VClipCommand.mc;
        final NetHandlerPlayClient connection2 = Minecraft.player.connection;
        final Minecraft mc7 = VClipCommand.mc;
        final double posX = Minecraft.player.posX;
        final Minecraft mc8 = VClipCommand.mc;
        final double yIn = Minecraft.player.posY + y;
        final Minecraft mc9 = VClipCommand.mc;
        connection2.sendPacket(new CPacketPlayer.Position(posX, yIn, Minecraft.player.posZ, false));
        final Minecraft mc10 = VClipCommand.mc;
        final EntityPlayerSP player = Minecraft.player;
        final Minecraft mc11 = VClipCommand.mc;
        final double posX2 = Minecraft.player.posX;
        final Minecraft mc12 = VClipCommand.mc;
        final double y2 = Minecraft.player.posY + y;
        final Minecraft mc13 = VClipCommand.mc;
        player.setPosition(posX2, y2, Minecraft.player.posZ);
    }
    
    @Override
    public void error() {
        this.sendMessage(ChatFormatting.GRAY + "Command use" + ChatFormatting.WHITE + ":");
        this.sendMessage(ChatFormatting.WHITE + ".vclip " + ChatFormatting.BLUE + "<value>");
    }
}
