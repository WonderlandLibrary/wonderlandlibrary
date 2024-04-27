package me.utils;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.HUD;
import net.ccbluex.liquidbounce.features.module.modules.render.OldHitting;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

public class Debug
extends MinecraftInstance {
    public static Boolean thePlayerisBlocking = false;

    public static void render(ScaledResolution sr, int itemX, float partialTicks) {
        GL11.glPushMatrix();
        HUD hud = (HUD)LiquidBounce.moduleManager.getModule(HUD.class);
        GL11.glPopMatrix();
        int middleScreen = sr.getScaledWidth() / 2;
    }

    public static void func_178096_b(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.translate((float)0.0f, (float)(equippedProg * -0.6f), (float)0.0f);
        GlStateManager.rotate((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)side * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)side * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        float var3 = MathHelper.sin((float)(swingProgress * swingProgress * (float)Math.PI));
        float var4 = MathHelper.sin((float)(MathHelper.sqrt((float)swingProgress) * (float)Math.PI));
        GlStateManager.rotate((float)(var3 * -20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(var4 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.rotate((float)(var4 * -80.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.scale((float)((Float)OldHitting.Scale.get()).floatValue(), (float)((Float)OldHitting.Scale.get()).floatValue(), (float)((Float)OldHitting.Scale.get()).floatValue());
    }

    public static void transformSideFirstPersonBlock(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate((double)((double)side * 0.56), (double)(-0.52 + (double)equippedProg * -0.6), (double)-0.72);
        GlStateManager.translate((double)((double)side * -0.1414214), (double)0.08, (double)0.1414214);
        GlStateManager.rotate((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)side * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)side * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        double f = Math.sin((double)(swingProgress * swingProgress) * Math.PI);
        double f1 = Math.sin(Math.sqrt(swingProgress) * Math.PI);
        GlStateManager.rotate((float)((float)(f * -20.0)), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)(f1 * -20.0)), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.rotate((float)((float)(f1 * -80.0)), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.scale((float)((Float)OldHitting.Scale.get()).floatValue(), (float)((Float)OldHitting.Scale.get()).floatValue(), (float)((Float)OldHitting.Scale.get()).floatValue());
    }

    public static void Push(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate((double)((double)side * 0.56), (double)(-0.52 + (double)equippedProg * -0.6), (double)-0.72);
        GlStateManager.translate((double)((double)side * -0.1414214), (double)0.08, (double)0.1414214);
        GlStateManager.rotate((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)side * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)side * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        double f = Math.sin((double)(swingProgress * swingProgress) * Math.PI);
        double f1 = Math.sin(Math.sqrt(swingProgress) * Math.PI);
        GlStateManager.rotate((float)((float)(f * -10.0)), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.rotate((float)((float)(f1 * -10.0)), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.rotate((float)((float)(f1 * -10.0)), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.scale((float)((Float)OldHitting.Scale.get()).floatValue(), (float)((Float)OldHitting.Scale.get()).floatValue(), (float)((Float)OldHitting.Scale.get()).floatValue());
    }

    public static void sigmaold(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.translate((float)0.0f, (float)(equippedProg * -0.6f), (float)0.0f);
        GlStateManager.rotate((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)side * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)side * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        float var3 = MathHelper.sin((float)(swingProgress * swingProgress * (float)Math.PI));
        float var4 = MathHelper.sin((float)(MathHelper.sqrt((float)swingProgress) * (float)Math.PI));
        GlStateManager.rotate((float)(var3 * -15.0f), (float)0.0f, (float)1.0f, (float)0.2f);
        GlStateManager.rotate((float)(var4 * -10.0f), (float)0.2f, (float)0.1f, (float)1.0f);
        GlStateManager.rotate((float)(var4 * -30.0f), (float)1.3f, (float)0.1f, (float)0.2f);
        GlStateManager.scale((float)((Float)OldHitting.Scale.get()).floatValue(), (float)((Float)OldHitting.Scale.get()).floatValue(), (float)((Float)OldHitting.Scale.get()).floatValue());
    }

    public static void jello(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.rotate((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)side * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)side * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        float var13 = MathHelper.sin((float)(swingProgress * swingProgress * (float)Math.PI));
        float var14 = MathHelper.sin((float)(MathHelper.sqrt((float)swingProgress) * (float)Math.PI));
        GlStateManager.rotate((float)(var13 * -35.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)(var14 * 0.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)(var14 * 20.0f), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.scale((float)((Float)OldHitting.Scale.get()).floatValue(), (float)((Float)OldHitting.Scale.get()).floatValue(), (float)((Float)OldHitting.Scale.get()).floatValue());
    }

    public static void func_178103_d() {
        GlStateManager.translate((float)-0.5f, (float)0.2f, (float)0.0f);
        GlStateManager.rotate((float)30.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)-80.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)60.0f, (float)0.0f, (float)1.0f, (float)0.0f);
    }

    public static void Test(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.translate((float)0.0f, (float)(equippedProg * -0.6f), (float)0.0f);
        GlStateManager.rotate((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)side * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)side * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        float var3 = MathHelper.sin((float)(swingProgress * swingProgress * (float)Math.PI));
        float var4 = MathHelper.sin((float)(MathHelper.sqrt((float)swingProgress) * (float)Math.PI));
        float var5 = MathHelper.ceil((float)((float)MathHelper.floor((float)swingProgress) * (float)Math.PI));
        GlStateManager.rotate((float)(var3 * -20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(var4 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.rotate((float)(var5 * -80.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.scale((float)((Float)OldHitting.Scale.get()).floatValue(), (float)((Float)OldHitting.Scale.get()).floatValue(), (float)((Float)OldHitting.Scale.get()).floatValue());
    }

    private void transformFirstPersonItem1(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.translate((float)0.0f, (float)(equippedProg * -0.6f), (float)0.0f);
        GlStateManager.rotate((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var3 = MathHelper.sin((float)(swingProgress * swingProgress * (float)Math.PI));
        float var4 = MathHelper.sin((float)(MathHelper.sqrt((float)swingProgress) * (float)Math.PI));
        float var5 = MathHelper.ceil((float)((float)MathHelper.floor((float)swingProgress) * (float)Math.PI));
        GlStateManager.rotate((float)(var3 * -34.0f), (float)0.0f, (float)1.0f, (float)0.2f);
        GlStateManager.rotate((float)(var4 * -20.7f), (float)0.2f, (float)0.1f, (float)1.0f);
        GlStateManager.rotate((float)(var4 * -68.6f), (float)1.3f, (float)0.1f, (float)0.2f);
        GlStateManager.scale((float)0.4f, (float)0.4f, (float)0.4f);
        GlStateManager.scale((float)((Float)OldHitting.Scale.get()).floatValue(), (float)((Float)OldHitting.Scale.get()).floatValue(), (float)((Float)OldHitting.Scale.get()).floatValue());
    }

    public static void ETB(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.translate((float)0.0f, (float)(equippedProg * -0.6f), (float)0.0f);
        GlStateManager.rotate((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)side * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)side * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        float var3 = MathHelper.sin((float)(swingProgress * swingProgress * (float)Math.PI));
        float var4 = MathHelper.sin((float)(MathHelper.sqrt((float)swingProgress) * (float)Math.PI));
        GlStateManager.rotate((float)(var3 * -34.0f), (float)0.0f, (float)1.0f, (float)0.2f);
        GlStateManager.rotate((float)(var4 * -20.7f), (float)0.2f, (float)0.1f, (float)1.0f);
        GlStateManager.rotate((float)(var4 * -68.6f), (float)1.3f, (float)0.1f, (float)0.2f);
        GlStateManager.scale((float)((Float)OldHitting.Scale.get()).floatValue(), (float)((Float)OldHitting.Scale.get()).floatValue(), (float)((Float)OldHitting.Scale.get()).floatValue());
    }

    public static void Zoom(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.translate((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.translate((float)0.0f, (float)(swingProgress * -0.6f), (float)0.0f);
        GlStateManager.rotate((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)side * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)((float)side * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        float var3 = MathHelper.sin((float)(equippedProg * equippedProg * (float)Math.PI));
        float var4 = MathHelper.sin((float)(MathHelper.sqrt((float)equippedProg) * (float)Math.PI));
        GlStateManager.rotate((float)(var3 * -20.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)(var4 * -20.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.rotate((float)(var4 * -20.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.scale((float)((Float)OldHitting.Scale.get()).floatValue(), (float)((Float)OldHitting.Scale.get()).floatValue(), (float)((Float)OldHitting.Scale.get()).floatValue());
    }
}
