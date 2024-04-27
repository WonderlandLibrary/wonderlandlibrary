/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.text.StringsKt
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.effect.EntityLightningBolt
 *  net.minecraft.util.EnumParticleTypes
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.features.module.modules.autumn;

import kotlin.TypeCastException;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="AttackParticles", category=ModuleCategory.AUTUMN, description="\u6253\u51fb\u7279\u6027")
public final class AttackParticles
extends Module {
    private final Minecraft oldMc;
    private final ListValue particlesMode;
    private final ListValue modeValue = new ListValue("RenderMode", new String[]{"Kill", "Keep"}, "Kill");

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent updateEvent) {
        EnumParticleTypes enumParticleTypes = EnumParticleTypes.HEART;
        Object object = (String)this.particlesMode.get();
        boolean bl = false;
        String string = object;
        if (string == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        switch (string.toLowerCase()) {
            case "heart": {
                enumParticleTypes = EnumParticleTypes.HEART;
                break;
            }
            case "barrier": {
                enumParticleTypes = EnumParticleTypes.BARRIER;
                break;
            }
            case "lava": {
                enumParticleTypes = EnumParticleTypes.LAVA;
                break;
            }
            case "smoke": {
                enumParticleTypes = EnumParticleTypes.SMOKE_NORMAL;
                break;
            }
            case "mobappearance": {
                enumParticleTypes = EnumParticleTypes.MOB_APPEARANCE;
                break;
            }
            case "slime": {
                enumParticleTypes = EnumParticleTypes.SLIME;
                break;
            }
            case "flame": {
                enumParticleTypes = EnumParticleTypes.FLAME;
                break;
            }
            case "explode": {
                enumParticleTypes = EnumParticleTypes.EXPLOSION_NORMAL;
                break;
            }
            case "largesmoke": {
                enumParticleTypes = EnumParticleTypes.SMOKE_LARGE;
                break;
            }
            case "largeexplode": {
                enumParticleTypes = EnumParticleTypes.EXPLOSION_LARGE;
                break;
            }
            case "hugeexplode": {
                enumParticleTypes = EnumParticleTypes.EXPLOSION_HUGE;
                break;
            }
        }
        if (((String)this.modeValue.get()).equals("Kill")) {
            Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class);
            if (module == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
            }
            object = ((KillAura)module).getTarget();
            if (object != null && (double)object.getHealth() < 0.1) {
                if (StringsKt.equals((String)((String)this.particlesMode.get()), (String)"lightning", (boolean)true)) {
                    EntityLightningBolt entityLightningBolt = new EntityLightningBolt((World)this.oldMc.field_71441_e, object.getPosX(), object.getPosY(), object.getPosZ(), true);
                    this.oldMc.field_71441_e.func_73027_a(-1, (Entity)entityLightningBolt);
                    MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.impact", 1.0f);
                    MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.thunder", 1.0f);
                } else {
                    this.oldMc.field_71438_f.func_180442_a(enumParticleTypes.func_179348_c(), false, object.getPosX(), object.getPosY(), object.getPosZ(), 1.0, 1.0, 1.0, new int[]{new int[]{1}.length});
                }
            }
        }
        if (((String)this.modeValue.get()).equals("Keep")) {
            Module module = LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class);
            if (module == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.features.module.modules.combat.KillAura");
            }
            object = ((KillAura)module).getTarget();
            if (object != null) {
                if (StringsKt.equals((String)((String)this.particlesMode.get()), (String)"lightning", (boolean)true)) {
                    EntityLightningBolt entityLightningBolt = new EntityLightningBolt((World)this.oldMc.field_71441_e, object.getPosX(), object.getPosY(), object.getPosZ(), true);
                    this.oldMc.field_71441_e.func_73027_a(-1, (Entity)entityLightningBolt);
                    MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.impact", 1.0f);
                    MinecraftInstance.mc.getSoundHandler().playSound("entity.lightning.thunder", 1.0f);
                } else {
                    this.oldMc.field_71438_f.func_180442_a(enumParticleTypes.func_179348_c(), false, object.getPosX(), object.getPosY(), object.getPosZ(), 1.0, 1.0, 1.0, new int[]{new int[]{1}.length});
                }
            }
        }
    }

    public AttackParticles() {
        this.oldMc = Minecraft.func_71410_x();
        this.particlesMode = new ListValue("Mode", new String[]{"lightning", "heart", "barrier", "lava", "smoke", "mobappearance", "slime", "flame", "explode", "hugeexplode", "largeexplode", "largesmoke"}, "lightning");
    }
}

