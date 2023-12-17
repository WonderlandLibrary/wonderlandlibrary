// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.inventory.Slot;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.entity.Entity;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.EnumActionResult;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.util.ITooltipFlag;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.util.StringUtils;
import net.minecraft.nbt.NBTTagCompound;

public class ItemWrittenBook extends Item
{
    public ItemWrittenBook() {
        this.setMaxStackSize(1);
    }
    
    public static boolean validBookTagContents(final NBTTagCompound nbt) {
        if (!ItemWritableBook.isNBTValid(nbt)) {
            return false;
        }
        if (!nbt.hasKey("title", 8)) {
            return false;
        }
        final String s = nbt.getString("title");
        return s != null && s.length() <= 32 && nbt.hasKey("author", 8);
    }
    
    public static int getGeneration(final ItemStack book) {
        return book.getTagCompound().getInteger("generation");
    }
    
    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        if (stack.hasTagCompound()) {
            final NBTTagCompound nbttagcompound = stack.getTagCompound();
            final String s = nbttagcompound.getString("title");
            if (!StringUtils.isNullOrEmpty(s)) {
                return s;
            }
        }
        return super.getItemStackDisplayName(stack);
    }
    
    @Override
    public void addInformation(final ItemStack stack, @Nullable final World worldIn, final List<String> tooltip, final ITooltipFlag flagIn) {
        if (stack.hasTagCompound()) {
            final NBTTagCompound nbttagcompound = stack.getTagCompound();
            final String s = nbttagcompound.getString("author");
            if (!StringUtils.isNullOrEmpty(s)) {
                tooltip.add(TextFormatting.GRAY + I18n.translateToLocalFormatted("book.byAuthor", s));
            }
            tooltip.add(TextFormatting.GRAY + I18n.translateToLocal("book.generation." + nbttagcompound.getInteger("generation")));
        }
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
        final ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote) {
            this.resolveContents(itemstack, playerIn);
        }
        playerIn.openBook(itemstack, handIn);
        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
    
    private void resolveContents(final ItemStack stack, final EntityPlayer player) {
        if (stack.getTagCompound() != null) {
            final NBTTagCompound nbttagcompound = stack.getTagCompound();
            if (!nbttagcompound.getBoolean("resolved")) {
                nbttagcompound.setBoolean("resolved", true);
                if (validBookTagContents(nbttagcompound)) {
                    final NBTTagList nbttaglist = nbttagcompound.getTagList("pages", 8);
                    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                        final String s = nbttaglist.getStringTagAt(i);
                        ITextComponent itextcomponent;
                        try {
                            itextcomponent = ITextComponent.Serializer.fromJsonLenient(s);
                            itextcomponent = TextComponentUtils.processComponent(player, itextcomponent, player);
                        }
                        catch (Exception var9) {
                            itextcomponent = new TextComponentString(s);
                        }
                        nbttaglist.set(i, new NBTTagString(ITextComponent.Serializer.componentToJson(itextcomponent)));
                    }
                    nbttagcompound.setTag("pages", nbttaglist);
                    if (player instanceof EntityPlayerMP && player.getHeldItemMainhand() == stack) {
                        final Slot slot = player.openContainer.getSlotFromInventory(player.inventory, player.inventory.currentItem);
                        ((EntityPlayerMP)player).connection.sendPacket(new SPacketSetSlot(0, slot.slotNumber, stack));
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
