/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RenderPotion
extends RenderSnowball<EntityPotion> {
    public RenderPotion(RenderManager renderManager, RenderItem renderItem) {
        super(renderManager, Items.potionitem, renderItem);
    }

    @Override
    public ItemStack func_177082_d(EntityPotion entityPotion) {
        return new ItemStack(this.field_177084_a, 1, entityPotion.getPotionDamage());
    }
}

