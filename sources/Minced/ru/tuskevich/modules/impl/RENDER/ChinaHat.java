// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.RENDER;

import ru.tuskevich.event.EventTarget;
import java.awt.Color;
import ru.tuskevich.modules.impl.HUD.Hud;
import ru.tuskevich.util.aliasing.AntiAliasingUtility;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventRender;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "ChinaHat", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.RENDER)
public class ChinaHat extends Module
{
    SliderSetting lineWidth;
    
    public ChinaHat() {
        this.lineWidth = new SliderSetting("Outline Width", 1.0f, 0.0f, 5.0f, 0.1f);
        this.add(this.lineWidth);
    }
    
    @EventTarget
    public void onRender(final EventRender eventDisplay) {
        if (ChinaHat.mc.gameSettings.thirdPersonView == 0) {
            return;
        }
        final Minecraft mc = ChinaHat.mc;
        final double lastTickPosX = Minecraft.player.lastTickPosX;
        final Minecraft mc2 = ChinaHat.mc;
        final double posX = Minecraft.player.posX;
        final Minecraft mc3 = ChinaHat.mc;
        final double ix = -(lastTickPosX + (posX - Minecraft.player.lastTickPosX) * eventDisplay.pt);
        final Minecraft mc4 = ChinaHat.mc;
        final double lastTickPosY = Minecraft.player.lastTickPosY;
        final Minecraft mc5 = ChinaHat.mc;
        final double posY = Minecraft.player.posY;
        final Minecraft mc6 = ChinaHat.mc;
        final double iy = -(lastTickPosY + (posY - Minecraft.player.lastTickPosY) * eventDisplay.pt);
        final Minecraft mc7 = ChinaHat.mc;
        final double lastTickPosZ = Minecraft.player.lastTickPosZ;
        final Minecraft mc8 = ChinaHat.mc;
        final double posZ = Minecraft.player.posZ;
        final Minecraft mc9 = ChinaHat.mc;
        final double iz = -(lastTickPosZ + (posZ - Minecraft.player.lastTickPosZ) * eventDisplay.pt);
        final Minecraft mc10 = ChinaHat.mc;
        final double lastTickPosX2 = Minecraft.player.lastTickPosX;
        final Minecraft mc11 = ChinaHat.mc;
        final double posX2 = Minecraft.player.posX;
        final Minecraft mc12 = ChinaHat.mc;
        final float x = (float)(lastTickPosX2 + (posX2 - Minecraft.player.lastTickPosX) * eventDisplay.pt);
        final Minecraft mc13 = ChinaHat.mc;
        final double lastTickPosY2 = Minecraft.player.lastTickPosY;
        final Minecraft mc14 = ChinaHat.mc;
        final double posY2 = Minecraft.player.posY;
        final Minecraft mc15 = ChinaHat.mc;
        final float n = (float)(lastTickPosY2 + (posY2 - Minecraft.player.lastTickPosY) * eventDisplay.pt);
        final Minecraft mc16 = ChinaHat.mc;
        final float n2 = n + Minecraft.player.height;
        final Minecraft mc17 = ChinaHat.mc;
        final float y = n2 - (Minecraft.player.isSneaking() ? 0.2f : 0.0f);
        final Minecraft mc18 = ChinaHat.mc;
        final double lastTickPosZ2 = Minecraft.player.lastTickPosZ;
        final Minecraft mc19 = ChinaHat.mc;
        final double posZ2 = Minecraft.player.posZ;
        final Minecraft mc20 = ChinaHat.mc;
        final float z = (float)(lastTickPosZ2 + (posZ2 - Minecraft.player.lastTickPosZ) * eventDisplay.pt);
        GlStateManager.pushMatrix();
        GL11.glDepthMask(false);
        GlStateManager.enableDepth();
        final Minecraft mc21 = ChinaHat.mc;
        GL11.glRotatef(-Minecraft.player.rotationYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(ix, iy, iz);
        GlStateManager.enableBlend();
        GL11.glBlendFunc(770, 771);
        GlStateManager.disableTexture2D();
        GL11.glDisable(2884);
        GL11.glShadeModel(7425);
        GL11.glDisable(3008);
        AntiAliasingUtility.hook(true, true, true);
        GlStateManager.alphaFunc(516, 0.0f);
        GL11.glBegin(6);
        final Color c1 = Hud.getColor(28);
        GL11.glColor4f(c1.getRed() / 255.0f, c1.getGreen() / 255.0f, c1.getBlue() / 255.0f, 0.39215687f);
        GL11.glVertex3f(x, y + 0.23f, z);
        for (int i = 0; i <= 360; ++i) {
            final double x2 = Math.cos(i * 3.141592653589793 / 180.0) * 0.55;
            final double z2 = Math.sin(i * 3.141592653589793 / 180.0) * 0.55;
            final Color c2 = Hud.getColor(i / 4);
            GL11.glColor4f(c2.getRed() / 255.0f, c2.getGreen() / 255.0f, c2.getBlue() / 255.0f, 0.39215687f);
            GL11.glVertex3d(x + x2, (double)y, z + z2);
        }
        GL11.glEnd();
        GL11.glLineWidth(this.lineWidth.getFloatValue());
        GL11.glBegin(2);
        for (int j = 0; j <= 360; ++j) {
            final double x3 = Math.cos(j * 3.141592653589793 / 180.0) * 0.55;
            final double z3 = Math.sin(j * 3.141592653589793 / 180.0) * 0.55;
            final Color c3 = Hud.getColor(j / 2);
            GL11.glColor4f(c3.getRed() / 255.0f, c3.getGreen() / 255.0f, c3.getBlue() / 255.0f, 1.0f);
            GL11.glVertex3d(x + x3, (double)y, z + z3);
        }
        GL11.glEnd();
        AntiAliasingUtility.unhook(true, true, true);
        GL11.glEnable(3008);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glEnable(2884);
        GlStateManager.resetColor();
        GL11.glDepthMask(true);
        GlStateManager.popMatrix();
    }
}
