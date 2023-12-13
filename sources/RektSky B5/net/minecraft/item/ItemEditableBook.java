/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
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
        if (!ItemWritableBook.isNBTValid(nbt)) {
            return false;
        }
        if (!nbt.hasKey("title", 8)) {
            return false;
        }
        String s2 = nbt.getString("title");
        return s2 != null && s2.length() <= 32 ? nbt.hasKey("author", 8) : false;
    }

    public static int getGeneration(ItemStack book) {
        return book.getTagCompound().getInteger("generation");
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        NBTTagCompound nbttagcompound;
        String s2;
        if (stack.hasTagCompound() && !StringUtils.isNullOrEmpty(s2 = (nbttagcompound = stack.getTagCompound()).getString("title"))) {
            return s2;
        }
        return super.getItemStackDisplayName(stack);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (stack.hasTagCompound()) {
            NBTTagCompound nbttagcompound = stack.getTagCompound();
            String s2 = nbttagcompound.getString("author");
            if (!StringUtils.isNullOrEmpty(s2)) {
                tooltip.add((Object)((Object)EnumChatFormatting.GRAY) + StatCollector.translateToLocalFormatted("book.byAuthor", s2));
            }
            tooltip.add((Object)((Object)EnumChatFormatting.GRAY) + StatCollector.translateToLocal("book.generation." + nbttagcompound.getInteger("generation")));
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (!worldIn.isRemote) {
            this.resolveContents(itemStackIn, playerIn);
        }
        playerIn.displayGUIBook(itemStackIn);
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStackIn;
    }

    private void resolveContents(ItemStack stack, EntityPlayer player) {
        NBTTagCompound nbttagcompound;
        if (stack != null && stack.getTagCompound() != null && !(nbttagcompound = stack.getTagCompound()).getBoolean("resolved")) {
            nbttagcompound.setBoolean("resolved", true);
            if (ItemEditableBook.validBookTagContents(nbttagcompound)) {
                NBTTagList nbttaglist = nbttagcompound.getTagList("pages", 8);
                for (int i2 = 0; i2 < nbttaglist.tagCount(); ++i2) {
                    IChatComponent lvt_7_1_;
                    String s2 = nbttaglist.getStringTagAt(i2);
                    try {
                        lvt_7_1_ = IChatComponent.Serializer.jsonToComponent(s2);
                        lvt_7_1_ = ChatComponentProcessor.processComponent(player, lvt_7_1_, player);
                    }
                    catch (Exception var9) {
                        lvt_7_1_ = new ChatComponentText(s2);
                    }
                    nbttaglist.set(i2, new NBTTagString(IChatComponent.Serializer.componentToJson(lvt_7_1_)));
                }
                nbttagcompound.setTag("pages", nbttaglist);
                if (player instanceof EntityPlayerMP && player.getCurrentEquippedItem() == stack) {
                    Slot slot = player.openContainer.getSlotFromInventory(player.inventory, player.inventory.currentItem);
                    ((EntityPlayerMP)player).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(0, slot.slotNumber, stack));
                }
            }
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}

