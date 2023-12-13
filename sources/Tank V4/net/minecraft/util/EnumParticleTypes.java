package net.minecraft.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Map;

public enum EnumParticleTypes {
   ITEM_TAKE("take", 40, false),
   REDSTONE("reddust", 30, false);

   private final int argumentCount;
   BARRIER("barrier", 35, false);

   private static final EnumParticleTypes[] ENUM$VALUES = new EnumParticleTypes[]{EXPLOSION_NORMAL, EXPLOSION_LARGE, EXPLOSION_HUGE, FIREWORKS_SPARK, WATER_BUBBLE, WATER_SPLASH, WATER_WAKE, SUSPENDED, SUSPENDED_DEPTH, CRIT, CRIT_MAGIC, SMOKE_NORMAL, SMOKE_LARGE, SPELL, SPELL_INSTANT, SPELL_MOB, SPELL_MOB_AMBIENT, SPELL_WITCH, DRIP_WATER, DRIP_LAVA, VILLAGER_ANGRY, VILLAGER_HAPPY, TOWN_AURA, NOTE, PORTAL, ENCHANTMENT_TABLE, FLAME, LAVA, FOOTSTEP, CLOUD, REDSTONE, SNOWBALL, SNOW_SHOVEL, SLIME, HEART, BARRIER, ITEM_CRACK, BLOCK_CRACK, BLOCK_DUST, WATER_DROP, ITEM_TAKE, MOB_APPEARANCE};
   FOOTSTEP("footstep", 28, false),
   SUSPENDED("suspended", 7, false),
   SLIME("slime", 33, false),
   SPELL_MOB("mobSpell", 15, false),
   CRIT_MAGIC("magicCrit", 10, false),
   VILLAGER_ANGRY("angryVillager", 20, false),
   ENCHANTMENT_TABLE("enchantmenttable", 25, false);

   private final String particleName;
   SPELL_INSTANT("instantSpell", 14, false),
   WATER_BUBBLE("bubble", 4, false),
   SPELL_WITCH("witchMagic", 17, false),
   SPELL("spell", 13, false),
   FIREWORKS_SPARK("fireworksSpark", 3, false),
   DRIP_WATER("dripWater", 18, false),
   EXPLOSION_HUGE("hugeexplosion", 2, true),
   NOTE("note", 23, false),
   WATER_WAKE("wake", 6, false);

   private final boolean shouldIgnoreRange;
   SUSPENDED_DEPTH("depthsuspend", 8, false),
   CLOUD("cloud", 29, false);

   private static final Map PARTICLES = Maps.newHashMap();
   EXPLOSION_NORMAL("explode", 0, true),
   EXPLOSION_LARGE("largeexplode", 1, true),
   FLAME("flame", 26, false),
   BLOCK_DUST("blockdust_", 38, false, 1),
   SPELL_MOB_AMBIENT("mobSpellAmbient", 16, false),
   SNOW_SHOVEL("snowshovel", 32, false),
   MOB_APPEARANCE("mobappearance", 41, true),
   SNOWBALL("snowballpoof", 31, false),
   HEART("heart", 34, false),
   SMOKE_NORMAL("smoke", 11, false),
   TOWN_AURA("townaura", 22, false),
   DRIP_LAVA("dripLava", 19, false),
   WATER_SPLASH("splash", 5, false),
   BLOCK_CRACK("blockcrack_", 37, false, 1),
   VILLAGER_HAPPY("happyVillager", 21, false),
   PORTAL("portal", 24, false);

   private final int particleID;
   LAVA("lava", 27, false);

   private static final String[] PARTICLE_NAMES;
   SMOKE_LARGE("largesmoke", 12, false),
   WATER_DROP("droplet", 39, false),
   ITEM_CRACK("iconcrack_", 36, false, 2),
   CRIT("crit", 9, false);

   private EnumParticleTypes(String var3, int var4, boolean var5) {
      this(var3, var4, var5, 0);
   }

   public int getParticleID() {
      return this.particleID;
   }

   public boolean getShouldIgnoreRange() {
      return this.shouldIgnoreRange;
   }

   public static String[] getParticleNames() {
      return PARTICLE_NAMES;
   }

   public int getArgumentCount() {
      return this.argumentCount;
   }

   public static EnumParticleTypes getParticleFromId(int var0) {
      return (EnumParticleTypes)PARTICLES.get(var0);
   }

   public String getParticleName() {
      return this.particleName;
   }

   public boolean hasArguments() {
      return this.argumentCount > 0;
   }

   private EnumParticleTypes(String var3, int var4, boolean var5, int var6) {
      this.particleName = var3;
      this.particleID = var4;
      this.shouldIgnoreRange = var5;
      this.argumentCount = var6;
   }

   static {
      ArrayList var0 = Lists.newArrayList();
      EnumParticleTypes[] var4;
      int var3 = (var4 = values()).length;

      for(int var2 = 0; var2 < var3; ++var2) {
         EnumParticleTypes var1 = var4[var2];
         PARTICLES.put(var1.getParticleID(), var1);
         if (!var1.getParticleName().endsWith("_")) {
            var0.add(var1.getParticleName());
         }
      }

      PARTICLE_NAMES = (String[])var0.toArray(new String[var0.size()]);
   }
}
