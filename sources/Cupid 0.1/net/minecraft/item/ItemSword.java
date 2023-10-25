package net.minecraft.item;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ItemSword extends Item {
  public float attackDamage;
  
  private final Item.ToolMaterial material;
  
  public float getAttackDamage() {
    return this.attackDamage;
  }
  
  public ItemSword(Item.ToolMaterial material) {
    this.material = material;
    this.maxStackSize = 1;
    setMaxDamage(material.getMaxUses());
    setCreativeTab(CreativeTabs.tabCombat);
    this.attackDamage = 4.0F + material.getDamageVsEntity();
  }
  
  public float getDamageVsEntity() {
    return this.material.getDamageVsEntity();
  }
  
  public float getStrVsBlock(ItemStack stack, Block block) {
    if (block == Blocks.web)
      return 15.0F; 
    Material material = block.getMaterial();
    return (material != Material.plants && material != Material.vine && material != Material.coral && material != Material.leaves && material != Material.gourd) ? 1.0F : 1.5F;
  }
  
  public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
    stack.damageItem(1, attacker);
    return true;
  }
  
  public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
    if (blockIn.getBlockHardness(worldIn, pos) != 0.0D)
      stack.damageItem(2, playerIn); 
    return true;
  }
  
  public boolean isFull3D() {
    return true;
  }
  
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.BLOCK;
  }
  
  public int getMaxItemUseDuration(ItemStack stack) {
    return 72000;
  }
  
  public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    playerIn.setItemInUse(itemStackIn, getMaxItemUseDuration(itemStackIn));
    return itemStackIn;
  }
  
  public boolean canHarvestBlock(Block blockIn) {
    return (blockIn == Blocks.web);
  }
  
  public int getItemEnchantability() {
    return this.material.getEnchantability();
  }
  
  public String getToolMaterialName() {
    return this.material.toString();
  }
  
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return (this.material.getRepairItem() == repair.getItem()) ? true : super.getIsRepairable(toRepair, repair);
  }
  
  public Multimap<String, AttributeModifier> getItemAttributeModifiers() {
    Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers();
    multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Weapon modifier", this.attackDamage, 0));
    return multimap;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\item\ItemSword.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */