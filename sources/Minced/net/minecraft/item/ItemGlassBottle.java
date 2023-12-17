// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.stats.StatList;
import net.minecraft.util.math.BlockPos;
import java.util.List;
import net.minecraft.potion.PotionUtils;
import net.minecraft.init.PotionTypes;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.init.Items;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.boss.EntityDragon;
import javax.annotation.Nullable;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;

public class ItemGlassBottle extends Item
{
    public ItemGlassBottle() {
        this.setCreativeTab(CreativeTabs.BREWING);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
        final List<EntityAreaEffectCloud> list = worldIn.getEntitiesWithinAABB((Class<? extends EntityAreaEffectCloud>)EntityAreaEffectCloud.class, playerIn.getEntityBoundingBox().grow(2.0), (com.google.common.base.Predicate<? super EntityAreaEffectCloud>)new Predicate<EntityAreaEffectCloud>() {
            public boolean apply(@Nullable final EntityAreaEffectCloud p_apply_1_) {
                return p_apply_1_ != null && p_apply_1_.isEntityAlive() && p_apply_1_.getOwner() instanceof EntityDragon;
            }
        });
        final ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (!list.isEmpty()) {
            final EntityAreaEffectCloud entityareaeffectcloud = list.get(0);
            entityareaeffectcloud.setRadius(entityareaeffectcloud.getRadius() - 0.5f);
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.NEUTRAL, 1.0f, 1.0f);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, this.turnBottleIntoItem(itemstack, playerIn, new ItemStack(Items.DRAGON_BREATH)));
        }
        final RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, true);
        if (raytraceresult == null) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK) {
            final BlockPos blockpos = raytraceresult.getBlockPos();
            if (!worldIn.isBlockModifiable(playerIn, blockpos) || !playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack)) {
                return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
            }
            if (worldIn.getBlockState(blockpos).getMaterial() == Material.WATER) {
                worldIn.playSound(playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0f, 1.0f);
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, this.turnBottleIntoItem(itemstack, playerIn, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER)));
            }
        }
        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
    }
    
    protected ItemStack turnBottleIntoItem(final ItemStack p_185061_1_, final EntityPlayer player, final ItemStack stack) {
        p_185061_1_.shrink(1);
        player.addStat(StatList.getObjectUseStats(this));
        if (p_185061_1_.isEmpty()) {
            return stack;
        }
        if (!player.inventory.addItemStackToInventory(stack)) {
            player.dropItem(stack, false);
        }
        return p_185061_1_;
    }
}
