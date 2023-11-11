package me.nyan.flush.command.impl;

import me.nyan.flush.command.Command;
import me.nyan.flush.utils.movement.MovementUtils;
import me.nyan.flush.utils.other.ChatUtils;
import net.minecraft.entity.player.EntityPlayer;

import java.util.concurrent.atomic.AtomicReference;

public class Find extends Command {
    public Find() {
        super("Find", "Gets a player's information like coordinates etc...", "Find <PlayerName> | Find me");
    }

    @Override
    public void onCommand(String[] args, String message) {
        if (args.length == 0) {
            sendSyntaxHelpMessage();
            return;
        }

        AtomicReference<EntityPlayer> player = new AtomicReference<>();
        if (args[0].equalsIgnoreCase("me")) {
            player.set(mc.thePlayer);
        } else {
            mc.getNetHandler().playerInfoMap.values().forEach(networkPlayerInfo -> {
                if (networkPlayerInfo.getGameProfile().getName().equalsIgnoreCase(args[0])) {
                    player.set(mc.theWorld.getPlayerEntityByUUID(networkPlayerInfo.getGameProfile().getId()));
                }
            });
        }

        if (player.get() != null) {
            ChatUtils.println("§9Information about " + (player.get().getName().equals(mc.thePlayer.getName()) ? "yourself"
                    : player.get().getName()) + ".");
            ChatUtils.println("Health: " + Math.round(player.get().getHealth() * 100F) / 100F + ".");
            ChatUtils.println("Coordinates: X: " + Math.round(player.get().posX) + " Y: " + Math.round(player.get().posY) +
                    " Z: " + Math.round(player.get().posZ) + ".");
            ChatUtils.println("Distance from you: " + Math.round(player.get().getDistanceToEntity(mc.thePlayer)) + " blocks.");
            ChatUtils.println("Ping: " + mc.getNetHandler().getPlayerInfo(player.get().getName()).getResponseTime() + " ms.");
            ChatUtils.println("Gamemode: " + mc.getNetHandler().getPlayerInfo(player.get().getName()).getGameType().getName() + ".");
            ChatUtils.println("Ticks existed: " + player.get().ticksExisted + ".");
            ChatUtils.println("Blocks per second: " + (Math.round(MovementUtils.getSpeed(player.get()) * 100F) / 100F) * 20 + ".");
            ChatUtils.println("Invisible: " + player.get().isInvisible() + ".");
            ChatUtils.println("Flying: " + player.get().capabilities.isFlying + ".");
            ChatUtils.println("UUID: " + player.get().getUniqueID().toString() + ".");

            return;
        }

        ChatUtils.println("§cFailed to find a player named \"" + args[0] + "\".");
    }
}