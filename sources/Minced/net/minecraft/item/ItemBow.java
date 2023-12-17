// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ActionResult;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.Entity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import net.minecraft.creativetab.CreativeTabs;

public class ItemBow extends Item
{
    public ItemBow() {
        this.maxStackSize = 1;
        this.setMaxDamage(384);
        this.setCreativeTab(CreativeTabs.COMBAT);
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
            @Override
            public float apply(final ItemStack stack, @Nullable final World worldIn, @Nullable final EntityLivingBase entityIn) {
                if (entityIn == null) {
                    return 0.0f;
                }
                return (entityIn.getActiveItemStack().getItem() != Items.BOW) ? 0.0f : ((stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0f);
            }
        });
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
            @Override
            public float apply(final ItemStack stack, @Nullable final World worldIn, @Nullable final EntityLivingBase entityIn) {
                return (entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack) ? 1.0f : 0.0f;
            }
        });
    }
    
    private ItemStack findAmmo(final EntityPlayer player) {
        if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND))) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND))) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
            final ItemStack itemstack = player.inventory.getStackInSlot(i);
            if (this.isArrow(itemstack)) {
                return itemstack;
            }
        }
        return ItemStack.EMPTY;
    }
    
    protected boolean isArrow(final ItemStack stack) {
        return stack.getItem() instanceof ItemArrow;
    }
    
    @Override
    public void onPlayerStoppedUsing(final ItemStack stack, final World worldIn, final EntityLivingBase entityLiving, final int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            final EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            final boolean flag = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemstack = this.findAmmo(entityplayer);
            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }
                final int i = this.getMaxItemUseDuration(stack) - timeLeft;
                final float f = getArrowVelocity(i);
                if (f >= 0.1) {
                    final boolean flag2 = flag && itemstack.getItem() == Items.ARROW;
                    if (!worldIn.isRemote) {
                        final ItemArrow itemarrow = (ItemArrow)((itemstack.getItem() instanceof ItemArrow) ? itemstack.getItem() : Items.ARROW);
                        final EntityArrow entityarrow = itemarrow.createArrow(worldIn, itemstack, entityplayer);
                        entityarrow.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0f, f * 3.0f, 1.0f);
                        if (f == 1.0f) {
                            entityarrow.setIsCritical(true);
                        }
                        final int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
                        if (j > 0) {
                            entityarrow.setDamage(entityarrow.getDamage() + j * 0.5 + 0.5);
                        }
                        final int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
                        if (k > 0) {
                            entityarrow.setKnockbackStrength(k);
                        }
                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
                            entityarrow.setFire(100);
                        }
                        stack.damageItem(1, entityplayer);
                        if (flag2 || (entityplayer.capabilities.isCreativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW))) {
                            entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                        }
                        worldIn.spawnEntity(entityarrow);
                    }
                    worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f / (ItemBow.itemRand.nextFloat() * 0.4f + 1.2f) + f * 0.5f);
                    if (!flag2 && !entityplayer.capabilities.isCreativeMode) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            entityplayer.inventory.deleteStack(itemstack);
                        }
                    }
                    entityplayer.addStat(StatList.getObjectUseStats(this));
                }
            }
        }
    }
    
    public static float getArrowVelocity(final int charge) {
        float f = charge / 20.0f;
        f = (f * f + f * 2.0f) / 3.0f;
        if (f > 1.0f) {
            f = 1.0f;
        }
        return f;
    }
    
    @Override
    public int getMaxItemUseDuration(final ItemStack stack) {
        return 72000;
    }
    
    @Override
    public EnumAction getItemUseAction(final ItemStack stack) {
        return EnumAction.BOW;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
        final ItemStack itemstack = playerIn.getHeldItem(handIn);
        final boolean flag = !this.findAmmo(playerIn).isEmpty();
        if (!playerIn.capabilities.isCreativeMode && !flag) {
            return flag ? new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack) : new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
    
    @Override
    public int getItemEnchantability() {
        return 1;
    }
}
