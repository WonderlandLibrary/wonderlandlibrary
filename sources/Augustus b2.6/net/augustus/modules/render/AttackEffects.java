// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.render;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.augustus.events.EventClickKillAura;
import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.events.EventAttackEntity;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.DoubleValue;
import net.augustus.modules.Module;

public class AttackEffects extends Module
{
    private final DoubleValue explosion;
    private final DoubleValue waterSplash;
    private final DoubleValue waterWake;
    private final DoubleValue waterDrop;
    private final DoubleValue suspendedDepth;
    private final DoubleValue crit;
    private final DoubleValue critMagic;
    private final DoubleValue smokeNormal;
    private final DoubleValue smokeLarge;
    private final DoubleValue spellInstant;
    private final DoubleValue spellMob;
    private final DoubleValue spellMobAmbient;
    private final DoubleValue spellWitch;
    private final DoubleValue dripWater;
    private final DoubleValue dripLava;
    private final DoubleValue villagerAngry;
    private final DoubleValue villagerHappy;
    private final DoubleValue note;
    private final DoubleValue portal;
    private final DoubleValue flame;
    private final DoubleValue lava;
    private final DoubleValue redStone;
    private final DoubleValue snowShovel;
    private final DoubleValue slime;
    private final DoubleValue heart;
    private final DoubleValue barrier;
    private final DoubleValue fireworkSpark;
    
    public AttackEffects() {
        super("AttackEffects", new Color(36, 169, 176), Categorys.RENDER);
        this.explosion = new DoubleValue(1, "Explosion", this, 0.0, 0.0, 20.0, 0);
        this.waterSplash = new DoubleValue(2, "WaterSplash", this, 0.0, 0.0, 20.0, 0);
        this.waterWake = new DoubleValue(3, "WaterWake", this, 0.0, 0.0, 20.0, 0);
        this.waterDrop = new DoubleValue(4, "WaterDrop", this, 0.0, 0.0, 20.0, 0);
        this.suspendedDepth = new DoubleValue(5, "SuspDepth", this, 0.0, 0.0, 20.0, 0);
        this.crit = new DoubleValue(6, "Crit", this, 0.0, 0.0, 20.0, 0);
        this.critMagic = new DoubleValue(7, "CtitMagic", this, 0.0, 0.0, 20.0, 0);
        this.smokeNormal = new DoubleValue(8, "SmokeNormal", this, 0.0, 0.0, 20.0, 0);
        this.smokeLarge = new DoubleValue(9, "SmokeLarge", this, 0.0, 0.0, 20.0, 0);
        this.spellInstant = new DoubleValue(10, "SpellInstant", this, 0.0, 0.0, 20.0, 0);
        this.spellMob = new DoubleValue(11, "SpellMob", this, 1.0, 0.0, 20.0, 0);
        this.spellMobAmbient = new DoubleValue(12, "SpellMAmbient", this, 0.0, 0.0, 20.0, 0);
        this.spellWitch = new DoubleValue(13, "SpellWitch", this, 0.0, 0.0, 20.0, 0);
        this.dripWater = new DoubleValue(14, "DripWater", this, 0.0, 0.0, 20.0, 0);
        this.dripLava = new DoubleValue(15, "DripLava", this, 0.0, 0.0, 20.0, 0);
        this.villagerAngry = new DoubleValue(16, "VillagerAngry", this, 0.0, 0.0, 20.0, 0);
        this.villagerHappy = new DoubleValue(17, "VillagerHappy", this, 0.0, 0.0, 20.0, 0);
        this.note = new DoubleValue(18, "Note", this, 0.0, 0.0, 20.0, 0);
        this.portal = new DoubleValue(19, "Portal", this, 0.0, 0.0, 20.0, 0);
        this.flame = new DoubleValue(20, "Flame", this, 0.0, 0.0, 20.0, 0);
        this.lava = new DoubleValue(21, "Lava", this, 0.0, 0.0, 20.0, 0);
        this.redStone = new DoubleValue(22, "RedStone", this, 0.0, 0.0, 20.0, 0);
        this.snowShovel = new DoubleValue(23, "SnowShovel", this, 0.0, 0.0, 20.0, 0);
        this.slime = new DoubleValue(24, "Slime", this, 0.0, 0.0, 20.0, 0);
        this.heart = new DoubleValue(25, "Heart", this, 0.0, 0.0, 20.0, 0);
        this.barrier = new DoubleValue(26, "Barrier", this, 0.0, 0.0, 20.0, 0);
        this.fireworkSpark = new DoubleValue(27, "FireworkSpark", this, 0.0, 0.0, 20.0, 0);
    }
    
    @EventTarget
    public void onEventAttack(final EventAttackEntity eventAttackEntity) {
        this.renderEffects();
    }
    
    @EventTarget
    public void onEventClickKillAura(final EventClickKillAura eventClickKillAura) {
        this.renderEffects();
    }
    
    private void renderEffects() {
        Entity entity = AttackEffects.mc.objectMouseOver.entityHit;
        if (AttackEffects.mm.killAura.isToggled() && AttackEffects.mm.killAura.mode.getSelected().equals("Basic") && AttackEffects.mm.killAura.target != null) {
            entity = AttackEffects.mm.killAura.target;
        }
        for (int i = 0; i < this.explosion.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.EXPLOSION_NORMAL);
        }
        for (int i = 0; i < this.waterSplash.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.WATER_SPLASH);
        }
        for (int i = 0; i < this.waterWake.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.WATER_WAKE);
        }
        for (int i = 0; i < this.waterDrop.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.WATER_DROP);
        }
        for (int i = 0; i < this.suspendedDepth.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.SUSPENDED_DEPTH);
        }
        for (int i = 0; i < this.crit.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
        }
        for (int i = 0; i < this.critMagic.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT_MAGIC);
        }
        for (int i = 0; i < this.smokeNormal.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.SMOKE_NORMAL);
        }
        for (int i = 0; i < this.smokeLarge.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.SMOKE_LARGE);
        }
        for (int i = 0; i < this.spellInstant.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.SPELL_INSTANT);
        }
        for (int i = 0; i < this.spellMob.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.SPELL_MOB);
        }
        for (int i = 0; i < this.spellMobAmbient.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.SPELL_MOB_AMBIENT);
        }
        for (int i = 0; i < this.spellWitch.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.SPELL_WITCH);
        }
        for (int i = 0; i < this.dripWater.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.DRIP_WATER);
        }
        for (int i = 0; i < this.dripLava.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.DRIP_LAVA);
        }
        for (int i = 0; i < this.villagerAngry.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.VILLAGER_ANGRY);
        }
        for (int i = 0; i < this.villagerHappy.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.VILLAGER_HAPPY);
        }
        for (int i = 0; i < this.note.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.NOTE);
        }
        for (int i = 0; i < this.portal.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.PORTAL);
        }
        for (int i = 0; i < this.flame.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.FLAME);
        }
        for (int i = 0; i < this.lava.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.LAVA);
        }
        for (int i = 0; i < this.redStone.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.REDSTONE);
        }
        for (int i = 0; i < this.snowShovel.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.SNOW_SHOVEL);
        }
        for (int i = 0; i < this.slime.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.SLIME);
        }
        for (int i = 0; i < this.heart.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.HEART);
        }
        for (int i = 0; i < this.barrier.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.BARRIER);
        }
        for (int i = 0; i < this.fireworkSpark.getValue(); ++i) {
            AttackEffects.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.FIREWORKS_SPARK);
        }
    }
}
