/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  net.minecraft.client.resources.DefaultResourcePack
 */
package net.dev.important.injection.forge.mixins.resources;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.minecraft.client.resources.DefaultResourcePack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={DefaultResourcePack.class})
public class MixinDefaultResourcePack {
    @Shadow
    public static final Set<String> field_110608_a = ImmutableSet.of((Object)"minecraft", (Object)"realms");
}

