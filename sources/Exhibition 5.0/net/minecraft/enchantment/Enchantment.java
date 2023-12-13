// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.enchantment;

import java.util.ArrayList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.util.DamageSource;
import java.util.Iterator;
import net.minecraft.util.ResourceLocation;
import java.util.Map;

public abstract class Enchantment
{
    private static final Enchantment[] field_180311_a;
    public static final Enchantment[] enchantmentsList;
    private static final Map field_180307_E;
    public static final Enchantment protection;
    public static final Enchantment fireProtection;
    public static final Enchantment field_180309_e;
    public static final Enchantment blastProtection;
    public static final Enchantment field_180308_g;
    public static final Enchantment field_180317_h;
    public static final Enchantment aquaAffinity;
    public static final Enchantment thorns;
    public static final Enchantment field_180316_k;
    public static final Enchantment sharpness;
    public static final Enchantment field_180315_m;
    public static final Enchantment field_180312_n;
    public static final Enchantment knockback;
    public static final Enchantment fireAspect;
    public static final Enchantment looting;
    public static final Enchantment efficiency;
    public static final Enchantment silkTouch;
    public static final Enchantment unbreaking;
    public static final Enchantment fortune;
    public static final Enchantment power;
    public static final Enchantment punch;
    public static final Enchantment flame;
    public static final Enchantment infinity;
    public static final Enchantment luckOfTheSea;
    public static final Enchantment lure;
    public final int effectId;
    private final int weight;
    public EnumEnchantmentType type;
    protected String name;
    private static final String __OBFID = "CL_00000105";
    
    public static Enchantment func_180306_c(final int p_180306_0_) {
        return (p_180306_0_ >= 0 && p_180306_0_ < Enchantment.field_180311_a.length) ? Enchantment.field_180311_a[p_180306_0_] : null;
    }
    
    protected Enchantment(final int p_i45771_1_, final ResourceLocation p_i45771_2_, final int p_i45771_3_, final EnumEnchantmentType p_i45771_4_) {
        this.effectId = p_i45771_1_;
        this.weight = p_i45771_3_;
        this.type = p_i45771_4_;
        if (Enchantment.field_180311_a[p_i45771_1_] != null) {
            throw new IllegalArgumentException("Duplicate enchantment id!");
        }
        Enchantment.field_180311_a[p_i45771_1_] = this;
        Enchantment.field_180307_E.put(p_i45771_2_, this);
    }
    
    public static Enchantment func_180305_b(final String p_180305_0_) {
        return Enchantment.field_180307_E.get(new ResourceLocation(p_180305_0_));
    }
    
    public static String[] func_180304_c() {
        final String[] var0 = new String[Enchantment.field_180307_E.size()];
        int var2 = 0;
        for (final ResourceLocation var4 : Enchantment.field_180307_E.keySet()) {
            var0[var2++] = var4.toString();
        }
        return var0;
    }
    
    public int getWeight() {
        return this.weight;
    }
    
    public int getMinLevel() {
        return 1;
    }
    
    public int getMaxLevel() {
        return 1;
    }
    
    public int getMinEnchantability(final int p_77321_1_) {
        return 1 + p_77321_1_ * 10;
    }
    
    public int getMaxEnchantability(final int p_77317_1_) {
        return this.getMinEnchantability(p_77317_1_) + 5;
    }
    
    public int calcModifierDamage(final int p_77318_1_, final DamageSource p_77318_2_) {
        return 0;
    }
    
    public float func_152376_a(final int p_152376_1_, final EnumCreatureAttribute p_152376_2_) {
        return 0.0f;
    }
    
    public boolean canApplyTogether(final Enchantment p_77326_1_) {
        return this != p_77326_1_;
    }
    
    public Enchantment setName(final String p_77322_1_) {
        this.name = p_77322_1_;
        return this;
    }
    
    public String getName() {
        return "enchantment." + this.name;
    }
    
    public String getTranslatedName(final int p_77316_1_) {
        final String var2 = StatCollector.translateToLocal(this.getName());
        return var2 + " " + StatCollector.translateToLocal("enchantment.level." + p_77316_1_);
    }
    
    public boolean canApply(final ItemStack p_92089_1_) {
        return this.type.canEnchantItem(p_92089_1_.getItem());
    }
    
    public void func_151368_a(final EntityLivingBase p_151368_1_, final Entity p_151368_2_, final int p_151368_3_) {
    }
    
    public void func_151367_b(final EntityLivingBase p_151367_1_, final Entity p_151367_2_, final int p_151367_3_) {
    }
    
    static {
        field_180311_a = new Enchantment[256];
        field_180307_E = Maps.newHashMap();
        protection = new EnchantmentProtection(0, new ResourceLocation("protection"), 10, 0);
        fireProtection = new EnchantmentProtection(1, new ResourceLocation("fire_protection"), 5, 1);
        field_180309_e = new EnchantmentProtection(2, new ResourceLocation("feather_falling"), 5, 2);
        blastProtection = new EnchantmentProtection(3, new ResourceLocation("blast_protection"), 2, 3);
        field_180308_g = new EnchantmentProtection(4, new ResourceLocation("projectile_protection"), 5, 4);
        field_180317_h = new EnchantmentOxygen(5, new ResourceLocation("respiration"), 2);
        aquaAffinity = new EnchantmentWaterWorker(6, new ResourceLocation("aqua_affinity"), 2);
        thorns = new EnchantmentThorns(7, new ResourceLocation("thorns"), 1);
        field_180316_k = new EnchantmentWaterWalker(8, new ResourceLocation("depth_strider"), 2);
        sharpness = new EnchantmentDamage(16, new ResourceLocation("sharpness"), 10, 0);
        field_180315_m = new EnchantmentDamage(17, new ResourceLocation("smite"), 5, 1);
        field_180312_n = new EnchantmentDamage(18, new ResourceLocation("bane_of_arthropods"), 5, 2);
        knockback = new EnchantmentKnockback(19, new ResourceLocation("knockback"), 5);
        fireAspect = new EnchantmentFireAspect(20, new ResourceLocation("fire_aspect"), 2);
        looting = new EnchantmentLootBonus(21, new ResourceLocation("looting"), 2, EnumEnchantmentType.WEAPON);
        efficiency = new EnchantmentDigging(32, new ResourceLocation("efficiency"), 10);
        silkTouch = new EnchantmentUntouching(33, new ResourceLocation("silk_touch"), 1);
        unbreaking = new EnchantmentDurability(34, new ResourceLocation("unbreaking"), 5);
        fortune = new EnchantmentLootBonus(35, new ResourceLocation("fortune"), 2, EnumEnchantmentType.DIGGER);
        power = new EnchantmentArrowDamage(48, new ResourceLocation("power"), 10);
        punch = new EnchantmentArrowKnockback(49, new ResourceLocation("punch"), 2);
        flame = new EnchantmentArrowFire(50, new ResourceLocation("flame"), 2);
        infinity = new EnchantmentArrowInfinite(51, new ResourceLocation("infinity"), 1);
        luckOfTheSea = new EnchantmentLootBonus(61, new ResourceLocation("luck_of_the_sea"), 2, EnumEnchantmentType.FISHING_ROD);
        lure = new EnchantmentFishingSpeed(62, new ResourceLocation("lure"), 2, EnumEnchantmentType.FISHING_ROD);
        final ArrayList var0 = Lists.newArrayList();
        for (final Enchantment var5 : Enchantment.field_180311_a) {
            if (var5 != null) {
                var0.add(var5);
            }
        }
        enchantmentsList = var0.toArray(new Enchantment[var0.size()]);
    }
}
