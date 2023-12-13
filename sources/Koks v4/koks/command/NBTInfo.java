package koks.command;

import koks.api.registry.command.Command;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author kroko
 * @created on 31.01.2021 : 21:39
 */
@Command.Info(name = "nbtinfo", aliases = {"nbt","nbti"}, description = "Its copy the NBT String from a Item")
public class NBTInfo extends Command {
    @Override
    public boolean execute(String[] args) {
        if(getPlayer().getHeldItem() != null) {
            final ItemStack held = getPlayer().getHeldItem();
            final NBTTagCompound compound = held.getTagCompound();
            if(compound != null) {
                sendMessage("Copied the NBTTagCompound from the Item!");
                sendMessage("§cRaw: §7" + held.getTagCompound().toString());
                GuiScreen.setClipboardString(held.getTagCompound().toString());
            }else{
                sendMessage("§cEmpty NBTTagCompound!");
            }
        } else {
            sendError("NullPointer", "Please hold a item in your hand!");
        }
        return true;
    }
}
