/*
 * Decompiled with CFR 0.152.
 */
package net.dev.important.patcher.util.enhancement;

import java.util.HashMap;
import java.util.Map;
import net.dev.important.patcher.util.enhancement.Enhancement;
import net.dev.important.patcher.util.enhancement.text.EnhancedFontRenderer;

public class EnhancementManager {
    private static final EnhancementManager instance = new EnhancementManager();
    private final Map<Class<? extends Enhancement>, Enhancement> enhancementMap = new HashMap<Class<? extends Enhancement>, Enhancement>();

    public EnhancementManager() {
        this.enhancementMap.put(EnhancedFontRenderer.class, new EnhancedFontRenderer());
    }

    public void tick() {
        for (Map.Entry<Class<? extends Enhancement>, Enhancement> entry : this.enhancementMap.entrySet()) {
            entry.getValue().tick();
        }
    }

    public <T extends Enhancement> T getEnhancement(Class<T> enhancement) {
        return (T)this.enhancementMap.get(enhancement);
    }

    public static EnhancementManager getInstance() {
        return instance;
    }
}

