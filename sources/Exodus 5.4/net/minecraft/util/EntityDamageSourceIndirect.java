/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StatCollector;

public class EntityDamageSourceIndirect
extends EntityDamageSource {
    private Entity indirectEntity;

    @Override
    public Entity getSourceOfDamage() {
        return this.damageSourceEntity;
    }

    @Override
    public Entity getEntity() {
        return this.indirectEntity;
    }

    @Override
    public IChatComponent getDeathMessage(EntityLivingBase entityLivingBase) {
        IChatComponent iChatComponent = this.indirectEntity == null ? this.damageSourceEntity.getDisplayName() : this.indirectEntity.getDisplayName();
        ItemStack itemStack = this.indirectEntity instanceof EntityLivingBase ? ((EntityLivingBase)this.indirectEntity).getHeldItem() : null;
        String string = "death.attack." + this.damageType;
        String string2 = String.valueOf(string) + ".item";
        return itemStack != null && itemStack.hasDisplayName() && StatCollector.canTranslate(string2) ? new ChatComponentTranslation(string2, entityLivingBase.getDisplayName(), iChatComponent, itemStack.getChatComponent()) : new ChatComponentTranslation(string, entityLivingBase.getDisplayName(), iChatComponent);
    }

    public EntityDamageSourceIndirect(String string, Entity entity, Entity entity2) {
        super(string, entity);
        this.indirectEntity = entity2;
    }
}

