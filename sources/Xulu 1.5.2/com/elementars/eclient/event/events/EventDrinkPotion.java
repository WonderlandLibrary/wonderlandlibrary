// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.event.events;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import com.elementars.eclient.event.Event;

public class EventDrinkPotion extends Event
{
    EntityLivingBase entityLivingBase;
    ItemStack stack;
    
    public EventDrinkPotion(final EntityLivingBase entityLivingBase, final ItemStack itemStack) {
        this.entityLivingBase = entityLivingBase;
        this.stack = itemStack;
    }
    
    public EntityLivingBase getEntityLivingBase() {
        return this.entityLivingBase;
    }
    
    public ItemStack getStack() {
        return this.stack;
    }
}
