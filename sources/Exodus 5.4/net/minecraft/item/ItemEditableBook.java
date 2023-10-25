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
    public static int getGeneration(ItemStack itemStack) {
        return itemStack.getTagCompound().getInteger("generation");
    }

    public static boolean validBookTagContents(NBTTagCompound nBTTagCompound) {
        if (!ItemWritableBook.isNBTValid(nBTTagCompound)) {
            return false;
        }
        if (!nBTTagCompound.hasKey("title", 8)) {
            return false;
        }
        String string = nBTTagCompound.getString("title");
        return string != null && string.length() <= 32 ? nBTTagCompound.hasKey("author", 8) : false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if (!world.isRemote) {
            this.resolveContents(itemStack, entityPlayer);
        }
        entityPlayer.displayGUIBook(itemStack);
        entityPlayer.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStack;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean bl) {
        if (itemStack.hasTagCompound()) {
            NBTTagCompound nBTTagCompound = itemStack.getTagCompound();
            String string = nBTTagCompound.getString("author");
            if (!StringUtils.isNullOrEmpty(string)) {
                list.add((Object)((Object)EnumChatFormatting.GRAY) + StatCollector.translateToLocalFormatted("book.byAuthor", string));
            }
            list.add((Object)((Object)EnumChatFormatting.GRAY) + StatCollector.translateToLocal("book.generation." + nBTTagCompound.getInteger("generation")));
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        NBTTagCompound nBTTagCompound;
        String string;
        if (itemStack.hasTagCompound() && !StringUtils.isNullOrEmpty(string = (nBTTagCompound = itemStack.getTagCompound()).getString("title"))) {
            return string;
        }
        return super.getItemStackDisplayName(itemStack);
    }

    private void resolveContents(ItemStack itemStack, EntityPlayer entityPlayer) {
        NBTTagCompound nBTTagCompound;
        if (itemStack != null && itemStack.getTagCompound() != null && !(nBTTagCompound = itemStack.getTagCompound()).getBoolean("resolved")) {
            nBTTagCompound.setBoolean("resolved", true);
            if (ItemEditableBook.validBookTagContents(nBTTagCompound)) {
                NBTTagList nBTTagList = nBTTagCompound.getTagList("pages", 8);
                int n = 0;
                while (n < nBTTagList.tagCount()) {
                    IChatComponent iChatComponent;
                    String string = nBTTagList.getStringTagAt(n);
                    try {
                        iChatComponent = IChatComponent.Serializer.jsonToComponent(string);
                        iChatComponent = ChatComponentProcessor.processComponent(entityPlayer, iChatComponent, entityPlayer);
                    }
                    catch (Exception exception) {
                        iChatComponent = new ChatComponentText(string);
                    }
                    nBTTagList.set(n, new NBTTagString(IChatComponent.Serializer.componentToJson(iChatComponent)));
                    ++n;
                }
                nBTTagCompound.setTag("pages", nBTTagList);
                if (entityPlayer instanceof EntityPlayerMP && entityPlayer.getCurrentEquippedItem() == itemStack) {
                    Slot slot = entityPlayer.openContainer.getSlotFromInventory(entityPlayer.inventory, entityPlayer.inventory.currentItem);
                    ((EntityPlayerMP)entityPlayer).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(0, slot.slotNumber, itemStack));
                }
            }
        }
    }

    public ItemEditableBook() {
        this.setMaxStackSize(1);
    }

    @Override
    public boolean hasEffect(ItemStack itemStack) {
        return true;
    }
}

