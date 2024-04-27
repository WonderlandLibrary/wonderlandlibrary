package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
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
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="BowAimbot", description="Automatically aims at players when using a bow.", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000L\n\n\n\b\n\n\u0000\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\b\n\n\b\n\n\u0000\n\n\u0000\b\u000020B¢J0\f2020HJ0J\b0HJ020HJ020HR0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\b0\tX¢\n\u0000R\n0X¢\n\u0000R0\fX¢\n\u0000R\r0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/BowAimbot;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "markValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "predictSizeValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "predictValue", "priorityValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "silentValue", "target", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "throughWallsValue", "getTarget", "throughWalls", "", "priorityMode", "", "hasTarget", "onDisable", "", "onRender3D", "event", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "Pride"})
public final class BowAimbot
extends Module {
    private final BoolValue silentValue = new BoolValue("Silent", true);
    private final BoolValue predictValue = new BoolValue("Predict", true);
    private final BoolValue throughWallsValue = new BoolValue("ThroughWalls", false);
    private final FloatValue predictSizeValue = new FloatValue("PredictSize", 2.0f, 0.1f, 5.0f);
    private final ListValue priorityValue = new ListValue("Priority", new String[]{"Health", "Distance", "Direction"}, "Direction");
    private final BoolValue markValue = new BoolValue("Mark", true);
    private IEntity target;

    @Override
    public void onDisable() {
        this.target = null;
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.target = null;
        Object object = MinecraftInstance.mc.getThePlayer();
        if (MinecraftInstance.classProvider.isItemBow(object != null && (object = object.getItemInUse()) != null ? object.getItem() : null)) {
            IEntity entity;
            IEntity iEntity = this.getTarget((Boolean)this.throughWallsValue.get(), (String)this.priorityValue.get());
            if (iEntity == null) {
                return;
            }
            this.target = entity = iEntity;
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
     * Unable to fully structure code
     */
    private final IEntity getTarget(boolean throughWalls, String priorityMode) {
        v0 = MinecraftInstance.mc.getTheWorld();
        if (v0 == null) {
            Intrinsics.throwNpe();
        }
        $this$filter$iv = v0.getLoadedEntityList();
        $i$f$filter = false;
        var6_5 = $this$filter$iv;
        destination$iv$iv = new ArrayList<E>();
        $i$f$filterTo = false;
        for (T element$iv$iv : $this$filterTo$iv$iv) {
            it = (IEntity)element$iv$iv;
            $i$a$-filter-BowAimbot$getTarget$targets$1 = false;
            if (!MinecraftInstance.classProvider.isEntityLivingBase(it) || !EntityUtils.isSelected(it, true)) ** GOTO lbl-1000
            if (throughWalls) ** GOTO lbl-1000
            v1 = MinecraftInstance.mc.getThePlayer();
            if (v1 == null) {
                Intrinsics.throwNpe();
            }
            if (v1.canEntityBeSeen(it)) lbl-1000:
            // 2 sources

            {
                v2 = true;
            } else lbl-1000:
            // 2 sources

            {
                v2 = false;
            }
            if (!v2) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        targets = (List)destination$iv$iv;
        if (StringsKt.equals(priorityMode, "distance", true)) {
            $this$minBy$iv = targets;
            $i$f$minBy = false;
            iterator$iv = $this$minBy$iv.iterator();
            if (!iterator$iv.hasNext()) {
                v3 = null;
            } else {
                minElem$iv = iterator$iv.next();
                if (!iterator$iv.hasNext()) {
                    v3 = minElem$iv;
                } else {
                    it = (IEntity)minElem$iv;
                    $i$a$-minBy-BowAimbot$getTarget$1 = false;
                    v4 = MinecraftInstance.mc.getThePlayer();
                    if (v4 == null) {
                        Intrinsics.throwNpe();
                    }
                    minValue$iv = v4.getDistanceToEntity(it);
                    do {
                        e$iv = iterator$iv.next();
                        it = (IEntity)e$iv;
                        $i$a$-minBy-BowAimbot$getTarget$1 = false;
                        v5 = MinecraftInstance.mc.getThePlayer();
                        if (v5 == null) {
                            Intrinsics.throwNpe();
                        }
                        if (Float.compare(minValue$iv, v$iv = v5.getDistanceToEntity(it)) <= 0) continue;
                        minElem$iv = e$iv;
                        minValue$iv = v$iv;
                    } while (iterator$iv.hasNext());
                    v3 = minElem$iv;
                }
            }
            v6 = v3;
        } else if (StringsKt.equals(priorityMode, "direction", true)) {
            $this$minBy$iv = targets;
            $i$f$minBy = false;
            iterator$iv = $this$minBy$iv.iterator();
            if (!iterator$iv.hasNext()) {
                v7 = null;
            } else {
                minElem$iv = iterator$iv.next();
                if (!iterator$iv.hasNext()) {
                    v7 = minElem$iv;
                } else {
                    it = (IEntity)minElem$iv;
                    $i$a$-minBy-BowAimbot$getTarget$2 = false;
                    minValue$iv = RotationUtils.getRotationDifference(it);
                    do {
                        e$iv = iterator$iv.next();
                        it = (IEntity)e$iv;
                        $i$a$-minBy-BowAimbot$getTarget$2 = false;
                        v$iv = RotationUtils.getRotationDifference(it);
                        if (Double.compare(minValue$iv, v$iv) <= 0) continue;
                        minElem$iv = e$iv;
                        minValue$iv = v$iv;
                    } while (iterator$iv.hasNext());
                    v7 = minElem$iv;
                }
            }
            v6 = v7;
        } else if (StringsKt.equals(priorityMode, "health", true)) {
            $this$minBy$iv = targets;
            $i$f$minBy = false;
            iterator$iv = $this$minBy$iv.iterator();
            if (!iterator$iv.hasNext()) {
                v8 = null;
            } else {
                minElem$iv = iterator$iv.next();
                if (!iterator$iv.hasNext()) {
                    v8 = minElem$iv;
                } else {
                    it = (IEntity)minElem$iv;
                    $i$a$-minBy-BowAimbot$getTarget$3 = false;
                    minValue$iv = it.asEntityLivingBase().getHealth();
                    do {
                        e$iv = iterator$iv.next();
                        it = (IEntity)e$iv;
                        $i$a$-minBy-BowAimbot$getTarget$3 = false;
                        v$iv = it.asEntityLivingBase().getHealth();
                        if (Float.compare(minValue$iv, v$iv) <= 0) continue;
                        minElem$iv = e$iv;
                        minValue$iv = v$iv;
                    } while (iterator$iv.hasNext());
                    v8 = minElem$iv;
                }
            }
            v6 = v8;
        } else {
            v6 = null;
        }
        return v6;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean hasTarget() {
        if (this.target == null) return false;
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        IEntity iEntity = this.target;
        if (iEntity == null) {
            Intrinsics.throwNpe();
        }
        if (!iEntityPlayerSP.canEntityBeSeen(iEntity)) return false;
        return true;
    }
}
