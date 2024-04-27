/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagCompound
 */
package net.dev.important.injection.forge.mixins.bugfixes.crashes;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={NBTTagCompound.class})
public class NBTTagCompoundMixin_FailFast {
    @Inject(method={"setTag"}, at={@At(value="HEAD")})
    private void patcher$failFast(String key, NBTBase value, CallbackInfo ci) {
        if (value == null) {
            throw new IllegalArgumentException("Invalid null NBT value with key " + key);
        }
    }
}

