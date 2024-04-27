/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GLAllocation
 */
package net.dev.important.patcher.util.enhancement.text;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.dev.important.patcher.util.enhancement.Enhancement;
import net.dev.important.patcher.util.enhancement.hash.StringHash;
import net.dev.important.patcher.util.enhancement.text.CachedString;
import net.minecraft.client.renderer.GLAllocation;

public final class EnhancedFontRenderer
implements Enhancement {
    private static final List<EnhancedFontRenderer> instances = new ArrayList<EnhancedFontRenderer>();
    private final List<StringHash> obfuscated = new ArrayList<StringHash>();
    private final Map<String, Integer> stringWidthCache = new HashMap<String, Integer>();
    private final Queue<Integer> glRemoval = new ConcurrentLinkedQueue<Integer>();
    private final Cache<StringHash, CachedString> stringCache = Caffeine.newBuilder().removalListener((key, value, cause) -> {
        if (value == null) {
            return;
        }
        this.glRemoval.add(((CachedString)value).getListId());
    }).executor(POOL).maximumSize(500L).build();

    public EnhancedFontRenderer() {
        instances.add(this);
    }

    public static List<EnhancedFontRenderer> getInstances() {
        return instances;
    }

    @Override
    public String getName() {
        return "Enhanced Font Renderer";
    }

    @Override
    public void tick() {
        this.stringCache.invalidateAll(this.obfuscated);
        this.obfuscated.clear();
    }

    public int getGlList() {
        Integer poll = this.glRemoval.poll();
        return poll == null ? GLAllocation.func_74526_a((int)1) : poll;
    }

    public CachedString get(StringHash key) {
        return this.stringCache.getIfPresent(key);
    }

    public void cache(StringHash key, CachedString value) {
        this.stringCache.put(key, value);
    }

    public Map<String, Integer> getStringWidthCache() {
        return this.stringWidthCache;
    }

    public void invalidateAll() {
        this.stringCache.invalidateAll();
    }

    public List<StringHash> getObfuscated() {
        return this.obfuscated;
    }
}

