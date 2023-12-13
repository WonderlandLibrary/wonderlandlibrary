package slavikcodd3r.rainbow.command.commands;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import slavikcodd3r.rainbow.command.Com;
import slavikcodd3r.rainbow.command.Command;
import slavikcodd3r.rainbow.utils.ClientUtils;

@Com(names = { "killpotion", "kp" })
public class KillPotion extends Command
{
	
	Minecraft mc = Minecraft.getMinecraft();
	
    public void runCommand(final String[] args) {
    	if (mc.playerController.isNotCreative()) {
    		ClientUtils.sendMessage("You need to be in creative mode to do this!");
    	}
    	final ItemStack stack = new ItemStack(Items.potionitem);
        stack.setItemDamage(999999990);
        final NBTTagList effects = new NBTTagList();
        final NBTTagCompound effect = new NBTTagCompound();
        effect.setInteger("Unbreakable", 1);
        effect.setInteger("HideFlags", 53);
        effect.setInteger("Amplifier", 125);
        effect.setInteger("Duration", Integer.MAX_VALUE);
        effect.setInteger("Id", 6);
        effects.appendTag(effect);
        stack.setTagInfo("CustomPotionEffects", effects);
    	stack.setStackDisplayName("§f§l§ki §c§lR§6§la§e§li§a§ln§b§lb§1§lo§d§lw §7§l2.0 §f§l§ki");
        mc.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, stack));
    }
    
    @Override
    public String getHelp() {
        return "killpotion";
    }
}
