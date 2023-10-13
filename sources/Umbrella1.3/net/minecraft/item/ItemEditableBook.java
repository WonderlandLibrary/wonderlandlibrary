/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00000077";

    public ItemEditableBook() {
        this.setMaxStackSize(1);
    }

    public static boolean validBookTagContents(NBTTagCompound p_77828_0_) {
        if (!ItemWritableBook.validBookPageTagContents(p_77828_0_)) {
            return false;
        }
        if (!p_77828_0_.hasKey("title", 8)) {
            return false;
        }
        String var1 = p_77828_0_.getString("title");
        return var1 != null && var1.length() <= 32 ? p_77828_0_.hasKey("author", 8) : false;
    }

    public static int func_179230_h(ItemStack p_179230_0_) {
        return p_179230_0_.getTagCompound().getInteger("generation");
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        NBTTagCompound var2;
        String var3;
        if (stack.hasTagCompound() && !StringUtils.isNullOrEmpty(var3 = (var2 = stack.getTagCompound()).getString("title"))) {
            return var3;
        }
        return super.getItemStackDisplayName(stack);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
        if (stack.hasTagCompound()) {
            NBTTagCompound var5 = stack.getTagCompound();
            String var6 = var5.getString("author");
            if (!StringUtils.isNullOrEmpty(var6)) {
                tooltip.add((Object)((Object)EnumChatFormatting.GRAY) + StatCollector.translateToLocalFormatted("book.byAuthor", var6));
            }
            tooltip.add((Object)((Object)EnumChatFormatting.GRAY) + StatCollector.translateToLocal("book.generation." + var5.getInteger("generation")));
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (!worldIn.isRemote) {
            this.func_179229_a(itemStackIn, playerIn);
        }
        playerIn.displayGUIBook(itemStackIn);
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStackIn;
    }

    private void func_179229_a(ItemStack p_179229_1_, EntityPlayer p_179229_2_) {
        NBTTagCompound var3;
        if (p_179229_1_ != null && p_179229_1_.getTagCompound() != null && !(var3 = p_179229_1_.getTagCompound()).getBoolean("resolved")) {
            var3.setBoolean("resolved", true);
            if (ItemEditableBook.validBookTagContents(var3)) {
                NBTTagList var4 = var3.getTagList("pages", 8);
                for (int var5 = 0; var5 < var4.tagCount(); ++var5) {
                    IChatComponent var7;
                    String var6 = var4.getStringTagAt(var5);
                    try {
                        IChatComponent var11 = IChatComponent.Serializer.jsonToComponent(var6);
                        var7 = ChatComponentProcessor.func_179985_a(p_179229_2_, var11, p_179229_2_);
                    }
                    catch (Exception var9) {
                        var7 = new ChatComponentText(var6);
                    }
                    var4.set(var5, new NBTTagString(IChatComponent.Serializer.componentToJson(var7)));
                }
                var3.setTag("pages", var4);
                if (p_179229_2_ instanceof EntityPlayerMP && p_179229_2_.getCurrentEquippedItem() == p_179229_1_) {
                    Slot var10 = p_179229_2_.openContainer.getSlotFromInventory(p_179229_2_.inventory, p_179229_2_.inventory.currentItem);
                    ((EntityPlayerMP)p_179229_2_).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(0, var10.slotNumber, p_179229_1_));
                }
            }
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}

