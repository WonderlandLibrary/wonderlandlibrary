package slavikcodd3r.rainbow.command.commands;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;

import com.jcraft.jorbis.Block;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

@Com(names = { "cmdblock" })
public class CMDBlock extends Command
{
	
	Minecraft mc = Minecraft.getMinecraft();
    
    @Override
    public String getHelp() {
        return "cmdblock";
    }
    
    @Override
    public void runCommand(final String[] args) {
    	if (mc.playerController.isNotCreative()) {
    		ClientUtils.sendMessage("You need to be in creative mode to do this!");
    	}
    	final ItemStack itm = new ItemStack(Items.command_block_minecart);
    	itm.setStackDisplayName("§f§l§ki §c§lR§6§la§e§li§a§ln§b§lb§1§lo§d§lw §7§l2.0 §f§l§ki");
        mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, itm));
    }
}
