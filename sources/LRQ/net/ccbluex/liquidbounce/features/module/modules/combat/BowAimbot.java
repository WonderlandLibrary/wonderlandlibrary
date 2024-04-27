/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import kotlin.TypeCastException;
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

@ModuleInfo(name="BowAimbot", description="Automatically aims at players when using a bow.", category=ModuleCategory.COMBAT)
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
    public final void onUpdate(UpdateEvent event) {
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
    public final void onRender3D(Render3DEvent event) {
        if (this.target != null && !StringsKt.equals((String)((String)this.priorityValue.get()), (String)"Multi", (boolean)true) && ((Boolean)this.markValue.get()).booleanValue()) {
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
        var6_8 = $this$filter$iv;
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
        var4_3 = priorityMode;
        $i$f$filter = false;
        v3 = var4_3;
        if (v3 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        var4_3 = v3.toUpperCase();
        tmp = -1;
        switch (var4_3.hashCode()) {
            case 1071086581: {
                if (!var4_3.equals("DISTANCE")) break;
                tmp = 1;
                break;
            }
            case 1824003935: {
                if (!var4_3.equals("DIRECTION")) break;
                tmp = 2;
                break;
            }
            case 2127033948: {
                if (!var4_3.equals("HEALTH")) break;
                tmp = 3;
                break;
            }
        }
        switch (tmp) {
            case 1: {
                $this$minBy$iv = targets;
                $i$f$minBy = false;
                iterator$iv = $this$minBy$iv.iterator();
                if (!iterator$iv.hasNext()) {
                    v4 = null;
                } else {
                    minElem$iv = iterator$iv.next();
                    if (!iterator$iv.hasNext()) {
                        v4 = minElem$iv;
                    } else {
                        it = (IEntity)minElem$iv;
                        $i$a$-minBy-BowAimbot$getTarget$1 = false;
                        v5 = MinecraftInstance.mc.getThePlayer();
                        if (v5 == null) {
                            Intrinsics.throwNpe();
                        }
                        minValue$iv = v5.getDistanceToEntity(it);
                        do {
                            e$iv = iterator$iv.next();
                            it = (IEntity)e$iv;
                            $i$a$-minBy-BowAimbot$getTarget$1 = false;
                            v6 = MinecraftInstance.mc.getThePlayer();
                            if (v6 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (Float.compare(minValue$iv, v$iv = v6.getDistanceToEntity(it)) <= 0) continue;
                            minElem$iv = e$iv;
                            minValue$iv = v$iv;
                        } while (iterator$iv.hasNext());
                        v4 = minElem$iv;
                    }
                }
                v7 = v4;
                break;
            }
            case 2: {
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
                        v8 = minElem$iv;
                    }
                }
                v7 = v8;
                break;
            }
            case 3: {
                $this$minBy$iv = targets;
                $i$f$minBy = false;
                iterator$iv = $this$minBy$iv.iterator();
                if (!iterator$iv.hasNext()) {
                    v9 = null;
                } else {
                    minElem$iv = iterator$iv.next();
                    if (!iterator$iv.hasNext()) {
                        v9 = minElem$iv;
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
                        v9 = minElem$iv;
                    }
                }
                v7 = v9;
                break;
            }
            default: {
                v7 = null;
            }
        }
        return v7;
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

