package net.minecraft.item;

import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class ItemEditableBook
extends Item {
    public ItemEditableBook() {
        this.setMaxStackSize(1);
    }

    public static boolean validBookTagContents(NBTTagCompound nbt) {
        if (!ItemWritableBook.isNBTValid((NBTTagCompound)nbt)) {
            return false;
        }
        if (!nbt.hasKey("title", 8)) {
            return false;
        }
        String s = nbt.getString("title");
        return s != null && s.length() <= 32 ? nbt.hasKey("author", 8) : false;
    }

    public static int getGeneration(ItemStack book) {
        return book.getTagCompound().getInteger("generation");
    }

    public String getItemStackDisplayName(ItemStack stack) {
        NBTTagCompound nbttagcompound;
        String s;
        if (stack.hasTagCompound() && !StringUtils.isNullOrEmpty((String)(s = (nbttagcompound = stack.getTagCompound()).getString("title")))) {
            return s;
        }
        return super.getItemStackDisplayName(stack);
    }

    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbttagcompound = stack.getTagCompound();
            String s = nbttagcompound.getString("author");
            if (!StringUtils.isNullOrEmpty((String)s)) {
                tooltip.add((Object)(EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted((String)"book.byAuthor", (Object[])new Object[]{s})));
            }
            tooltip.add((Object)(EnumChatFormatting.GRAY + StatCollector.translateToLocal((String)("book.generation." + nbttagcompound.getInteger("generation")))));
        }
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (!worldIn.isRemote) {
            this.resolveContents(itemStackIn, playerIn);
        }
        playerIn.displayGUIBook(itemStackIn);
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem((Item)this)]);
        return itemStackIn;
    }

    private void resolveContents(ItemStack stack, EntityPlayer player) {
        NBTTagCompound nbttagcompound;
        if (stack != null && stack.getTagCompound() != null && !(nbttagcompound = stack.getTagCompound()).getBoolean("resolved")) {
            nbttagcompound.setBoolean("resolved", true);
            if (ItemEditableBook.validBookTagContents(nbttagcompound)) {
                NBTTagList nbttaglist = nbttagcompound.getTagList("pages", 8);
                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    IChatComponent ichatcomponent;
                    String s = nbttaglist.getStringTagAt(i);
                    try {
                        ichatcomponent = IChatComponent.Serializer.jsonToComponent((String)s);
                        ichatcomponent = ChatComponentProcessor.processComponent((ICommandSender)player, (IChatComponent)ichatcomponent, (Entity)player);
                    }
                    catch (Exception var9) {
                        ichatcomponent = new ChatComponentText(s);
                    }
                    nbttaglist.set(i, (NBTBase)new NBTTagString(IChatComponent.Serializer.componentToJson((IChatComponent)ichatcomponent)));
                }
                nbttagcompound.setTag("pages", (NBTBase)nbttaglist);
                if (player instanceof EntityPlayerMP && player.getCurrentEquippedItem() == stack) {
                    Slot slot = player.openContainer.getSlotFromInventory((IInventory)player.inventory, player.inventory.currentItem);
                    ((EntityPlayerMP)player).playerNetServerHandler.sendPacket((Packet)new S2FPacketSetSlot(0, slot.slotNumber, stack));
                }
            }
        }
    }

    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}
