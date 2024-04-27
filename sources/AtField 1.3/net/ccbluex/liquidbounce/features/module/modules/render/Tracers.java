/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.FloatValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import net.ccbluex.liquidbounce.value.ListValue;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="Tracers", description="Draws a line to targets around you.", category=ModuleCategory.RENDER)
public final class Tracers
extends Module {
    private final FloatValue thicknessValue;
    private final IntegerValue colorRedValue;
    private final IntegerValue colorGreenValue;
    private final IntegerValue colorBlueValue;
    private final ListValue colorMode = new ListValue("Color", new String[]{"Custom", "DistanceColor", "Rainbow"}, "Custom");

    private final void drawTraces(IEntity iEntity, Color color) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        double d = iEntity.getLastTickPosX() + (iEntity.getPosX() - iEntity.getLastTickPosX()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosX();
        double d2 = iEntity.getLastTickPosY() + (iEntity.getPosY() - iEntity.getLastTickPosY()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosY();
        double d3 = iEntity.getLastTickPosZ() + (iEntity.getPosZ() - iEntity.getLastTickPosZ()) * (double)MinecraftInstance.mc.getTimer().getRenderPartialTicks() - MinecraftInstance.mc.getRenderManager().getRenderPosZ();
        WVec3 wVec3 = new WVec3(0.0, 0.0, 1.0).rotatePitch((float)(-Math.toRadians(iEntityPlayerSP2.getRotationPitch()))).rotateYaw((float)(-Math.toRadians(iEntityPlayerSP2.getRotationYaw())));
        RenderUtils.glColor(color);
        GL11.glVertex3d((double)wVec3.getXCoord(), (double)((double)iEntityPlayerSP2.getEyeHeight() + wVec3.getYCoord()), (double)wVec3.getZCoord());
        GL11.glVertex3d((double)d, (double)d2, (double)d3);
        GL11.glVertex3d((double)d, (double)d2, (double)d3);
        GL11.glVertex3d((double)d, (double)(d2 + (double)iEntity.getHeight()), (double)d3);
    }

    @EventTarget
    public final void onRender3D(Render3DEvent render3DEvent) {
        IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
        if (iEntityPlayerSP == null) {
            return;
        }
        IEntityPlayerSP iEntityPlayerSP2 = iEntityPlayerSP;
        GL11.glBlendFunc((int)770, (int)771);
        GL11.glEnable((int)3042);
        GL11.glEnable((int)2848);
        GL11.glLineWidth((float)((Number)this.thicknessValue.get()).floatValue());
        GL11.glDisable((int)3553);
        GL11.glDisable((int)2929);
        GL11.glDepthMask((boolean)false);
        GL11.glBegin((int)1);
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        for (IEntity iEntity : iWorldClient.getLoadedEntityList()) {
            if (!(iEntity.equals(iEntityPlayerSP2) ^ true) || !EntityUtils.isSelected(iEntity, false)) continue;
            int n = (int)(iEntityPlayerSP2.getDistanceToEntity(iEntity) * (float)2);
            if (n > 255) {
                n = 255;
            }
            Object object = (String)this.colorMode.get();
            boolean bl = false;
            String string = object;
            if (string == null) {
                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
            }
            String string2 = string.toLowerCase();
            object = EntityUtils.isFriend((IEntity)iEntity) ? new Color(0, 0, 255, 150) : (string2.equals("custom") ? new Color(((Number)this.colorRedValue.get()).intValue(), ((Number)this.colorGreenValue.get()).intValue(), ((Number)this.colorBlueValue.get()).intValue(), 150) : (string2.equals("distancecolor") ? new Color(255 - n, n, 0, 150) : (string2.equals("rainbow") ? ColorUtils.rainbow() : new Color(255, 255, 255, 150))));
            this.drawTraces(iEntity, (Color)object);
        }
        GL11.glEnd();
        GL11.glEnable((int)3553);
        GL11.glDisable((int)2848);
        GL11.glEnable((int)2929);
        GL11.glDepthMask((boolean)true);
        GL11.glDisable((int)3042);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
    }

    public Tracers() {
        this.thicknessValue = new FloatValue("Thickness", 2.0f, 1.0f, 5.0f);
        this.colorRedValue = new IntegerValue("R", 0, 0, 255);
        this.colorGreenValue = new IntegerValue("G", 160, 0, 255);
        this.colorBlueValue = new IntegerValue("B", 255, 0, 255);
    }
}

