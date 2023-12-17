// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.event.events.impl;

import net.minecraft.item.Item;
import ru.tuskevich.event.events.Event;

public class EventCalculateCooldown implements Event
{
    private Item stack;
    private float cooldown;
    
    public EventCalculateCooldown(final Item stack) {
        this.stack = stack;
    }
    
    public float getCooldown() {
        return this.cooldown;
    }
    
    public void setCooldown(final float f) {
        this.cooldown = f;
    }
    
    public Item getStack() {
        return this.stack;
    }
}
