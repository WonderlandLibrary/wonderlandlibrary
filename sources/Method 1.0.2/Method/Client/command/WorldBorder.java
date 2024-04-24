package Method.Client.command;

import Method.Client.utils.visual.ChatUtils;

@Command.CommandFeatures(description = "WorldBorder distance", Syntax = "WorldBorder")

public class WorldBorder extends Command {


    @Override
    public void runCommand(String s, String[] args) {
        final net.minecraft.world.border.WorldBorder worldBorder = this.mc.world.getWorldBorder();
        ChatUtils.message("World border is at:\nMinX: " + worldBorder.minX() + "\nMinZ: " + worldBorder.minZ() + "\nMaxX: " + worldBorder.maxX() + "\nMaxZ: " + worldBorder.maxZ() + "\n");

    }


}