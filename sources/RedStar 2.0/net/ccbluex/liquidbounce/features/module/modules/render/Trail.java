package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.event.WorldEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

@ModuleInfo(name="Trail", category=ModuleCategory.RENDER, description="Leaves a trail behind you")
@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000h\n\n\n\b\n\n\b\n\n\b\n\n\b\n%\n\b\n!\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\b\b\u000020:*B¢J\b0HJ 02!0\"HJ#02!0$HJ%02!0&HJ'02(0)HR08F¢\bR0\bX¢\n\u0000R\t0\bX¢\n\u0000R\n0\bX¢\n\u0000R0\fX¢\n\u0000R\r0\bX¢\n\u0000R0\fX¢\n\u0000R0\fX¢\n\u0000R0\fX¢\n\u0000R0\bX¢\n\u0000R0\bX¢\n\u0000R 0\n\b000X¢\n\u0000R0\bX¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000¨+"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/Trail;", "Lnet/ccbluex/liquidbounce/features/module/Module;", "()V", "color", "Ljava/awt/Color;", "getColor", "()Ljava/awt/Color;", "colorAlphaValue", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "colorBlueValue", "colorGreenValue", "colorRainbow", "Lnet/ccbluex/liquidbounce/value/BoolValue;", "colorRedValue", "drawTargets", "drawThePlayer", "fade", "fadeTime", "lineWidth", "points", "", "", "", "Lnet/ccbluex/liquidbounce/features/module/modules/render/Trail$BreadcrumbPoint;", "precision", "sphereList", "sphereScale", "Lnet/ccbluex/liquidbounce/value/FloatValue;", "typeValue", "Lnet/ccbluex/liquidbounce/value/ListValue;", "onDisable", "", "onRender3D", "event", "Lnet/ccbluex/liquidbounce/event/Render3DEvent;", "onUpdate", "Lnet/ccbluex/liquidbounce/event/UpdateEvent;", "onWorld", "Lnet/ccbluex/liquidbounce/event/WorldEvent;", "updatePoints", "entity", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "BreadcrumbPoint", "Pride"})
public final class Trail
extends Module {
    private final ListValue typeValue = new ListValue("Type", new String[]{"Line", "Rect", "Sphere"}, "Line");
    private final IntegerValue colorRedValue = new IntegerValue("R", 255, 0, 255);
    private final IntegerValue colorGreenValue = new IntegerValue("G", 255, 0, 255);
    private final IntegerValue colorBlueValue = new IntegerValue("B", 255, 0, 255);
    private final IntegerValue colorAlphaValue = new IntegerValue("Alpha", 255, 0, 255);
    private final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    private final BoolValue fade = new BoolValue("Fade", true);
    private final BoolValue drawThePlayer = new BoolValue("DrawThePlayer", true);
    private final BoolValue drawTargets = new BoolValue("DrawTargets", true);
    private final IntegerValue fadeTime = new IntegerValue("FadeTime", 5, 1, 20);
    private final IntegerValue precision = new IntegerValue("Precision", 1, 1, 20);
    private final IntegerValue lineWidth = new IntegerValue("LineWidth", 1, 1, 10);
    private final FloatValue sphereScale = new FloatValue("SphereScale", 1.0f, 0.1f, 2.0f);
    private final Map<Integer, List<BreadcrumbPoint>> points;
    private final int sphereList;

    @NotNull
    public final Color getColor() {
        return (Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue());
    }

    /*
     * Iterators could be improved
     * Unable to fully structure code
     */
    @EventTarget
    public final void onRender3D(@NotNull Render3DEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        fTime = ((Number)this.fadeTime.get()).intValue() * 1000;
        fadeSec = System.currentTimeMillis() - (long)fTime;
        colorAlpha = ((Number)this.colorAlphaValue.get()).floatValue() / 255.0f;
        GL11.glPushMatrix();
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)2929);
        MinecraftInstance.mc.getEntityRenderer().disableLightmap();
        renderPosX = MinecraftInstance.mc.getRenderManager().getViewerPosX();
        renderPosY = MinecraftInstance.mc.getRenderManager().getViewerPosY();
        renderPosZ = MinecraftInstance.mc.getRenderManager().getViewerPosZ();
        $this$forEach$iv = this.points;
        $i$f$forEach = false;
        var14_10 = $this$forEach$iv;
        var15_11 = false;
        var16_12 = var14_10.entrySet().iterator();
        while (var16_12.hasNext()) {
            $dstr$_u24__u24$mutableList = element$iv = var16_12.next();
            $i$a$-forEach-Trail$onRender3D$1 = false;
            var20_16 = $dstr$_u24__u24$mutableList;
            var21_17 = false;
            mutableList = var20_16.getValue();
            lastPosX = 114514.0;
            lastPosY = 114514.0;
            lastPosZ = 114514.0;
            var29_22 = (String)this.typeValue.get();
            var30_23 = false;
            v0 = var29_22;
            if (v0 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            v1 = v0.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(v1, "(this as java.lang.String).toLowerCase()");
            var29_22 = v1;
            switch (var29_22.hashCode()) {
                case 3496420: {
                    if (!var29_22.equals("rect")) ** break;
                    break;
                }
                case 3321844: {
                    if (!var29_22.equals("line")) ** break;
                    GL11.glLineWidth((float)((Number)this.lineWidth.get()).intValue());
                    GL11.glEnable((int)2848);
                    GL11.glBegin((int)3);
                    ** break;
                }
            }
            GL11.glDisable((int)2884);
lbl47:
            // 7 sources

            for (BreadcrumbPoint point : CollectionsKt.reversed((Iterable)mutableList)) {
                block24: {
                    if (((Boolean)this.fade.get()).booleanValue()) {
                        pct = (float)(point.getTime() - fadeSec) / (float)fTime;
                        if (pct < (float)false || pct > (float)true) {
                            mutableList.remove(point);
                            continue;
                        }
                        v2 = pct;
                    } else {
                        v2 = 1.0f;
                    }
                    alpha = v2 * colorAlpha;
                    RenderUtils.glColor2(point.getColor(), alpha);
                    var31_26 = (String)this.typeValue.get();
                    var33_28 = false;
                    v3 = var31_26;
                    if (v3 == null) {
                        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
                    }
                    v4 = v3.toLowerCase();
                    Intrinsics.checkExpressionValueIsNotNull(v4, "(this as java.lang.String).toLowerCase()");
                    var31_26 = v4;
                    switch (var31_26.hashCode()) {
                        case 3496420: {
                            if (!var31_26.equals("rect")) ** break;
                            break;
                        }
                        case -895981619: {
                            if (!var31_26.equals("sphere")) ** break;
                            break block24;
                        }
                        case 3321844: {
                            if (!var31_26.equals("line")) ** break;
                            GL11.glVertex3d((double)(point.getX() - renderPosX), (double)(point.getY() - renderPosY), (double)(point.getZ() - renderPosZ));
                            ** break;
                        }
                    }
                    if (lastPosX != 114514.0 || lastPosY != 114514.0 || lastPosZ != 114514.0) {
                        GL11.glBegin((int)7);
                        GL11.glVertex3d((double)(point.getX() - renderPosX), (double)(point.getY() - renderPosY), (double)(point.getZ() - renderPosZ));
                        GL11.glVertex3d((double)lastPosX, (double)lastPosY, (double)lastPosZ);
                        v5 = MinecraftInstance.mc.getThePlayer();
                        if (v5 == null) {
                            Intrinsics.throwNpe();
                        }
                        GL11.glVertex3d((double)lastPosX, (double)(lastPosY + (double)v5.getHeight()), (double)lastPosZ);
                        v6 = point.getX() - renderPosX;
                        v7 = point.getY() - renderPosY;
                        v8 = MinecraftInstance.mc.getThePlayer();
                        if (v8 == null) {
                            Intrinsics.throwNpe();
                        }
                        GL11.glVertex3d((double)v6, (double)(v7 + (double)v8.getHeight()), (double)(point.getZ() - renderPosZ));
                        GL11.glEnd();
                    }
                    lastPosX = point.getX() - renderPosX;
                    lastPosY = point.getY() - renderPosY;
                    lastPosZ = point.getZ() - renderPosZ;
                    ** break;
                }
                GL11.glPushMatrix();
                GL11.glTranslated((double)(point.getX() - renderPosX), (double)(point.getY() - renderPosY), (double)(point.getZ() - renderPosZ));
                GL11.glScalef((float)((Number)this.sphereScale.get()).floatValue(), (float)((Number)this.sphereScale.get()).floatValue(), (float)((Number)this.sphereScale.get()).floatValue());
                GL11.glCallList((int)this.sphereList);
                GL11.glPopMatrix();
lbl103:
                // 7 sources

            }
            var29_22 = (String)this.typeValue.get();
            var30_23 = false;
            v9 = var29_22;
            if (v9 == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            v10 = v9.toLowerCase();
            Intrinsics.checkExpressionValueIsNotNull(v10, "(this as java.lang.String).toLowerCase()");
            var29_22 = v10;
            switch (var29_22.hashCode()) {
                case 3496420: {
                    if (!var29_22.equals("rect")) ** break;
                    break;
                }
                case 3321844: {
                    if (!var29_22.equals("line")) ** break;
                    GL11.glEnd();
                    GL11.glDisable((int)2848);
                    ** break;
                }
            }
            GL11.glEnable((int)2884);
lbl122:
            // 5 sources

        }
        GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
        GL11.glEnable((int)2929);
        GL11.glDisable((int)3042);
        GL11.glEnable((int)3553);
        GL11.glPopMatrix();
    }

    @EventTarget
    public final void onUpdate(@NotNull UpdateEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        Object $this$forEach$iv = this.points;
        boolean $i$f$forEach = false;
        Object object = $this$forEach$iv;
        boolean bl = false;
        Iterator<Map.Entry<Integer, List<BreadcrumbPoint>>> iterator2 = object.entrySet().iterator();
        while (iterator2.hasNext()) {
            Map.Entry<Integer, List<BreadcrumbPoint>> element$iv;
            Map.Entry<Integer, List<BreadcrumbPoint>> $dstr$id$_u24__u24 = element$iv = iterator2.next();
            boolean bl2 = false;
            Map.Entry<Integer, List<BreadcrumbPoint>> entry = $dstr$id$_u24__u24;
            boolean bl3 = false;
            int id = ((Number)entry.getKey()).intValue();
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            if (iWorldClient.getEntityByID(id) != null) continue;
            this.points.remove(id);
        }
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            Intrinsics.throwNpe();
        }
        if (iEntityPlayerSP.getTicksExisted() % ((Number)this.precision.get()).intValue() != 0) {
            return;
        }
        if (((Boolean)this.drawTargets.get()).booleanValue()) {
            IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
            if (iWorldClient == null) {
                Intrinsics.throwNpe();
            }
            $this$forEach$iv = iWorldClient.getLoadedEntityList();
            $i$f$forEach = false;
            object = $this$forEach$iv.iterator();
            while (object.hasNext()) {
                Object element$iv = object.next();
                IEntity it = (IEntity)element$iv;
                boolean bl4 = false;
                if (!EntityUtils.isSelected(it, true)) continue;
                IEntity iEntity = it;
                if (iEntity == null) {
                    throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase");
                }
                this.updatePoints((IEntityLivingBase)iEntity);
            }
        }
        if (((Boolean)this.drawThePlayer.get()).booleanValue()) {
            IEntityPlayerSP iEntityPlayerSP2 = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP2 == null) {
                Intrinsics.throwNpe();
            }
            this.updatePoints(iEntityPlayerSP2);
        }
    }

    private final void updatePoints(IEntityLivingBase entity) {
        List list = this.points.get(entity.getEntityId());
        if (list == null) {
            boolean bl = false;
            List list2 = new ArrayList();
            boolean bl2 = false;
            boolean bl3 = false;
            List it = list2;
            boolean bl4 = false;
            this.points.put(entity.getEntityId(), it);
            list = list2;
        }
        list.add((BreadcrumbPoint)new BreadcrumbPoint(entity.getPosX(), entity.getEntityBoundingBox().getMinY(), entity.getPosZ(), System.currentTimeMillis(), this.getColor().getRGB()));
    }

    @EventTarget
    public final void onWorld(@NotNull WorldEvent event) {
        Intrinsics.checkParameterIsNotNull(event, "event");
        this.points.clear();
    }

    @Override
    public void onDisable() {
        this.points.clear();
    }

    public Trail() {
        Map map;
        Trail trail = this;
        boolean bl = false;
        trail.points = map = (Map)new LinkedHashMap();
        this.sphereList = GL11.glGenLists((int)1);
        GL11.glNewList((int)this.sphereList, (int)4864);
        Sphere shaft = new Sphere();
        shaft.setDrawStyle(100012);
        shaft.draw(0.3f, 25, 10);
        GL11.glEndList();
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000 \n\n\u0000\n\u0000\n\n\b\n\t\n\u0000\n\b\n\b\n\u000020B-0000\b0\t¢\nR\b0\t¢\b\n\u0000\b\fR0¢\b\n\u0000\b\rR0¢\b\n\u0000\bR0¢\b\n\u0000\bR0¢\b\n\u0000\b¨"}, d2={"Lnet/ccbluex/liquidbounce/features/module/modules/render/Trail$BreadcrumbPoint;", "", "x", "", "y", "z", "time", "", "color", "", "(DDDJI)V", "getColor", "()I", "getTime", "()J", "getX", "()D", "getY", "getZ", "Pride"})
    public static final class BreadcrumbPoint {
        private final double x;
        private final double y;
        private final double z;
        private final long time;
        private final int color;

        public final double getX() {
            return this.x;
        }

        public final double getY() {
            return this.y;
        }

        public final double getZ() {
            return this.z;
        }

        public final long getTime() {
            return this.time;
        }

        public final int getColor() {
            return this.color;
        }

        public BreadcrumbPoint(double x, double y, double z, long time, int color) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.time = time;
            this.color = color;
        }
    }
}
