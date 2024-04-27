/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.module.modules.combat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;

@ModuleInfo(name="BowAimbot", description="Automatically aims at players when using a bow.", category=ModuleCategory.COMBAT)
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u000e\u001a\u0004\u0018\u00010\f2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J\u0006\u0010\u0013\u001a\u00020\u0010J\b\u0010\u0014\u001a\u00020\u0015H\u0016J\u0010\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u0018H\u0007J\u0010\u0010\u0019\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u001aH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/combat/BowAimbot;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "markValue", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "predictSizeValue", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "predictValue", "priorityValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "silentValue", "target", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "throughWallsValue", "getTarget", "throughWalls", "", "priorityMode", "", "hasTarget", "onDisable", "", "onRender3D", "event", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "LiKingSense"})
public final class BowAimbot
extends Module {
    public final BoolValue silentValue = new BoolValue("Silent", true);
    public final BoolValue predictValue = new BoolValue("Predict", true);
    public final BoolValue throughWallsValue = new BoolValue("ThroughWalls", false);
    public final FloatValue predictSizeValue = new FloatValue("PredictSize", 2.0f, 0.1f, 5.0f);
    public final ListValue priorityValue = new ListValue("Priority", new String[]{"Health", "Distance", "Direction"}, "Direction");
    public final BoolValue markValue = new BoolValue("Mark", true);
    public IEntity target;

    @Override
    public void onDisable() {
        this.target = null;
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull((Object)event, (String)"event");
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

    /*
     * Exception decompiling
     */
    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Invalid stack depths @ lbl27 : RETURN - null : trying to set 0 previously set to 2
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:207)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * WARNING - void declaration
     */
    public final IEntity getTarget(boolean throughWalls, String priorityMode) {
        IEntity iEntity;
        String string;
        void $this$filterTo$iv$iv;
        Iterable $this$filter$iv;
        Iterable iterable = $this$filter$iv = (Iterable)MinecraftInstance.mc.getTheWorld().getLoadedEntityList();
        Collection destination$iv$iv = new ArrayList();
        for (Object element$iv$iv : $this$filterTo$iv$iv) {
            IEntity it = (IEntity)element$iv$iv;
            if ((MinecraftInstance.classProvider.isEntityLivingBase(it) && EntityUtils.isSelected(it, true) && (throughWalls || MinecraftInstance.mc.getThePlayer().canEntityBeSeen(it)) ? 1 : 0) == 0) continue;
            destination$iv$iv.add(element$iv$iv);
        }
        List targets = (List)destination$iv$iv;
        String string2 = string = priorityMode;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toUpperCase();
        Intrinsics.checkExpressionValueIsNotNull((Object)string3, (String)"(this as java.lang.String).toUpperCase()");
        switch (string3) {
            case "DISTANCE": {
                Object e$iv;
                IEntity it;
                Object v2;
                Iterable $this$minBy$iv = targets;
                Iterator iterator$iv = $this$minBy$iv.iterator();
                if (!iterator$iv.hasNext()) {
                    v2 = null;
                } else {
                    Object minElem$iv = iterator$iv.next();
                    if (!iterator$iv.hasNext()) {
                        v2 = minElem$iv;
                    } else {
                        it = (IEntity)minElem$iv;
                        float minValue$iv = MinecraftInstance.mc.getThePlayer().getDistanceToEntity(it);
                        do {
                            e$iv = iterator$iv.next();
                            IEntity it2 = (IEntity)e$iv;
                            float v$iv = MinecraftInstance.mc.getThePlayer().getDistanceToEntity(it2);
                            if (Float.compare(minValue$iv, v$iv) <= 0) continue;
                            minElem$iv = e$iv;
                            minValue$iv = v$iv;
                        } while (iterator$iv.hasNext());
                        v2 = minElem$iv;
                    }
                }
                iEntity = v2;
                break;
            }
            case "DIRECTION": {
                Object v4;
                Object e$iv;
                IEntity it;
                Iterable $this$minBy$iv = targets;
                Iterator iterator$iv = $this$minBy$iv.iterator();
                if (!iterator$iv.hasNext()) {
                    v4 = null;
                } else {
                    Object minElem$iv = iterator$iv.next();
                    if (!iterator$iv.hasNext()) {
                        v4 = minElem$iv;
                    } else {
                        it = (IEntity)minElem$iv;
                        double minValue$iv = RotationUtils.getRotationDifference(it);
                        do {
                            IEntity it3;
                            double v$iv;
                            if (Double.compare(minValue$iv, v$iv = RotationUtils.getRotationDifference(it3 = (IEntity)(e$iv = iterator$iv.next()))) <= 0) continue;
                            minElem$iv = e$iv;
                            minValue$iv = v$iv;
                        } while (iterator$iv.hasNext());
                        v4 = minElem$iv;
                    }
                }
                iEntity = v4;
                break;
            }
            case "HEALTH": {
                Object v5;
                Object e$iv;
                IEntity it;
                Iterable $this$minBy$iv = targets;
                Iterator iterator$iv = $this$minBy$iv.iterator();
                if (!iterator$iv.hasNext()) {
                    v5 = null;
                } else {
                    Object minElem$iv = iterator$iv.next();
                    if (!iterator$iv.hasNext()) {
                        v5 = minElem$iv;
                    } else {
                        it = (IEntity)minElem$iv;
                        float minValue$iv = it.asEntityLivingBase().getHealth();
                        do {
                            IEntity it4;
                            float v$iv;
                            if (Float.compare(minValue$iv, v$iv = (it4 = (IEntity)(e$iv = iterator$iv.next())).asEntityLivingBase().getHealth()) <= 0) continue;
                            minElem$iv = e$iv;
                            minValue$iv = v$iv;
                        } while (iterator$iv.hasNext());
                        v5 = minElem$iv;
                    }
                }
                iEntity = v5;
                break;
            }
            default: {
                iEntity = null;
            }
        }
        return iEntity;
    }

    public final boolean hasTarget() {
        return (this.target != null && MinecraftInstance.mc.getThePlayer().canEntityBeSeen(this.target) ? 1 : 0) != 0;
    }
}

