package net.minecraft.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Map;

public enum EnumParticleTypes
{
  private final String field_179369_Q;
  private final int field_179372_R;
  private final boolean field_179371_S;
  private final int field_179366_T;
  private static final Map field_179365_U;
  private static final String[] field_179368_V;
  private static final EnumParticleTypes[] $VALUES;
  private static final String __OBFID = "CL_00002317";
  
  private EnumParticleTypes(String p_i46011_1_, int p_i46011_2_, String p_i46011_3_, int p_i46011_4_, boolean p_i46011_5_, int p_i46011_6_)
  {
    this.field_179369_Q = p_i46011_3_;
    this.field_179372_R = p_i46011_4_;
    this.field_179371_S = p_i46011_5_;
    this.field_179366_T = p_i46011_6_;
  }
  
  private EnumParticleTypes(String p_i46012_1_, int p_i46012_2_, String p_i46012_3_, int p_i46012_4_, boolean p_i46012_5_)
  {
    this(p_i46012_1_, p_i46012_2_, p_i46012_3_, p_i46012_4_, p_i46012_5_, 0);
  }
  
  public static String[] func_179349_a()
  {
    return field_179368_V;
  }
  
  public String func_179346_b()
  {
    return this.field_179369_Q;
  }
  
  public int func_179348_c()
  {
    return this.field_179372_R;
  }
  
  public int func_179345_d()
  {
    return this.field_179366_T;
  }
  
  public boolean func_179344_e()
  {
    return this.field_179371_S;
  }
  
  public boolean func_179343_f()
  {
    return this.field_179366_T > 0;
  }
  
  public static EnumParticleTypes func_179342_a(int p_179342_0_)
  {
    return (EnumParticleTypes)field_179365_U.get(Integer.valueOf(p_179342_0_));
  }
  
  static
  {
    field_179365_U = Maps.newHashMap();
    
    $VALUES = new EnumParticleTypes[] { EXPLOSION_NORMAL, EXPLOSION_LARGE, EXPLOSION_HUGE, FIREWORKS_SPARK, WATER_BUBBLE, WATER_SPLASH, WATER_WAKE, SUSPENDED, SUSPENDED_DEPTH, CRIT, CRIT_MAGIC, SMOKE_NORMAL, SMOKE_LARGE, SPELL, SPELL_INSTANT, SPELL_MOB, SPELL_MOB_AMBIENT, SPELL_WITCH, DRIP_WATER, DRIP_LAVA, VILLAGER_ANGRY, VILLAGER_HAPPY, TOWN_AURA, NOTE, PORTAL, ENCHANTMENT_TABLE, FLAME, LAVA, FOOTSTEP, CLOUD, REDSTONE, SNOWBALL, SNOW_SHOVEL, SLIME, HEART, BARRIER, ITEM_CRACK, BLOCK_CRACK, BLOCK_DUST, WATER_DROP, ITEM_TAKE, MOB_APPEARANCE };
    
    ArrayList var0 = Lists.newArrayList();
    EnumParticleTypes[] var1 = values();
    int var2 = var1.length;
    for (int var3 = 0; var3 < var2; var3++)
    {
      EnumParticleTypes var4 = var1[var3];
      field_179365_U.put(Integer.valueOf(var4.func_179348_c()), var4);
      if (!var4.func_179346_b().endsWith("_")) {
        var0.add(var4.func_179346_b());
      }
    }
    field_179368_V = (String[])var0.toArray(new String[var0.size()]);
  }
}
