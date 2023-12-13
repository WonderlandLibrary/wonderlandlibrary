package slavikcodd3r.rainbow.command.commands;

import net.minecraft.client.Minecraft;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Com(names = { "vc", "vclip" })
public class VClip extends Command
{
    @Override
    public void runCommand(final String[] args) {
        final double posMod = Double.parseDouble(args[1]);
        final Minecraft mc = Minecraft.getMinecraft();
        ClientUtils.player().setPosition(ClientUtils.player().posX, ClientUtils.player().posY + posMod, ClientUtils.player().posZ);
        ClientUtils.sendMessage("Teleported " + posMod + " blocks");
    }
    
    @Override
    public String getHelp() {
        return "vclip";
    }
}
