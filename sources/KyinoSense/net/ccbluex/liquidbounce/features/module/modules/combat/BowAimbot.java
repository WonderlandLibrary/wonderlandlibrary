/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="BowAimbot", description="Automatically aims at players when using a bow.", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u000e\u001a\u0004\u0018\u00010\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\u0006\u0010\u0013\u001a\u00020\u0010J\b\u0010\u0014\u001a\u00020\u0015H\u0016J\u0010\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u0018H\u0007J\u0010\u0010\u0019\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u001aH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/BowAimbot;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "markValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "predictSizeValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "predictValue", "priorityValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "silentValue", "target", "Lnet/minecraft/entity/Entity;", "throughWallsValue", "getTarget", "throughWalls", "", "priorityMode", "", "hasTarget", "onDisable", "", "onRender3D", "event", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "KyinoClient"})
public final class BowAimbot
extends Module {
    private final BoolValue silentValue = new BoolValue("Silent", true);
    private final BoolValue predictValue = new BoolValue("Predict", true);
    private final BoolValue throughWallsValue = new BoolValue("ThroughWalls", false);
    private final FloatValue predictSizeValue = new FloatValue("PredictSize", 2.0f, 0.1f, 5.0f);
    private final ListValue priorityValue = new ListValue("Priority", new String[]{"Health", "Distance", "Direction"}, "Direction");
    private final BoolValue markValue = new BoolValue("Mark", true);
    private Entity target;

    @Override
    public void onDisable() {
        this.target = null;
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.target = null;
        EntityPlayerSP entityPlayerSP = BowAimbot.access$getMc$p$s1046033730().field_71439_g;
        Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc.thePlayer");
        ItemStack itemStack = entityPlayerSP.func_71011_bu();
        if ((itemStack != null ? itemStack.func_77973_b() : null) instanceof ItemBow) {
            Entity entity;
            Entity entity2 = this.getTarget((Boolean)this.throughWallsValue.get(), (String)this.priorityValue.get());
            if (entity2 == null) {
                return;
            }
            this.target = entity = entity2;
            RotationUtils.faceBow(this.target, (Boolean)this.silentValue.get(), (Boolean)this.predictValue.get(), ((Number)this.predictSizeValue.get()).floatValue());
        }
    }

    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (this.target != null && !StringsKt.equals((String)this.priorityValue.get(), "Multi", true) && ((Boolean)this.markValue.get()).booleanValue()) {
            RenderUtils.drawPlatform(this.target, new Color(37, 126, 255, 70));
        }
    }

    /*
     * WARNING - void declaration
     */
    private final Entity getTarget(boolean throughWalls, String priorityMode) {
        Entity entity;
        void $this$filterTo$iv$iv;
        List list = BowAimbot.access$getMc$p$s1046033730().field_71441_e.field_72996_f;
        Intrinsics.checkExpressionValueIsNotNull(list, "mc.theWorld.loadedEntityList");
        Iterable $this$filter$iv = list;
        boolean $i$f$filter = false;
        Iterable iterable = $this$filter$iv;
        Collection destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            Entity it = (Entity)element$iv$iv;
            boolean bl = false;
            if (!(it instanceof EntityLivingBase && EntityUtils.isSelected(it, true) && (throughWalls || BowAimbot.access$getMc$p$s1046033730().field_71439_g.func_70685_l(it)))) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        List targets = (List)destination$iv$iv;
        String string = priorityMode;
        $i$f$filter = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toUpperCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toUpperCase()");
        switch (string3) {
            case "DISTANCE": {
                Entity it;
                Object v3;
                Iterable $this$minBy$iv = targets;
                boolean $i$f$minBy = false;
                Iterator iterator$iv = $this$minBy$iv.iterator();
                if (!iterator$iv.hasNext()) {
                    v3 = null;
                } else {
                    Object minElem$iv = iterator$iv.next();
                    if (!iterator$iv.hasNext()) {
                        v3 = minElem$iv;
                    } else {
                        it = (Entity)minElem$iv;
                        boolean bl = false;
                        float minValue$iv = BowAimbot.access$getMc$p$s1046033730().field_71439_g.func_70032_d(it);
                        do {
                            Object e$iv = iterator$iv.next();
                            Entity it2 = (Entity)e$iv;
                            $i$a$-minBy-BowAimbot$getTarget$1 = false;
                            float v$iv = BowAimbot.access$getMc$p$s1046033730().field_71439_g.func_70032_d(it2);
                            if (Float.compare(minValue$iv, v$iv) <= 0) continue;
                            minElem$iv = e$iv;
                            minValue$iv = v$iv;
                        } while (iterator$iv.hasNext());
                        v3 = minElem$iv;
                    }
                }
                entity = v3;
                break;
            }
            case "DIRECTION": {
                Object v5;
                Entity it;
                Iterable $this$minBy$iv = targets;
                boolean $i$f$minBy = false;
                Iterator iterator$iv = $this$minBy$iv.iterator();
                if (!iterator$iv.hasNext()) {
                    v5 = null;
                } else {
                    Object minElem$iv = iterator$iv.next();
                    if (!iterator$iv.hasNext()) {
                        v5 = minElem$iv;
                    } else {
                        it = (Entity)minElem$iv;
                        boolean bl = false;
                        double minValue$iv = RotationUtils.getRotationDifference(it);
                        do {
                            Object e$iv = iterator$iv.next();
                            Entity it3 = (Entity)e$iv;
                            $i$a$-minBy-BowAimbot$getTarget$2 = false;
                            double v$iv = RotationUtils.getRotationDifference(it3);
                            if (Double.compare(minValue$iv, v$iv) <= 0) continue;
                            minElem$iv = e$iv;
                            minValue$iv = v$iv;
                        } while (iterator$iv.hasNext());
                        v5 = minElem$iv;
                    }
                }
                entity = v5;
                break;
            }
            case "HEALTH": {
                Object v6;
                Entity it;
                Iterable $this$minBy$iv = targets;
                boolean $i$f$minBy = false;
                Iterator iterator$iv = $this$minBy$iv.iterator();
                if (!iterator$iv.hasNext()) {
                    v6 = null;
                } else {
                    Object minElem$iv = iterator$iv.next();
                    if (!iterator$iv.hasNext()) {
                        v6 = minElem$iv;
                    } else {
                        it = (Entity)minElem$iv;
                        boolean bl = false;
                        Entity entity2 = it;
                        if (entity2 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.EntityLivingBase");
                        }
                        float minValue$iv = ((EntityLivingBase)entity2).func_110143_aJ();
                        do {
                            Object e$iv = iterator$iv.next();
                            Entity it4 = (Entity)e$iv;
                            $i$a$-minBy-BowAimbot$getTarget$3 = false;
                            Entity entity3 = it4;
                            if (entity3 == null) {
                                throw new TypeCastException("null cannot be cast to non-null type net.minecraft.entity.EntityLivingBase");
                            }
                            float v$iv = ((EntityLivingBase)entity3).func_110143_aJ();
                            if (Float.compare(minValue$iv, v$iv) <= 0) continue;
                            minElem$iv = e$iv;
                            minValue$iv = v$iv;
                        } while (iterator$iv.hasNext());
                        v6 = minElem$iv;
                    }
                }
                entity = v6;
                break;
            }
            default: {
                entity = null;
            }
        }
        return entity;
    }

    public final boolean hasTarget() {
        return this.target != null && BowAimbot.access$getMc$p$s1046033730().field_71439_g.func_70685_l(this.target);
    }

    public static final /* synthetic */ Minecraft access$getMc$p$s1046033730() {
        return MinecraftInstance.mc;
    }
}

