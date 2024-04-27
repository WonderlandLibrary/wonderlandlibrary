/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.resources.ResourcePackRepository
 *  net.minecraft.client.resources.ResourcePackRepository$Entry
 */
package net.dev.important.patcher.hooks.misc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import net.dev.important.injection.forge.mixins.accessors.ResourcePackRepositoryAccessor;
import net.minecraft.client.resources.ResourcePackRepository;

public class ResourcePackRepositoryHook {
    public static void updateRepositoryEntriesAll(ResourcePackRepository repository) {
        ResourcePackRepositoryAccessor accessor = (ResourcePackRepositoryAccessor)repository;
        HashMap<Integer, ResourcePackRepository.Entry> all = new HashMap<Integer, ResourcePackRepository.Entry>();
        for (ResourcePackRepository.Entry entry : repository.func_110609_b()) {
            all.put(entry.hashCode(), entry);
        }
        LinkedHashSet<Object> newSet = new LinkedHashSet<Object>();
        for (File file : accessor.callGetResourcePackFiles()) {
            ResourcePackRepository resourcePackRepository = repository;
            resourcePackRepository.getClass();
            ResourcePackRepository.Entry entry = new ResourcePackRepository.Entry(resourcePackRepository, file);
            int entryHash = entry.hashCode();
            if (!all.containsKey(entryHash)) {
                try {
                    entry.func_110516_a();
                    newSet.add(entry);
                }
                catch (Exception ignored) {
                    newSet.remove(entry);
                }
                continue;
            }
            newSet.add(all.get(entryHash));
        }
        for (ResourcePackRepository.Entry entry : all.values()) {
            if (newSet.contains(entry)) continue;
            entry.func_110517_b();
        }
        accessor.setRepositoryEntriesAll(new ArrayList<ResourcePackRepository.Entry>(newSet));
    }
}

