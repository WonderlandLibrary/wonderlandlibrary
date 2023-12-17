// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import com.google.common.collect.Maps;
import java.util.Set;
import java.util.Map;

public enum SoundCategory
{
    MASTER("master"), 
    MUSIC("music"), 
    RECORDS("record"), 
    WEATHER("weather"), 
    BLOCKS("block"), 
    HOSTILE("hostile"), 
    NEUTRAL("neutral"), 
    PLAYERS("player"), 
    AMBIENT("ambient"), 
    VOICE("voice");
    
    private static final Map<String, SoundCategory> SOUND_CATEGORIES;
    private final String name;
    
    private SoundCategory(final String nameIn) {
        this.name = nameIn;
    }
    
    public String getName() {
        return this.name;
    }
    
    public static SoundCategory getByName(final String categoryName) {
        return SoundCategory.SOUND_CATEGORIES.get(categoryName);
    }
    
    public static Set<String> getSoundCategoryNames() {
        return SoundCategory.SOUND_CATEGORIES.keySet();
    }
    
    static {
        SOUND_CATEGORIES = Maps.newHashMap();
        for (final SoundCategory soundcategory : values()) {
            if (SoundCategory.SOUND_CATEGORIES.containsKey(soundcategory.getName())) {
                throw new Error("Clash in Sound Category name pools! Cannot insert " + soundcategory);
            }
            SoundCategory.SOUND_CATEGORIES.put(soundcategory.getName(), soundcategory);
        }
    }
}
