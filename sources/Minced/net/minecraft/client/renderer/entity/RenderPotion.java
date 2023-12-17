// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.projectile.EntityPotion;

public class RenderPotion extends RenderSnowball<EntityPotion>
{
    public RenderPotion(final RenderManager renderManagerIn, final RenderItem itemRendererIn) {
        super(renderManagerIn, Items.POTIONITEM, itemRendererIn);
    }
    
    @Override
    public ItemStack getStackToRender(final EntityPotion entityIn) {
        return entityIn.getPotion();
    }
}
