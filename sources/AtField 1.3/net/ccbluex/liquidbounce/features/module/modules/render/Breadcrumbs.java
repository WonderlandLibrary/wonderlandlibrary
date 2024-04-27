/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Unit
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import java.util.LinkedList;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="Breadcrumbs", description="Leaves a trail behind you.", category=ModuleCategory.RENDER)
public final class Breadcrumbs
extends Module {
    private final IntegerValue colorBlueValue;
    private final LinkedList positions;
    private final IntegerValue colorRedValue = new IntegerValue("R", 255, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 179, 0, 255);
    private final BoolValue colorRainbow;

    @EventTarget
    public final void onUpdate(@Nullable UpdateEvent updateEvent) {
        LinkedList linkedList = this.positions;
        boolean bl = false;
        boolean bl2 = false;
        synchronized (linkedList) {
            boolean bl3 = false;
            double[] dArray = new double[3];
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            dArray[0] = iEntityPlayerSP.getPosX();
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            dArray[1] = iEntityPlayerSP2.getEntityBoundingBox().getMinY();
            IEntityPlayerSP iEntityPlayerSP3 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP3 == null) {
                Intrinsics.throwNpe();
            }
            dArray[2] = iEntityPlayerSP3.getPosZ();
            bl2 = this.positions.add(dArray);
        }
    }

    public final IntegerValue getColorRedValue() {
        return this.colorRedValue;
    }

    public final IntegerValue getColorGreenValue() {
        return this.colorGreenValue;
    }

    @Override
    public void onEnable() {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        LinkedList linkedList = this.positions;
        boolean bl = false;
        boolean bl2 = false;
        synchronized (linkedList) {
            boolean bl3 = false;
            this.positions.add(new double[]{iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getEntityBoundingBox().getMinY() + (double)(iEntityPlayerSP2.getEyeHeight() * 0.5f), iEntityPlayerSP2.getPosZ()});
            bl2 = this.positions.add(new double[]{iEntityPlayerSP2.getPosX(), iEntityPlayerSP2.getEntityBoundingBox().getMinY(), iEntityPlayerSP2.getPosZ()});
        }
        super.onEnable();
    }

    public Breadcrumbs() {
        this.colorBlueValue = new IntegerValue("B", 72, 0, 255);
        this.colorRainbow = new BoolValue("Rainbow", false);
        this.positions = new LinkedList();
    }

    public final IntegerValue getColorBlueValue() {
        return this.colorBlueValue;
    }

    @Override
    public void onDisable() {
        LinkedList linkedList = this.positions;
        boolean bl = false;
        boolean bl2 = false;
        synchronized (linkedList) {
            boolean bl3 = false;
            this.positions.clear();
            Unit unit = Unit.INSTANCE;
        }
        super.onDisable();
    }

    public final BoolValue getColorRainbow() {
        return this.colorRainbow;
    }

    @EventTarget
    public final void onRender3D(@Nullable Render3DEvent render3DEvent) {
        Color color = (Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue());
        LinkedList linkedList = this.positions;
        boolean bl = false;
        boolean bl2 = false;
        synchronized (linkedList) {
            boolean bl3 = false;
            GL11.glPushMatrix();
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)2848);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)2929);
            MinecraftInstance.mc.getEntityRenderer().disableLightmap();
            GL11.glBegin((int)3);
            RenderUtils.glColor(color);
            double d = MinecraftInstance.mc.getRenderManager().getViewerPosX();
            double d2 = MinecraftInstance.mc.getRenderManager().getViewerPosY();
            double d3 = MinecraftInstance.mc.getRenderManager().getViewerPosZ();
            for (double[] dArray : this.positions) {
                GL11.glVertex3d((double)(dArray[0] - d), (double)(dArray[1] - d2), (double)(dArray[2] - d3));
            }
            GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
            GL11.glEnd();
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glDisable((int)3042);
            GL11.glEnable((int)3553);
            GL11.glPopMatrix();
            Unit unit = Unit.INSTANCE;
        }
    }
}

