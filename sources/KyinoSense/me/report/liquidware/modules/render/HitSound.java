/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.client.particle.EffectRenderer
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.effect.EntityLightningBolt
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C02PacketUseEntity
 *  net.minecraft.network.play.client.C02PacketUseEntity$Action
 *  net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity
 *  net.minecraft.util.EnumParticleTypes
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.report.liquidware.modules.render;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ModuleInfo(name="HitSound", category=ModuleCategory.RENDER, description="qwq", array=false)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0007R\u0013\u0010\u0003\u001a\u0004\u0018\u00010\u0001\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0005R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000b\u00a8\u0006\u0010"}, d2={"Lme/report/liquidware/modules/render/HitSound;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "killAura", "getKillAura", "()Lnet/ccbluex/liquidbounce/features/module/Module;", "target", "Lnet/minecraft/entity/EntityLivingBase;", "getTarget", "()Lnet/minecraft/entity/EntityLivingBase;", "setTarget", "(Lnet/minecraft/entity/EntityLivingBase;)V", "onPacket", "", "e", "Lnet/ccbluex/liquidbounce/event/PacketEvent;", "KyinoClient"})
public final class HitSound
extends Module {
    @Nullable
    private final Module killAura = LiquidBounce.INSTANCE.getModuleManager().getModule(KillAura.class);
    @Nullable
    private EntityLivingBase target;

    @Nullable
    public final Module getKillAura() {
        return this.killAura;
    }

    @Nullable
    public final EntityLivingBase getTarget() {
        return this.target;
    }

    public final void setTarget(@Nullable EntityLivingBase entityLivingBase) {
        this.target = entityLivingBase;
    }

    @EventTarget
    public final void onPacket(@NotNull PacketEvent e) {
        block4: {
            Intrinsics.checkParameterIsNotNull(e, "e");
            Packet<?> packet = e.getPacket();
            if (!(packet instanceof C02PacketUseEntity) || ((C02PacketUseEntity)packet).func_149565_c() != C02PacketUseEntity.Action.ATTACK) break block4;
            Minecraft minecraft = HitSound.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft, "mc");
            minecraft.func_147114_u().func_147292_a(new S2CPacketSpawnGlobalEntity((Entity)new EntityLightningBolt((World)HitSound.access$getMc$p$s1046033730().field_71441_e, ((C02PacketUseEntity)packet).func_149564_a((World)((World)HitSound.access$getMc$p$s1046033730().field_71441_e)).field_70165_t, ((C02PacketUseEntity)packet).func_149564_a((World)((World)HitSound.access$getMc$p$s1046033730().field_71441_e)).field_70163_u, ((C02PacketUseEntity)packet).func_149564_a((World)((World)HitSound.access$getMc$p$s1046033730().field_71441_e)).field_70161_v)));
            Minecraft minecraft2 = HitSound.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft2, "mc");
            minecraft2.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("random.explode"), (float)1.0f));
            Minecraft minecraft3 = HitSound.access$getMc$p$s1046033730();
            Intrinsics.checkExpressionValueIsNotNull(minecraft3, "mc");
            minecraft3.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147674_a((ResourceLocation)new ResourceLocation("ambient.weather.thunder"), (float)1.0f));
            EntityLivingBase entityLivingBase = this.target;
            if (entityLivingBase != null) {
                EntityLivingBase entityLivingBase2 = entityLivingBase;
                boolean bl = false;
                boolean bl2 = false;
                EntityLivingBase it = entityLivingBase2;
                boolean bl3 = false;
                EffectRenderer effectRenderer = HitSound.access$getMc$p$s1046033730().field_71452_i;
                int n = EnumParticleTypes.LAVA.func_179348_c();
                EntityLivingBase entityLivingBase3 = this.target;
                if (entityLivingBase3 == null) {
                    Intrinsics.throwNpe();
                }
                double d = entityLivingBase3.field_70165_t;
                EntityLivingBase entityLivingBase4 = this.target;
                if (entityLivingBase4 == null) {
                    Intrinsics.throwNpe();
                }
                double d2 = entityLivingBase4.field_70163_u;
                EntityLivingBase entityLivingBase5 = this.target;
                if (entityLivingBase5 == null) {
                    Intrinsics.throwNpe();
                }
                effectRenderer.func_178927_a(n, d, d2, entityLivingBase5.field_70161_v, 0.0, 0.0, 0.0, new int[0]);
            }
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

