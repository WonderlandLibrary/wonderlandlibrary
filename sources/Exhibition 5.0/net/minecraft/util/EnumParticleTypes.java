// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import java.util.ArrayList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Map;

public enum EnumParticleTypes
{
    EXPLOSION_NORMAL("EXPLOSION_NORMAL", 0, "explode", 0, true), 
    EXPLOSION_LARGE("EXPLOSION_LARGE", 1, "largeexplode", 1, true), 
    EXPLOSION_HUGE("EXPLOSION_HUGE", 2, "hugeexplosion", 2, true), 
    FIREWORKS_SPARK("FIREWORKS_SPARK", 3, "fireworksSpark", 3, false), 
    WATER_BUBBLE("WATER_BUBBLE", 4, "bubble", 4, false), 
    WATER_SPLASH("WATER_SPLASH", 5, "splash", 5, false), 
    WATER_WAKE("WATER_WAKE", 6, "wake", 6, false), 
    SUSPENDED("SUSPENDED", 7, "suspended", 7, false), 
    SUSPENDED_DEPTH("SUSPENDED_DEPTH", 8, "depthsuspend", 8, false), 
    CRIT("CRIT", 9, "crit", 9, false), 
    CRIT_MAGIC("CRIT_MAGIC", 10, "magicCrit", 10, false), 
    SMOKE_NORMAL("SMOKE_NORMAL", 11, "smoke", 11, false), 
    SMOKE_LARGE("SMOKE_LARGE", 12, "largesmoke", 12, false), 
    SPELL("SPELL", 13, "spell", 13, false), 
    SPELL_INSTANT("SPELL_INSTANT", 14, "instantSpell", 14, false), 
    SPELL_MOB("SPELL_MOB", 15, "mobSpell", 15, false), 
    SPELL_MOB_AMBIENT("SPELL_MOB_AMBIENT", 16, "mobSpellAmbient", 16, false), 
    SPELL_WITCH("SPELL_WITCH", 17, "witchMagic", 17, false), 
    DRIP_WATER("DRIP_WATER", 18, "dripWater", 18, false), 
    DRIP_LAVA("DRIP_LAVA", 19, "dripLava", 19, false), 
    VILLAGER_ANGRY("VILLAGER_ANGRY", 20, "angryVillager", 20, false), 
    VILLAGER_HAPPY("VILLAGER_HAPPY", 21, "happyVillager", 21, false), 
    TOWN_AURA("TOWN_AURA", 22, "townaura", 22, false), 
    NOTE("NOTE", 23, "note", 23, false), 
    PORTAL("PORTAL", 24, "portal", 24, false), 
    ENCHANTMENT_TABLE("ENCHANTMENT_TABLE", 25, "enchantmenttable", 25, false), 
    FLAME("FLAME", 26, "flame", 26, false), 
    LAVA("LAVA", 27, "lava", 27, false), 
    FOOTSTEP("FOOTSTEP", 28, "footstep", 28, false), 
    CLOUD("CLOUD", 29, "cloud", 29, false), 
    REDSTONE("REDSTONE", 30, "reddust", 30, false), 
    SNOWBALL("SNOWBALL", 31, "snowballpoof", 31, false), 
    SNOW_SHOVEL("SNOW_SHOVEL", 32, "snowshovel", 32, false), 
    SLIME("SLIME", 33, "slime", 33, false), 
    HEART("HEART", 34, "heart", 34, false), 
    BARRIER("BARRIER", 35, "barrier", 35, false), 
    ITEM_CRACK("ITEM_CRACK", 36, "iconcrack_", 36, false, 2), 
    BLOCK_CRACK("BLOCK_CRACK", 37, "blockcrack_", 37, false, 1), 
    BLOCK_DUST("BLOCK_DUST", 38, "blockdust_", 38, false, 1), 
    WATER_DROP("WATER_DROP", 39, "droplet", 39, false), 
    ITEM_TAKE("ITEM_TAKE", 40, "take", 40, false), 
    MOB_APPEARANCE("MOB_APPEARANCE", 41, "mobappearance", 41, true);
    
    private final String field_179369_Q;
    private final int field_179372_R;
    private final boolean field_179371_S;
    private final int field_179366_T;
    private static final Map field_179365_U;
    private static final String[] field_179368_V;
    private static final EnumParticleTypes[] $VALUES;
    private static final String __OBFID = "CL_00002317";
    
    private EnumParticleTypes(final String p_i46011_1_, final int p_i46011_2_, final String p_i46011_3_, final int p_i46011_4_, final boolean p_i46011_5_, final int p_i46011_6_) {
        this.field_179369_Q = p_i46011_3_;
        this.field_179372_R = p_i46011_4_;
        this.field_179371_S = p_i46011_5_;
        this.field_179366_T = p_i46011_6_;
    }
    
    private EnumParticleTypes(final String p_i46012_1_, final int p_i46012_2_, final String p_i46012_3_, final int p_i46012_4_, final boolean p_i46012_5_) {
        this(p_i46012_1_, p_i46012_2_, p_i46012_3_, p_i46012_4_, p_i46012_5_, 0);
    }
    
    public static String[] func_179349_a() {
        return EnumParticleTypes.field_179368_V;
    }
    
    public String func_179346_b() {
        return this.field_179369_Q;
    }
    
    public int func_179348_c() {
        return this.field_179372_R;
    }
    
    public int func_179345_d() {
        return this.field_179366_T;
    }
    
    public boolean func_179344_e() {
        return this.field_179371_S;
    }
    
    public boolean func_179343_f() {
        return this.field_179366_T > 0;
    }
    
    public static EnumParticleTypes func_179342_a(final int p_179342_0_) {
        return EnumParticleTypes.field_179365_U.get(p_179342_0_);
    }
    
    static {
        field_179365_U = Maps.newHashMap();
        $VALUES = new EnumParticleTypes[] { EnumParticleTypes.EXPLOSION_NORMAL, EnumParticleTypes.EXPLOSION_LARGE, EnumParticleTypes.EXPLOSION_HUGE, EnumParticleTypes.FIREWORKS_SPARK, EnumParticleTypes.WATER_BUBBLE, EnumParticleTypes.WATER_SPLASH, EnumParticleTypes.WATER_WAKE, EnumParticleTypes.SUSPENDED, EnumParticleTypes.SUSPENDED_DEPTH, EnumParticleTypes.CRIT, EnumParticleTypes.CRIT_MAGIC, EnumParticleTypes.SMOKE_NORMAL, EnumParticleTypes.SMOKE_LARGE, EnumParticleTypes.SPELL, EnumParticleTypes.SPELL_INSTANT, EnumParticleTypes.SPELL_MOB, EnumParticleTypes.SPELL_MOB_AMBIENT, EnumParticleTypes.SPELL_WITCH, EnumParticleTypes.DRIP_WATER, EnumParticleTypes.DRIP_LAVA, EnumParticleTypes.VILLAGER_ANGRY, EnumParticleTypes.VILLAGER_HAPPY, EnumParticleTypes.TOWN_AURA, EnumParticleTypes.NOTE, EnumParticleTypes.PORTAL, EnumParticleTypes.ENCHANTMENT_TABLE, EnumParticleTypes.FLAME, EnumParticleTypes.LAVA, EnumParticleTypes.FOOTSTEP, EnumParticleTypes.CLOUD, EnumParticleTypes.REDSTONE, EnumParticleTypes.SNOWBALL, EnumParticleTypes.SNOW_SHOVEL, EnumParticleTypes.SLIME, EnumParticleTypes.HEART, EnumParticleTypes.BARRIER, EnumParticleTypes.ITEM_CRACK, EnumParticleTypes.BLOCK_CRACK, EnumParticleTypes.BLOCK_DUST, EnumParticleTypes.WATER_DROP, EnumParticleTypes.ITEM_TAKE, EnumParticleTypes.MOB_APPEARANCE };
        final ArrayList var0 = Lists.newArrayList();
        for (final EnumParticleTypes var5 : values()) {
            EnumParticleTypes.field_179365_U.put(var5.func_179348_c(), var5);
            if (!var5.func_179346_b().endsWith("_")) {
                var0.add(var5.func_179346_b());
            }
        }
        field_179368_V = var0.toArray(new String[var0.size()]);
    }
}
