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
    private IEntity target;
    private final BoolValue predictValue;
    private final BoolValue markValue;
    private final BoolValue silentValue = new BoolValue("Silent", true);
    private final BoolValue throughWallsValue;
    private final FloatValue predictSizeValue;
    private final ListValue priorityValue;

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

    @EventTarget
    public final void onRender3D(Render3DEvent render3DEvent) {
        if (this.target != null && !StringsKt.equals((String)((String)this.priorityValue.get()), (String)"Multi", (boolean)true) && ((Boolean)this.markValue.get()).booleanValue()) {
            RenderUtils.drawPlatform(this.target, new Color(37, 126, 255, 70));
        }
    }

    @EventTarget
    public final void onUpdate(UpdateEvent updateEvent) {
        this.target = null;
        Object object = MinecraftInstance.mc.getThePlayer();
        if (MinecraftInstance.classProvider.isItemBow(object != null && (object = object.getItemInUse()) != null ? object.getItem() : null)) {
            IEntity iEntity;
            IEntity iEntity2 = this.getTarget((Boolean)this.throughWallsValue.get(), (String)this.priorityValue.get());
            if (iEntity2 == null) {
                return;
            }
            this.target = iEntity = iEntity2;
            RotationUtils.faceBow(this.target, (Boolean)this.silentValue.get(), (Boolean)this.predictValue.get(), ((Number)this.predictSizeValue.get()).floatValue());
        }
    }

    /*
     * Unable to fully structure code
     */
    private final IEntity getTarget(boolean var1_1, String var2_2) {
        v0 = MinecraftInstance.mc.getTheWorld();
        if (v0 == null) {
            Intrinsics.throwNpe();
        }
        var4_3 = v0.getLoadedEntityList();
        var5_4 = false;
        var6_8 = var4_3;
        var7_12 = new ArrayList<E>();
        var8_13 = false;
        for (T var11_21 : var6_8) {
            var12_28 = (IEntity)var11_21;
            var14_35 = false;
            if (!MinecraftInstance.classProvider.isEntityLivingBase(var12_28) || !EntityUtils.isSelected(var12_28, true)) ** GOTO lbl-1000
            if (var1_1) ** GOTO lbl-1000
            v1 = MinecraftInstance.mc.getThePlayer();
            if (v1 == null) {
                Intrinsics.throwNpe();
            }
            if (v1.canEntityBeSeen(var12_28)) lbl-1000:
            // 2 sources

            {
                v2 = true;
            } else lbl-1000:
            // 2 sources

            {
                v2 = false;
            }
            if (!v2) continue;
            var7_12.add(var11_21);
        }
        var3_36 = (List)var7_12;
        var4_3 = var2_2;
        var5_4 = false;
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
                var5_5 = var3_36;
                var6_9 = false;
                var7_12 = var5_5.iterator();
                if (!var7_12.hasNext()) {
                    v4 = null;
                } else {
                    var8_14 = var7_12.next();
                    if (!var7_12.hasNext()) {
                        v4 = var8_14;
                    } else {
                        var9_17 = (IEntity)var8_14;
                        var11_22 = false;
                        v5 = MinecraftInstance.mc.getThePlayer();
                        if (v5 == null) {
                            Intrinsics.throwNpe();
                        }
                        var9_18 = v5.getDistanceToEntity((IEntity)var9_17);
                        do {
                            var11_23 = var7_12.next();
                            var12_29 = (IEntity)var11_23;
                            var14_35 = false;
                            v6 = MinecraftInstance.mc.getThePlayer();
                            if (v6 == null) {
                                Intrinsics.throwNpe();
                            }
                            if (Float.compare(var9_18, var12_30 = v6.getDistanceToEntity(var12_29)) <= 0) continue;
                            var8_14 = var11_23;
                            var9_18 = var12_30;
                        } while (var7_12.hasNext());
                        v4 = var8_14;
                    }
                }
                v7 = v4;
                break;
            }
            case 2: {
                var5_6 = var3_36;
                var6_10 = false;
                var7_12 = var5_6.iterator();
                if (!var7_12.hasNext()) {
                    v8 = null;
                } else {
                    var8_15 = var7_12.next();
                    if (!var7_12.hasNext()) {
                        v8 = var8_15;
                    } else {
                        var9_17 = (IEntity)var8_15;
                        var11_24 = false;
                        var9_19 = RotationUtils.getRotationDifference((IEntity)var9_17);
                        do {
                            var11_25 = var7_12.next();
                            var12_31 = (IEntity)var11_25;
                            var14_35 = false;
                            var12_32 = RotationUtils.getRotationDifference(var12_31);
                            if (Double.compare(var9_19, var12_32) <= 0) continue;
                            var8_15 = var11_25;
                            var9_19 = var12_32;
                        } while (var7_12.hasNext());
                        v8 = var8_15;
                    }
                }
                v7 = v8;
                break;
            }
            case 3: {
                var5_7 = var3_36;
                var6_11 = false;
                var7_12 = var5_7.iterator();
                if (!var7_12.hasNext()) {
                    v9 = null;
                } else {
                    var8_16 = var7_12.next();
                    if (!var7_12.hasNext()) {
                        v9 = var8_16;
                    } else {
                        var9_17 = (IEntity)var8_16;
                        var11_26 = false;
                        var9_20 = var9_17.asEntityLivingBase().getHealth();
                        do {
                            var11_27 = var7_12.next();
                            var12_33 = (IEntity)var11_27;
                            var14_35 = false;
                            var12_34 = var12_33.asEntityLivingBase().getHealth();
                            if (Float.compare(var9_20, var12_34) <= 0) continue;
                            var8_16 = var11_27;
                            var9_20 = var12_34;
                        } while (var7_12.hasNext());
                        v9 = var8_16;
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

    public BowAimbot() {
        this.predictValue = new BoolValue("Predict", true);
        this.throughWallsValue = new BoolValue("ThroughWalls", false);
        this.predictSizeValue = new FloatValue("PredictSize", 2.0f, 0.1f, 5.0f);
        this.priorityValue = new ListValue("Priority", new String[]{"Health", "Distance", "Direction"}, "Direction");
        this.markValue = new BoolValue("Mark", true);
    }

    @Override
    public void onDisable() {
        this.target = null;
    }
}

