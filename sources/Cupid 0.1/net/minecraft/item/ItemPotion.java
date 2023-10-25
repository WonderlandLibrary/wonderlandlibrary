package net.minecraft.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemPotion extends Item {
  private Map<Integer, List<PotionEffect>> effectCache = Maps.newHashMap();
  
  private static final Map<List<PotionEffect>, Integer> SUB_ITEMS_CACHE = Maps.newLinkedHashMap();
  
  public ItemPotion() {
    setMaxStackSize(1);
    setHasSubtypes(true);
    setMaxDamage(0);
    setCreativeTab(CreativeTabs.tabBrewing);
  }
  
  public List<PotionEffect> getEffects(ItemStack stack) {
    if (stack.hasTagCompound() && stack.getTagCompound().hasKey("CustomPotionEffects", 9)) {
      List<PotionEffect> list1 = Lists.newArrayList();
      NBTTagList nbttaglist = stack.getTagCompound().getTagList("CustomPotionEffects", 10);
      for (int i = 0; i < nbttaglist.tagCount(); i++) {
        NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
        PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);
        if (potioneffect != null)
          list1.add(potioneffect); 
      } 
      return list1;
    } 
    List<PotionEffect> list = this.effectCache.get(Integer.valueOf(stack.getMetadata()));
    if (list == null) {
      list = PotionHelper.getPotionEffects(stack.getMetadata(), false);
      this.effectCache.put(Integer.valueOf(stack.getMetadata()), list);
    } 
    return list;
  }
  
  public List<PotionEffect> getEffects(int meta) {
    List<PotionEffect> list = this.effectCache.get(Integer.valueOf(meta));
    if (list == null) {
      list = PotionHelper.getPotionEffects(meta, false);
      this.effectCache.put(Integer.valueOf(meta), list);
    } 
    return list;
  }
  
  public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
    if (!playerIn.capabilities.isCreativeMode)
      stack.stackSize--; 
    if (!worldIn.isRemote) {
      List<PotionEffect> list = getEffects(stack);
      if (list != null)
        for (PotionEffect potioneffect : list)
          playerIn.addPotionEffect(new PotionEffect(potioneffect));  
    } 
    playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
    if (!playerIn.capabilities.isCreativeMode) {
      if (stack.stackSize <= 0)
        return new ItemStack(Items.glass_bottle); 
      playerIn.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
    } 
    return stack;
  }
  
  public int getMaxItemUseDuration(ItemStack stack) {
    return 32;
  }
  
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.DRINK;
  }
  
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    if (isSplash(itemStackIn.getMetadata())) {
      if (!playerIn.capabilities.isCreativeMode)
        itemStackIn.stackSize--; 
      worldIn.playSoundAtEntity((Entity)playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
      if (!worldIn.isRemote)
        worldIn.spawnEntityInWorld((Entity)new EntityPotion(worldIn, (EntityLivingBase)playerIn, itemStackIn)); 
      playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
      return itemStackIn;
    } 
    playerIn.setItemInUse(itemStackIn, getMaxItemUseDuration(itemStackIn));
    return itemStackIn;
  }
  
  public static boolean isSplash(int meta) {
    return ((meta & 0x4000) != 0);
  }
  
  public int getColorFromDamage(int meta) {
    return PotionHelper.getLiquidColor(meta, false);
  }
  
  public int getColorFromItemStack(ItemStack stack, int renderPass) {
    return (renderPass > 0) ? 16777215 : getColorFromDamage(stack.getMetadata());
  }
  
  public boolean isEffectInstant(int meta) {
    List<PotionEffect> list = getEffects(meta);
    if (list != null && !list.isEmpty()) {
      for (PotionEffect potioneffect : list) {
        if (Potion.potionTypes[potioneffect.getPotionID()].isInstant())
          return true; 
      } 
      return false;
    } 
    return false;
  }
  
  public String getItemStackDisplayName(ItemStack stack) {
    if (stack.getMetadata() == 0)
      return StatCollector.translateToLocal("item.emptyPotion.name").trim(); 
    String s = "";
    if (isSplash(stack.getMetadata()))
      s = StatCollector.translateToLocal("potion.prefix.grenade").trim() + " "; 
    List<PotionEffect> list = Items.potionitem.getEffects(stack);
    if (list != null && !list.isEmpty()) {
      String s2 = ((PotionEffect)list.get(0)).getEffectName();
      s2 = s2 + ".postfix";
      return s + StatCollector.translateToLocal(s2).trim();
    } 
    String s1 = PotionHelper.getPotionPrefix(stack.getMetadata());
    return StatCollector.translateToLocal(s1).trim() + " " + super.getItemStackDisplayName(stack);
  }
  
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    if (stack.getMetadata() != 0) {
      List<PotionEffect> list = Items.potionitem.getEffects(stack);
      HashMultimap hashMultimap = HashMultimap.create();
      if (list != null && !list.isEmpty()) {
        for (PotionEffect potioneffect : list) {
          String s1 = StatCollector.translateToLocal(potioneffect.getEffectName()).trim();
          Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
          Map<IAttribute, AttributeModifier> map = potion.getAttributeModifierMap();
          if (map != null && map.size() > 0)
            for (Map.Entry<IAttribute, AttributeModifier> entry : map.entrySet()) {
              AttributeModifier attributemodifier = entry.getValue();
              AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.getAttributeModifierAmount(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
              hashMultimap.put(((IAttribute)entry.getKey()).getAttributeUnlocalizedName(), attributemodifier1);
            }  
          if (potioneffect.getAmplifier() > 0)
            s1 = s1 + " " + StatCollector.translateToLocal("potion.potency." + potioneffect.getAmplifier()).trim(); 
          if (potioneffect.getDuration() > 20)
            s1 = s1 + " (" + Potion.getDurationString(potioneffect) + ")"; 
          if (potion.isBadEffect()) {
            tooltip.add(EnumChatFormatting.RED + s1);
            continue;
          } 
          tooltip.add(EnumChatFormatting.GRAY + s1);
        } 
      } else {
        String s = StatCollector.translateToLocal("potion.empty").trim();
        tooltip.add(EnumChatFormatting.GRAY + s);
      } 
      if (!hashMultimap.isEmpty()) {
        tooltip.add("");
        tooltip.add(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal("potion.effects.whenDrank"));
        for (Map.Entry<String, AttributeModifier> entry1 : (Iterable<Map.Entry<String, AttributeModifier>>)hashMultimap.entries()) {
          double d1;
          AttributeModifier attributemodifier2 = entry1.getValue();
          double d0 = attributemodifier2.getAmount();
          if (attributemodifier2.getOperation() != 1 && attributemodifier2.getOperation() != 2) {
            d1 = attributemodifier2.getAmount();
          } else {
            d1 = attributemodifier2.getAmount() * 100.0D;
          } 
          if (d0 > 0.0D) {
            tooltip.add(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier2.getOperation(), new Object[] { ItemStack.DECIMALFORMAT.format(d1), StatCollector.translateToLocal("attribute.name." + (String)entry1.getKey()) }));
            continue;
          } 
          if (d0 < 0.0D) {
            d1 *= -1.0D;
            tooltip.add(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted("attribute.modifier.take." + attributemodifier2.getOperation(), new Object[] { ItemStack.DECIMALFORMAT.format(d1), StatCollector.translateToLocal("attribute.name." + (String)entry1.getKey()) }));
          } 
        } 
      } 
    } 
  }
  
  public boolean hasEffect(ItemStack stack) {
    List<PotionEffect> list = getEffects(stack);
    return (list != null && !list.isEmpty());
  }
  
  public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
    super.getSubItems(itemIn, tab, subItems);
    if (SUB_ITEMS_CACHE.isEmpty())
      for (int i = 0; i <= 15; i++) {
        for (int j = 0; j <= 1; j++) {
          int lvt_6_1_;
          if (j == 0) {
            lvt_6_1_ = i | 0x2000;
          } else {
            lvt_6_1_ = i | 0x4000;
          } 
          for (int l = 0; l <= 2; l++) {
            int i1 = lvt_6_1_;
            if (l != 0)
              if (l == 1) {
                i1 = lvt_6_1_ | 0x20;
              } else if (l == 2) {
                i1 = lvt_6_1_ | 0x40;
              }  
            List<PotionEffect> list = PotionHelper.getPotionEffects(i1, false);
            if (list != null && !list.isEmpty())
              SUB_ITEMS_CACHE.put(list, Integer.valueOf(i1)); 
          } 
        } 
      }  
    Iterator<Integer> iterator = SUB_ITEMS_CACHE.values().iterator();
    while (iterator.hasNext()) {
      int j1 = ((Integer)iterator.next()).intValue();
      subItems.add(new ItemStack(itemIn, 1, j1));
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemPotion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */