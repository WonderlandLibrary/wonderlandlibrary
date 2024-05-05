package slavikcodd3r.rainbow.command.commands;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;
import slavikcodd3r.rainbow.utils.NetworkUtils;

@Com(names = { "nbtitem", "nbt", "ni" })
public class NBTItem extends Command
{
    private final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    private StringSelection nbt;
	Minecraft mc = Minecraft.getMinecraft();
	
    @Override
    public void runCommand(final String[] args) {
    	if (args[1].isEmpty()) {
            return;
        }
        ItemStack item = mc.thePlayer.inventory.getCurrentItem();

        if (args[1].equalsIgnoreCase("get")) {
            if (item.getTagCompound() != null) {
            }
        } else if (args[1].equalsIgnoreCase("copy")) {
            if (item.getTagCompound() != null) {
                nbt = new StringSelection(item.getTagCompound() + "");
                clipboard.setContents(nbt, nbt);
        } else if (args[1].equalsIgnoreCase("wipe")) {
            item.setTagCompound(new NBTTagCompound());
        }
    }
    }
    
    @Override
    public String getHelp() {
        return "nbtitem";
    }
}
