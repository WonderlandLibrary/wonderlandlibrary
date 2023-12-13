package koks.module.visual;

import koks.api.event.Event;
import koks.api.manager.value.annotation.Value;
import koks.api.registry.module.Module;
import koks.api.registry.module.ModuleRegistry;
import koks.event.AttackEffectEvent;
import koks.module.combat.KillAura;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;

@Module.Info(name = "AttackEffects", description = "You can change your attack effects", category = Module.Category.VISUAL)
public class AttackEffects extends Module{
    @Value(name = "Explosion", minimum = 0, maximum = 20)
    int explosion = 0;

    @Value(name = "WaterSplash", minimum = 0, maximum = 20)
    int waterSplash = 0;

    @Value(name = "WaterWake", minimum = 0, maximum = 20)
    int waterWake = 0;

    @Value(name = "WaterDrop", minimum = 0, maximum = 20)
    int waterDrop = 0;

    @Value(name = "SuspDepth", minimum = 0, maximum = 20)
    int suspendedDepth = 0;

    @Value(name = "Crit", minimum = 0, maximum = 20)
    int crit = 0;

    @Value(name = "CtitMagic", minimum = 0, maximum = 20)
    int critMagic = 0;

    @Value(name = "SpellInstant", minimum = 0, maximum = 20)
    int spellInstant = 0;

    @Value(name = "SmokeNormal", minimum = 0, maximum = 20)
    int smokeNormal = 0;

    @Value(name = "SpellMob", minimum = 0, maximum = 20)
    int spellMob = 0;

    @Value(name = "SmokeLarge", minimum = 0, maximum = 20)
    int smokeLarge = 0;

    @Value(name = "SpellMAmbient", minimum = 0, maximum = 20)
    int spellMobAmbient = 0;

    @Value(name = "SpellWitch", minimum = 0, maximum = 20)
    int spellWitch = 0;

    @Value(name = "DripWater", minimum = 0, maximum = 20)
    int dripWater = 0;

    @Value(name = "DripLava", minimum = 0, maximum = 20)
    int dripLava = 0;

    @Value(name = "VillagerAngry", minimum = 0, maximum = 20)
    int villagerAngry = 0;

    @Value(name = "VillagerHappy", minimum = 0, maximum = 20)
    int villagerHappy = 0;

    @Value(name = "Note", minimum = 0, maximum = 20)
    int note = 0;

    @Value(name = "Portal", minimum = 0, maximum = 20)
    int portal = 0;

    @Value(name = "Flame", minimum = 0, maximum = 20)
    int flame = 0;

    @Value(name = "Lava", minimum = 0, maximum = 20)
    int lava = 0;

    @Value(name = "RedStone", minimum = 0, maximum = 20)
    int redStone = 0;

    @Value(name = "SnowShovel", minimum = 0, maximum = 20)
    int snowShovel = 0;

    @Value(name = "Slime", minimum = 0, maximum = 20)
    int slime = 0;

    @Value(name = "Heart", minimum = 0, maximum = 20)
    int heart = 0;

    @Value(name = "Barrier", minimum = 0, maximum = 20)
    int barrier = 0;

    @Value(name = "FireworkSpark", minimum = 0, maximum = 20)
    int fireworkSpark = 0;

    private KillAura killAura;


    @Override
    @Event.Info
    public void onEvent(Event event) {
        if (event instanceof AttackEffectEvent) {
            Entity entity = mc.objectMouseOver.entityHit;
            if (this.killAura != null && this.killAura.isToggled() && KillAura.curEntity != null) {
                entity = KillAura.curEntity;
            }
            if (entity == null) {
                return;
            }
            for (int i = 0; i < explosion; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.EXPLOSION_NORMAL);
                event.setCanceled(true);
            }
            for (int i = 0; i < waterSplash; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.WATER_SPLASH);
                event.setCanceled(true);
            }
            for (int i = 0; i < waterWake; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.WATER_WAKE);
                event.setCanceled(true);
            }
            for (int i = 0; i < waterDrop; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.WATER_DROP);
                event.setCanceled(true);
            }
            for (int i = 0; i < suspendedDepth; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.SUSPENDED_DEPTH);
                event.setCanceled(true);
            }
            for (int i = 0; i < crit; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT);
                event.setCanceled(true);
            }
            for (int i = 0; i < critMagic; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.CRIT_MAGIC);
                event.setCanceled(true);
            }
            for (int i = 0; i < smokeNormal; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.SMOKE_NORMAL);
                event.setCanceled(true);
            }
            for (int i = 0; i < smokeLarge; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.SMOKE_LARGE);
                event.setCanceled(true);
            }
            for (int i = 0; i < spellInstant; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.SPELL_INSTANT);
                event.setCanceled(true);
            }
            for (int i = 0; i < spellMob; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.SPELL_MOB);
                event.setCanceled(true);
            }
            for (int i = 0; i < spellMobAmbient; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.SPELL_MOB_AMBIENT);
                event.setCanceled(true);
            }
            for (int i = 0; i < spellWitch; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.SPELL_WITCH);
                event.setCanceled(true);
            }
            for (int i = 0; i < dripWater; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.DRIP_WATER);
                event.setCanceled(true);
            }
            for (int i = 0; i < dripLava; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.DRIP_LAVA);
                event.setCanceled(true);
            }
            for (int i = 0; i < villagerAngry; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.VILLAGER_ANGRY);
                event.setCanceled(true);
            }
            for (int i = 0; i < villagerHappy; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.VILLAGER_HAPPY);
                event.setCanceled(true);
            }
            for (int i = 0; i < note; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.NOTE);
                event.setCanceled(true);
            }
            for (int i = 0; i < portal; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.PORTAL);
                event.setCanceled(true);
            }
            for (int i = 0; i < flame; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.FLAME);
                event.setCanceled(true);
            }
            for (int i = 0; i < lava; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.LAVA);
                event.setCanceled(true);
            }
            for (int i = 0; i < redStone; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.REDSTONE);
                event.setCanceled(true);
            }
            for (int i = 0; i < snowShovel; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.SNOW_SHOVEL);
                event.setCanceled(true);
            }
            for (int i = 0; i < slime; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.SLIME);
                event.setCanceled(true);
            }
            for (int i = 0; i < heart; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.HEART);
                event.setCanceled(true);
            }
            for (int i = 0; i < barrier; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.BARRIER);
                event.setCanceled(true);
            }
            for (int i = 0; i < fireworkSpark; i++) {
                mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.FIREWORKS_SPARK);
                event.setCanceled(true);
            }
        }
    }

    @Override
    public void onEnable() {
        this.killAura = ModuleRegistry.getModule(KillAura.class);
    }

    @Override
    public void onDisable() {

    }
}
