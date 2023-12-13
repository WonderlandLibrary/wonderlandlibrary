package slavikcodd3r.rainbow.command.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Com(names = { "hc", "hclip" })
public class HClip extends Command
{
    @Override
    public void runCommand(final String[] args) {
        final double posMod = Double.parseDouble(args[1]);
        final Minecraft mc = Minecraft.getMinecraft();
        if (ClientUtils.player().getHorizontalFacing() == EnumFacing.SOUTH) {
            ClientUtils.player().setPosition(ClientUtils.player().posX, ClientUtils.player().posY, ClientUtils.player().posZ + posMod);
        }
        if (ClientUtils.player().getHorizontalFacing() == EnumFacing.WEST) {
            ClientUtils.player().setPosition(ClientUtils.player().posX + -posMod, ClientUtils.player().posY, ClientUtils.player().posZ);
        }
        if (ClientUtils.player().getHorizontalFacing() == EnumFacing.EAST) {
            ClientUtils.player().setPosition(ClientUtils.player().posX + posMod, ClientUtils.player().posY, ClientUtils.player().posZ);
        }
        if (ClientUtils.player().getHorizontalFacing() == EnumFacing.NORTH) {
            ClientUtils.player().setPosition(ClientUtils.player().posX, ClientUtils.player().posY, ClientUtils.player().posZ + -posMod);
        }
        ClientUtils.sendMessage("Teleported " + posMod + " blocks horizontally.");
    }
    
    @Override
    public String getHelp() {
        return "hclip";
    }
}
