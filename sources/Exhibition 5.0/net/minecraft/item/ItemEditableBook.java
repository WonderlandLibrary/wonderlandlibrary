// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatComponentText;
import net.minecraft.entity.Entity;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentProcessor;
import net.minecraft.util.IChatComponent;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraft.util.StatCollector;
import net.minecraft.util.EnumChatFormatting;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import net.minecraft.nbt.NBTTagCompound;

public class ItemEditableBook extends Item
{
    private static final String __OBFID = "CL_00000077";
    
    public ItemEditableBook() {
        this.setMaxStackSize(1);
    }
    
    public static boolean validBookTagContents(final NBTTagCompound p_77828_0_) {
        if (!ItemWritableBook.validBookPageTagContents(p_77828_0_)) {
            return false;
        }
        if (!p_77828_0_.hasKey("title", 8)) {
            return false;
        }
        final String var1 = p_77828_0_.getString("title");
        return var1 != null && var1.length() <= 32 && p_77828_0_.hasKey("author", 8);
    }
    
    public static int func_179230_h(final ItemStack p_179230_0_) {
        return p_179230_0_.getTagCompound().getInteger("generation");
    }
    
    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        if (stack.hasTagCompound()) {
            final NBTTagCompound var2 = stack.getTagCompound();
            final String var3 = var2.getString("title");
            if (!StringUtils.isNullOrEmpty(var3)) {
                return var3;
            }
        }
        return super.getItemStackDisplayName(stack);
    }
    
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List tooltip, final boolean advanced) {
        if (stack.hasTagCompound()) {
            final NBTTagCompound var5 = stack.getTagCompound();
            final String var6 = var5.getString("author");
            if (!StringUtils.isNullOrEmpty(var6)) {
                tooltip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted("book.byAuthor", var6));
            }
            tooltip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("book.generation." + var5.getInteger("generation")));
        }
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        if (!worldIn.isRemote) {
            this.func_179229_a(itemStackIn, playerIn);
        }
        playerIn.displayGUIBook(itemStackIn);
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return itemStackIn;
    }
    
    private void func_179229_a(final ItemStack p_179229_1_, final EntityPlayer p_179229_2_) {
        if (p_179229_1_ != null && p_179229_1_.getTagCompound() != null) {
            final NBTTagCompound var3 = p_179229_1_.getTagCompound();
            if (!var3.getBoolean("resolved")) {
                var3.setBoolean("resolved", true);
                if (validBookTagContents(var3)) {
                    final NBTTagList var4 = var3.getTagList("pages", 8);
                    for (int var5 = 0; var5 < var4.tagCount(); ++var5) {
                        final String var6 = var4.getStringTagAt(var5);
                        Object var8;
                        try {
                            final IChatComponent var7 = IChatComponent.Serializer.jsonToComponent(var6);
                            var8 = ChatComponentProcessor.func_179985_a(p_179229_2_, var7, p_179229_2_);
                        }
                        catch (Exception var10) {
                            var8 = new ChatComponentText(var6);
                        }
                        var4.set(var5, new NBTTagString(IChatComponent.Serializer.componentToJson((IChatComponent)var8)));
                    }
                    var3.setTag("pages", var4);
                    if (p_179229_2_ instanceof EntityPlayerMP && p_179229_2_.getCurrentEquippedItem() == p_179229_1_) {
                        final Slot var9 = p_179229_2_.openContainer.getSlotFromInventory(p_179229_2_.inventory, p_179229_2_.inventory.currentItem);
                        ((EntityPlayerMP)p_179229_2_).playerNetServerHandler.sendPacket(new S2FPacketSetSlot(0, var9.slotNumber, p_179229_1_));
                    }
                }
            }
        }
    }
    
    @Override
    public boolean hasEffect(final ItemStack stack) {
        return true;
    }
}
