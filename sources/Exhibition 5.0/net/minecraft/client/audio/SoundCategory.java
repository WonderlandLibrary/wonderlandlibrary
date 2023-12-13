// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.audio;

import com.google.common.collect.Maps;
import java.util.Map;

public enum SoundCategory
{
    MASTER("MASTER", 0, "master", 0), 
    MUSIC("MUSIC", 1, "music", 1), 
    RECORDS("RECORDS", 2, "record", 2), 
    WEATHER("WEATHER", 3, "weather", 3), 
    BLOCKS("BLOCKS", 4, "block", 4), 
    MOBS("MOBS", 5, "hostile", 5), 
    ANIMALS("ANIMALS", 6, "neutral", 6), 
    PLAYERS("PLAYERS", 7, "player", 7), 
    AMBIENT("AMBIENT", 8, "ambient", 8);
    
    private static final Map field_147168_j;
    private static final Map field_147169_k;
    private final String categoryName;
    private final int categoryId;
    private static final SoundCategory[] $VALUES;
    private static final String __OBFID = "CL_00001686";
    
    private SoundCategory(final String p_i45126_1_, final int p_i45126_2_, final String p_i45126_3_, final int p_i45126_4_) {
        this.categoryName = p_i45126_3_;
        this.categoryId = p_i45126_4_;
    }
    
    public String getCategoryName() {
        return this.categoryName;
    }
    
    public int getCategoryId() {
        return this.categoryId;
    }
    
    public static SoundCategory func_147154_a(final String p_147154_0_) {
        return SoundCategory.field_147168_j.get(p_147154_0_);
    }
    
    static {
        field_147168_j = Maps.newHashMap();
        field_147169_k = Maps.newHashMap();
        $VALUES = new SoundCategory[] { SoundCategory.MASTER, SoundCategory.MUSIC, SoundCategory.RECORDS, SoundCategory.WEATHER, SoundCategory.BLOCKS, SoundCategory.MOBS, SoundCategory.ANIMALS, SoundCategory.PLAYERS, SoundCategory.AMBIENT };
        for (final SoundCategory var4 : values()) {
            if (SoundCategory.field_147168_j.containsKey(var4.getCategoryName()) || SoundCategory.field_147169_k.containsKey(var4.getCategoryId())) {
                throw new Error("Clash in Sound Category ID & Name pools! Cannot insert " + var4);
            }
            SoundCategory.field_147168_j.put(var4.getCategoryName(), var4);
            SoundCategory.field_147169_k.put(var4.getCategoryId(), var4);
        }
    }
}
