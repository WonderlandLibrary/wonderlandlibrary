/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemBow
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.modules.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Render3DEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.EntityUtils;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.RotationUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.ListValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Info(name="BowAimbot", spacedName="Bow Aimbot", description="Automatically aims at players when using a bow.", category=Category.COMBAT, cnName="\u5f13\u7bad\u7784\u51c6")
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u000e\u001a\u0004\u0018\u00010\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\u0006\u0010\u0013\u001a\u00020\u0010J\b\u0010\u0014\u001a\u00020\u0015H\u0016J\u0010\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u0018H\u0007J\u0010\u0010\u0019\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u001aH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2={"Lnet/dev/important/modules/module/modules/combat/BowAimbot;", "Lnet/dev/important/modules/module/Module;", "()V", "markValue", "Lnet/dev/important/value/BoolValue;", "predictSizeValue", "Lnet/dev/important/value/FloatValue;", "predictValue", "priorityValue", "Lnet/dev/important/value/ListValue;", "silentValue", "target", "Lnet/minecraft/entity/Entity;", "throughWallsValue", "getTarget", "throughWalls", "", "priorityMode", "", "hasTarget", "onDisable", "", "onRender3D", "event", "Lnet/dev/important/event/Render3DEvent;", "onUpdate", "Lnet/dev/important/event/UpdateEvent;", "LiquidBounce"})
public final class BowAimbot
extends Module {
    @NotNull
    private final BoolValue silentValue = new BoolValue("Silent", true);
    @NotNull
    private final BoolValue predictValue = new BoolValue("Predict", true);
    @NotNull
    private final BoolValue throughWallsValue = new BoolValue("ThroughWalls", false);
    @NotNull
    private final FloatValue predictSizeValue = new FloatValue("PredictSize", 2.0f, 0.1f, 5.0f, "m");
    @NotNull
    private final ListValue priorityValue;
    @NotNull
    private final BoolValue markValue;
    @Nullable
    private Entity target;

    public BowAimbot() {
        String[] stringArray = new String[]{"Health", "Distance", "Direction"};
        this.priorityValue = new ListValue("Priority", stringArray, "Direction");
        this.markValue = new BoolValue("Mark", true);
    }

    @Override
    public void onDisable() {
        this.target = null;
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkNotNullParameter(event, "event");
        this.target = null;
        ItemStack itemStack = MinecraftInstance.mc.field_71439_g.func_71011_bu();
        if ((itemStack == null ? null : itemStack.func_77973_b()) instanceof ItemBow) {
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
        Intrinsics.checkNotNullParameter(event, "event");
        if (this.target != null && !StringsKt.equals((String)this.priorityValue.get(), "Multi", true) && ((Boolean)this.markValue.get()).booleanValue()) {
            RenderUtils.drawPlatform(this.target, new Color(37, 126, 255, 70));
        }
    }

    private final Entity getTarget(boolean throughWalls, String priorityMode) {
        Entity entity;
        Locale $this$filterTo$iv$iv;
        Object object = MinecraftInstance.mc.field_71441_e.field_72996_f;
        Intrinsics.checkNotNullExpressionValue(object, "mc.theWorld.loadedEntityList");
        Iterable $this$filter$iv = (Iterable)object;
        boolean $i$f$filter = false;
        Iterable iterable = $this$filter$iv;
        Object destination$iv$iv = new ArrayList();
        boolean $i$f$filterTo = false;
        Iterator iterator2 = $this$filterTo$iv$iv.iterator();
        while (iterator2.hasNext()) {
            Object element$iv$iv = iterator2.next();
            Entity it = (Entity)element$iv$iv;
            boolean bl = false;
            boolean bl2 = it instanceof EntityLivingBase && EntityUtils.isSelected(it, true) && (throughWalls || MinecraftInstance.mc.field_71439_g.func_70685_l(it));
            if (!bl2) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        List targets = (List)destination$iv$iv;
        $this$filterTo$iv$iv = Locale.getDefault();
        Intrinsics.checkNotNullExpressionValue($this$filterTo$iv$iv, "getDefault()");
        destination$iv$iv = priorityMode.toUpperCase($this$filterTo$iv$iv);
        Intrinsics.checkNotNullExpressionValue(destination$iv$iv, "this as java.lang.String).toUpperCase(locale)");
        switch (destination$iv$iv) {
            case "DISTANCE": {
                Entity it;
                Object v1;
                Iterable $this$minByOrNull$iv = targets;
                boolean $i$f$minByOrNull = false;
                Iterator iterator$iv = $this$minByOrNull$iv.iterator();
                if (!iterator$iv.hasNext()) {
                    v1 = null;
                } else {
                    Object minElem$iv = iterator$iv.next();
                    if (!iterator$iv.hasNext()) {
                        v1 = minElem$iv;
                    } else {
                        it = (Entity)minElem$iv;
                        boolean bl = false;
                        float minValue$iv = MinecraftInstance.mc.field_71439_g.func_70032_d(it);
                        do {
                            Object e$iv = iterator$iv.next();
                            Entity it2 = (Entity)e$iv;
                            $i$a$-minByOrNull-BowAimbot$getTarget$1 = false;
                            float v$iv = MinecraftInstance.mc.field_71439_g.func_70032_d(it2);
                            if (Float.compare(minValue$iv, v$iv) <= 0) continue;
                            minElem$iv = e$iv;
                            minValue$iv = v$iv;
                        } while (iterator$iv.hasNext());
                        v1 = minElem$iv;
                    }
                }
                entity = v1;
                break;
            }
            case "DIRECTION": {
                Object v3;
                Entity it;
                Iterable $this$minByOrNull$iv = targets;
                boolean $i$f$minByOrNull = false;
                Iterator iterator$iv = $this$minByOrNull$iv.iterator();
                if (!iterator$iv.hasNext()) {
                    v3 = null;
                } else {
                    Object minElem$iv = iterator$iv.next();
                    if (!iterator$iv.hasNext()) {
                        v3 = minElem$iv;
                    } else {
                        it = (Entity)minElem$iv;
                        boolean bl = false;
                        double minValue$iv = RotationUtils.getRotationDifference(it);
                        do {
                            Object e$iv = iterator$iv.next();
                            Entity it3 = (Entity)e$iv;
                            $i$a$-minByOrNull-BowAimbot$getTarget$2 = false;
                            double v$iv = RotationUtils.getRotationDifference(it3);
                            if (Double.compare(minValue$iv, v$iv) <= 0) continue;
                            minElem$iv = e$iv;
                            minValue$iv = v$iv;
                        } while (iterator$iv.hasNext());
                        v3 = minElem$iv;
                    }
                }
                entity = v3;
                break;
            }
            case "HEALTH": {
                Object v4;
                Entity it;
                Iterable $this$minByOrNull$iv = targets;
                boolean $i$f$minByOrNull = false;
                Iterator iterator$iv = $this$minByOrNull$iv.iterator();
                if (!iterator$iv.hasNext()) {
                    v4 = null;
                } else {
                    Object minElem$iv = iterator$iv.next();
                    if (!iterator$iv.hasNext()) {
                        v4 = minElem$iv;
                    } else {
                        it = (Entity)minElem$iv;
                        boolean bl = false;
                        Entity entity2 = it;
                        if (entity2 == null) {
                            throw new NullPointerException("null cannot be cast to non-null type net.minecraft.entity.EntityLivingBase");
                        }
                        float minValue$iv = ((EntityLivingBase)entity2).func_110143_aJ();
                        do {
                            Object e$iv = iterator$iv.next();
                            Entity it4 = (Entity)e$iv;
                            $i$a$-minByOrNull-BowAimbot$getTarget$3 = false;
                            Entity entity3 = it4;
                            if (entity3 == null) {
                                throw new NullPointerException("null cannot be cast to non-null type net.minecraft.entity.EntityLivingBase");
                            }
                            float v$iv = ((EntityLivingBase)entity3).func_110143_aJ();
                            if (Float.compare(minValue$iv, v$iv) <= 0) continue;
                            minElem$iv = e$iv;
                            minValue$iv = v$iv;
                        } while (iterator$iv.hasNext());
                        v4 = minElem$iv;
                    }
                }
                entity = v4;
                break;
            }
            default: {
                entity = null;
            }
        }
        return entity;
    }

    public final boolean hasTarget() {
        return this.target != null && MinecraftInstance.mc.field_71439_g.func_70685_l(this.target);
    }
}

