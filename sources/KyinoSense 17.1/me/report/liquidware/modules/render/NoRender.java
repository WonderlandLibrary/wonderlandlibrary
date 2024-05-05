/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.item.EntityArmorStand
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.player.EntityPlayer
 *  org.jetbrains.annotations.NotNull
 */
package me.report.liquidware.modules.render;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.MotionEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.extensions.PlayerExtensionKt;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="NoRender", description="Increase fps by decreasing or stop rendering visible entities.", category=ModuleCategory.RENDER)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\u0010\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014H\u0007J\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0011\u0010\b\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0006R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2={"Lme/report/liquidware/modules/render/NoRender;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "allValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "getAllValue", "()Lnet/ccbluex/liquidbounce/value/BoolValue;", "animalsValue", "armorStandValue", "getArmorStandValue", "autoResetValue", "itemsValue", "maxRenderRange", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "mobsValue", "playersValue", "onDisable", "", "onMotion", "event", "Lnet/ccbluex/liquidbounce/event/MotionEvent;", "shouldStopRender", "", "entity", "Lnet/minecraft/entity/Entity;", "KyinoClient"})
public final class NoRender
extends Module {
    private final BoolValue itemsValue = new BoolValue("Items", true);
    private final BoolValue playersValue = new BoolValue("Players", true);
    private final BoolValue mobsValue = new BoolValue("Mobs", true);
    private final BoolValue animalsValue = new BoolValue("Animals", true);
    @NotNull
    private final BoolValue armorStandValue = new BoolValue("ArmorStand", true);
    @NotNull
    private final BoolValue allValue = new BoolValue("All", true);
    private final BoolValue autoResetValue = new BoolValue("AutoReset", true);
    private final FloatValue maxRenderRange = new FloatValue("MaxRenderRange", 4.0f, 0.0f, 16.0f);

    @NotNull
    public final BoolValue getArmorStandValue() {
        return this.armorStandValue;
    }

    @NotNull
    public final BoolValue getAllValue() {
        return this.allValue;
    }

    @EventTarget
    public final void onMotion(@NotNull MotionEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        for (Entity en : NoRender.access$getMc$p$s1046033730().field_71441_e.field_72996_f) {
            Entity entity;
            if (en == null) {
                Intrinsics.throwNpe();
            }
            if (entity == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.Entity");
            }
            if (this.shouldStopRender(entity)) {
                entity.field_70155_l = 0.0;
                continue;
            }
            if (!((Boolean)this.autoResetValue.get()).booleanValue()) continue;
            entity.field_70155_l = 1.0;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean shouldStopRender(@NotNull Entity entity) {
        Intrinsics.checkParameterIsNotNull(entity, "entity");
        if (!(((Boolean)this.allValue.get()).booleanValue() || ((Boolean)this.itemsValue.get()).booleanValue() && entity instanceof EntityItem || ((Boolean)this.playersValue.get()).booleanValue() && entity instanceof EntityPlayer || ((Boolean)this.mobsValue.get()).booleanValue() && EntityUtils.isMob(entity) || ((Boolean)this.animalsValue.get()).booleanValue() && EntityUtils.isAnimal(entity))) {
            if ((Boolean)this.armorStandValue.get() == false) return false;
            if (!(entity instanceof EntityArmorStand)) return false;
        }
        EntityPlayerSP entityPlayerSP = NoRender.access$getMc$p$s1046033730().field_71439_g;
        if (entityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (!(Intrinsics.areEqual(entity, entityPlayerSP) ^ true)) return false;
        EntityPlayerSP entityPlayerSP2 = NoRender.access$getMc$p$s1046033730().field_71439_g;
        if (entityPlayerSP2 == null) {
            Intrinsics.throwNpe();
        }
        if (!((float)PlayerExtensionKt.getDistanceToEntityBox((Entity)entityPlayerSP2, entity) > ((Number)this.maxRenderRange.get()).floatValue())) return false;
        return true;
    }

    @Override
    public void onDisable() {
        for (Entity en : NoRender.access$getMc$p$s1046033730().field_71441_e.field_72996_f) {
            Entity entity;
            if (en == null) {
                Intrinsics.throwNpe();
            }
            if (entity == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.Entity");
            }
            EntityPlayerSP entityPlayerSP = NoRender.access$getMc$p$s1046033730().field_71439_g;
            if (entityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            if (!(Intrinsics.areEqual(entity, entityPlayerSP) ^ true) || !(entity.field_70155_l <= 0.0)) continue;
            entity.field_70155_l = 1.0;
        }
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

