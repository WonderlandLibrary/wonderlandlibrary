package summer.base.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import summer.base.manager.Command;
import summer.base.utilities.ChatUtils;

public class HClipCommand implements Command {

    @Override
    public boolean run(String[] args) {
        if (args.length == 2) {
            double posMod = Double.parseDouble(args[1]);
            if (Minecraft.thePlayer.getHorizontalFacing() == EnumFacing.SOUTH) {
                Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ + posMod);
            }
            if (Minecraft.thePlayer.getHorizontalFacing() == EnumFacing.WEST) {
                Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX + -posMod, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
            }
            if (Minecraft.thePlayer.getHorizontalFacing() == EnumFacing.EAST) {
                Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX + posMod, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ);
            }
            if (Minecraft.thePlayer.getHorizontalFacing() == EnumFacing.NORTH) {
                Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ + -posMod);
            }
            ChatUtils.sendMessage("Teleported " + posMod + " blocks horizontally.");
            return true;
        }
        return false;
    }

    @Override
    public String usage() {
        return ChatFormatting.WHITE + "hc | hclip <value>";
    }
}