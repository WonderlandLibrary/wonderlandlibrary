package slavikcodd3r.rainbow.command.commands;

import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;
import net.minecraft.client.Minecraft;

@Com(names = { "hat" })
public class Hat extends Command
{
	Minecraft mc = Minecraft.getMinecraft();
	
    @Override
    public String getHelp() {
        return "hat";
    }
    
    @Override
    public void runCommand(final String[] args) {
    	if (mc.playerController.isNotCreative()) {
    		ClientUtils.sendMessage("You need to be in creative mode to do this!");
    	}
        String player = "";
        if (args.length > 1) {
            player = args[1];
        }
        final ItemStack item = ClientUtils.player().getCurrentEquippedItem();
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(5, item));
    }
}
