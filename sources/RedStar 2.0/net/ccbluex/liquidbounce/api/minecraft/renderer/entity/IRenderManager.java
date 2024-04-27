package net.ccbluex.liquidbounce.api.minecraft.renderer.entity;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntity;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityLivingBase;
import net.ccbluex.liquidbounce.api.minecraft.tileentity.ITileEntity;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000@\n\n\u0000\n\u0000\n\n\b\n\n\b\n\n\b\r\n\n\u0000\n\n\b\n\n\b\n\n\b\bf\u000020J0020 2!02\"02#02$0\bH&J %020&2'0\b2(0H&J8)02*0+2,02-02.02/0\b200\bH&R0X¦¢\f\b\"\bR0\bX¦¢\b\t\nR0\bX¦¢\f\b\f\n\"\b\rR0X¦¢\bR0X¦¢\bR0X¦¢\bR0X¦¢\bR0X¦¢\bR0X¦¢\b¨1"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/renderer/entity/IRenderManager;", "", "isRenderShadow", "", "()Z", "setRenderShadow", "(Z)V", "playerViewX", "", "getPlayerViewX", "()F", "playerViewY", "getPlayerViewY", "setPlayerViewY", "(F)V", "renderPosX", "", "getRenderPosX", "()D", "renderPosY", "getRenderPosY", "renderPosZ", "getRenderPosZ", "viewerPosX", "getViewerPosX", "viewerPosY", "getViewerPosY", "viewerPosZ", "getViewerPosZ", "renderEntityAt", "", "entity", "Lnet/ccbluex/liquidbounce/api/minecraft/tileentity/ITileEntity;", "x", "y", "z", "partialTicks", "renderEntityStatic", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntity;", "renderPartialTicks", "hideDebugBox", "renderEntityWithPosYaw", "entityLivingBase", "Lnet/ccbluex/liquidbounce/api/minecraft/client/entity/IEntityLivingBase;", "d", "d1", "d2", "fl", "fl1", "Pride"})
public interface IRenderManager {
    public boolean isRenderShadow();

    public void setRenderShadow(boolean var1);

    public double getViewerPosX();

    public double getViewerPosY();

    public double getViewerPosZ();

    public float getPlayerViewX();

    public float getPlayerViewY();

    public void setPlayerViewY(float var1);

    public double getRenderPosX();

    public double getRenderPosY();

    public double getRenderPosZ();

    public boolean renderEntityStatic(@NotNull IEntity var1, float var2, boolean var3);

    public void renderEntityAt(@NotNull ITileEntity var1, double var2, double var4, double var6, float var8);

    public boolean renderEntityWithPosYaw(@NotNull IEntityLivingBase var1, double var2, double var4, double var6, float var8, float var9);
}
