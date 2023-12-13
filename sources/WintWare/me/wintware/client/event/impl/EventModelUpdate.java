/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.event.impl;

import me.wintware.client.event.callables.EventCancellable;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.player.EntityPlayer;

public class EventModelUpdate
extends EventCancellable {
    private final EntityPlayer player;
    private final ModelPlayer model;

    public EventModelUpdate(EntityPlayer player, ModelPlayer model) {
        this.player = player;
        this.model = model;
    }

    public final EntityPlayer getPlayer() {
        return this.player;
    }

    public final ModelPlayer getModel() {
        return this.model;
    }
}

