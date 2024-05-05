package slavikcodd3r.rainbow.command.commands;

import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

@Com(names = { "head", "skull" })
public class Head extends Command
{
	
	Minecraft mc = Minecraft.getMinecraft();
	
    @Override
    public String getHelp() {
        return "head";
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
        final ItemStack item = new ItemStack(Items.skull, 1, 3);
        final NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("SkullOwner", player);
        item.stackTagCompound = nbt;
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, item));
    }
}
