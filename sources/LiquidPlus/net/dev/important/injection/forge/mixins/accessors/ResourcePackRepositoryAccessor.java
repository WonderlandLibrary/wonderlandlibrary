/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.ResourcePackRepository
 *  net.minecraft.client.resources.ResourcePackRepository$Entry
 */
package net.dev.important.injection.forge.mixins.accessors;

import java.io.File;
import java.util.List;
import net.minecraft.client.resources.ResourcePackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={ResourcePackRepository.class})
public interface ResourcePackRepositoryAccessor {
    @Invoker
    public List<File> callGetResourcePackFiles();

    @Accessor
    public void setRepositoryEntriesAll(List<ResourcePackRepository.Entry> var1);
}

