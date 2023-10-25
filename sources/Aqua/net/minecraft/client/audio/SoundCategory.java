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

    private static final Map<String, SoundCategory> NAME_CATEGORY_MAP;
    private static final Map<Integer, SoundCategory> ID_CATEGORY_MAP;
    private final String categoryName;
    private final int categoryId;

    private SoundCategory(String name, int id) {
        this.categoryName = name;
        this.categoryId = id;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public int getCategoryId() {
        return this.categoryId;
    }

    public static SoundCategory getCategory(String name) {
        return (SoundCategory)((Object)NAME_CATEGORY_MAP.get((Object)name));
    }

    static {
        NAME_CATEGORY_MAP = Maps.newHashMap();
        ID_CATEGORY_MAP = Maps.newHashMap();
        for (SoundCategory soundcategory : SoundCategory.values()) {
            if (NAME_CATEGORY_MAP.containsKey((Object)soundcategory.getCategoryName()) || ID_CATEGORY_MAP.containsKey((Object)soundcategory.getCategoryId())) {
                throw new Error("Clash in Sound Category ID & Name pools! Cannot insert " + (Object)((Object)soundcategory));
            }
            NAME_CATEGORY_MAP.put((Object)soundcategory.getCategoryName(), (Object)soundcategory);
            ID_CATEGORY_MAP.put((Object)soundcategory.getCategoryId(), (Object)soundcategory);
        }
    }
}
