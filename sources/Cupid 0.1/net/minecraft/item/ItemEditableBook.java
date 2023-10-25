package net.minecraft.item;

import java.util.List;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
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

public class ItemEditableBook extends Item {
  public ItemEditableBook() {
    setMaxStackSize(1);
  }
  
  public static boolean validBookTagContents(NBTTagCompound nbt) {
    if (!ItemWritableBook.isNBTValid(nbt))
      return false; 
    if (!nbt.hasKey("title", 8))
      return false; 
    String s = nbt.getString("title");
    return (s != null && s.length() <= 32) ? nbt.hasKey("author", 8) : false;
  }
  
  public static int getGeneration(ItemStack book) {
    return book.getTagCompound().getInteger("generation");
  }
  
  public String getItemStackDisplayName(ItemStack stack) {
    if (stack.hasTagCompound()) {
      NBTTagCompound nbttagcompound = stack.getTagCompound();
      String s = nbttagcompound.getString("title");
      if (!StringUtils.isNullOrEmpty(s))
        return s; 
    } 
    return super.getItemStackDisplayName(stack);
  }
  
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    if (stack.hasTagCompound()) {
      NBTTagCompound nbttagcompound = stack.getTagCompound();
      String s = nbttagcompound.getString("author");
      if (!StringUtils.isNullOrEmpty(s))
        tooltip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted("book.byAuthor", new Object[] { s })); 
      tooltip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("book.generation." + nbttagcompound.getInteger("generation")));
    } 
  }
  
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    if (!worldIn.isRemote)
      resolveContents(itemStackIn, playerIn); 
    playerIn.displayGUIBook(itemStackIn);
    playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
    return itemStackIn;
  }
  
  private void resolveContents(ItemStack stack, EntityPlayer player) {
    if (stack != null && stack.getTagCompound() != null) {
      NBTTagCompound nbttagcompound = stack.getTagCompound();
      if (!nbttagcompound.getBoolean("resolved")) {
        nbttagcompound.setBoolean("resolved", true);
        if (validBookTagContents(nbttagcompound)) {
          NBTTagList nbttaglist = nbttagcompound.getTagList("pages", 8);
          for (int i = 0; i < nbttaglist.tagCount(); i++) {
            ChatComponentText chatComponentText;
            String s = nbttaglist.getStringTagAt(i);
            try {
              IChatComponent lvt_7_1_ = IChatComponent.Serializer.jsonToComponent(s);
              lvt_7_1_ = ChatComponentProcessor.processComponent((ICommandSender)player, lvt_7_1_, (Entity)player);
            } catch (Exception var9) {
              chatComponentText = new ChatComponentText(s);
            } 
            nbttaglist.set(i, (NBTBase)new NBTTagString(IChatComponent.Serializer.componentToJson((IChatComponent)chatComponentText)));
          } 
          nbttagcompound.setTag("pages", (NBTBase)nbttaglist);
          if (player instanceof EntityPlayerMP && player.getCurrentEquippedItem() == stack) {
            Slot slot = player.openContainer.getSlotFromInventory((IInventory)player.inventory, player.inventory.currentItem);
            ((EntityPlayerMP)player).playerNetServerHandler.sendPacket((Packet)new S2FPacketSetSlot(0, slot.slotNumber, stack));
          } 
        } 
      } 
    } 
  }
  
  public boolean hasEffect(ItemStack stack) {
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemEditableBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */