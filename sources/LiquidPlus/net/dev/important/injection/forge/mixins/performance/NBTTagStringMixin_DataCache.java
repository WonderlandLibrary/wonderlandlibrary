/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.NBTTagString
 */
package net.dev.important.injection.forge.mixins.performance;

import net.minecraft.nbt.NBTTagString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={NBTTagString.class})
public class NBTTagStringMixin_DataCache {
    @Shadow
    private String field_74751_a;
    @Unique
    private String patcher$dataCache;

    @Inject(method={"read"}, at={@At(value="HEAD")})
    private void patcher$emptyDataCache(CallbackInfo ci) {
        this.patcher$dataCache = null;
    }

    @Overwrite
    public String toString() {
        if (this.patcher$dataCache == null) {
            this.patcher$dataCache = "\"" + this.field_74751_a.replace("\"", "\\\"") + "\"";
        }
        return this.patcher$dataCache;
    }
}

