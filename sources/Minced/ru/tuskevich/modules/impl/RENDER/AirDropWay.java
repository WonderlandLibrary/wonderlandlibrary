// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.RENDER;

import ru.tuskevich.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import ru.tuskevich.util.render.RenderUtility;
import ru.tuskevich.util.font.Fonts;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiBossOverlay;
import ru.tuskevich.event.events.impl.EventDisplay;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "AirDropWay", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd GPS \ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.RENDER)
public class AirDropWay extends Module
{
    @EventTarget
    public void onDisplay(final EventDisplay eventDisplay) {
        final int xWay = GuiBossOverlay.xWay;
        final int zWay = GuiBossOverlay.zWay;
        if (xWay <= 0 || zWay <= 0) {
            return;
        }
        final Minecraft mc = AirDropWay.mc;
        final EntityPlayerSP player = Minecraft.player;
        final double x = xWay;
        final Minecraft mc2 = AirDropWay.mc;
        if ((int)player.getDistance(x, Minecraft.player.posY, zWay) <= 10) {
            return;
        }
        final double n = zWay;
        final Minecraft mc3 = AirDropWay.mc;
        final double y = n - Minecraft.player.posZ;
        final double n2 = xWay;
        final Minecraft mc4 = AirDropWay.mc;
        final double degrees = Math.toDegrees(Math.atan2(y, n2 - Minecraft.player.posX));
        final Minecraft mc5 = AirDropWay.mc;
        final double yaw = degrees - Minecraft.player.rotationYaw - 90.0;
        final double n3 = xWay;
        final Minecraft mc6 = AirDropWay.mc;
        final double pow = Math.pow(n3 - Minecraft.player.posX, 2.0);
        final double n4 = zWay;
        final Minecraft mc7 = AirDropWay.mc;
        final double dst = Math.sqrt(pow + Math.pow(n4 - Minecraft.player.posZ, 2.0));
        GL11.glPushMatrix();
        GL11.glTranslated(eventDisplay.sr.getScaledWidth_double() / 2.0 + 0.5, eventDisplay.sr.getScaledHeight_double() / 2.0 - 55.0, 0.0);
        GL11.glTranslated(Math.cos(Math.toRadians(yaw - 90.0)) * 1.3 * (Fonts.MONTSERRAT12.getStringWidth("(\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd)" + (int)dst + "m") / 2), Math.sin(Math.toRadians(yaw - 90.0)) * 10.0, 0.0);
        GL11.glRotated(yaw, 0.0, 0.0, 1.0);
        RenderUtility.drawTriangle();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslated(eventDisplay.sr.getScaledWidth_double() / 2.0, eventDisplay.sr.getScaledHeight_double() / 2.0 - 55.0, 0.0);
        Fonts.MONTSERRAT12.drawCenteredString("(\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd)" + (int)dst + "m", 0.0f, 0.0f, -1);
        GL11.glPopMatrix();
    }
}
