/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.network.NetworkPlayerInfo
 */
package net.dev.important.injection.forge.mixins.accessors;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={AbstractClientPlayer.class})
public interface AbstractClientPlayerAccessor {
    @Accessor
    public NetworkPlayerInfo getPlayerInfo();

    @Accessor
    public void setPlayerInfo(NetworkPlayerInfo var1);
}

