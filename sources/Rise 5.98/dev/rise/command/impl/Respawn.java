package dev.rise.command.impl;

import dev.rise.Rise;
import dev.rise.command.Command;
import dev.rise.command.api.CommandInfo;
import dev.rise.util.player.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;

@CommandInfo(name = "Respawn", description = "Respawns on Bedwars to allow for teleportation exploit", syntax = ".respawn", aliases = {"respawn", "rs", "void"})
public final class Respawn extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        final int steps = (int) Math.ceil(mc.thePlayer.posY / 9);
        final double distance = mc.thePlayer.posY / steps;

        for (int i = 1; i <= steps; i++) {
            PacketUtil.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - (distance * i), mc.thePlayer.posZ, false));
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - (distance * i), mc.thePlayer.posZ);
        }
    }
}
