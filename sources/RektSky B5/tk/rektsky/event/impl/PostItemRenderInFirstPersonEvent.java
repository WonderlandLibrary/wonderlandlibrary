/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.event.impl;

import net.minecraft.item.ItemStack;
import tk.rektsky.event.Event;

public class PostItemRenderInFirstPersonEvent
extends Event {
    ItemStack itemToRender;
    float partialTicks;

    public PostItemRenderInFirstPersonEvent(ItemStack itemToRender, float partialTicks) {
        this.itemToRender = itemToRender;
        this.partialTicks = partialTicks;
    }
}

