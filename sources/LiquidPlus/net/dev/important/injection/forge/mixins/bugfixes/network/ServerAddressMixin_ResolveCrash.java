/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ServerAddress
 */
package net.dev.important.injection.forge.mixins.bugfixes.network;

import java.net.IDN;
import net.minecraft.client.multiplayer.ServerAddress;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={ServerAddress.class})
public class ServerAddressMixin_ResolveCrash {
    @Shadow
    @Final
    private String field_78866_a;

    @Overwrite
    public String func_78861_a() {
        try {
            return IDN.toASCII(this.field_78866_a);
        }
        catch (IllegalArgumentException e) {
            return "";
        }
    }
}

