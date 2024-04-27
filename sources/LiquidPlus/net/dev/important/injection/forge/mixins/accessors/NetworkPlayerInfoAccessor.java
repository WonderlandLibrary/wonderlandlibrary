/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.network.NetworkPlayerInfo
 *  net.minecraft.util.ResourceLocation
 */
package net.dev.important.injection.forge.mixins.accessors;

import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={NetworkPlayerInfo.class})
public interface NetworkPlayerInfoAccessor {
    @Accessor
    public void setLocationSkin(ResourceLocation var1);
}

