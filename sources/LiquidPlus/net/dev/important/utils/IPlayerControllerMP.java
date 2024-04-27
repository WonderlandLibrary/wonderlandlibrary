/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 */
package net.dev.important.utils;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={PlayerControllerMP.class})
public interface IPlayerControllerMP {
    @Invoker(value="syncCurrentPlayItem")
    public void invokeSyncCurrentItem();
}

