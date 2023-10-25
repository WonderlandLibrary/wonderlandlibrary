/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.client.audio;

import com.google.common.collect.Maps;
import java.util.Map;

public enum SoundCategory {
    MASTER("master", 0),
    MUSIC("music", 1),
    RECORDS("record", 2),
    WEATHER("weather", 3),
    BLOCKS("block", 4),
    MOBS("hostile", 5),
    ANIMALS("neutral", 6),
    PLAYERS("player", 7),
    AMBIENT("ambient", 8);

    private static final Map<Integer, SoundCategory> ID_CATEGORY_MAP;
    private final int categoryId;
    private static final Map<String, SoundCategory> NAME_CATEGORY_MAP;
    private final String categoryName;

    private SoundCategory(String string2, int n2) {
        this.categoryName = string2;
        this.categoryId = n2;
    }

    public static SoundCategory getCategory(String string) {
        return NAME_CATEGORY_MAP.get(string);
    }

    static {
        NAME_CATEGORY_MAP = Maps.newHashMap();
        ID_CATEGORY_MAP = Maps.newHashMap();
        SoundCategory[] soundCategoryArray = SoundCategory.values();
        int n = soundCategoryArray.length;
        int n2 = 0;
        while (n2 < n) {
            SoundCategory soundCategory = soundCategoryArray[n2];
            if (NAME_CATEGORY_MAP.containsKey(soundCategory.getCategoryName()) || ID_CATEGORY_MAP.containsKey(soundCategory.getCategoryId())) {
                throw new Error("Clash in Sound Category ID & Name pools! Cannot insert " + (Object)((Object)soundCategory));
            }
            NAME_CATEGORY_MAP.put(soundCategory.getCategoryName(), soundCategory);
            ID_CATEGORY_MAP.put(soundCategory.getCategoryId(), soundCategory);
            ++n2;
        }
    }

    public int getCategoryId() {
        return this.categoryId;
    }

    public String getCategoryName() {
        return this.categoryName;
    }
}

