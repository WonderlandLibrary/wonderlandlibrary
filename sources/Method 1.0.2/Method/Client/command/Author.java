package Method.Client.command;

import Method.Client.utils.visual.ChatUtils;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;

@Command.CommandFeatures(description = "Change BookSign Author Creative Only", Syntax = "Author <author> ")
public class Author extends Command {

    @Override
    public void runCommand(String s, String[] args) {
        if (!mc.player.capabilities.isCreativeMode)
            ChatUtils.error("Creative mode only.");

        ItemStack heldItem = mc.player.inventory.getCurrentItem();
        int heldItemID = Item.getIdFromItem(heldItem.getItem());
        int writtenBookID = Item.getIdFromItem(Items.WRITTEN_BOOK);

        if (heldItemID != writtenBookID)
            ChatUtils.error("You must hold a written book in your main hand.");
        else {
            heldItem.setTagInfo("author", new NBTTagString(args[0]));
            ChatUtils.message("Author Changed! Open Inventory.");
        }
    }
}